package com.zarkcigarettes.DailyDeepDive_ERP.api.auth;

import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.auth.UserService;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.User;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.VerificationToken;
import com.zarkcigarettes.DailyDeepDive_ERP.registration.listener.RegistrationListener;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("api3/cl")
@Slf4j
public class PasswordReset {
    private final UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @PostMapping("/passwordReset")
    public ResponseEntity<Response> resetPassword(@RequestBody EmailResetForm email, final HttpServletRequest request) {
        log.info(email.getEmail());
        User userDetails=userService.findUserByEmail(email.getEmail());
        final VerificationToken newToken = userService.generateNewVerificationTokenByUser(userDetails);
        final User user = userService.getUserByToken(newToken.getToken());
        log.info(String.format("token is %s ",newToken.getToken()));
        final String confirmationUrl = env.getProperty("frontend.appURL")+ "confirmRegistration?token=" + newToken.getToken();
       boolean sentMail= sendEmailMessage(user.getEmail(), RegistrationListener.buildEmail(user.getFirstName(),confirmationUrl,"You have been requested to reset your password in DDD Production System. Please click on the link/button below to set new Password: "));

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message(sentMail?"Password Reset link sent to user email":"failed to send password reset link to email, check if you are registered in the system")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/registrationConfirm")
    public ResponseEntity<Response> getAllSubsidiary(@RequestBody RegConformationAndPasswordReset resetCredentials) {
        log.info(String.format("body details %s", resetCredentials.toString()));

        String token = resetCredentials.getToken();
        String password = resetCredentials.getPassword();
        String secret = resetCredentials.getSecret();

        final String result = userService.validateVerificationToken(token);

        if (result.equals("valid")) {
            final User user = userService.getUserByToken(token);
            userService.updateUserCredentials(user, password, secret);
        }
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message(result.equals("valid") ? "successfully reset password, you can now login" : "Failed to reset account, token might have expired or incorrect")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @Async
    public  boolean sendEmailMessage(String to, String buildEmail) {
        try{
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(buildEmail,true);
            helper.setTo(to);
            helper.setSubject("Registration Confirmation");
            helper.setFrom(env.getProperty("support.email"));
            mailSender.send(mimeMessage);
        }catch(MessagingException e){
            return false;
            //throw new IllegalStateException("failed to send email");
        }
        return true;
    }



}
@Data
class RegConformationAndPasswordReset {
    private String token;
    private String secret;
    private String password;
}

@Data
class EmailResetForm{
    private String email;
}