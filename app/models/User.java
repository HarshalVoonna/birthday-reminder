package models;

import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;

@Entity
@Table(name="User")
public class User extends Model {
    
    @Required
    @MaxSize(15)
    @MinSize(4)
    @Match(value="^\\w*$", message="Not a valid username")
    public String username;
    
	@Required
    public String emailAddress;
    
    @Required
    @MaxSize(15)
    @MinSize(5)
    public String password;
    
    @Required
    @MaxSize(15)
    public String name;

	public int hourCount; //setting hourCount as 24 by default
	public static final int DEFAULT_HOUR_COUNT = 24;
	
    public User(String name, String password, String emailAddress, String username) {
        this.name = name;
        this.password = password;
        this.username = username;
		this.emailAddress = emailAddress;
		this.hourCount = DEFAULT_HOUR_COUNT;
    }

    public String toString()  {
        return "User(" + username + ")";
    }
}
