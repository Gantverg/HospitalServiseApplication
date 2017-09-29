package tel_ran.hsa.controller;

import org.springframework.boot.SpringApplication;
//import org.springframework.context.support.*;
//
//import tel_ran.communication.udp.ClientUdp;
//import tel_ran.communication.udp.LoadBalancer;

public class HSAWebService{	

	public static void main(String[] args) {
//		AbstractApplicationContext ctx = new FileSystemXmlApplicationContext("web.xml");
//		ClientUdp clientUdp = ctx.getBean(ClientUdp.class);
//		int sleepingTimeMin = 1;
//		Thread t = new Thread(new LoadBalancer(clientUdp, sleepingTimeMin));
//		t.start();
		
		SpringApplication.run(WebController.class, args);

//		try {
//			t.join();
//		} catch (InterruptedException e) {
//		}
//		ctx.close();
	}

}
