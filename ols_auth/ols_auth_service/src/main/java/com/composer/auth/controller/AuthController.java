package com.composer.auth.controller;

import com.composer.auth.config.JwtProperties;
import com.composer.auth.service.AuthService;
import com.composer.common.pojo.UserInfo;
import com.composer.common.utils.CookieUtils;
import com.composer.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request,
            HttpServletResponse response
            ){
        String token = this.authService.accredit(username,password);

        if (StringUtils.isBlank(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CookieUtils.setCookie(request, response, this.jwtProperties.getCookieName(),token, this.jwtProperties.getExpire()*60);

        return ResponseEntity.ok(null);
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("OLS_TOKEN")String token,
            HttpServletRequest request,
            HttpServletResponse response
            ){
        try {
            //通过jwt工具类使用公钥解析jwt
            UserInfo user = JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());

            if (user == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            //刷新JWT中的有效时间
            token = JwtUtils.generateToken(user,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());

            //刷新cookie中有效时间   cookie中过期时间是秒
            CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),token,this.jwtProperties.getExpire()*60);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
