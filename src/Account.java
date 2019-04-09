import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 
 * @author Michael Beiley
 *
 * This class is for each account. It stores the 
 * username and the number of times the user
 * says each of the pre-defined Moods. This class
 * also supports saving and then retrieving Account
 * objects.
 */
public class Account implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private ArrayList<Mood> moodList;
	private HashMap<String, Mood> moods;
	private int daysInGraph;
	private int count;
	
	// Initialize the Moods HashMap with all
	// possible Moods
	public Account(String username) {
		this.username = username;
		this.moodList = new ArrayList<Mood>();
		this.moods = new HashMap<String, Mood>();
		this.daysInGraph = 7;
		this.count = 0;
		initMoods();
	}
	
	/**
	 * Adds all the initial Moods into the Moods HashMap
	 */
	private void initMoods() {
		this.moods.put("!happy", new Mood("!happy"));
		this.moods.put("!reallyhappy", new Mood("!reallyhappy"));
		this.moods.put("!content", new Mood("!content"));
		this.moods.put("!excited", new Mood("!excited"));
		this.moods.put("!okay", new Mood("!okay"));
		this.moods.put("!indifferent", new Mood("!indifferent"));
		this.moods.put("!sad", new Mood("!sad"));
		this.moods.put("!lonely", new Mood("!lonely"));
		this.moods.put("!anxious", new Mood("!anxious"));
		this.moods.put("!sick", new Mood("!sick"));
		this.moods.put("!tired", new Mood("!tired"));
		this.moods.put("!depressed", new Mood("!depressed"));
		this.moods.put("!angry", new Mood("!angry"));
		this.moods.put("!stressed", new Mood("!stressed"));
		this.moods.put("!overwhelmed", new Mood("!overwhelmed"));	
		System.out.println("put in all emotions");
	}
	/**
	 * Increments the counter for the 
	 * specified Moods
	 * 
	 * @param Moods - the selected Moods
	 */
	public void add(String mood) {
		
		// Moods hasn't been initialized/wasn't in constructor originally
		if(this.moods.get(mood) == null)
			this.moods.put(mood, new Mood(mood));

		this.moods.get(mood).increment(); 	// increment HashMap
		moodList.add(0, new Mood(mood)); 	// add new Mood (w/ timestamp) to list
		count++; 							// increment number of total user responses
			
		System.out.println(this.moods.get(mood).getCount()); // debugging print
	}
	
	/**
	 * Writes the Account object to a file
	 * 
	 * The filename is the username.ser
	 * 
	 * @throws IOException
	 */
	public void saveAccountData() throws IOException {
		File outFile = new File(this.username + ".ser");
		outFile.createNewFile();
		FileOutputStream outFileStream = new FileOutputStream(outFile);
		ObjectOutputStream out = new ObjectOutputStream(outFileStream);
		out.writeObject(this);
		out.close();
		outFileStream.close();
	}
	
	/**
	 * Reads a pre-existing Account object and
	 * returns it
	 * 
	 * Checks if the object exists. If not, null
	 * is returned. If it exists, it is read
	 * from a sequence of sources. In order,
	 * they are a file, a FileInputStream,
	 * and then an ObjectInputStream.
	 * 
	 * @param username - used to open the Account
	 * @return Account or null
	 */
	public static Account openAccount(String username) {
		Account account = null;
		File inFile = new File(username + ".ser");
		try {
			FileInputStream inFileStream = new FileInputStream(inFile);
			ObjectInputStream inStream = new ObjectInputStream(inFileStream);
			account = (Account) inStream.readObject();
			inStream.close();
			inFileStream.close();
		} catch (FileNotFoundException e) {
			account = null;
			System.out.println("User " + username + " has no save data.");
		} catch (IOException e) {
			e.printStackTrace();
			account = null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			account = null;
		}

		return account;
	}

	public HashMap<String, Mood> getMoods() {
		return this.moods;
	}
	
	public String getUsername() {
		return this.username;
	}

	public ArrayList<Mood> getMoodList() {
		return this.moodList;
	}

	
	/**
	 * This method creates an ArrayList of all moods (max of 5 per day)
	 * the user has recorded over the last 7 days.
	 * 
	 * This method uses the Date object to check if the moods
	 * in moodList are within the last 7 days. If so,
	 * they are added to the list.
	 * 
	 * @return recentMoods - all moods in the last week (max 5 per day)
	 */
	public ArrayList<Mood> getRecentMoods(int days) {
		
		ArrayList<Mood> recentMoods = new ArrayList<Mood>();
		
		long DAY_IN_MS = 1000 * 60 * 60 * 24;
		Date nDaysAgo = new Date(System.currentTimeMillis() - (days * DAY_IN_MS));
		
		for(int i=0; i<moodList.size(); i++) {
			
			if(moodList.get(i).getCreation().after(nDaysAgo)) {
				recentMoods.add(moodList.get(i));
			}
		}
		
		return recentMoods;
	}

	public int getCount() {
		return this.count;
	}

	public void setDaysInGraph(int i) {
		this.daysInGraph = i;
	}
	
	public int getDaysInGraph() {
		return this.daysInGraph;
	}
}
