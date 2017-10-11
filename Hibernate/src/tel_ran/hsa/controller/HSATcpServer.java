package tel_ran.hsa.controller;

import java.net.*;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import tel_ran.communication.udp.ServerInfo;
import tel_ran.hsa.api.HsaProtocolJson;
import tel_ran.hsa.api.Protocol;
import tel_ran.hsa.communication.tcp.TcpClient;
import tel_ran.hsa.communication.udp.ServerManager;
import tel_ran.hsa.model.interfaces.*;
import tel_ran.hsa.protocols.api.UdpRequest;

public class HSATcpServer {

	public static void main(String[] args) throws Exception {
		AbstractApplicationContext ctx = new FileSystemXmlApplicationContext("beans.xml");
		IHospital hospital = ctx.getBean(IHospital.class);
		ServerSocket serverSocket = new ServerSocket(0);
		
		int port = serverSocket.getLocalPort();
		ServerInfo serverInfo = new ServerInfo(serverSocket.getInetAddress().getHostAddress(), port);
		Thread thread = new Thread(new ServerManager(serverInfo, UdpRequest.UDP_PORT));
		thread.start();
		
		System.out.println("Library server is listening on the port: " + port);
		Protocol protocol = new HsaProtocolJson(hospital);
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				TcpClient tcpClient = new TcpClient(socket, protocol);
				tcpClient.run();
				ctx.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
