package com.hss.modules.scada.ws.terminal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hss.modules.message.util.TerminalWebSocketMessage;
import com.hss.modules.system.entity.BaseTerminal;
import com.hss.modules.system.model.TerminalInfoModel;
import com.hss.modules.system.service.IBaseTerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hd
 */
@Service
@Slf4j
public class TerminalWebSocketServiceImpl implements TerminalWebSocketService {

    @Autowired
    private IBaseTerminalService baseTerminalService;
    @Autowired
    private List<TerminalInfoService> infoServices;

    private static final Map<String, Map<String,SessionWrapper>> TERMINAL_MAP = new HashMap<>();



    private static final long CHECK_TIME = 10000L;

    @Scheduled(fixedRate = CHECK_TIME, initialDelay = CHECK_TIME)
    public void check() {
        Collection<String> terminalIds = getTerminalId();
        if (terminalIds.isEmpty()) {
            return;
        }
        terminalIds.stream().map(id -> {
            BaseTerminal byId = baseTerminalService.getById(id);
            if (byId == null) {
                return null;
            }
            TerminalWebSocketMessage terminalWebSocketMessage = new TerminalWebSocketMessage();
            terminalWebSocketMessage.setType(TerminalWebSocketMessage.TYPE_INFO);
            terminalWebSocketMessage.setMessage(byId);
            String msg = JSONObject.toJSONString(terminalWebSocketMessage, SerializerFeature.WriteDateUseDateFormat);
            List<String> list = new ArrayList<>();
            list.add(msg);
            List<String> msgList = getMsgList(byId);
            if (msgList != null) {
                list.addAll(msgList);
            }
            return new JobTerminalMsg(id, list);
        }).filter(Objects::nonNull).map(m -> {
            Collection<SessionWrapper> session = getSession(m.getId());
            if (session == null || session.isEmpty()) {
                return null;
            }
            return session.stream().map(s ->  new JobTerminalMsgSession(s, m.getMessage()));
        }).filter(Objects::nonNull).flatMap(o -> o).parallel().forEach(o -> {
            SessionWrapper sessionWrapper = o.getSessionWrapper();
            for (String s : o.getMessage()) {
                sessionWrapper.sendMsg(s);
            }

        });
    }

    @Override
    public void initOpen(String terminalId, Session session) {
        BaseTerminal byId = baseTerminalService.getById(terminalId);
        if (byId == null) {
            if (log.isWarnEnabled()) {
                log.warn("终端不存在id={}", terminalId);
            }
            return;
        }
        SessionWrapper sessionWrapper = addSession(terminalId, session);
        List<String> messages = getMsgList(byId);
        if (messages == null) {
            return;
        }
        for (String msg : messages) {
            sentMessage(sessionWrapper, msg);
        }
    }

    /**
     * 根据终端信息获取
     * @param byId 终端信息
     * @return 消息列表
     */
    private List<String> getMsgList(BaseTerminal byId) {
        TerminalInfoModel[] infoList = byId.getInfoList();
        if (infoList == null || infoList.length == 0) {
            return null;
        }
        HashSet<String> types = new HashSet<>();
        for (TerminalInfoModel type : infoList) {
            types.add(type.getInfoType());
        }
        return infoServices.stream()
                .filter(s -> s.processHave(types))
                .map(s -> s.list(byId, types))
                .flatMap(Collection::stream)
                .map(TerminalMsg::toMsg)
                .collect(Collectors.toList());
    }

    /**
     * 添加会话缓存
     * @param terminalId 终端id
     * @param session 会话
     * @return 会话包装类
     */
    private synchronized SessionWrapper addSession(String terminalId, Session session) {
        Map<String, SessionWrapper> map = TERMINAL_MAP.computeIfAbsent(terminalId, k -> new HashMap<>());
        SessionWrapper sessionWrapper = new SessionWrapper(session);
        map.put(session.getId(), sessionWrapper);
        return sessionWrapper;
    }

    @Override
    public synchronized void removeSession(String terminalId, Session session) {
        Map<String, SessionWrapper> map = TERMINAL_MAP.get(terminalId);
        if (map != null) {
            map.remove(session.getId());
            if (map.isEmpty()) {
                TERMINAL_MAP.remove(terminalId);
            }
        }
    }

    /**
     * 获取会话
     * @param terminalId 终端id
     * @return 会话集合
     */
    @Nullable
    private synchronized Collection<SessionWrapper> getSession(String terminalId) {
        Map<String, SessionWrapper> map = TERMINAL_MAP.get(terminalId);
        if (map == null) {
            return null;
        }
        return map.values();
    }
    private synchronized Collection<String> getTerminalId() {
        return TERMINAL_MAP.keySet();
    }

    /**
     * 发送消息
     * @param session 会话包装类
     * @param msg 消息
     */
    private  void sentMessage(SessionWrapper session, String msg) {
        session.sendMsg(msg);
    }

    /**
     * session包装类
     */
    @Slf4j
    private static class SessionWrapper {
        private final Session session;
        public SessionWrapper(Session session) {
            this.session = session;
        }
        public String getId() {
            return session.getId();
        }
        public synchronized void sendMsg(String msg) {
            if (msg == null) {
                return;
            }
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                log.error("发送消息设备", e);
            }
        }
    }
    private static class JobTerminalMsg{
        private String id;
        private List<String> message;

        public String getId() {
            return id;
        }

        public List<String> getMessage() {
            return message;
        }

        public JobTerminalMsg(String id, List<String> message) {
            this.id = id;
            this.message = message;
        }
    }
    private static class JobTerminalMsgSession {
        private SessionWrapper sessionWrapper;
        private List<String> message;

        public JobTerminalMsgSession(SessionWrapper sessionWrapper, List<String> message) {
            this.sessionWrapper = sessionWrapper;
            this.message = message;
        }

        public SessionWrapper getSessionWrapper() {
            return sessionWrapper;
        }

        public List<String> getMessage() {
            return message;
        }
    }




}
