package com.example.elasticsearchspringboot.controller;

import com.example.elasticsearchspringboot.entity.Secilog;
import org.apache.lucene.queryparser.flexible.standard.builders.PhraseQueryNodeBuilder;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    /**
     * 如果es是2.版本，不能使用太高的boot版本，将报错，对于5. 6. 版本可以使用较高的boot版本
     */
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 查询所有文档（测试）
     */
    @RequestMapping(value = "/index")
    public String index(){
        SearchQuery searchQuery = new NativeSearchQueryBuilder().build();
        List<Secilog> secilogs = elasticsearchTemplate.queryForList(searchQuery, Secilog.class);
        for(Secilog secilog : secilogs) {
            System.out.println(secilog);
        }
        return "success!";
    }

    /**
     * 单字符全文查询（会检测所有字段）
     */
    @RequestMapping(value = "/searchByWord")
    public String searchByWord (@RequestParam String word, @PageableDefault(sort = "pv", direction = Sort.Direction.DESC) Pageable pageable) {
        pageable = new PageRequest(0, 3, Sort.Direction.DESC, "pv");
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new QueryStringQueryBuilder(word)).withPageable(pageable).build();
        List<Secilog> secilogs = elasticsearchTemplate.queryForList(searchQuery, Secilog.class);
        for(Secilog secilog : secilogs) {
            System.out.println(secilog);
        }
        return "success!";
    }

    /**
     * 某字段按字符串模糊查询 (会被es解析并进行分词)（只针对单独字段）
     */
    @RequestMapping("/singleMatch")
    public Object singleMatch(String content, Integer pv) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new MatchQueryBuilder("message", content)).build();
        //查询非字符串(例如整型)不会进行分词，必须完全匹配
        //SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new MatchQueryBuilder("pv", pv)).build();
        List<Secilog> secilogs = elasticsearchTemplate.queryForList(searchQuery, Secilog.class);
        for(Secilog secilog : secilogs) {
            System.out.println(secilog);
        }
        return "success!";
    }

    /**
     * PhraseMatch查询，短语匹配 (例如输入“hello world”不会进行分词，必须完全包含hello world”，只包含“hello”或者“”world)不行
     */
    @RequestMapping("/singlePhraseMatch")
    public Object singlePhraseMatch(String content) {
        MatchQueryBuilder queryBuilder = new MatchQueryBuilder("message", content).type(MatchQueryBuilder.Type.PHRASE);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        List<Secilog> secilogs = elasticsearchTemplate.queryForList(searchQuery, Secilog.class);
        for(Secilog secilog : secilogs) {
            System.out.println(secilog);
        }
        return "success!";
    }

    /**
     * term匹配，即不分词匹配，你传来什么值就会拿你传的值去做完全匹配
     */
    @RequestMapping("/singleTerm")
    public String singleTerm(String value) {
        //不对传来的值分词，去找完全匹配的
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new TermQueryBuilder("message", value)).build();
        List<Secilog> secilogs = elasticsearchTemplate.queryForList(searchQuery, Secilog.class);
        for(Secilog secilog : secilogs) {
            System.out.println(secilog);
        }
        return "success!";
    }

    @RequestMapping("/multiMatch")
    public String multiMatch (String word) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new MultiMatchQueryBuilder(word, "computer", "message")).build();
        List<Secilog> secilogs = elasticsearchTemplate.queryForList(searchQuery, Secilog.class);
        for(Secilog secilog : secilogs) {
            System.out.println(secilog);
        }
        return "success!";
    }

    /**
     * 完全包含查询，当我们输入“我天”时，ES会把分词后所有包含“我”和“天”的都查询出来，
     * 如果我们希望必须是包含了两个字的才能被查询出来，那么我们就需要设置一下Operator。
     * matchQuery，multiMatchQuery，queryStringQuery等，都可以设置operator。默认为Or
     */
    @RequestMapping("/contain")
    public Object contain(String title) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new MatchQueryBuilder("message", title).operator(MatchQueryBuilder.Operator.AND)).build();
        List<Secilog> secilogs = elasticsearchTemplate.queryForList(searchQuery, Secilog.class);
        for(Secilog secilog : secilogs) {
            System.out.println(secilog);
        }
        return "success!";
    }

    /**
     *即boolQuery，可以设置多个条件的查询方式。它的作用是用来组合多个Query，有四种方式来组合，must，mustnot，filter，should。
     * must代表返回的文档必须满足must子句的条件，会参与计算分值；
     * filter代表返回的文档必须满足filter子句的条件，但不会参与计算分值；
     * should代表返回的文档可能满足should子句的条件，也可能不满足，有多个should时满足任何一个就可以，通过minimum_should_match设置至少满足几个。
     * mustnot代表必须不满足子句的条件。
     */
    @RequestMapping("/bool")
    public String bool(String message, Integer pv) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(new BoolQueryBuilder()
                .should(new RangeQueryBuilder("pv").gt(6))
                .should(new RangeQueryBuilder("pv").lt(pv)).minimumNumberShouldMatch(1)//多个should满足一个就可以
                .must(new MatchQueryBuilder("message", message))).build();
        List<Secilog> secilogs = elasticsearchTemplate.queryForList(searchQuery, Secilog.class);
        for(Secilog secilog : secilogs) {
            System.out.println(secilog);
        }
        return "success!";
    }

}
