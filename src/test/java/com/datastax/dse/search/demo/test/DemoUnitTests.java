package com.datastax.dse.search.demo.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.QueryBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.async.DeferredResult;

import com.datastax.dse.search.demo.controller.AsyncController;
import com.datastax.dse.search.demo.repositories.SimpleRepository;
import com.datastax.dse.search.demo.util.GeoCoderUtil;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration

@SpringBootTest
public class DemoUnitTests {
	private MockMvc mockMvc;
	private AsyncController controller;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	GeoCoderUtil geoCoderUtil;

	@Autowired
	SimpleRepository simpleRepository;

	// @Test
	public void hello() throws Exception {

		System.out.println("Hello");
	}

	@Before
	public void setUp() {
		controller = new AsyncController();
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	// @Test
	// public void luceneQueries() throws Exception {
	// QueryBuilder builder = new QueryBuilder(new WhitespaceAnalyzer());
	//
	// Query res = builder.createPhraseQuery("description", "queryText");
	//
	//
	// System.out.println("result --"+res.toString());
	// }

	// @Test
	// public void luceneQueries() throws Exception {
	// SolrClient client = new
	// HttpSolrClient.Builder("http://localhost:8983/solr/search_demo.library_catalogue/").build();
	// SolrQuery query = new SolrQuery();
	// query.setQuery("autocomplete_quote:when");
	// query.addFacetField("quotes");
	//
	// QueryResponse res = client.query(query);
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
	// for (String iterable_element : ret) {
	// System.out.println(iterable_element);
	//
	// }
	// @Test
	public void getLatLongFromZip() throws Exception {
		geoCoderUtil.getLatLong("80218");

	}

	@Test
	public void getClosestLocationsTest() throws Exception {

		try {
			System.out.println("started");

			MvcResult result = mockMvc
					.perform(get("/getClosestLocations").param("zipCode", "80218").param("distanceInKm", "10000"))
					.andReturn();
//			MvcResult result = mockMvc
//					.perform(get("/searchSoundex").param("query", "Rex"))
//					.andReturn();
//			System.out.println("2");
			

			mockMvc.perform(asyncDispatch(result));
			
			System.out.println("3--" + result.getResponse().getContentAsString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
