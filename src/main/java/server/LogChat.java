package server;

import java.io.*;
import java.nio.file.*;
import java.util.Date;

public class LogChat {
	String pathFiles = "LogChat.txt";

	public LogChat() {
		
	}
	public void addLog(String name, String text) {
		
		try (OutputStream outputStream = 
				new BufferedOutputStream(
						Files.newOutputStream(
								Paths.get(pathFiles),StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE));) {
			String log = new Date().toString() + "::"+name+"::"+text+"\n";
			outputStream.write(log.getBytes());
			outputStream.flush();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public String getLog() {
		String text = "";
		try(InputStream inputStream = 
				new BufferedInputStream(
						Files.newInputStream(
								Paths.get(pathFiles),StandardOpenOption.CREATE, StandardOpenOption.READ));){
			int i;
			i = inputStream.read();
			while (i!=-1) {
				text += (char)i;
				i = inputStream.read();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return text;
		
	}
}
