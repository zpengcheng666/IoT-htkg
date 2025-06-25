package com.hss.config;

import com.hss.modules.scada.model.SubModule;
import com.hss.modules.scada.model.SubSystem;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: scada 配置信息
 * @Author: hd  chushubin
 * @Date: 2022-11-03
 * @Version: V1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "scada")
public class ScadaConfigProperties {

    /**
     * 摄像机类型
     */
    private List<String> cameraTypeList;

    /**
     * 广播设备类型
     */
    private List<String> publishTypeList;

    /**
     * 模块信息，用于场景管理中
     */
    private List<SubSystem> subSystem;

    /**
     * 获取子系统名字
     * @param subSystemId
     * @return
     */
    public String getSubSystemName(String subSystemId){
        if (this.subSystem == null || this.subSystem.isEmpty())
            return "N/A";

        List<SubSystem> tmpLists = subSystem.stream().filter(e -> e.getId().equals(subSystemId)).collect(Collectors.toList());

        if (tmpLists.isEmpty())
            return "N/A";

        return tmpLists.get(0).getName();
    }

    /**
     * 获取子系统下的模块的名字
     * @param subSystemId
     * @param moduleId
     * @return
     */
    public String getModuleName(String subSystemId, String moduleId){
        if (this.subSystem == null || this.subSystem.isEmpty())
            return "N/A";

        List<SubSystem> tmpLists = this.subSystem.stream().filter(e -> e.getId().equals(subSystemId)).collect(Collectors.toList());

        if (tmpLists.isEmpty())
            return "N/A";

        List<SubModule> subModules = tmpLists.get(0).getSubModules().stream().filter(e -> e.getId().equals(moduleId)).collect(Collectors.toList());

        if (subModules.isEmpty())
            return "N/A";

        return subModules.get(0).getName();
    }
}
