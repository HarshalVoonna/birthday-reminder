package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.util.*;
import models.*;

public class Portal extends Application {
    
    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Please log in first");
            Application.index();
        }
    }
   
    public static void index() {
        List<Contact> contactList = Contact.find("byUser",connected()).fetch();
        render(contactList);
    }
	
    public static void addContact() {
        render();
    }

	/*
	 * Update hour count
	 */
    public static void updateHourCount(int hourCount) {
        User user = connected();
		user.hourCount = hourCount;
		user.save();
		flash.success("Hour count updated");
		index();
    }
	
	
	/*
	 * Saves the added contact. Also checks if contact is duplicate or not
	 */
	public static void saveContact(Contact contact) {
		contact.user = connected();
		contact.isWished = false;
		Contact earlierContact = Contact.find("byUserAnduserContactNameAndbirthDate",connected(),contact.userContactName,contact.birthDate).first();
		if(earlierContact!=null){
			flash.error("Contact already exists");
			Portal.index();
		}	
		contact.create();
        //session.put("user", user.username);
        flash.success("Contact created: " + contact.userContactName);
        Portal.index();
        render(contact);
    }
	
	public static void deleteContact(String userContactName) {
        Contact contact = Contact.find("byUserAndUserContactName",connected(),userContactName).first();
        contact.delete();
        Portal.index();
    }
    
	
    public static void settings() {
        render();
    }
    
	/*
	 * Saving password related settings
	 */
    public static void saveSettings(String password, String verifyPassword) {
        User connected = connected();
        connected.password = password;
        validation.valid(connected);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, password).message("Your password doesn't match");
        if(validation.hasErrors()) {
            render("@settings", connected, verifyPassword);
        }
        connected.save();
        flash.success("Password updated");
        index();
    }    
}

