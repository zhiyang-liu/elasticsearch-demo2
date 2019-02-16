package com.example.elasticsearchspringboot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = "secisland", type = "secilog")
public class Secilog implements Serializable {

    @Id
    private Long id;

    private Integer pv;

    private String computer;

    private String message;

    /**
     * 无参的构造函数是必须的，否则会反序列化失败，在添加构造函数后，也必须再添加无参构造函数
     */
    public Secilog(){}

    public Secilog(Long id, Integer pv, String computer, String message){
        this.id = id;
        this.pv = pv;
        this.computer = computer;
        this.message = message;
    }

    @Override
    public String toString() {
        return "id:" + id + "computer:" + computer + " message:" + message + " pv:" + pv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public String getComputer() {
        return computer;
    }

    public void setComputer(String computer) {
        this.computer = computer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
