package com.application.sushi.controller.Auth;

import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import com.application.sushi.domain.User;
import com.application.sushi.domain.request.ReqLoginDto;
import com.application.sushi.domain.request.ReqSignupDto;
import com.application.sushi.domain.response.ResLoginDto;
import com.application.sushi.domain.response.ResSignupDto;
import com.application.sushi.domain.response.ResUpdatePassword;
import com.application.sushi.service.EmailService;
import com.application.sushi.service.UserService;
import com.application.sushi.util.SecurityUtil;
import com.application.sushi.util.error.InvalidException;


import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;

    public AuthController (AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
        UserService userService, PasswordEncoder passwordEncoder, RedisTemplate<String, String> redisTemplate, EmailService emailService){
            this.authenticationManagerBuilder = authenticationManagerBuilder;
            this.securityUtil = securityUtil;
            this.userService = userService;
            this.passwordEncoder = passwordEncoder;
            this.redisTemplate = redisTemplate;
            this.emailService = emailService;
        }

    @PostMapping("/auth/signup")
    public ResponseEntity<ResSignupDto> signupPage(@RequestBody ReqSignupDto reqLoginDto) {
        User current_user = this.userService.convertDtoToUser(reqLoginDto) ;
        ResSignupDto resSignupDto = new ResSignupDto();
        resSignupDto.setName(current_user.getEmail());
        resSignupDto.setRole(current_user.getRole());
        current_user.setPassword(this.passwordEncoder.encode(current_user.getPassword()));
        this.userService.createNewUser(current_user);
        return ResponseEntity.ok().body(resSignupDto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDto> loginPage(@RequestBody ReqLoginDto reqLoginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(reqLoginDto.getUsername(), reqLoginDto.getPassword());
        // viết hàm loaduser
        Authentication authentication =  authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDto resLoginDto = new ResLoginDto();
        ResLoginDto.UserLogin user_login = new ResLoginDto.UserLogin();
        User current_user = this.userService.fetchUserByEmail(reqLoginDto.getUsername());
        if (current_user != null){
            user_login.setEmail(current_user.getEmail());
            user_login.setName(current_user.getName());
            user_login.setRole(current_user.getRole());
            user_login.setId(current_user.getId());
        }
        resLoginDto.setUser(user_login);
        String access_token = this.securityUtil.createAccessToken(reqLoginDto.getUsername(), resLoginDto);
        resLoginDto.setAccess_token(access_token);
        return ResponseEntity.ok().body(resLoginDto);
    }
    @PostMapping("/auth/forget")
    public ResponseEntity<String> forgetPage(@RequestBody Map<String, String> user_name){
        String email = user_name.get("email");
        User forgot_user = this.userService.fetchUserByEmail(email);
        if(forgot_user != null){
                String otp = this.securityUtil.createOtp();
            this.redisTemplate.opsForValue().set(email, otp,Duration.ofMinutes(3));
            this.emailService.sendVerificationEmail(email, "Your OTP", otp);
            return ResponseEntity.ok().body("300");
        }
        else{
            return ResponseEntity.badRequest().body("Your email is invalid");
        }
    }
    @PostMapping("/auth/forget/check")
    public ResponseEntity<ResLoginDto> checkOtp(@RequestBody Map<String, String> user_otp) throws InvalidException {
        String otp = user_otp.get("otp");
        String email = user_otp.get("email");
        String server_otp = this.redisTemplate.opsForValue().get(email);
        ResLoginDto resLoginDto = new ResLoginDto();
        ResLoginDto.UserLogin user_login = new ResLoginDto.UserLogin();
        if (otp == server_otp){
            User current_user = this.userService.fetchUserByEmail(email);
            user_login.setId(current_user.getId());
            user_login.setName(current_user.getName());
            user_login.setRole(current_user.getRole());
            user_login.setEmail(current_user.getEmail());
            resLoginDto.setUser(user_login);
            String user_token = this.securityUtil.createTokenForResetPassword(email, resLoginDto);
            resLoginDto.setAccess_token(user_token);
            return ResponseEntity.ok().body(resLoginDto);
        }
        else{
            throw new InvalidException("Your otp is incorrect");
        }
    }
    @PostMapping("/forget/check/reset")
    public ResponseEntity<ResUpdatePassword> newPasswordReset(@RequestBody ReqLoginDto reqLoginDto) {
        User current_user = this.userService.fetchUserByEmail(reqLoginDto.getUsername());
        current_user.setPassword(this.passwordEncoder.encode(reqLoginDto.getPassword()));
        ResUpdatePassword resUpdatePassword = new ResUpdatePassword();
        resUpdatePassword.setEmail(current_user.getEmail());
        resUpdatePassword.setStatus(200);
        return ResponseEntity.ok().body(resUpdatePassword);
    }
}
