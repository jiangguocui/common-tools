package com.jgc.tools.http;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component("httpClientFactory")
public class HttpClientFactoryBean extends DefaultConnectionKeepAliveStrategy implements FactoryBean<HttpClient>,
        Runnable, InitializingBean, DisposableBean{
    /**
     * 保持最多的连接数
     */
    private static final int CONNECTION_MAX_TOTAL = 50;

    /**
     * domain最多连接数
     */
    private static final int CONNECTION_MAX_ROUTE = 10;

    /**
     * 空闲时间
     */
    private static final int IDLE_TIMEOUT = 30 * 1000;

    private ScheduledExecutorService service;

    private PoolingHttpClientConnectionManager connectionManager;

    private HttpClient httpClient;

    public HttpClient getObject(){

        HttpClientBuilder builder = HttpClients.custom();

        builder.setKeepAliveStrategy(this);
        builder.setConnectionManager(connectionManager);

        httpClient = builder.build();

        return httpClient;
    }

    public void afterPropertiesSet(){
        service = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setNameFormat("httpclient-%d").build());
        service.scheduleWithFixedDelay(this, 60, 60, TimeUnit.SECONDS);

        connectionManager = new PoolingHttpClientConnectionManager();

        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(10 * 1000).build());
        connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom().setBufferSize(8 * 1024)
                .setFragmentSizeHint(8 * 1024).build());
        connectionManager.setMaxTotal(CONNECTION_MAX_TOTAL);
        connectionManager.setDefaultMaxPerRoute(CONNECTION_MAX_ROUTE);

    }

    public void destroy() {
        service.shutdownNow();
        connectionManager.close();
    }

    public Class<HttpClient> getObjectType() {
        return HttpClient.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void run() {

        if (connectionManager == null) {
            return;

        }
        connectionManager.closeExpiredConnections();
        connectionManager.closeIdleConnections(IDLE_TIMEOUT, TimeUnit.SECONDS);

    }
}
