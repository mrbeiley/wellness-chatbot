import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
/**
 * 
 * @author mbeil
 *
 * This class stores an mood, the number of times
 * it's been used, and what dates it was used.
 * 
 * Used in a HashMap<String, mood> in Account.java
 */
public class Mood implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String mood;
	private int count;
	private ArrayList<Date> dates;
	private Date creation;
	
	/**
	 * Sets mood to param, sets count and dates to defaults
	 * 
	 * @param mood - new mood to add
	 */
	public Mood(String mood) {
		this.mood = mood;
		this.count = 0;
		this.dates = new ArrayList<Date>();
		this.creation = new Date();
	}
		
	public void increment() {
		count++;
		dates.add(new Date());
	}
	
	public int getCount() {
		return this.count;
	}
	
	public String getMood() {
		return this.mood;
	}
	
	public Date getCreation() {
		return this.creation;
	}
	
	
}
