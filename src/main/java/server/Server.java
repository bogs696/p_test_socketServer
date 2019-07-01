package server;

import java.io.IOException;
import java.net.*;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Server implements Runnable {
	private final int port = 8080;
	private ServerSocket socket;
	private Clients clients;

	public Server() {
		ClassPathXmlApplicationContext pathBean =  new ClassPathXmlApplicationContext("Client.xml");
		clients = (Clients)pathBean.getBean("clients");
		pathBean.close();
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void run() {		
		try {
			
			while(true) {
				Socket client = socket.accept();
				clients.addClient(client);
				new Thread(new Client(client)).start();
				
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			try {
				if(socket!=null && !socket.isClosed()) {
					socket.close();					
				}
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
		}finally {
			clients.removeAllClient();
			try {
				if(socket!=null && !socket.isClosed()) {
					socket.close();					
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}

}
