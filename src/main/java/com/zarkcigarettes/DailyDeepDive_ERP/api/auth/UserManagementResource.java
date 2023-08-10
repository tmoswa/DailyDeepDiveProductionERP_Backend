package com.zarkcigarettes.DailyDeepDive_ERP.api.auth;

import com.zarkcigarettes.DailyDeepDive_ERP.api.util.GenericResponse;
import com.zarkcigarettes.DailyDeepDive_ERP.api.util.Response;
import com.zarkcigarettes.DailyDeepDive_ERP.auth.UserService;
import com.zarkcigarettes.DailyDeepDive_ERP.jwt.JwtConfig;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.UserRepository;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Role;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.Subsidiary;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.User;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.VerificationToken;
import com.zarkcigarettes.DailyDeepDive_ERP.registration.OnRegistrationCompleteEvent;
import com.zarkcigarettes.DailyDeepDive_ERP.registration.listener.RegistrationListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.crypto.SecretKey;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
@Slf4j
public class UserManagementResource {

    private final UserService userService;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final UserRepository userRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;




    @GetMapping(path = "/getloggedName")
    @PreAuthorize("hasAuthority('PRIVILEGE-USERS-READ')")
    public ResponseEntity<Response> getloggedName(Authentication authentication) {
Optional<User> user=userService.selectUserByEmail(authentication.getName());
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("name",user.get().getFirstName()))
                        .message("successfully retrieved logged in name")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping
    @PreAuthorize("hasAuthority('PRIVILEGE-USERS-READ')")
    public ResponseEntity<Response> getUsers() {
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("users",userService.getUsers()))
                        .data_corresponding(of("roles",userService.roleList(30)))
                        .data_corresponding0(of("subsidiaries",userService.subsidiaryList(30)))
                        .data_corresponding1(of("designations",userService.designationList(30)))
                        .message("successfully retrieved all roles")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping(path = {"/profile"})
    @PreAuthorize("hasAuthority('PRIVILEGE-USERS-READ')")
    public ResponseEntity<Response> getUserProfile(Authentication authentication) {
        Optional<User> user=userService.selectUserByEmail(authentication.getName());
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("user",user))
                        .message("successfully retrieved user")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    @PostMapping()
    @PreAuthorize("hasAuthority('PRIVILEGE-USERS-ALTER')")
    public ResponseEntity<Response> createUser(@RequestBody User user,final HttpServletRequest request) {
        user.setPassword("D&*^$%@tDJ");
        user.setMatchingPassword("D&*^$%@tDJ");
        User userDetailes=userService.saveUser(user);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userDetailes, request.getLocale(), getAppUrl(request)));

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("userDetails",userDetailes))
                        .message("successfully created user")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );


    }

    @PutMapping(path = "{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-USERS-ALTER')")
    public ResponseEntity<Response> updateUser(@PathVariable("id") Long userID,@RequestBody User user) {
        log.info(String.format("user body details %s", user.toString()));
        boolean updatedSuccessfully=userService.update(userID,user);
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("roles",updatedSuccessfully))
                        .message(updatedSuccessfully?"successfully updated user":"Error in updating user, Either User is already taken or does not exist to be updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PutMapping(path = "updateProfile/{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE-USERS-ALTER')")
    public ResponseEntity<Response> updateUserProfile(@PathVariable("id") Long userID,@RequestBody ProfileUpdateForm user) {

        String password = user.getPassword();
        String secret = user.getSecret();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();

        log.info(String.format("update details %s : ",user));

        User userToUpdate = userRepository.findById(userID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("user with id %d not found", userID)));
if(!password.equals("")){
    userService.updateUserCredentials(userToUpdate, password, secret);
}


        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);
        userToUpdate.setEmail(email);


        boolean updatedSuccessfully=userService.update(userID,userToUpdate);;
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message(updatedSuccessfully?"successfully updated user, you need to login again!":"failed to update user Details")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }



    @DeleteMapping()
    @PreAuthorize("hasAuthority('PRIVILEGE-SUBSIDIARIES-DELETE')")
    public ResponseEntity<Response> deleteUser(User user){

        boolean successfullyDeletedUser= userService.deleteUser(user);

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("users",successfullyDeletedUser))
                        .message(successfullyDeletedUser?"successfully deleted user":"Error in deleting user, either already deleted or does not not exist")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }




    @PostMapping("/saveUsers2")
    public GenericResponse saveUser2(@RequestBody @Valid final User user, final HttpServletRequest request){

        log.info(String.format("Tirimuno user details %s ",user.toString()));
        URI uri= URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString());
        User registered=userService.saveUser(user);
        //userService.addUserLocation(registered, getClientIP(request));
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return new GenericResponse("success");
    }


    // User activation - verification
    @GetMapping("/resendRegistrationToken")
    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }
    @Async
    public  void sendEmailMessage(String to, String buildEmail) {
        try{
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(buildEmail,true);
            helper.setTo(to);
            helper.setSubject("Registration Confirmation");
            helper.setFrom(env.getProperty("support.email"));
            mailSender.send(mimeMessage);
        }catch(MessagingException e){
            throw new IllegalStateException("failed to send email");
        }
    }
    @PostMapping("/saveUsers")
    public ResponseEntity<User>saveUser(@RequestBody User user, final HttpServletRequest request){
        log.info("Tirimuno");
        URI uri= URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/saveUsers").toUriString());
       return ResponseEntity.created(null).body(userService.saveUser(user));
    }

    @PostMapping("/addRole")
