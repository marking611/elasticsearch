package com.example.demo.constant;

/**
 * Created by makai on 2018/3/20.
 */
public enum SourceConstant {
    InfoQ("InfoQ", "InfoQ", "http://www.infoq.com/cn/{column}/{type}/{from}", "java,architecture-design,architecture,", "articles,");
    public final String code;
    public final String name;
    public final String url;
    public final String columns;
    public final String types;

    SourceConstant(String code, String name, String url, String columns, String types) {
        this.code = code;
        this.name = name;
        this.url = url;
        this.columns = columns;
        this.types = types;
    }
}
