package com.example.demo.service;

import com.example.demo.dao.BookDao;
import com.example.demo.entity.Book;
import com.example.demo.es.ESBookInit;
import com.example.demo.es.ESClient;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by makai on 2018/3/16.
 */
@Service
public class ESBookService {
    @Autowired
    private ESClient esClient;
    @Autowired
    private ESBookInit esBookInit;
    @Autowired
    private BookDao bookDao;

    @Value("${es.book.index}")
    private String bookIndex;
    @Value("${es.book.type}")
    private String bookType;

    //保存索引(批量)
    public void save() {
        if (!esBookInit.usable()) return;
        List<Book> list = bookDao.findAll();
        list.forEach(book -> save(book));
    }

    //保存索引(单个)
    public void save(Book book) {
        if (!esBookInit.usable()) return;
        JSONObject jsonObject = JSONObject.fromObject(book);
        esClient.get().prepareIndex(bookIndex, bookType).setId(String.valueOf(book.getId())).setSource(jsonObject).execute().actionGet();
    }

    //重建索引
    public void resetIndex() {
        if (!esBookInit.usable()) return;
        deleteIndex();
        esBookInit.resetIndex();
        save();
    }

    //删除索引库
    public void deleteIndex() {
        if (!esBookInit.usable()) return;
        esClient.get().admin().indices().delete(new DeleteIndexRequest(bookIndex));
    }

    //查询
    public void search(Integer id, String keyword, Integer max, Integer min) {
        SearchRequestBuilder request = esClient.get().prepareSearch(bookIndex).setTypes(bookType);
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (id != null) {
            builder.must(QueryBuilders.idsQuery().addIds(String.valueOf(id)));
        }
        if (StringUtils.isNotBlank(keyword)) {
            builder.must(QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchPhraseQuery("title", keyword).boost(10f))
                    .should(QueryBuilders.matchPhraseQuery("description", keyword).boost(7f))
                    .should(QueryBuilders.multiMatchQuery(keyword, "title").boost(5f))
                    .should(QueryBuilders.multiMatchQuery(keyword, "description").boost(1f))
            );
        }
        if (max != null) {

        }
        if (min != null) {

        }
    }
}