public ResponseEntity<Role>saveRole(@RequestBody Role role){
URI uri= URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/role/addRole").toUriString());
return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/addSubsidiary")
    public ResponseEntity<Subsidiary>saveSubsidiary(@RequestBody Subsidiary subsidiary){
        URI uri= URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/role/addSubsidiary").toUriString());
        return ResponseEntity.created(uri).body(userService.saveSubsidiary(subsidiary));
    }

    @PostMapping("/AddRoleToUser")
    public ResponseEntity<?>saveRoleToUser(@RequestBody RoleToUserAttributesForm form){
        userService.addRoleToUser(form.getUsername(),form.getRoleName());
        return  ResponseEntity.ok().build();
    }

    @PostMapping("/addSubsidiaryToUser")
    public ResponseEntity<?>saveSubsidiaryToUser(@RequestBody SubsidiaryToUserAttributesForm form){
        userService.addSubsidiaryToUser(form.getUsername(),form.getSubsidiary());
        return  ResponseEntity.ok().build();
    }


    @GetMapping("/registrationConfirm/{username}")
    public ResponseEntity<Response> resetPassword(@PathVariable("username") String username,final HttpServletRequest request) {
        User userDetails=userService.getUser(username);
        final VerificationToken newToken = userService.generateNewVerificationTokenByUser(userDetails);
        final User user = userService.getUserByToken(newToken.getToken());
        log.info(String.format("token is %s ",newToken.getToken()));
        final String confirmationUrl = env.getProperty("frontend.appURL")+ "confirmRegistration?token=" + newToken.getToken();
        sendEmailMessage(user.getEmail(),RegistrationListener.buildEmail(user.getFirstName(),confirmationUrl,"You have been requested to reset your password with Cavendish Lloyd Global Enterprise System. Please click on the link/button below to set new Password: "));

        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message("Password Reset link sent to user email")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }



    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            throw new IllegalStateException("refresh token is missin");
        }
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try {
            Jws<Claims> claimsJws =
                    Jwts.parser()
                            .setSigningKey(secretKey)
                            .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            String username = body.getSubject();
            User user=userService.getUser(username);
String refresh_token=authorizationHeader.substring(jwtConfig.getTokenPrefix().length());
            String access_token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .claim("authorities",user.getRoles() )
                    .claim("subsidiaries",user.getSubsidiaries() )
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                    .signWith(secretKey)
                    .compact();
            //   response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
            //   response.addHeader("refresh_token", jwtConfig.getTokenPrefix() + refresh_token);

            Map<String,String> tokens=new HashMap<>();
            tokens.put("token",access_token);
            tokens.put("refresher_token",refresh_token);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        } catch (JwtException | IOException e) {
            response.setHeader("error",e.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String,String> error=new HashMap<>();
            error.put("error_message",e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),error);
        }

    }

    // ============== NON-API ============



    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    private String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        log.info(String.format("user 1  %s , to the database", user.toString()));

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}

@Data
class RoleToUserAttributesForm{
    private String username;
    private String roleName;
}


@Data
class SubsidiaryToUserAttributesForm{
    private String username;
    private String subsidiary;
}

@Data
class ProfileUpdateForm{
    private String firstName;
    private String lastName;

    private String email;
    private String password;
    private String passwordConfirmation;
    private String secret;


}

