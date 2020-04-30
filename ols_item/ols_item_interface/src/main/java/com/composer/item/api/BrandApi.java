package com.composer.item.api;

import com.composer.item.pojo.Brand;
import org.springframework.web.bind.annotation.*;

@RequestMapping("brand")
public interface BrandApi {

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id")Long id);


}