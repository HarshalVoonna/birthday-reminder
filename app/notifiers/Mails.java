package notifiers;

import org.apache.commons.mail.*;

import play.*;
import play.libs.Mail;
import play.mvc.*;
import java.util.*;

public class Mails extends Mailer {

	public static final String FROM_EMAIL_ADDRESS = "birthdayreminder@grr.la";
	
	/*
	 * Method to send email. Email smtp is set to mailtrap.io
	 */
    public static void message(String address, String message) {
        try {
            SimpleEmail email = new SimpleEmail();
            email.setFrom(FROM_EMAIL_ADDRESS);
            email.addTo(address);
            email.setSubject("Birthday Reminder");
            email.setMsg(message);
            Mail.send(email);
            System.out.println("Sending email message was successful");
        } catch (Exception e) {
            System.out.println("Error...");
            System.out.println(e);
       }

    }

}