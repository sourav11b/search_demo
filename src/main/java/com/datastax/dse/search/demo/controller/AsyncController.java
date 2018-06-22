package com.datastax.dse.search.demo.controller;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.datastax.dse.search.demo.repositories.SimpleRepository;

@RestController
public class AsyncController {

	static Logger logger = LoggerFactory.getLogger(AsyncController.class);

	@Autowired
	SimpleRepository simpleRepository;

	@RequestMapping("/searchSynonyms")
	public DeferredResult<ResponseEntity<?>> searchSynonyms(@RequestParam("query") String query) {

		logger.info("Received Query " + query);

		final DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<ResponseEntity<?>>(5000l);
		deferredResult.onTimeout(new Runnable() {

			public void run() { // Retry on timeout
				deferredResult.setErrorResult(
						ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred."));
			}
		});
		simpleRepository.searchSynonyms(deferredResult, query);
		logger.info(Thread.currentThread().getName() + "Done with main");

		return deferredResult;
	}

	@RequestMapping("/searchSoundex")
	public DeferredResult<ResponseEntity<?>> searchSoundex(@RequestParam("query") String query) {

		logger.info("Received Query " + query);

		final DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<ResponseEntity<?>>(5000l);
		deferredResult.onTimeout(new Runnable() {

			public void run() { // Retry on timeout
				deferredResult.setErrorResult(
						ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred."));
			}
		});

		simpleRepository.searchSoundex(deferredResult, query);
		logger.info(Thread.currentThread().getName() + "Done with main");

		return deferredResult;
	}

	@RequestMapping("/getAutoCompleteSuggestions")
	public DeferredResult<ResponseEntity<?>> getAutoCompleteSuggestions(@RequestParam("query") String query) {

		logger.info("Received Query " + query);

		final DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<ResponseEntity<?>>(5000l);
		deferredResult.onTimeout(new Runnable() {

			public void run() { 
				deferredResult.setErrorResult(
						ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred."));
			}
		});

		simpleRepository.getAutoCompleteSuggestions(deferredResult, query);
		logger.info(Thread.currentThread().getName() + "Done with main");

		return deferredResult;
	}

	@RequestMapping("/getClosestLocations")
	public DeferredResult<ResponseEntity<?>> getClosestLocations(@RequestParam("zipCode") String zipCode,
			@RequestParam("distanceInKM") String distanceInKM) {

		logger.info("Received zipCode " + zipCode);
		logger.info("Received distanceInKM " + distanceInKM);

		final DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<ResponseEntity<?>>(5000l);
		deferredResult.onTimeout(new Runnable() {

			public void run() { // Retry on timeout
				deferredResult.setErrorResult(
						ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred."));
			}
		});

		simpleRepository.getClosestLocations(deferredResult, zipCode, Integer.parseInt(distanceInKM));
		logger.info(Thread.currentThread().getName() + "Done with main");

		return deferredResult;
	}

	@RequestMapping("/getDistinctAutoCompleteSuggestions")
	public @ResponseBody List<String> getDistinctAutoCompleteSuggestions(@RequestParam("query") String query)
			throws SolrServerException, IOException {

		logger.info("Received Query " + query);

		return simpleRepository.getDistinctAutoCompleteSuggestions(query);

	}

}
