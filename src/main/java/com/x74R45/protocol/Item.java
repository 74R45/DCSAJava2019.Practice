package com.x74R45.protocol;

import org.json.simple.JSONObject;

public class Item {
	
	private String name;
	private int amount;
	private int price;
	private int id;

	public Item(String name, int amount, int price, int id) {
		this.name = name;
		this.amount = amount;
		this.price = price;
		this.id = id;
	}
	
	public Item(String name, int amount, int price) {
		this.name = name;
		this.amount = amount;
		this.price = price;
		this.id = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void add(int amount) {
		if (this.amount + amount < 0) 
			this.amount = 0;
		else
			this.amount += amount;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", amount=" + amount + ", price=" + price + "]";
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject res = new JSONObject();
		res.put("id", getId());
		res.put("name", getName());
		res.put("amount", getAmount());
		res.put("price", getPrice());
		return res;
	}
	
	public boolean isValid() {
		if (getAmount() >= 0 && getPrice() >= 0 && getName().length() > 0)
			return true;
		return false;
	}
}