package com.example.CRUD;

import java.io.Serializable;

public class Item implements Serializable {
	private String name, price, url;
    private float rating;
	
	
	public Item(String name, String price, String url, float rating) {
		this.name = name;
		this.price = price;
		this.url = url;
        this.rating = rating;
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

    public void setRating(float value) {
        rating = value;
    }

    public float getRating() {
        return rating;
    }
	
}
