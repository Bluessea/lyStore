package com.composer.auth.service;

import com.composer.auth.client.UserClient;
import com.composer.auth.config.JwtProperties;
import com.composer.common.pojo.UserInfo;
import com.composer.common.utils.JwtUtils;
import com.composer.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    public String accredit(String username, String password) {
        //1.根据用户名和密码查询
        User user = this.userClient.login(username,password);

        //2.判断user
        if (user == null){
            return null;
        }

        //3.通过jwtUtils生成jwt类型的token
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            return JwtUtils.generateToken(userInfo, this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
