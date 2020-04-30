package com.composer.goods.service;

import com.composer.goods.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service
public class GoodsHtmlService {

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private GoodsService goodsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);

    public void createHtml(Long spuId){

        //初始化运行上下文
        Context context = new Context();
        //设置数据模型
        context.setVariables(this.goodsService.loadData(spuId));
        PrintWriter printWriter = null;
        try {
            File file = new File("D:\\Software\\nginx-1.12.2\\html\\item\\"+spuId+".html");
            printWriter = new PrintWriter(file);

            this.engine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("页面静态化出错：{}，"+ e, spuId);
        } finally {
            if (printWriter != null){
                printWriter.close();
            }
        }
    }
    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Long spuId) {
        ThreadUtils.execute(()->createHtml(spuId));
        /*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }

    /**
     * 删除静态页面
     * @param id
     */
    public void deleteHtml(Long id) {
        File  file = new File("D:\\Software\\nginx-1.12.2\\html\\item\\"+id+".html");
        file.deleteOnExit();
    }
}
