package org.jeecg.common.util.minio;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MultiPartUploadResp {
    private String uploadId;
    private List<PartFileResp> parts = new ArrayList<>();
    private String originalFileName;
    private String md5;
    private String path;
    private Date createdAt;
    private String createdBy;
    private int partCount;

}
