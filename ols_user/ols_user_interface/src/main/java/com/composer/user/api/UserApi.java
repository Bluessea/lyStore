package com.composer.user.api;

import com.composer.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {

    @PostMapping("query")
    public User login(@RequestParam("username")String username, @RequestParam("password")String password);
}
