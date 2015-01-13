package com.example.CRUD;

import java.io.Serializable;

public class Item implements Serializable {
	private String name, price, url;
	
	
	public Item(String name, String price, String url) {
		this.name = name;
		this.price = price;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
