package com.x74R45.protocol;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class Message {
	
	private byte src;
	private long pktId;
	private static AtomicLong pktIdCounter = new AtomicLong(0);
	private int type;
	private int userId;
	private byte[] message;
	
	public Message(byte src, int type, int userId, byte[] message) {
		this.src = src;
		synchronized (pktIdCounter) {
			this.pktId = pktIdCounter.getAndIncrement();
		}
		this.type = type;
		this.userId = userId;
		this.message = Arrays.copyOf(message, message.length);
	}
	
	public Message(byte src, int type, int userId, byte[] message, long pktId) {
		this.src = src;
		this.pktId = pktId;
		this.type = type;
		this.userId = userId;
		this.message = Arrays.copyOf(message, message.length);
	}
	
	public byte getSrc() {
		return src;
	}
	
	public void setSrc(byte src) {
		this.src = src;
	}
	
	public long getPktId() {
		return pktId;
	}
	
	public void setPktId(long pktId) {
		this.pktId = pktId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = Arrays.copyOf(message, message.length);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (!Arrays.equals(message, other.message))
			return false;
		if (pktId != other.pktId)
			return false;
		if (src != other.src)
			return false;
		if (type != other.type)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String mes = "";
		try {
			mes = new String(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "[src=" + src + ", pktId=" + pktId + ", type=" + type 
			+ ", userId=" + userId + ", message=\"" + mes + "\"]";
	}
}
