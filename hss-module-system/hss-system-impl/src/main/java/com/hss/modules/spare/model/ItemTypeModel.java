package com.hss.modules.spare.model;

import com.hss.core.common.system.vo.SelectTreeNode;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料类型下拉树 model
 *
 * @author wuyihan
 */
@Data
public class ItemTypeModel implements Serializable {

    private String id;
    private String parentId;
    private String typeName;
    private Integer status;
    private Integer orderNum;

    private List<ItemTypeModel> children = new ArrayList<>();

    public void addChild(ItemTypeModel child){
        if (this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }


}
