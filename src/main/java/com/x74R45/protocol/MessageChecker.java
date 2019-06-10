package com.x74R45.protocol;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageChecker {

	private static volatile ArrayList<Message> sent = new ArrayList<>();
	private static volatile ArrayList<Message> received = new ArrayList<>();
	
	public static void addSent(Message msg) {
		synchronized (sent) {
			sent.add(msg);
		}
	}
	
	public static void addReceived(Message msg) {
		synchronized (received) {
			received.add(msg);
		}
	}
	
	public static boolean check() {
		//System.out.println("Sent:     " + sent + "\nReceived: " + received);
		for (Iterator<Message> itr = sent.iterator(); itr.hasNext();) {
			Message m = itr.next();
			if (!received.contains(m))
				return false;
		}
		return true;
	}
}