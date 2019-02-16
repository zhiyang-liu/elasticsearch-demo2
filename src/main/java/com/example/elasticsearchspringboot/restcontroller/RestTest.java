package com.example.elasticsearchspringboot.restcontroller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
public class RestTest {

    @Autowired
    private RestClient restClient;

    /**
     * 利用RestClient采用Rest方式访问es
     */
    @RequestMapping(value = "/restTest")
    public String search() throws IOException {
        Map<String, String> params = Collections.emptyMap();
        String queryString = "{\n" +
                "\t\"query\": {\n" +
                "\t\t\"term\": {\n" +
                "\t\t\t\"computer\": \"huipu\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        HttpEntity entity = new NStringEntity(queryString, ContentType.APPLICATION_JSON);
        JSONObject jsonObject = null;
        try {
            Response response = restClient.performRequest("POST", "/secisland/secilog/_search", params, entity);
            System.out.println("响应状态；" + response.getStatusLine().getStatusCode());
            String responseBody = null;
            responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("******************************************** ");
            jsonObject = JSON.parseObject(responseBody);
            System.out.println(jsonObject);
            System.out.println(jsonObject.get("hits"));
        }catch (ResponseException e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
