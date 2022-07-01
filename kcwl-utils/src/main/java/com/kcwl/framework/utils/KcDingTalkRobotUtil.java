package com.kcwl.framework.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 钉钉机器人发消息
 *
 * 简单发送 {@link KcDingTalkRobotUtil#sendText(java.lang.String, java.lang.String)}
 * 复杂发送 {@link KcDingTalkRobotUtil#send(KcDingTalkRobotUtil.Message)}
 */
@Slf4j
public class KcDingTalkRobotUtil {

    /**
     * 钉钉机器人发消息
     *
     * @param webhook 钉钉机器人回调地址
     * @param content 内容
     */
    public static HttpResponse sendText(String webhook, String content) {
        return send(webhook, null, content, MessageType.text);
    }

    /**
     * 钉钉机器人发消息
     *
     * @param webhook 钉钉机器人回调地址
     * @param secret  密钥
     * @param content 内容
     */
    public static HttpResponse sendText(String webhook, String secret, String content) {
        return send(webhook, secret, content, MessageType.text);
    }


    /**
     * 钉钉机器人发消息 markdown 格式
     *
     * @see KcDingTalkRobotUtil#sendText(String, String)
     */
    public static HttpResponse sendMarkdown(String webhook, String content) {
        return send(webhook, null, content, MessageType.markdown);
    }

    /**
     * 钉钉机器人发消息 markdown 格式
     *
     * @see KcDingTalkRobotUtil#sendText(String, String, String)
     */
    public static HttpResponse sendMarkdown(String webhook, String secret, String content) {
        return send(webhook, secret, content, MessageType.markdown);
    }

    // 发送
    private static HttpResponse send(String webhook, String secret, String content, MessageType msgType) {
        JSONObject body = new JSONObject();
        body.set("msgtype", msgType.name());
        body.putByPath("text.content", content);

        String url = webhook;
        if (StrUtil.isNotBlank(secret)) {
            long timestamp = System.currentTimeMillis();
            url += String.format("&timestamp=%s&sign=%s", timestamp, getSign(timestamp, secret));
        }

        return HttpRequest.post(url)
                .contentType("application/json")
                .body(body.toString())
                .execute();
    }


    // 获取 builder，用于复杂发送
    public static Message.MessageBuilder getBuilder() {
        return Message.builder();
    }

    //

    /**
     * 复杂发送
     *
     * <pre>{@code
     *     DingTalkRobotUtil.getBuilder()
     *             .webhook("https://oapi.dingtalk.com/robot/send?access_token=xxxxxxx")
     *             .secret("你的签名，没有设置则不填")
     *             .msgType(MessageType.text)
     *             .content("发送内容")
     *             .atMobiles(Arrays.asList("18211112222", "18833334444"))
     *             .build()
     *             .send();
     *
     * }</pre>
     */
    private static HttpResponse send(Message message) {

        JSONObject body = new JSONObject();
        body.set("msgtype", message.msgType.name());
        body.putByPath("text.content", message.getContent());

        if (CollUtil.isNotEmpty(message.getAtMobiles())) {
            body.putByPath("at.atMobiles", message.getAtMobiles());
        } else if (message.atAll) {
            body.putByPath("at.isAtAll", true);
        }

        String url = message.getWebhook();
        if (StrUtil.isNotBlank(message.getSecret())) {
            long timestamp = System.currentTimeMillis();
            url += String.format("&timestamp=%s&sign=%s", timestamp, getSign(timestamp, message.getSecret()));
        }

        return HttpRequest.post(url)
                .contentType("application/json")
                .body(body.toString())
                .execute();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {

        // 回调地址
        private String webhook;

        // 内容类型
        private MessageType msgType;

        // 密钥（签名用）
        private String secret; // 设置签名时填写

        // 内容
        private String content;

        // @手机号
        private List<String> atMobiles;

        // @所有人，会被 手机号覆盖
        private boolean atAll = false;

        public HttpResponse send() {
            return KcDingTalkRobotUtil.send(this);
        }
    }

    // 消息内容
    public enum MessageType {
        text,
        markdown,
    }

    /**
     * 获取签名
     *
     * @param timestamp 时间戳
     * @param secret    密钥
     */
    public static String getSign(long timestamp, String secret) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return URLEncoder.encode(Base64.encode(signData), "UTF-8");
        } catch (Exception e) {
            log.error("获取签名失败", e);
            return null;
        }
    }

    // 检查钉钉机器人 返回
    public static void checkDingTalkHttpResponse(HttpResponse response) {
        if (response == null) {
            log.error("ding_talk_response is null");
        } else if (response.getStatus() != 200) {
            log.error("ding_talk_response failed, status: {}, body: {}", response.getStatus(), response.body());
        } else {
            String responseBody = response.body();
            if (StrUtil.isBlank(responseBody)) {
                log.error("ding_talk_response failed body is empty, status: {}, body: {}", response.getStatus(), response.body());
            } else {
                if (!"ok".equals(new JSONObject(responseBody).get("errmsg"))) {
                    log.error("ding_talk_response failed, status: {}, body: {}", response.getStatus(), response.body());
                }
            }
        }
    }

}
