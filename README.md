### Alibaba Cloud IDaaS Device Code Flow Sample

阿里云IDaaS EIAM实现了RFC8628-OAuth 2.0 Device Authorization Grant(https://datatracker.ietf.org/doc/html/rfc8628)
规范，可以在打印机、机顶盒等场景下进行设备登录，本示例演示了Java程序在终端显示QR码进行账号登录。

测试该功能需要在阿里云IDaaS EIAM创建一个应用，过程如下：

1. 在阿里云IDaaS产品创建实例(https://yundun.console.aliyun.com/?p=idaas#/overview/new/cn-hangzhou)
2. 创建一个OIDC应用： "应用" -> "添加应用" -> "标准协议" -> 添加"OIDC"应用
3. 在应用 "单点登录" -> "授权模式" 中勾选中 "设置模式"

测试本示例，运行命令行如下：

```shell
clientId=app_miml7nkm****** \
clientSecret=CS2sE4kK9S****** \
issuer='https://eiam-api-cn-hangzhou.aliyuncs.com/v2/idaas_paxf******/app_miml******/oidc' \
mvn compile exec:java -Dexec.mainClass=com.aliyun.idaas.device.DeviceCodeFlowApplication
```

其中参数说明如下：

| 字段 | 说明                                |
| ---- |-----------------------------------|
| clientId | IDaaS EIAM "通用配置" -> "client_id"  |
| clientSecret | IDaaS EIAM "通用配置" -> "client_secret" |
| issuer | IDaaS EIAM "单点登录" -> "应用配置信息" -> "Issuer" |

License MIT
