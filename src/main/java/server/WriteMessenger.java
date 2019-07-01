package server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WriteMessenger {
	private final int MAX_CLIENT_SEMAPHORE = 1;
	private Clients clients;
	private Socket socket;
	private LogChat logChat;
	private Semaphore semaphore;

	public WriteMessenger() {
		semaphore = new Semaphore(MAX_CLIENT_SEMAPHORE);
		ClassPathXmlApplicationContext pathBean = new ClassPathXmlApplicationContext("Client.xml");
		clients = (Clients) pathBean.getBean("clients");
		pathBean.close();
		pathBean = new ClassPathXmlApplicationContext("LogChat.xml");
		logChat = (LogChat) pathBean.getBean("logChat");
		pathBean.close();
	}

	public void write(String name, String text) {
		logChat.addLog(name, text);
		String dataString = name + "::" + text;
		byte[] dataByte = dataString.getBytes();
		for (int index = 0; index < clients.size(); index++) {
			socket = clients.getClient(index);
			try {
				socket.getOutputStream().write(dataByte);
				socket.getOutputStream().flush();
			} catch (IOException e) {
				clients.removeClient(socket);
				System.out.println(e.getMessage());
			}
		}

	}

	public Semaphore getSemaphor() {
		return semaphore;

	}
}
