package com.hss.core.common.aspect;

import com.hss.core.common.api.CommonAPI;
import com.hss.core.common.aspect.annotation.PermissionData;
import com.hss.core.common.constant.CommonConstant;
import com.hss.core.common.constant.SymbolConstant;
import com.hss.core.common.system.util.HssDataAutorUtils;
import com.hss.core.common.system.util.JwtUtil;
import com.hss.core.common.system.vo.SysPermissionDataRuleModel;
import com.hss.core.common.system.vo.SysUserCacheInfo;
import com.hss.core.common.util.OConvertUtils;
import com.hss.core.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 数据权限切面处理类
 *  当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
 * @Date 2019年4月10日
 * @Version: 1.0
 * @author: jeecg-boot
 */
@Aspect
@Component
@Slf4j
public class PermissionDataAspect {
    @Lazy
    @Autowired
    private CommonAPI commonApi;

    @Pointcut("@annotation(com.hss.core.common.aspect.annotation.PermissionData)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object arround(ProceedingJoinPoint point) throws  Throwable{
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        PermissionData pd = method.getAnnotation(PermissionData.class);
        String component = pd.pageComponent();
        String requestMethod = request.getMethod();
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
        requestPath = filterUrl(requestPath);
        //update-begin-author:taoyan date:20211027 for:JTC-132【online报表权限】online报表带参数的菜单配置数据权限无效
        //先判断是否online报表请求
        // TODO 参数顺序调整有隐患
        if(requestPath.indexOf(UrlMatchEnum.CGREPORT_DATA.getMatchUrl())>=0){
            // 获取地址栏参数
            String urlParamString = request.getParameter(CommonConstant.ONL_REP_URL_PARAM_STR);
            if(OConvertUtils.isNotEmpty(urlParamString)){
                requestPath+="?"+urlParamString;
            }
        }
        //update-end-author:taoyan date:20211027 for:JTC-132【online报表权限】online报表带参数的菜单配置数据权限无效
        log.info("拦截请求 >> {} ; 请求类型 >> {} . ", requestPath, requestMethod);
        String username = JwtUtil.getUserNameByToken(request);
        //查询数据权限信息
        //TODO 微服务情况下也得支持缓存机制
        List<SysPermissionDataRuleModel> dataRules = commonApi.queryPermissionDataRule(component, requestPath, username);
        if(dataRules!=null && dataRules.size()>0) {
            //临时存储
            HssDataAutorUtils.installDataSearchConditon(request, dataRules);
            //TODO 微服务情况下也得支持缓存机制
            SysUserCacheInfo userinfo = commonApi.getCacheUser(username);
            HssDataAutorUtils.installUserInfo(request, userinfo);
        }
        return  point.proceed();
    }

    private String filterUrl(String requestPath){
        String url = "";
        if(OConvertUtils.isNotEmpty(requestPath)){
            url = requestPath.replace("\\", "/");
            url = url.replace("//", "/");
            if(url.indexOf(SymbolConstant.DOUBLE_SLASH)>=0){
                url = filterUrl(url);
            }
			/*if(url.startsWith("/")){
				url=url.substring(1);
			}*/
        }
        return url;
    }
}
