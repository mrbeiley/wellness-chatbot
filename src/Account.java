import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author Michael Beiley
 *
 * This class is for each account. It stores the 
 * username and the number of times the user
 * says each of the pre-defined emotions. This class
 * also supports saving and then retrieving Account
 * objects.
 */
public class Account implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private HashMap<String, Integer> emotions;
	
	// Initialize the emotions HashMap with all
	// possible emotions
	public Account(String username) {
		this.username = username;
		this.emotions = new HashMap<>();
		this.emotions.put("!happy", 0);
		this.emotions.put("!reallyhappy", 0);
		this.emotions.put("!content", 0);
		this.emotions.put("!excited", 0);
		this.emotions.put("!okay", 0);
		this.emotions.put("!indifferent", 0);
		this.emotions.put("!sad", 0);
		this.emotions.put("!lonely", 0);
		this.emotions.put("!anxious", 0);
		this.emotions.put("!sick", 0);
		this.emotions.put("!tired", 0);
		this.emotions.put("!depressed", 0);
		this.emotions.put("!angry", 0);
		this.emotions.put("!stressed", 0);
		this.emotions.put("!overwhelmed", 0);
	}
	
	/**
	 * Increments the counter for the 
	 * specified emotion
	 * 
	 * @param emotion - the selected emotion
	 */
	public void add(String emotion) {
		Integer count = emotions.get(emotion);
		
		// emotion hasn't been initialized/wasn't in constructor originally
		if(count == null)
			emotions.put(emotion, 1);
		else
			emotions.put(emotion, ++count);
		
		System.out.println(emotions.get(emotion));
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
}
