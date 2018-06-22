package com.datastax.dse.search.demo.util;

import java.io.IOException;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

public class GeoCoderUtil {

	public String getLatLong(String zipCode) throws IOException {
		final Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(zipCode).getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		List<GeocoderResult> results = geocoderResponse.getResults();
		System.out.println("results :  " + results); // This will print geographical information

		StringBuffer result = new StringBuffer();

		for (GeocoderResult geocoderResult : results) {

			result.append(geocoderResult.getGeometry().getLocation().getLat().floatValue());
			result.append(",");
			result.append(geocoderResult.getGeometry().getLocation().getLng().floatValue());

		}

		System.out.println("Lat/Long :  " + result);

		return result.toString();

	}

}
