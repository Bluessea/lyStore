package com.composer.goods.service;

import com.composer.goods.client.BrandClient;
import com.composer.goods.client.CategoryClient;
import com.composer.goods.client.GoodsClient;
import com.composer.goods.client.SpecificationClient;
import com.composer.item.pojo.*;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String,Object> loadData(Long spuId){

        Map<String,Object> model = new HashMap<>();

        //根据spuId查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);

        //查询spu的detail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);

        //查询分类：Map<String,Object>
        List<Long> cids = Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        //初始化一个分类的map
        List<Map<String,Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }

        //查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);

        //查询规格参数组
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());

        //查询特殊的规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null,spu.getCid3(),false,null);
        Map<Long,String> paramMap = new HashMap<>();
        params.forEach(param->{
            paramMap.put(param.getId(),param.getName());
        });

        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("groups",groups);
        model.put("paramMap",paramMap);
        return model;
    }

}
