package com.aliyun.idaas.device;

import com.aliyun.idaas.device.objects.DeviceCodeResponse;
import com.aliyun.idaas.device.objects.ErrorResponse;
import com.aliyun.idaas.device.objects.OpenIdConfiguration;
import com.aliyun.idaas.device.objects.Token;
import com.aliyun.idaas.device.util.TerminalQrUtil;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

/**
 * Alibaba Cloud IDaaS
 *
 * @author hatterjiang
 */
public class DeviceCodeFlowApplication {
    private static final String AUTHORIZATION_PENDING = "authorization_pending";
    private static final String SLOW_DOWN = "slow_down";

    static Gson gson = new Gson();
    static String clientId = getParameter("clientId");
    static String clientSecret = getParameter("clientSecret");
    static String issuer = getParameter("issuer");

    public static void main(String[] args) throws WriterException, IOException, InterruptedException {
        final String discovery = issuer + "/.well-known/openid-configuration";

        final OkHttpClient httpClient = new OkHttpClient();
        System.out.println("[INFO] Find device authorization endpoint");
        final OpenIdConfiguration openIdConfiguration = findOpenIdConfiguration(httpClient, discovery);
        final String deviceAuthorization = openIdConfiguration.getDeviceAuthorizationEndpoint();
        if (StringUtils.isEmpty(deviceAuthorization)) {
            throw new RuntimeException("Cannot find device authorization endpoint");
        }

        System.out.println("[INFO] Get device code");
        final DeviceCodeResponse deviceCodeResponse = getDeviceCode(httpClient, deviceAuthorization);

        System.out.println("SCAN QR:\n" + TerminalQrUtil.toTerminalQr(deviceCodeResponse.getVerificationUriComplete(), 10));
        System.out.println("OR open URL: " + deviceCodeResponse.getVerificationUriComplete());
        System.out.println("OR open URL: " + deviceCodeResponse.getVerificationUri() + " and input: " + deviceCodeResponse.getUserCode());

        Token token;
        for (; ; ) {
            Thread.sleep((deviceCodeResponse.getInterval() == null ? 2 : deviceCodeResponse.getInterval()) * 1000);

            token = getToken(httpClient, openIdConfiguration.getTokenEndpoint(), deviceCodeResponse.getDeviceCode());
            if (token != null) {
                break;
            }
        }

        System.out.println("[INFO] Found access token: " + token.getAccessToken());
        System.out.println("[INFO] Found id token: " + token.getIdToken());
        System.exit(0);
    }

    private static String getParameter(String key) {
        return StringUtils.defaultIfEmpty(System.getProperty(key), System.getenv(key));
    }

    private static Token getToken(OkHttpClient httpClient, String token, String deviceCode) throws IOException {
        final Request request = new Request.Builder()
                .url(token)
                .addHeader("ContentType", "application/x-www-form-urlencoded")
                .post(new FormBody.Builder()
                        .add("client_id", clientId)
                        .add("client_secret", clientSecret)
                        .add("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
                        .add("device_code", deviceCode)
                        .build())
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            final int responseCode = response.code();
            if (responseCode >= 400 && responseCode < 500) {
                final String responseBody = response.body().string();
                final ErrorResponse errorResponse = gson.fromJson(responseBody, ErrorResponse.class);
                if (!AUTHORIZATION_PENDING.equals(errorResponse.getError()) && !SLOW_DOWN.equals(errorResponse.getError())) {
                    throw new RuntimeException("Get token failed: " + responseBody);
                }
                return null;
            }
            if (responseCode != 200) {
                throw new RuntimeException("Get OpenID Configuration failed: " + responseCode);
            }
            final String responseBody = response.body().string();
            return gson.fromJson(responseBody, Token.class);
        }
    }

    private static DeviceCodeResponse getDeviceCode(OkHttpClient httpClient, String deviceAuthorization) throws IOException {
        final Request request = new Request.Builder()
                .url(deviceAuthorization)
                .addHeader("ContentType", "application/x-www-form-urlencoded")
                .post(new FormBody.Builder()
                        .add("client_id", clientId)
                        .add("scope", "openid email profile")
                        .build())
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            final int responseCode = response.code();
            if (responseCode != 200) {
                throw new RuntimeException("Get OpenID Configuration failed: " + responseCode);
            }
            final String responseBody = response.body().string();
            return gson.fromJson(responseBody, DeviceCodeResponse.class);
        }
    }

    private static OpenIdConfiguration findOpenIdConfiguration(OkHttpClient httpClient, String discovery) throws IOException {
        final Request request = new Request.Builder()
                .url(discovery)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            final int responseCode = response.code();
            if (responseCode != 200) {
                throw new RuntimeException("Get OpenID Configuration failed: " + responseCode);
            }
            final String responseBody = response.body().string();
            return gson.fromJson(responseBody, OpenIdConfiguration.class);
        }
    }
}
