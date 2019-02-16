package com.example.elasticsearchspringboot.dao;

import com.example.elasticsearchspringboot.entity.Secilog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SecilogDao extends ElasticsearchRepository<Secilog, Long> {
}
