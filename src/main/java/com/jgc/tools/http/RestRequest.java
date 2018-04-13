package com.jgc.tools.http;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class RestRequest extends RestTemplate {

	@Autowired
	@Qualifier("httpClientFactory")
	private HttpClientFactoryBean httpClientFactory;

	@PostConstruct
	public void init(){

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClientFactory.getObject());

		setRequestFactory(factory);

		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new FastJsonHttpMessageConverter4());

		setMessageConverters(converters);

		setErrorHandler(new DefaultResponseErrorHandler());

	}

}
