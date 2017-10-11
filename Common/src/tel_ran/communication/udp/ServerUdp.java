package tel_ran.communication.udp;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ServerUdp {
	private static final int MAX_LENGTH = 1500;
	DatagramSocket socket;
	InetAddress addressClient;
	int portClient;
	
	public ServerUdp(int port) throws SocketException {
		socket = new DatagramSocket(port);
	}
	
	public byte[] recieve() throws IOException{
		byte[] res = new byte[MAX_LENGTH];
		DatagramPacket rPacket = new DatagramPacket(res, MAX_LENGTH);
		socket.receive(rPacket);
		addressClient = rPacket.getAddress();
		portClient = rPacket.getPort();
		return Arrays.copyOf(rPacket.getData(), rPacket.getLength());
	}
	
	public void send(byte[] buf) throws IOException {
		DatagramPacket sPacket = new DatagramPacket(buf, buf.length, addressClient, portClient);
		socket.send(sPacket);
	}
}
