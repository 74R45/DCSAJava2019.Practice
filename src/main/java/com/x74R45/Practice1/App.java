package com.x74R45.Practice1;

public class App 
{
	public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Please provide an input!");
            System.exit(0);
        }
        
        byte[] message = args[0].getBytes();
        
        System.out.println(Receiver.validate(Sender.send(message, 1, 1)));
    }
}