package com.datastax.dse.search.demo.beans;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.dse.search.demo.model.LibraryCatalogue;
import com.datastax.dse.search.demo.util.GeoCoderUtil;

@Component
public class UtilityBeans {

	@Autowired
	DseSession session;

	@Value("${application.thread.pool.core.size}")
	private int threadPoolCoreSize;
	@Value("${application.thread.pool.max.size}")
	private int threadPoolMaxSize;
	@Value("${application.thread.pool.queue.size}")
	private int queueSize;

	@Bean
	public Mapper<LibraryCatalogue> LibraryCatalogueMapper() {
		return new MappingManager(session).mapper(LibraryCatalogue.class);
	}

	@Bean
	public ExecutorService executorService() {
		// return Executors.newFixedThreadPool(80);
		return new ThreadPoolExecutor(threadPoolCoreSize, threadPoolMaxSize, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());

	}

	@Bean
	public QueryBuilder queryBuilder() {
		return new QueryBuilder(new WhitespaceAnalyzer());
	}

	@Bean
	public GeoCoderUtil geoCoderUtil() {
		return new GeoCoderUtil();
	}

}
