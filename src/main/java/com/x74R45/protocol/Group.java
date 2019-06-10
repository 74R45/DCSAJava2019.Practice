package com.x74R45.protocol;

import java.util.ArrayList;

public class Group {
	
	private String name;
	private ArrayList<String> items;

	public Group(String name) {
		this.setName(name);
		this.setItems(new ArrayList<String>());
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public void setItems(ArrayList<String> items) {
		this.items = items;
	}
	
	public void addItem(String item) {
		if (!items.contains(item)) 
			items.add(item);
	}
	
	public void deleteItem(String item) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).equals(item))
				items.remove(i);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}