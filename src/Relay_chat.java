import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Relay_chat {
	ServerSocket providerSocket;
	String message;

	List<ClientHandler> clients;
	RelayHandler relay;
	
	public Relay_chat() {
		clients = new ArrayList<ClientHandler>();
		relay = new RelayHandler(clients);
	}

	void run() {
		try {
			// 1. creating a server socket
			providerSocket = new ServerSocket(8081);
			while(true){
				Socket connection = null;
				
				// 2. Wait for connection
				System.out.println("Waiting for clients");
				connection = providerSocket.accept();
	
				System.out.println("Connection received from client at "
						+ connection.getInetAddress().getHostName());
				ClientHandler client = new ClientHandler(connection, relay);
				clients.add(client);
			}
		} catch (IOException e) {
			System.out.println("error at client");
			e.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		Relay_chat server = new Relay_chat();
		while (true) {
			server.run();
		}
	}
}
