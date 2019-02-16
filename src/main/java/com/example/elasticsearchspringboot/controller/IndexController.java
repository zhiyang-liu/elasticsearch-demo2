package com.example.elasticsearchspringboot.controller;

import com.example.elasticsearchspringboot.dao.SecilogDao;
import com.example.elasticsearchspringboot.entity.Secilog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SecilogDao secilogDao;

    @RequestMapping(value = "/createIndex")
    public String createIndex(){
        elasticsearchTemplate.createIndex(Secilog.class);
        return "success!";
    }

    @RequestMapping(value = "/deleteIndex")
    public String deleteIndex(){
        elasticsearchTemplate.deleteIndex(Secilog.class);
        return "success!";
    }

    @RequestMapping(value = "addDoc")
    public String addDoc(){
        /**
         * 索引中的id会与Secilog中的id相同
         */
        Secilog secilog1 = new Secilog(1L, 1, "huipu", "hello apple");
        Secilog secilog2 = new Secilog(2L, 2, "apple", "hello jack");
        Secilog secilog3 = new Secilog(3L, 3, "daier", "hello dell");
        Secilog secilog4 = new Secilog(4L, 4, "lianxing", "nihao lianxiang");
        Secilog secilog5 = new Secilog(5L, 6, "huawei", "hello moto");
        Secilog secilog6 = new Secilog(6L, 5, "hongji", "welcome acer");
        Secilog secilog7 = new Secilog(7L, 7, "shenzhou", "delete shen");
        List<Secilog> secilogs = new ArrayList<Secilog>();
        secilogs.add(secilog1);
        secilogs.add(secilog2);
        secilogs.add(secilog3);
        secilogs.add(secilog4);
        secilogs.add(secilog5);
        secilogs.add(secilog6);
        secilogs.add(secilog7);
        secilogDao.save(secilogs);
        return "success!";
    }

    @RequestMapping(value = "deleteDoc")
    public String deleteDoc(Long id){
        secilogDao.delete(id);
        return "success!";
    }
}
