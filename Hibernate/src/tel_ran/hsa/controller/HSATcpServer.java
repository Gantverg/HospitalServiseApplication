package tel_ran.hsa.controller;

import java.io.IOException;
import java.net.*;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import tel_ran.hsa.api.HsaProtocolJson;
import tel_ran.hsa.api.Protocol;
import tel_ran.hsa.communication.tcp.TcpClient;
import tel_ran.hsa.model.interfaces.*;

public class HSATcpServer {

	public static void main(String[] args) throws IOException {
		AbstractApplicationContext ctx = new FileSystemXmlApplicationContext("beans.xml");
		IHospital hospital = ctx.getBean(IHospital.class);
		ServerSocket serverSocket = new ServerSocket(0);
		int port = serverSocket.getLocalPort();
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
