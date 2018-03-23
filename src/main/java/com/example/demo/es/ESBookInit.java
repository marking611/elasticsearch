package com.example.demo.es;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化book索引库
 * Created by makai on 2018/3/15.
 */
@Service
public class ESBookInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESBookInit.class);

    @Autowired
    private ESClient esClient;

    @Value("${es.usable}")
    private String usable;
    @Value("${es.book.index}")
    private String bookIndex;
    @Value("${es.book.type}")
    private String bookType;

    @PostConstruct
    private void init() {
        try {
            resetIndex();
        } catch (Exception e) {
            LOGGER.error("ElasticSearch服务异常",e);
        }
    }

    public void resetIndex() {
        if (!usable()) return;
        initIndex();
        initType();
    }

    private void initIndex() {
        boolean isExists = esClient.get().admin().indices().exists(new IndicesExistsRequest(bookIndex)).actionGet().isExists();
        if (isExists) return;
        esClient.get().admin().indices().create(new CreateIndexRequest(bookIndex)).actionGet();
    }

    private void initType() {
        boolean isExists = esClient.get().admin().indices().typesExists(new TypesExistsRequest(new String[]{bookIndex}, bookType)).actionGet().isExists();
        if (isExists) return;
        new XContentFactory();
        XContentBuilder builder = null;
        try {
            List excludes = new ArrayList(); //过滤不保存字段
            excludes.add("description");
            builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(bookType)
                        .startObject("_all")
                        .field("analyzer", "ik_max_word")
                        .field("search_analyzer", "ik_max_word")
                        .field("term_vector", "no").field("store", "false")
                        .endObject()
                        .startObject("_source").field("excludes", excludes).endObject()
                        .startObject("properties")
                            .startObject("title").field("type", "text").field("analyzer", "ik_max_word").field("index", "analyzed").field("search_analyzer", "ik_max_word").endObject() //分词查询
                            .startObject("price").field("type","long").endObject()
                        .endObject()
                    .endObject()
                    .endObject();

        } catch (IOException e) {
            LOGGER.error("创建索引type失败", e);
            e.printStackTrace();
        }
        PutMappingRequest request = Requests.putMappingRequest(bookIndex).type(bookType).source(builder);
        esClient.get().admin().indices().putMapping(request).actionGet();
    }


    public boolean usable() {
        return Boolean.parseBoolean(usable);
    }

}
