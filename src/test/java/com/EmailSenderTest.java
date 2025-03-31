package com;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
public class EmailSenderTest {

    @SpringBootApplication
    static class TestConfig {}

    @Autowired
    private com.manager.smartcontactmanager.services.EmailService emailService;

    @Test
    void emailSenderTest() {
        System.out.println("Sending Email");
        emailService.sendEmail("tanmaybukka007@gmail.com", "Test Subject", "Test Body");
    }

    @Test
    void sendHtmlEmail(){
        emailService.sendEmailWithHtml("tanmaybukka007@gmail.com","Test Subject", "<h1 style='color:red'>Test Body</h1> ", "true");
    }

    @Test
    void sendEmailWithFile(){
        emailService.sendEmailWithFile("tanmaybukka007@gmail.com",
                "Email With file",
                "This email contains file",
                new File("D:\\dev\\Email Sender Application\\demo\\src\\main\\resources\\static\\iiitp logo.jpg") );
    }


    @Test
    void sendEmailWithFileWithStream(){
        File file= new File("D:\\dev\\Email Sender Application\\demo\\src\\main\\resources\\static\\iiitp logo.jpg") ;
        try {
            InputStream is=new FileInputStream(file);
            emailService.sendEmailWithFile("tanmaybukka007@gmail.com",
                    "Email With file",
                    "This email contains file",
                    is
            );
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



}