import org.springframework.context.support.ClassPathXmlApplicationContext;

import server.Server;

public class Main {

	public static void main(String[] args) {
		Server server;
		ClassPathXmlApplicationContext pathBean =  new ClassPathXmlApplicationContext("Server.xml");
		server = (Server)pathBean.getBean("server");
		pathBean.close();
		new Thread(server).start();
	}

}
