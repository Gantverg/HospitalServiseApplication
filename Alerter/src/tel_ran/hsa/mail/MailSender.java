package tel_ran.hsa.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.hsa.entities.dto.*;


@Component("telranalertsender")
@EnableBinding(Sink.class)
public class MailSender implements SenderMail{
	
	String from = "kosmotakumi@gmail.com";
	
	@Autowired
	JavaMailSender javaMailSender;
	
	//Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@StreamListener(Sink.INPUT)
	public void listeningInformation(String data) {
		ObjectMapper mapper = new ObjectMapper();
		System.err.println(data);
		try {
			System.err.println("Trying to parse inputting data...");
			HeartBeatData heartBeat = mapper.readValue(data, HeartBeatData.class);
			System.err.println("Successfully");
			if(heartBeat!=null) {
				//System.out.println("Success \n  trying to get patient from DB...");
			Patient patient = heartBeat.getPatient();
			//System.out.println("trying to get Doctor from DB...");
			Doctor doctor = patient.getTherapist();
			System.err.println("Success \n Creating mail...");
			String body = String.format("Dear, '%s' M.D., your patient, '%s' have a innormal pulse that equals %d. He need your help", doctor.getName(), patient.getName(), heartBeat.getValue());
			String to = doctor.geteMail();
			String subject="Alert, One of your patients have an unnormal pulse";
			//sendMail(from, to, subject, body);
			System.err.println("mail has sended");
			}
			
		}catch(Exception e) {
			
		}
	}
	
	public void sendMail(String from, String to, String subject, String body) {
		
		SimpleMailMessage mail = new SimpleMailMessage();

		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(body);
		
		System.err.println("Sending...");
		
		javaMailSender.send(mail);
		
		System.err.println("Done! Alert has been sent");
	}
}