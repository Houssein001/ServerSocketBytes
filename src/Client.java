import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	Socket requestSocket;
	PrintWriter out;
	Scanner in;
	String message;

	RelayHandler relay;
	

	void run() {
		try {
			// 1. creating a socket to connect to the server
			requestSocket = new Socket("192.168.0.100", 8081);
			System.out.println("Connected to relay wifi-module in port 8081");
			// 2. get Input and Output streams
			out = new PrintWriter(requestSocket.getOutputStream(),true);
			out.flush();
			in = new Scanner(requestSocket.getInputStream());
			// 3: Communicating with the server
			
			sendMessage("DX");
			
				message = (String) in.nextLine();
				System.out.println("server>" + message);
				
				
				String msg = in.nextLine();
				System.out.println(msg);
			
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	void sendMessage(String msg) {
		out.println(msg);
		System.out.println("client>" + msg);
	}

	public static void main(String args[]) {
		Client client = new Client();
		client.run();
	}
}