package server.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.SwitchPoint;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	private static void handleConnection(Socket client) throws IOException {
		Scanner in = new Scanner(client.getInputStream());
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);

		String factor1 = in.nextLine();
		String factor2 = in.nextLine();

		out.println(new BigInteger(factor1).multiply(new BigInteger(factor2)));
	}

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(3141);

		while (true) {
			Socket client = null;

			try {
				client = server.accept();
				handleConnection(client);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (client != null)
					try {
						client.close();
					} catch (IOException e) {
						
					}
			}
		}
	}


}
