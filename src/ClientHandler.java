import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
	Socket connection = null;
	OutputStream out;
	InputStream in;
	String message;
	RelayHandler relay;

	public ClientHandler(Socket cn, RelayHandler relay) {
		this.relay = relay;
		connection = cn;
		try {
			out = connection.getOutputStream();
			out.flush();
			in = connection.getInputStream();
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		super.run();
		System.out.println("Connection successful");

		// 4. The two parts communicate via the input and output streams
		while (true) {
			byte[] buff = getStringFromInputStream(in);
			String msg = buff.toString();
//			System.out.println("client>" + msg);

		}
	}

	private byte[] getStringFromInputStream(InputStream in) {

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
				relay.sendMessage(resultBuff);
				String s = new String(resultBuff);
				System.out.println("client>" + s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(resultBuff.length + " bytes read.");
		return resultBuff;

	}

	public void sendMessage(byte[] msg) {
		try {
			out.write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
