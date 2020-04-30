package com.composer.cart.service;

import com.composer.cart.client.GoodsClient;
import com.composer.cart.interceptor.LoginInterceptor;
import com.composer.cart.pojo.Cart;
import com.composer.common.pojo.UserInfo;
import com.composer.common.utils.JsonUtils;
import com.composer.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "user:cart:";

    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //查询购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String key = cart.getSkuId().toString();
        Integer num = cart.getNum();
        //判断当前的商品是否在购物车中
        if (hashOperations.hasKey(key)){
            //若在 更新数量
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum() + num);
        }else{
            //不在 新增
            Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages())? "" : StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());
        }
        //更新或新增后写入到redis
        hashOperations.put(key,JsonUtils.serialize(cart));

    }

    public List<Cart> queryCarts() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //判断用户是否有购物车记录
        if (!this.redisTemplate.hasKey(KEY_PREFIX+userInfo.getId())){
            return null;
        }

        //获取用户的购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        //获取购物车中所有cart值
        List<Object> cartsJson = hashOperations.values();

        //如果购物车集合为空
        if (CollectionUtils.isEmpty(cartsJson)){
            return null;
        }

        //把list<object>集合转化为List<cart>集合
        return cartsJson.stream().map(cartJson->JsonUtils.parse(cartJson.toString(),Cart.class)).collect(Collectors.toList());
    }

    public void updateNum(Cart cart) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        if (!this.redisTemplate.hasKey(KEY_PREFIX+userInfo.getId())){
            return ;
        }
        Integer num = cart.getNum();
        //获取用户的购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String cartJson  = hashOperations.get(cart.getSkuId().toString()).toString();
        //序列化为对象
        cart = JsonUtils.parse(cartJson, Cart.class);

        cart.setNum(num);
        //反序列化为字符串后写入
        hashOperations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    public void deleteCart(String skuId) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getUserInfo();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId);
    }
}
