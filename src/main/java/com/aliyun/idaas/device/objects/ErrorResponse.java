package com.aliyun.idaas.device.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Alibaba Cloud IDaaS
 *
 * @author hatterjiang
 */
public class ErrorResponse {
    private String error;
    @SerializedName("error_description")
    private String errorDescription;
    @SerializedName("error_uri")
    private String errorUri;
    @SerializedName("request_id")
    private String requestId;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorUri() {
        return errorUri;
    }

    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
