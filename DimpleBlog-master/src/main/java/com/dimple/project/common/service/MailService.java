package com.dimple.project.common.service;

import org.thymeleaf.context.Context;

/**
 * @className: MailService
 * @description:
 * @auther: Dimple
 * @date: 2019/7/18
 * @version: 1.0
 */
public interface MailService {
    /**
     * 发送纯文本邮件
     *
     * @param to      邮件地址
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送带附件的邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendAttachmentMail(String to, String subject, String content, String filePath, boolean isHTML);


    void sendReplyEmail(String to, Integer id, String originalContent, String content, String url);
    
    
    void sendTemplateMail(String to, String subject, String templateName,Context context);

}
