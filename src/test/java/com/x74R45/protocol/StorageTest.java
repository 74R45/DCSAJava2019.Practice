package com.x74R45.protocol;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

// The database needs to be empty.
public class StorageTest {

	private static boolean DBInitialized = false;
	
	@Before
	public void setup() {
		if (!DBInitialized) {
			DBInteractor.initialize();
			DBInitialized = true;
		}
	}
	
	@Test
	public void groupsAndItemsTest() {
		try {
			Storage.addGroup("group1");
			Storage.addGroup("group2");
			Storage.addItem("buckwheat", 3);
			Storage.addItem("rice", 0);
			Storage.addItem("wheat", -3);
			Storage.addItemToGroup("buckwheat", "group1");
			Storage.addItemToGroup("rice", "group1");
			Storage.addItemToGroup("wheat", "group2");
			
			assertEquals(3, Storage.countItem("buckwheat"));
			assertEquals(0, Storage.countItem("rice"));
			assertEquals(0, Storage.countItem("wheat"));
			
			Storage.subtractItem("buckwheat", 4);
			assertEquals(0, Storage.countItem("buckwheat"));
			
			Storage.addItem("buckwheat", 15);
			Storage.subtractItem("buckwheat", 10);
			assertEquals(5, Storage.countItem("buckwheat"));
			
			ArrayList<Item> items = Storage.getItemsInGroup("group1");
			boolean buckwheat = false, rice = false;
			for (Item i : items) {
				if (i.getName().equals("buckwheat") && i.getAmount() == 5 && i.getPrice() == 0)
					buckwheat = true;
				if (i.getName().equals("rice") && i.getAmount() == 0 && i.getPrice() == 0)
					rice = true;
			}
			assertTrue(buckwheat && rice && items.size() == 2);
			
			Storage.deleteGroup("group1");
			Storage.deleteGroup("group2");
			Storage.deleteItem("buckwheat");
			Storage.deleteItem("rice");
			Storage.deleteItem("wheat");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("An SQLException has been thrown.");
		}
	}
	
	@Test
	public void priceTest() {
		try {
			Storage.addItem("bread", 10);
			Storage.setPrice("bread", 1000);
			assertEquals(1000, Storage.getPrice("bread"));
			Storage.deleteItem("bread");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("An SQLException has been thrown.");
		}
	}
	
	private class Truck implements Runnable {

		private String item;
		private int amount;
		
		public Truck(String item, int amount) {
			this.item = item;
			this.amount = amount;
		}
		
		public void run() {
			try {
				Storage.addItem(item, amount);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void threadsTest() {
		Thread t1 = new Thread(new Truck("buckwheat", 10));
		Thread t2 = new Thread(new Truck("buckwheat", 20));
		Thread t3 = new Thread(new Truck("buckwheat", 50));
		Thread t4 = new Thread(new Truck("buckwheat", 13));
		Thread t5 = new Thread(new Truck("buckwheat", 18));
		Thread t6 = new Thread(new Truck("buckwheat", 64));
		Thread t7 = new Thread(new Truck("buckwheat", 93));
		Thread t8 = new Thread(new Truck("buckwheat", 91));
		Thread t9 = new Thread(new Truck("buckwheat", -1));
		t1.start();	t2.start();	t3.start();	t4.start();	t5.start();
		t6.start();	t7.start();	t8.start();	t9.start();
		
		try {
			t1.join(); t2.join(); t3.join(); t4.join();	t5.join();
			t6.join(); t7.join(); t8.join(); t9.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			assertEquals(358, Storage.countItem("buckwheat"));
			Storage.deleteItem("buckwheat");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("An SQLException has been thrown.");
		}
	}
}
