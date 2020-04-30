package com.composer.search.listener;

import com.composer.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "OLS.SEARCH.SAVE.QUEUE",durable = "true"),
            exchange = @Exchange(value = "OLS.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    //如果抛出异常，会自动取消ACK，若没有异常则自动启动ACK  SpringAOP实现
    public void save(Long id) throws IOException {
        if (id == null){
            return;
        }
        this.searchService.save(id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "OLS.SEARCH.DELETE.QUEUE",durable = "true"),
            exchange = @Exchange(value = "OLS.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    //如果抛出异常，会自动取消ACK，若没有异常则自动启动ACK  SpringAOP实现
    public void delete(Long id) throws IOException {
        if (id == null){
            return;
        }
        this.searchService.delete(id);
    }



}
