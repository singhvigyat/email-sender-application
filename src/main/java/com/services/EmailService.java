package com.manager.smartcontactmanager.services;
import java.io.File;
import java.io.InputStream;

public interface EmailService {
    // send email to single user
    void sendEmail(String to, String subject, String body);

    // send email to multiple users
    void sendEmail(String[] to, String subject, String body);

    // send email to multiple users with html body
    void sendEmailWithHtml(String to, String subject, String body, String isHtml);

    // send email to multiple users with attachment
    void sendEmailWithFile(String to, String subject, String body, File attachmentPath);

    void sendEmailWithFile(String to, String subject, String message, InputStream is);
}