import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class RelayHandler extends Thread {
	Socket connection = null;
	OutputStream out;
	InputStream in;
	String message;
	ServerSocket server;

	List<ClientHandler> clients;

	public RelayHandler(List<ClientHandler> clients) {
		this.clients = clients;
		this.start();
	}

	@Override
	public
	 void run() {
//		while (true) {
			try {
				// 1. creating a server socket
				server = new ServerSocket(8080);
				// 2. Wait for connection
				System.out.println("Waiting for relay");
				connection = server.accept();
				System.out.println("Connection received from relay at "
						+ connection.getInetAddress().getHostName());
				// 3. get Input and Output streams
				out = connection.getOutputStream();
				in = connection.getInputStream();
				
				// 4. The two parts communicate via the input and output streams
				while(true) {
					String s = new String(getStringFromInputStream(in));
					System.out.println("relay says>" + s);
				}
			} catch (IOException ioException) {
				ioException.printStackTrace();
			} finally {
				try {
					if(server!=null)
						server.close();
					if(in!=null)
						in.close();
					if(out!=null)
						out.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
//			}
		}
	}

	private  byte[] getStringFromInputStream(InputStream in) {

		byte[] resultBuff = new byte[0];
		byte[] buff = new byte[8];
		int k = -1;
		try {
			if ((k = in.read(buff, 0, buff.length)) > 0) {
				byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer
																// size = bytes
																// already read
																// + bytes last
																// read
				System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy
																				// previous
																				// bytes
				System.arraycopy(buff, 0, tbuff, resultBuff.length, k); // copy
																		// current
																		// lot
				resultBuff = tbuff; // call the temp buffer as your result buff
				broadcast(resultBuff);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(resultBuff.length + " bytes read.");
		return resultBuff;

	}


	public void broadcast(byte[] msg) {
		if(clients == null) return;
		for (ClientHandler client : clients) {
			client.sendMessage(msg);
		}
//		System.out.println("msg send from relay to clients");
	}

	
	
	public void sendMessage(byte[] msg) {
		try {
			out.write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("client>" + msg.toString());
	}
}
