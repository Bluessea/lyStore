package com.composer.goods.controller;

import com.composer.goods.service.GoodsHtmlService;
import com.composer.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id")Long id, Model model){
        // 加载所需的数据
        Map<String,Object> map = this.goodsService.loadData(id);
        // 把数据放入数据模型
        model.addAllAttributes(map);
        this.goodsHtmlService.createHtml(id);
        // 页面静态化
        //this.goodsHtmlService.asyncExcute(id);
        return "item";
    }
}
