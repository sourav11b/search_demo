package com.datastax.dse.search.demo.repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.lucene.search.Query;
import org.apache.lucene.util.QueryBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.datastax.dse.search.demo.model.LibraryCatalogue;
import com.datastax.dse.search.demo.model.Results;
import com.datastax.dse.search.demo.util.GeoCoderUtil;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class SimpleRepository {

	// or no results or exceptions change response code

	@Autowired
	DseCluster cluster;

	@Autowired
	DseSession session;

	@Autowired
	PreparedStatement simpleSelectPS;

	@Autowired
	PreparedStatement simpleSoundexSelectPS;

	@Autowired
	Mapper<LibraryCatalogue> LibraryCatalogueMapper;

	@Autowired
	ExecutorService executorService;

	@Autowired
	QueryBuilder queryBuilder;

	@Autowired
	GeoCoderUtil geoCoderUtil;

	static Logger logger = LoggerFactory.getLogger(SimpleRepository.class);

	public void searchSynonyms(DeferredResult<ResponseEntity<?>> deferredResult, String query) {

		logger.info("Starting select");

		final List<LibraryCatalogue> results = new ArrayList<LibraryCatalogue>();

		logger.info("Creating statement with fetch size");

		Query solrQuery = queryBuilder.createPhraseQuery("more_description", query);
		logger.info(" solrQuery :" + solrQuery.toString());

		ResultSetFuture resultSet = session.executeAsync(simpleSelectPS.bind(solrQuery.toString()));

		logger.info("Adding callback to record exception in response");

		Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
			public void onSuccess(ResultSet resultSet) {

			}

			public void onFailure(Throwable t) {
				logger.error("Error during processing resultSet", t);

				deferredResult.setResult(ResponseEntity.ok(t));

			}
		}, executorService);

		// make sure a new threadpool is not getting created per request
		// we have dedicated threadpool for database interactions

		Futures.transformAsync(resultSet, iterate(10, deferredResult, results, LibraryCatalogueMapper, executorService),
				executorService);
		logger.info("Done with main thread");

	}

	public void searchSoundex(DeferredResult<ResponseEntity<?>> deferredResult, String query) {

		logger.info("Starting select");

		final List<LibraryCatalogue> results = new ArrayList<LibraryCatalogue>();

		logger.info("Creating statement with fetch size");

		Query solrQuery = queryBuilder.createPhraseQuery("soundex_author", query);
		logger.info(" solrQuery :" + solrQuery.toString());

		ResultSetFuture resultSet = session.executeAsync(simpleSelectPS.bind(solrQuery.toString()));

		logger.info("Adding callback to record exception in response");

		Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
			public void onSuccess(ResultSet resultSet) {

			}

			public void onFailure(Throwable t) {
				logger.error("Error during processing resultSet", t);

				deferredResult.setResult(ResponseEntity.ok(t));

			}
		}, executorService);

		// make sure a new threadpool is not getting created per request
		// we have dedicated threadpool for database interactions

		Futures.transformAsync(resultSet, iterate(10, deferredResult, results, LibraryCatalogueMapper, executorService),
				executorService);
		logger.info("Done with main thread");

	}

	public void getAutoCompleteSuggestions(DeferredResult<ResponseEntity<?>> deferredResult, String query) {

		logger.info("Starting select");

		final List<LibraryCatalogue> results = new ArrayList<LibraryCatalogue>();

		logger.info("Creating statement with fetch size");

		Query solrQuery = queryBuilder.createPhraseQuery("autocomplete_quote", query);
		logger.info(" solrQuery :" + solrQuery.toString());

		ResultSetFuture resultSet = session.executeAsync(simpleSelectPS.bind(solrQuery.toString()));

		logger.info("Adding callback to record exception in response");

		Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
			public void onSuccess(ResultSet resultSet) {

			}

			public void onFailure(Throwable t) {
				logger.error("Error during processing resultSet", t);

				deferredResult.setResult(ResponseEntity.ok(t));

			}
		}, executorService);

		// make sure a new threadpool is not getting created per request
		// we have dedicated threadpool for database interactions

		Futures.transformAsync(resultSet, iterate(10, deferredResult, results, LibraryCatalogueMapper, executorService),
				executorService);
		logger.info("Done with main thread");

	}

	public List<String> getDistinctAutoCompleteSuggestions(String query) throws SolrServerException, IOException {
		SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/search_demo.library_catalogue/")
				.build();
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("autocomplete_quote:" + query);
		solrQuery.addFacetField("quotes");

		QueryResponse res = client.query(solrQuery);
		List<String> ret = new ArrayList<>();

		FacetField facetField = res.getFacetField("quotes");
		if (facetField != null) {
			for (Count count : facetField.getValues()) {
				if (count.getCount() == 0) {
					continue;
				}
				ret.add(count.getName());
			}
		}

		Collections.sort(ret);
		return ret;

	}

	public void getClosestLocations(DeferredResult<ResponseEntity<?>> deferredResult, String zipCode, int distInKM) {

		logger.info("Starting select");

		final List<LibraryCatalogue> results = new ArrayList<LibraryCatalogue>();

		logger.info("Creating statement with fetch size");

		String latLongField = "lat_long";
		String latLongValue = null;
		try {
			latLongValue = geoCoderUtil.getLatLong(zipCode);
		} catch (IOException e) {
			deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e));
		}
		String query = "{ \"q\" : \"*:*\", \"fq\" : \"{!geofilt pt=" + latLongValue + " sfield=" + latLongField + " d="
				+ distInKM + "}\",\"sort\": \"geodist(" + latLongField + "," + latLongValue + ") asc\" }";

		// SolrQuery solrQuery = new SolrQuery();
		// solrQuery.setQuery("*:*");
		//
		// String fq = "{!geofilt pt=" + latLongValue + " sfield=" + latLongField + "
		// d=" + distInKM + "}";
		// solrQuery.addFilterQuery(fq);
		// SortClause sortClause = new SortClause("geodist(" + latLongField + "," +
		// latLongValue + ")", "desc");
		// solrQuery.addSort(sortClause);

		// Query solrQuery = queryBuilder.createPhraseQuery("*", "*");
		//
		//
		// logger.info(" solrQuery :" + solrQuery.toString());

		ResultSetFuture resultSet = session.executeAsync(simpleSelectPS.bind(query));

		logger.info("Adding callback to record exception in response");

		Futures.addCallback(resultSet, new FutureCallback<ResultSet>() {
			public void onSuccess(ResultSet resultSet) {

			}

			public void onFailure(Throwable t) {
				logger.error("Error during processing resultSet", t);

				deferredResult.setResult(ResponseEntity.ok(t));

			}
		}, executorService);

		// make sure a new threadpool is not getting created per request
		// we have dedicated threadpool for database interactions

		Futures.transformAsync(resultSet, iterate(10, deferredResult, results, LibraryCatalogueMapper, executorService),
				executorService);
		logger.info("Done with main thread");

	}

	// public List<String> getClosestLocations(String zipCode, int distInKM) throws
	// SolrServerException, IOException{
	// String latLongField="lat_long";
	// String latLongValue=geoCoderUtil.getLatLong(zipCode);
	//
	// SolrClient client = new
	// HttpSolrClient.Builder("http://localhost:8983/solr/search_demo.library_catalogue/").build();
	// SolrQuery solrQuery = new SolrQuery();
	// String fq="{!geofilt pt="+latLongValue+" sfield="+latLongField+"
	// d="+distInKM+"}";
	// solrQuery.addFilterQuery(fq);
	//
	// SortClause sortClause = new
	// SortClause("geodist("+latLongField+","+latLongValue+")", "desc");
	//
	// QueryResponse res = client.query(solrQuery);
	// List<String> ret = new ArrayList<>();
	//
	// FacetField facetField = res.getFacetField("quotes");
	// if (facetField != null) {
	// for (Count count : facetField.getValues()) {
	// if (count.getCount() == 0) {
	// continue;
	// }
	// ret.add(count.getName());
	// }
	// }
	//
	// Collections.sort(ret);
	// return ret;
	//
	// }

	private static AsyncFunction<ResultSet, ResultSet> iterate(final int page,
			final DeferredResult<ResponseEntity<?>> deferredResult, final List<LibraryCatalogue> results,
			Mapper<LibraryCatalogue> mapper, final ExecutorService executorService) {
		return new AsyncFunction<ResultSet, ResultSet>() {
			@Override
			public ListenableFuture<ResultSet> apply(ResultSet rs) throws Exception {

				// How far we can go without triggering the blocking fetch:
				int remainingInPage = rs.getAvailableWithoutFetching();

				logger.info("Starting page {} ({} rows)", page, remainingInPage);

				Result<LibraryCatalogue> temp = mapper.map(rs);

				for (LibraryCatalogue row : temp) {

					results.add(row);
					// System.out.printf(Thread.currentThread().getName() + "[page %d - %d] row =
					// %s%n", page,
					// remainingInPage, row);
					if (--remainingInPage == 0)
						break;
				}

				logger.info("Done page {}", page);

				boolean wasLastPage = rs.getExecutionInfo().getPagingState() == null;
				if (wasLastPage) {
					logger.info("Done through all pages");
					if (results.isEmpty()) {
						deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new Exception("No Results Returned")));

					} else {
						deferredResult.setResult(ResponseEntity.ok(new Results(results)));
					}

					return Futures.immediateFuture(rs);
				} else {
					ListenableFuture<ResultSet> future = rs.fetchMoreResults();
					return Futures.transformAsync(future,
							iterate(page + 1, deferredResult, results, mapper, executorService), executorService);
				}
			}
		};
	}

}
