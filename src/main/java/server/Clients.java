package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Clients {
	private ArrayList<Socket> clients = new ArrayList<Socket>();
	private static Clients instanse = new Clients();

	private Clients() {
	}

	public static Clients getInstanse() {
		return instanse;
	}

	public Socket getClient(int index) {
		return clients.get(index);
	}

	public int size() {
		return clients.size();
	}

	public void addClient(Socket client) {
		clients.add(client);
	}

	public boolean removeClient(Socket client) {
		boolean result;
		try {
			if (client != null && client.getInputStream() != null) {
				client.getInputStream().close();
			}
			if (client != null && client.getOutputStream() != null) {
				client.getOutputStream().close();
			}
			if (client != null && !client.isClosed()) {
				client.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}finally {
			result= clients.remove(client);
		}
		return result;
	}

	public void removeAllClient() {
		for (Socket socket : clients) {
			removeClient(socket);
		}
	}
}
