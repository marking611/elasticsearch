package com.example.demo.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * elasticsearch 客户端
 * Created by makai on 2018/3/15.
 */
@Component
public class ESClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ESClient.class);

    @Value("${es.cluster.name}")
    private String clusterName;
    @Value("${es.hosts}")
    private String hosts;

    private TransportClient client = null;
    private Settings settings = Settings.builder()
            //配置集群名称
            .put("cluster.name", clusterName)
            //配置是否自动发现集群中新增节点
            .put("client.transport.sniff", true).build();

    @PostConstruct
    public void init() {
        client = new PreBuiltTransportClient(settings);
        String[] hostArray = hosts.split(";");
        try {
            for (String host : hostArray) {
                String ip = host.split(":")[0];
                String port = host.split(":")[1];
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), Integer.valueOf(port)));
            }
        } catch (UnknownHostException e) {
            LOGGER.error("初始化elasticsearch客户端失败", e);
            e.printStackTrace();
        }
    }

    public TransportClient get() {
        return client;
    }
}
