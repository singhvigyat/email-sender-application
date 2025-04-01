//package com;
//
//import com.example.demo.DemoApplication;
//import com.services.EmailService;
//import com.services.Impl.EmailServiceImpl;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Import;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//@SpringBootTest(classes = DemoApplication.class)
//@Import(EmailServiceImpl.class)  // Explicitly import the implementation
//public class EmailSenderTest {
//
//    @Autowired
//    private EmailService emailService;
//
//    @Test
//    void emailSenderTest() {
//        System.out.println("Sending Email");
//        emailService.sendEmail("tanmaybukka007@gmail.com", "Test Subject", "Test Body");
//    }
//
//    @Test
//    void sendHtmlEmail(){
//        emailService.sendEmailWithHtml("tanmaybukka007@gmail.com","Test Subject", "<h1 style='color:red'>Test Body</h1> ", "true");
//    }
//
//    @Test
//    void sendEmailWithFile(){
//        emailService.sendEmailWithFile("tanmaybukka007@gmail.com",
//                "Email With file",
//                "This email contains file",
//                new File("D:\\dev\\Email Sender Application\\demo\\src\\main\\resources\\static\\iiitp logo.jpg") );
//    }
//
//    @Test
//    void sendEmailWithFileWithStream(){
//        File file= new File("D:\\dev\\Email Sender Application\\demo\\src\\main\\resources\\static\\iiitp logo.jpg") ;
//        try {
//            InputStream is=new FileInputStream(file);
//            emailService.sendEmailWithFile("tanmaybukka007@gmail.com",
//                    "Email With file",
//                    "This email contains file",
//                    is
//            );
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}

package com;

import com.example.demo.DemoApplication;
import com.services.EmailService;
import com.services.Impl.EmailServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest(classes = DemoApplication.class)
@Import(EmailServiceImpl.class)
public class EmailSenderTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${test.email.recipient:your-test-email@example.com}")
    private String testRecipient;

    @Test
    void emailSenderTest() {
        System.out.println("Sending Email");
        emailService.sendEmail(testRecipient, "Test Subject", "Test Body");
    }

    @Test
    void sendHtmlEmail() {
        emailService.sendEmailWithHtml(testRecipient, "Test Subject", "<h1 style='color:red'>Test Body</h1>", "true");
    }

    @Test
    void sendEmailWithFile() {
        try {
            File file = resourceLoader.getResource("classpath:static/test-image.jpg").getFile();
            emailService.sendEmailWithFile(testRecipient, "Email With file", "This email contains file", file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
//    void sendEmailWithFileWithStream() {
//        try {
//            InputStream is = resourceLoader.getResource("classpath:static/test-image.jpg").getInputStream();
//            emailService.sendEmailWithFile(testRecipient, "Email With file", "This email contains file", is);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}