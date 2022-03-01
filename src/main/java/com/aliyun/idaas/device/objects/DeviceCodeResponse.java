package com.aliyun.idaas.device.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Alibaba Cloud IDaaS
 *
 * @author hatterjiang
 */
public class DeviceCodeResponse {
    @SerializedName("device_code")
    private String deviceCode;
    @SerializedName("user_code")
    private String userCode;
    @SerializedName("verification_uri")
    private String verificationUri;
    @SerializedName("verification_uri_complete")
    private String verificationUriComplete;
    @SerializedName("expires_in")
    private Long expiresIn;
    @SerializedName("expires_at")
    private Long expiresAt;
    private Long interval;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getVerificationUri() {
        return verificationUri;
    }

    public void setVerificationUri(String verificationUri) {
        this.verificationUri = verificationUri;
    }

    public String getVerificationUriComplete() {
        return verificationUriComplete;
    }

    public void setVerificationUriComplete(String verificationUriComplete) {
        this.verificationUriComplete = verificationUriComplete;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }
}
