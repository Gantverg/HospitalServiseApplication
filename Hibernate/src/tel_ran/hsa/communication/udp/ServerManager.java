package tel_ran.hsa.communication.udp;

import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.communication.udp.*;
import tel_ran.hsa.protocols.api.UdpRequest;

public class ServerManager implements Runnable {

	ServerUdp server;
	private ServerInfo tcpServer;

	public ServerManager(ServerInfo tcpServer, int port) throws Exception {
		server = new ServerUdp(port);
		this.tcpServer = tcpServer;
	}

	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		while (true) {
			byte[] arRequest;
			try {
				arRequest = server.recieve();
				if ((new String(arRequest)).equals(UdpRequest.GET_SERVERS_INFO)) {
					String response = mapper.writeValueAsString(tcpServer);
					server.send(response.getBytes());
				}
			} catch (Exception e) {
			}
		}
	}

}
