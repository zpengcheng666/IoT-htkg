package com.hss.modules.scada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zpc
 * @version 1.0
 * @description: 设备表，查询场景下的所有广播设备、根据场景id查询设备、查询所有摄像头、查询关联的变量、根据设备id查询设备类型ids、
 * 根据子系统查询设备、根据场景id和属性enName查询设备id和设备名称、通过类型和场景id查询设备名称，英文名、别名、根据设备类型统计、
 * 查询网关设设备的关联关系、查询点位设备id、设备类型是否存在、根据子系统查询子系统下的设备数量
 * @date 2024/3/19 15:58
 */
public interface ConSheBeiMapper extends BaseMapper<ConSheBei> {

    /**
     * 分页查询
     * @param page
     * @param sceneId
     * @param name
     * @return
     */
    IPage<ConSheBei> queryPage(Page<ConSheBei> page, @Param("sceneId") String sceneId,
                               @Param("name") String name,@Param("deviceTypeId") String deviceTypeId);

    /**
     * 根据场景id查询
     * @param sceneId
     * @return
     */
    List<ConSheBei> listBySceneId(String sceneId);

    /**
     * 查询场景下的所有广播设备
     * @param deviceTypes
     * @param sceneId     场景id
     * @return
     */
    List<ConSheBei> listPublishBySceneId(@Param("deviceTypes") List<String> deviceTypes, @Param("sceneId") String sceneId);

    /**
     * 查询所有摄像头
     * @param deviceTypes
     * @return
     */
    List<ConSheBei> ListCamera(@Param("deviceTypes") List<String> deviceTypes);

    /**
     * 分页查询关联的变量
     * @param page
     * @param deviceIds
     * @param name
     * @return
     */
    IPage<ConSheBei> pageVariable(Page<ConSheBei> page, @Param("deviceIds") List<String> deviceIds, @Param("name") String name);

    /**
     * 根据设备id查询设备类型ids
     * @param deviceIds
     * @return
     */
    List<ConSheBeiDoorOptions> listDeviceTypeByDeviceIds(List<String> deviceIds);

    /**
     * 根据子系统查询设备
     * @param subsystem
     * @return
     */
    List<String> listIdBySubsystem(String subsystem);

    /**
     * 根据设备类型查询
     * @param typeId
     * @return
     */
    List<ConSheBei> listByDeviceTypeId(String typeId);

    /**
     * 设备列表
     * @param page
     * @param dto
     * @return
     */
    IPage<DeviceListVO> deviceList(Page<DeviceListVO> page, @Param("dto") DeviceListDTO dto, @Param("ids") List<String> Ids);

    /**
     * 分页查询没有在场景中的设备
     * @param page
     * @param name
     * @return
     */
    IPage<ConSheBei> notScenePage(Page<ConSheBei> page, @Param("name") String name,@Param("deviceTypeId") String deviceTypeId);


    /**
     * 查询没在场景中的设备id
     * @param name
     * @return
     */
    List<String> ListIdByNotScenePage(@Param("name") String name);


    /**
     * 根据场景id和属性enName查询设备id和设备名称
     * @param sceneIds   场景ids
     * @param attrEnName 属性英文名称
     * @return
     */
    List<ConSheBei> listDeviceIdAndNameBySceneIdsAndAttrEnName(@Param("sceneIds") List<String> sceneIds, @Param("attrEnName") String attrEnName);

    /**
     * 设备类型是否存在
     * @param deviceTypeId
     * @return
     */
    int countByDeviceTypeId(String deviceTypeId);

    /**
     * 查询网关设设备的关联关系
     * @param attrEnName 属性英文名称
     * @return 关联关系
     */
    List<GatewayDevice> listGatewayAndDeviceByEnName(String attrEnName);

    /**
     * 查询点位设备id
     * @param deviceId   设备id
     * @param attrEnName 属性英文名
     * @return 点位设备id
     */
    String getPointDeviceIdByDeviceIdAndAttrEnName(@Param("deviceId") String deviceId, @Param("attrEnName") String attrEnName);

    /**
     * 根据设备类型统计
     * @return 统计列表
     */
    List<StateByDeviceTypeDeviceBO> statByDeviceType();

    /**
     * 根据子系统查询子系统下的设备数量
     * @return 统计列表
     */
    List<StateByDeviceTypeDeviceBO> statByDevices(@Param("subSystems") String subSystems);

    /**
     * 属性查询
     * @return
     */
    List<ConDeviceAttribute> listAttrIdAndDeviceIdBySceneId(String sceneId);

    /**
     * 通过类型和场景id查询设备名称，英文名、别名
     * @return
     */
    List<ConSheBei> listBySceneIdAndType(@Param("sceneId") String sceneId, @Param("types") List<String> types);
}
