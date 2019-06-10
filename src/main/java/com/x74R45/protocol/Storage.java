package com.x74R45.protocol;

import java.util.ArrayList;

public class Storage {

	private static volatile ArrayList<Item> items = new ArrayList<Item>();
	private static volatile ArrayList<Group> groups = new ArrayList<Group>();
	
	/* Command 0
	 * Format of the message: 
	 *   0-end itemName
	 */
	public static int countItem(String itemName) {
		for (Item i : items) {
			if (i.getName().equals(itemName)) {
				return i.getAmount();
			}
		}
		System.out.println("Received a request to count the number of " + itemName + " in the storage.");
		return 0;
	}
	
	/* Command 1
	 * Format of the message: 
	 *   0-3   amount
	 *   4-end name
	 */
	public static void addItems(String name, int amount) {
		synchronized (items) {
			for (int pos = 0; pos < items.size(); pos++) {
				if (items.get(pos).getName().equals(name)) {
					items.get(pos).add(amount);
					break;
				}
			}
		}
		System.out.println(amount + " units of " + name + " have been added to the storage.");
	}
	
	/* Command 2
	 * Format of the message: 
	 *   0-3   amount
	 *   4-end name
	 */
	public static void subtractItems(String name, int amount) {
		addItems(name, -amount);
	}
	
	/* Command 3
	 * Format of the message: 
	 *   0-end name
	 */
	public static void addGroup(String name) {
		synchronized (groups) {
			for (int i = 0; i < groups.size(); i++)
				if (groups.get(i).getName().equals(name))
					return;
			groups.add(new Group(name));
		}
		System.out.println("A group " + name + " was created.");
	}

	/* Command 4
	 * Format of the message: 
	 *   0-3       Len
	 *   4-Len+3   itemName
	 *   Len+4-end groupName
	 */
	public static void addItemToGroup(String itemName, String groupName) {
		synchronized (groups) {
			boolean success = false;
			for (int i = 0; i < groups.size(); i++) {
				if (groups.get(i).getName().equals(groupName)) {
					groups.get(i).addItem(itemName);
					success = true;
					break;
				}
			}
			if (!success) {
				groups.add(new Group(groupName));
				groups.get(groups.size()-1).addItem(itemName);
			}
		}
		synchronized (items) {
			boolean contains = false;
			for (Item i : items)
				if (i.getName().equals(itemName)) contains = true;
			if (!contains) {
				items.add(new Item(itemName, 0, 0));
			}
		}
		System.out.println("An item " + itemName + " was successfully added to group " + groupName + '.');
	}
	
	/* Command 5
	 * Format of the message: 
	 *   0-3   price
	 *   4-end itemName
	 */
	public static void setPrice(String itemName, int price) {
		synchronized (items) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getName().equals(itemName)) {
					items.get(i).setPrice(price);
				}
			}
		}
		System.out.println("The price of item " + itemName + " has been set to " + price + '.');
	}
	
	public static int totalItems() {
		int counter = 0;
		for (Item i : items) {
			counter += i.getAmount();
		}
		return counter;
	}
	
	public static Group getGroup(String groupName) {
		for (Group g : groups) {
			if (g.getName().equals(groupName)) return g;
		}
		return null;
	}
	
	public static int getPrice(String itemName) {
		for (Item i : items) {
			if (i.getName().equals(itemName)) {
				return i.getPrice();
			}
		}
		return 0;
	}
	
	public static void clear() {
		synchronized (items) {
			items.clear();
		}
		synchronized (groups) {
			groups.clear();
		}
	}
	
}