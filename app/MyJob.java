import play.test.*;
import play.jobs.*;
import models.*;
import controllers.*;
import notifiers.*;
import java.util.*;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Every("5s")
public class MyJob extends Job {
    
	/*
	 * Returns the number of hours left for next hour
	 */
	public long calculateNextBirthday(Date birthDate){    
		Calendar c = Calendar.getInstance();
		c.setTime(birthDate); 
		Calendar today = Calendar.getInstance();
		int birthdayMonth = c.get(Calendar.MONTH);
		int currentMonth = today.get(Calendar.MONTH);
		int birthdayDay = c.get(Calendar.DAY_OF_MONTH);
		int currentDay = today.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.YEAR, today.get(Calendar.YEAR));
		if (birthdayMonth < currentMonth) {
			c.set(Calendar.YEAR, today.get(Calendar.YEAR) + 1);
		}
		else if (birthdayMonth == currentMonth){
			if(birthdayDay < currentDay){
				c.set(Calendar.YEAR, today.get(Calendar.YEAR) + 1);
			}

		}
		Date d1 = c.getTime();
		Date d2 = today.getTime();
		long duration  = d1.getTime() - d2.getTime();
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		//System.out.println(diffInHours);		
		return diffInHours;
	}
	
	/*
	 * Sends an email to the user's email address
	 */
	public void sendMail(User user, Contact contact){
		Mails mail = new Mails();
		mail.message(user.emailAddress, "Birthday Reminder : Don't forget to wish your contact "+contact.userContactName);
	}

	public int hoursDifference(Date date1, Date date2) {
		final int MILLI_TO_HOUR = 1000 * 60 * 60;
		return (int) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
	}
	
	public Date getCurrentDate(){
		Timestamp stamp = new Timestamp(System.currentTimeMillis());	
		return new Date(stamp.getTime());
	}
	
	/*
	 * This job gets executed in background every 5s
	 */
    public void doJob() {
		System.out.println("Running Cron Job");	
		List<User> userList =  User.all().fetch();
		for(User eachUser : userList){
			System.out.println(eachUser.name);	
			List<Contact> contactList =  Contact.find("byUser",eachUser).fetch();
			for(Contact eachContact : contactList){
				System.out.println("Logging "+ eachUser.hourCount + " "+calculateNextBirthday(eachContact.birthDate));	
				
				if(calculateNextBirthday(eachContact.birthDate)== eachUser.hourCount){
					System.out.println(eachContact.isWished);
					if(eachContact.isWished == false){
						System.out.println(eachContact.userContactName + eachContact.birthDate + eachUser.hourCount);
						sendMail(eachUser,eachContact);
						eachContact.isWished = true;
						eachContact.save();
						System.out.println("isWished boolean has been set to "+ eachContact.isWished);
					}
				}
				else if(calculateNextBirthday(eachContact.birthDate) < eachUser.hourCount){
					System.out.println("Reseting here ");
					eachContact.isWished = false;
				}
			}
		}		
    }    
}