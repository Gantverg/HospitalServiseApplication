package tel_ran.hsa.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;

import com.google.gson.Gson;

import tel_ran.hsa.dto.*;

@EnableBinding(Source.class)
public class SensorDataGenerator {
	private static final int MAXBEATRATE = 250;
	private static final int MINBEAT = 20;
	private static final int BASIS = 120;
	private static final int MAXPERSONS = 999;
	private static final String MAXMESSAGES = "100";
	LocalDateTime timeStart;
	int count = 0;
	Random r = new Random();

	@InboundChannelAdapter(channel = Source.OUTPUT, poller = @Poller(maxMessagesPerPoll = MAXMESSAGES))
	String sendSensorData() {
		if (count == 0) {
			sleep();
		}
		return checkPulse();
	}

	private synchronized void sleep() {
		try {
			if (timeStart != null) {
				Thread.sleep(60000 - (ChronoUnit.MILLIS.between(timeStart, LocalDateTime.now())));

			} else {
				Thread.sleep(60000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeStart = LocalDateTime.now();
	}

	private synchronized String checkPulse() {
		//int pulse = MINBEAT + new Random().nextInt(MAXBEATRATE - MINBEAT);
		int pulse = (BASIS + r.nextInt(MAXBEATRATE - BASIS)) / (1 + r.nextInt((BASIS / MINBEAT) - 1));
		return getJson(pulse);
	}

	private String getJson(int pulse) {
		Gson gson = new Gson();
		InputInfo inf = new InputInfo(count, pulse);
		checkCount();

		return gson.toJson(inf);
	}

	private synchronized void checkCount() {
		if (count != MAXPERSONS) {
			count++;
		} else {
			count = 0;
		}
	}

}
