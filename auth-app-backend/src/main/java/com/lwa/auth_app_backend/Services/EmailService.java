package com.lwa.auth_app_backend.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
//    private final SimpleMailMessage simpleMailMessage;
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail,String name){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Welcome to Secure-App Core");
        simpleMailMessage.setText("Hello "+name+"\n\n thanks for using Secure-Auth app, \n\n Regards,\n Secure-Auth Core team");
        javaMailSender.send(simpleMailMessage);
    }

    public void sendResetOtp(String toEmail,String otp){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset Otp");
        message.setText("OTP to reset your password is: "+otp+"\n The OTP is valid for 15 minutes only");
        javaMailSender.send(message);
    }

    public void sendPasswordUpdatedConfirmationEmail(String toEmail,String name){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Updated Successfully");
        message.setText("Dear "+name+" your password was updated successfully \n\n Regards \n Secure-Auth Core team");
        javaMailSender.send(message);
    }
}
