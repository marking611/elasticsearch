package com.example.demo.service;

import com.example.demo.dao.BookDao;
import com.example.demo.entity.Book;
import com.example.demo.es.ESBookInit;
import com.example.demo.es.ESClient;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> search(int id) {
        return search(id, null, null, null, null, null);
    }

    public Map<String, Object> search(String keyword, int pageNum, int pageSize) {
        return search(null, keyword, pageNum, pageSize, null, null);
    }

    //查询
    public Map<String, Object> search(Integer id, String keyword, Integer pageNum, Integer pageSize, Integer max, Integer min) {
        SearchRequestBuilder request = esClient.get().prepareSearch(bookIndex).setTypes(bookType);
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (id != null) {
            builder.must(QueryBuilders.idsQuery().addIds(String.valueOf(id)));
        }
        if (StringUtils.isNotBlank(keyword)) {
            builder.must(QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchPhraseQuery("name", keyword).boost(10f))
                    .should(QueryBuilders.matchPhraseQuery("description", keyword).boost(7f))
                    .should(QueryBuilders.multiMatchQuery(keyword, "name").boost(5f))
                    .should(QueryBuilders.multiMatchQuery(keyword, "description").boost(1f))
            );
        }
        if (max != null) {
            builder.must(QueryBuilders.rangeQuery("price").lte(max));
        }
        if (min != null) {
            builder.must(QueryBuilders.rangeQuery("price").gte(min));
        }
        if (pageNum != null && pageSize != null) {
            int from = pageNum == 0 ? 0 : (pageNum - 1) * pageSize, size = pageSize;
            request.setFrom(from).setSize(size);
        }
        SearchResponse response = request.setQuery(builder).highlighter(getHighlightBuilder("red", "name")).execute().actionGet();
        Map<String, Object> result = handleResult(response);
        return result;
    }

    private Map<String, Object> handleResult(SearchResponse response) {
        Map<String, Object> result = new HashMap<>();
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits();
        SearchHit[] hitArray = hits.getHits();
        List<Object> list = new ArrayList<>(hitArray.length);
        for (SearchHit hit : hitArray) {
            Map<String, Object> resource = hit.getSource();
            highlightFiled(resource, hit, "name", "description");
            list.add(resource);
        }
        result.put("total", total);
        result.put("data", list);
        return result;
    }

    //高亮字段，颜色设置
    private HighlightBuilder getHighlightBuilder(String color, String... filed) {
        HighlightBuilder builder = new HighlightBuilder();
        if (filed == null) return builder;
        for (int i = 0; i < filed.length; i++) {
            builder.field(filed[i]);
        }
        builder.preTags("<span style=\"color:" + color + "\">");
        builder.postTags("</span>");
        return builder;
    }

    //字段高亮处理
    private void highlightFiled(Map<String, Object> resource, SearchHit hit, String... filed) {
        if (filed == null) return;
        Map<String, HighlightField> map = hit.getHighlightFields();
        if (map == null && map.isEmpty()) return;
        for (int i = 0; i < filed.length; i++) {
            HighlightField highlightField = map.get(filed[i]);
            if (highlightField == null) continue;
            Text[] texts = highlightField.fragments();
            StringBuffer value = new StringBuffer();
            for (Text text : texts) {
                value.append(text.string());
            }
            resource.put(filed[i], value.toString());
        }


    }
}
