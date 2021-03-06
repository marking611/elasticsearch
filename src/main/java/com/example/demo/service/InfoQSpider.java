package com.example.demo.service;

import com.example.demo.constant.SourceConstant;
import com.example.demo.dao.ArticleDao;
import com.example.demo.entity.Article;
import com.example.demo.thread.ThreadPool;
import com.example.demo.utils.SpiderUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by makai on 2018/3/19.
 */
@Service
public class InfoQSpider {
    private final static String BASIC = SourceConstant.InfoQ.url;
    private final static String COLUMNS = SourceConstant.InfoQ.columns;
    private final static String TYPES = SourceConstant.InfoQ.types;

    @Autowired
    private ArticleDao articleDao;

    public void spider() {
        spider(0);
    }

    public void spider(int from) {
        String[] columnArray = COLUMNS.split(",");
        String[] typeArray = TYPES.split(",");
        for (String column : columnArray) {
            for (String type : typeArray) {
                String url = BASIC.replace("{column}", column).replace("{type}", type).replace("{from}", String.valueOf(from));
                ThreadPool.getInstance().getPool().execute(() -> spider(url, column, type));
                //spider(url, column, type);
            }
        }
    }

    private void spider(String url, String column, String type) {
        Document document = SpiderUtil.getDocument(url);
        if (document == null) return;
        Element content = document.selectFirst("#content");
        Elements elements1 = content.select(".news_type1");
        elements1.forEach(element -> {

            Element p = element.selectFirst("p");
            Element a = p.selectFirst("a");
            String href = a.attr("href");
            String title = a.attr("title");
            String description = p.text();
            String complete = SpiderUtil.getCompleteURL(url, href);
            if (complete == null) return;
            String publishTime = getPublishTime(element);
            save(title, complete, description, column, type, publishTime);
        });

        Elements elements2 = content.select(".news_type2");
        elements2.forEach(element -> {
            Element a = element.selectFirst("h2 a");
            String href = a.attr("href");
            String title = a.attr("title");
            String description = element.selectFirst(".txt p").text();
            String complete = SpiderUtil.getCompleteURL(url, href);
            if (complete == null) return;
            String publishTime = getPublishTime(element);
            save(title, complete, description, column, type, publishTime);
        });
    }

    private String getPublishTime(Element element) {
        String baseInfo = element.selectFirst(".author").text();
        String publishTime = baseInfo.substring(baseInfo.indexOf("发布于"), baseInfo.lastIndexOf("日") + 1);
        publishTime = publishTime.replace("发布于", "");
        publishTime = handlePublishTime(publishTime);
        return publishTime;
    }

    private String handlePublishTime(String publishTime) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date date = sdf1.parse(publishTime);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            publishTime = sdf2.format(date);
        } catch (ParseException e) {
            return publishTime;
        }
        return publishTime;
    }

    private void save(String title, String url, String description, String column, String type, String publishTime) {
        int total = articleDao.countByTitleAndUrl(title, url);
        if (total > 0) return;
        Article article = new Article();
        article.setSource(SourceConstant.InfoQ.name);
        article.setTitle(title);
        article.setUrl(url);
        article.setDescription(description);
        article.setChannel(column);
        article.setType(type);
        article.setCreateTime(new Timestamp(System.currentTimeMillis()));
        article.setPublishTime(publishTime);
        articleDao.saveAndFlush(article);
    }

}
