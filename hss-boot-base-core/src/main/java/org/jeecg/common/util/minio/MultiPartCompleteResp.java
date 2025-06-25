package org.jeecg.common.util.minio;

import lombok.Data;

@Data
public class MultiPartCompleteResp {
    private String uploadId;
    private boolean succeed;
}
