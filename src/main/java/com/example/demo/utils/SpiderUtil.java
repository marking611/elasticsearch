package com.example.demo.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * Created by makai on 2018/3/20.
 */
public class SpiderUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpiderUtil.class);

    public static Document getDocument(String url) {
        Document document = null;
        try {
            document = Jsoup
                    .connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
//                    .timeout(2000)
                    .get();
        } catch (IOException e) {
            LOGGER.error("获取Document失败！url:"+url, e);
        }
        return document;
    }

    public static String getCompleteURL(String basic, String relative) {
        String complete = null;
        try {
            URI basicURI = new URI(basic);
            URI relativeURI = basicURI.resolve(relative);
            URL completeURL = relativeURI.toURL();
            complete = completeURL.toString();
        } catch (Exception e) {
            LOGGER.error("获取完整url错误", e);
        }
        return complete;
    }
}
