package tel_ran.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages="tel_ran.mail")
public class SenderTest{
/*
	@Autowired
	@Qualifier("telranalertsender")
	public SenderMail mailSender;
	
	
	@Override
	public void run(String... arg0) throws Exception {
		String from = "kosmotakumi@gmail.com";
		String to = "alex-and-co@yandex.ru";
		String subject = "JavaMailSender";
		String body="Sucess, man";
		
		mailSender.sendMail(from, to, subject, body);
	}
*/
	public static void main(String[] args) {
		SpringApplication.run(SenderTest.class, args);

	}

}
