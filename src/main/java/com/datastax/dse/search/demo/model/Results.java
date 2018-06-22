package com.datastax.dse.search.demo.model;

import java.io.Serializable;
import java.util.List;

public class Results implements Serializable {

	private List<LibraryCatalogue> rows;

	public List<LibraryCatalogue> getRows() {
		return rows;
	}

	public void setRows(List<LibraryCatalogue> rows) {
		this.rows = rows;
	}

	public Results(List<LibraryCatalogue> rows) {
		super();
		this.rows = rows;
	}

	public Results() {
		super();
	}

}
