package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client implements Runnable {
	WriteMessenger writeMessenger;
	private Clients clients;
	Socket socket;
	String reg = "::";
	private InputStream in;

	public Client(Socket socket) {
		ClassPathXmlApplicationContext pathBean = new ClassPathXmlApplicationContext("WriteMessenger.xml");
		writeMessenger = (WriteMessenger) pathBean.getBean("writeMessenger");
		pathBean.close();
		pathBean =  new ClassPathXmlApplicationContext("Client.xml");
		clients = (Clients)pathBean.getBean("clients");
		pathBean.close();
		this.socket = socket;
		try {
			in = socket.getInputStream();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		// TODO отослать все сообщения из лога
	}

	public void run() {
		String text;
		String name;
		
		byte[] buffer = new byte[1024*4];
		while (true) {
			try {
				int count = in.read(buffer, 0, buffer.length);
				if(count==-1) {
					System.out.println("Close connect");
					clients.removeClient(socket);
					break;
					
				}
				text = new String(buffer, 0, count);
				if(text == null || text=="") {
					continue;
				}
				name = text.split(reg)[0];
				text = text.substring(name.length() + reg.length());
				writeMessenger.getSemaphor().acquire();
				writeMessenger.write(name, text);
				writeMessenger.getSemaphor().release();
				System.out.println(name+"::"+text);

			} catch (IOException e) {
				System.out.println(e.getMessage());
				clients.removeClient(socket);
				break;
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				clients.removeClient(socket);
				break;
			} 
			

		}
	}
}
