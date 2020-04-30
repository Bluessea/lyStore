package com.composer.item.api;


import com.composer.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {
    @GetMapping
    public List<String> queryNamesByIds(@RequestParam("ids")List<Long> ids);

    @GetMapping("all/level")
    public List<Category> queryAllByCid3(@RequestParam("id") Long id);

}
