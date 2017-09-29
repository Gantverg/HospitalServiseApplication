package tel_ran.communication.udp;

import java.net.*;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.hsa.protocols.api.UdpRequest;

public class LoadBalancer implements Runnable{
	static List<Socket> socketList = new ArrayList<>();
	static int currentPosition = 0;
	
	ClientUdp clientUdp;
	private long sleepingTime;

	public LoadBalancer(ClientUdp clientUdp, int sleepingTimeMin) {
		this.clientUdp = clientUdp;
		this.sleepingTime = sleepingTimeMin * 60 * 1000; 
	}

	public static Socket getSocket() {
		if(LoadBalancer.socketList.isEmpty())
			return null;
		if(currentPosition >= socketList.size())
			currentPosition = 0;
		return socketList.get(currentPosition++);
	}

	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		while (true) {
			try {
				clientUdp.send(mapper.writeValueAsString(UdpRequest.GET_SERVERS_INFO));
				byte[] arResponse = clientUdp.receive();
				if (arResponse != null) {
					List<ServerInfo> response = mapper.readValue(arResponse, new TypeReference<List<ServerInfo>>() {});
					LoadBalancer.socketList.clear();
					for (ServerInfo serverInfo : response) {
						LoadBalancer.socketList.add(new Socket(serverInfo.getHostName(), serverInfo.getPort()));
					}
				} 
				Thread.sleep(sleepingTime);
			} catch (Exception e) {
			}
		}
	}

}
