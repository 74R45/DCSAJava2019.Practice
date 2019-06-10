package com.x74R45.protocol;

import static org.junit.Assert.*;

import org.junit.Test;

public class StorageTest {
	
	@Test
	public void zeroItemsTest() {
		assertEquals(0, Storage.totalItems());
	}

	@Test
	public void groupsAndItemsTest() {
		Storage.addItemToGroup("buckwheat", "group 1");
		Storage.addItemToGroup("rice", "group 1");
		Storage.addItemToGroup("wheat", "group 2");
		Storage.addItems("buckwheat", 3);
		Storage.addItems("rice", 0);
		Storage.addItems("wheat", -3);
		
		assertEquals(3, Storage.countItem("buckwheat"));
		assertEquals(0, Storage.countItem("rice"));
		assertEquals(0, Storage.countItem("wheat"));
		assertEquals(3, Storage.totalItems());
		
		Storage.subtractItems("buckwheat", 4);
		assertEquals(0, Storage.countItem("buckwheat"));
		
		Storage.addItems("buckwheat", 15);
		Storage.subtractItems("buckwheat", 10);
		assertEquals(5, Storage.countItem("buckwheat"));
		Storage.clear();
		assertEquals(0, Storage.totalItems());
	}
	
	@Test
	public void priceTest() {
		Storage.addItemToGroup("bread", "food");
		Storage.setPrice("bread", 1000);
		
		assertEquals(1000, Storage.getPrice("bread"));
	}
	
	private class Truck implements Runnable {

		private String item;
		private int amount;
		
		public Truck(String item, int amount) {
			this.item = item;
			this.amount = amount;
		}
		
		public void run() {
			Storage.addItems(item, amount);
		}
	}
	
	@Test
	public void threadsTest() {
		Storage.addItemToGroup("buckwheat", "food");
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
		
		assertEquals(358, Storage.countItem("buckwheat"));
		Storage.clear();
	}
}
