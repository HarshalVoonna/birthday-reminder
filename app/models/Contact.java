package models;

import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;

@Entity
@Table(name="Contact")
public class Contact extends Model {
    
	@Required
    @ManyToOne
    public User user;

	@Required
    @MaxSize(20)
    public String userContactName;
    
    @Required
    @Temporal(TemporalType.DATE) 
    public Date birthDate;
	
	public boolean isWished;

    public Contact(User user, String name, Date date) {
        this.user = user;
		this.userContactName = name;
        this.birthDate = date;
		this.isWished = false;
    }

    public String toString()  {
        return "Contact(" + userContactName + "," +user + ")";
    }    
}
