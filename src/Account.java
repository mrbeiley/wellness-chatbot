import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author Michael Beiley
 *
 * This class is for each user. It stores the 
 * username and the number of times the user
 * says each of the pre-defined emotions.
 */
public class Account implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private HashMap<String, Integer> emotions;
	
	public Account(String username) {
		this.username = username;
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
	}
	
	public void add(String emotion) {
		Integer count = emotions.get(emotion);
		emotions.put(emotion, ++count);
	}
}
