package com.datastax.dse.search.demo.model;

import java.io.Serializable;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Table;

@Table(name = "library_catalogue", keyspace = "search_demo")
public class LibraryCatalogue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3759424184026397780L;

	private UUID article_id;
	private String name;
	private String description;
	private String category;
	private String author;
	private String quotes;
	private String lat_long;
	private String library_name;

	public UUID getArticle_id() {
		return article_id;
	}

	public void setArticle_id(UUID article_id) {
		this.article_id = article_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "LibraryCatalogue [article_id=" + article_id + ", name=" + name + ", description=" + description
				+ ", category=" + category + ", author=" + author + ", quotes=" + quotes + ", lat_long=" + lat_long
				+ ", library_name=" + library_name + "]";
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getQuotes() {
		return quotes;
	}

	public void setQuotes(String quotes) {
		this.quotes = quotes;
	}

	public String getLibrary_name() {
		return library_name;
	}

	public void setLibrary_name(String library_name) {
		this.library_name = library_name;
	}

}
