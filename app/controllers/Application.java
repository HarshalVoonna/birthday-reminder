package controllers;

import notifiers.*;
import play.mvc.*;
import play.data.validation.*;
import play.libs.Mail.*;
import models.*;

public class Application extends Controller {

	public static final int DEFAULT_HOUR_COUNT = 24;
	
    @Before
    static void addUser() {
        User user = connected();
        if(user != null) {
            renderArgs.put("user", user);
        }
    }
    
	/*
	 * Checking if user session exists and returns user 
	 */
    static User connected() {
        if(renderArgs.get("user") != null) {
            return renderArgs.get("user", User.class);
        }
        String username = session.get("user");
        if(username != null) {
            return User.find("byUsername", username).first();
        } 
        return null;
    }

    public static void index() {
        if(connected() != null) {
            Portal.index();
        }
        render();
    }
    
    public static void register() {
        render();
    }
    
	
	/*
	 * Registers a new user
	 */
    public static void saveUser(@Valid User user, String verifyPassword) {
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("Your password doesn't match");
        if(validation.hasErrors()) {
            render("@register", user, verifyPassword);
        }
		user.hourCount = DEFAULT_HOUR_COUNT;
		user.create();
        session.put("user", user.username);
        flash.success("Welcome, " + user.name);
        Portal.index();
    }

	/*
	 * Logs in the user 
	 */
	public static void login(String username, String password) {
        User user = User.find("byUsernameAndPassword", username, password).first();
        if(user != null) {
            session.put("user", user.username);
            flash.success("Welcome, " + user.name);
            Portal.index();         
        }
        flash.put("username", username);
        flash.error("Login is failed");
        index();
    }
    
	
	/*
	 * Logs out the user 
	 */
    public static void logout() {
        session.clear();
        index();
    }
}