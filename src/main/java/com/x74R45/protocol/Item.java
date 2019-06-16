package com.x74R45.protocol;

public class Item {
	
	private String name;
	private int amount;
	private int price;

	public Item(String name, int amount, int price) {
		this.name = name;
		this.amount = amount;
		this.price = price;
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
	
	@Override
	public String toString() {
		return "Item [name=" + name + ", amount=" + amount + ", price=" + price + "]";
	}
}