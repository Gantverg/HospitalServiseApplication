package tel_ran.hospital.communication.tcp;
import java.net.*;

import tel_ran.hospital.api.Protocol;

import java.io.*;
public class TcpClient {
private Socket socket;
private BufferedReader reader;
private PrintStream writer;
private Protocol protocol;

public TcpClient(Socket socket,Protocol protocol) throws Exception{
	
	this.protocol=protocol;
	this.socket=socket;
	this.reader=
	new BufferedReader(new InputStreamReader(socket.getInputStream()));
	this.writer=new PrintStream(socket.getOutputStream());
}
public void run(){
	try {
	while(true){
		
			String request=reader.readLine();
			if(request==null)
				return;
			String response=protocol.getResponse(request);
			writer.println(response);
	}
		} catch (IOException e) {
			System.out.println(" client error "+e.getMessage());
			
		}finally {
			try {
				socket.close();
			} catch (IOException e) {
				
			}
		}
	}
	
}
