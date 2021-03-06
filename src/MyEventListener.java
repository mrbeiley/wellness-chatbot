import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 
 * @author Michael Beiley
 * 
 * This class is the primary event listener for the bot. For each emotion, there is
 * a unique, appropriate set of responses from the bot. This class uses an EventWaiter
 * to allow the bot to wait for responses from the user.
 *
 */
public class MyEventListener extends ListenerAdapter{

	private EventWaiter waiter;
	public MessageChannel currentChannel = null;
	public static final int RESPONSE_WAIT_TIME = 15; // seconds
	public Account account = null;
	
	public MyEventListener(EventWaiter waiter) {
		this.waiter = waiter;
	}
	
	/**
	 * The primary event listener that contains responses to all
	 * basic emotions.
	 */
	public void onMessageReceived(MessageReceivedEvent event) {
		// get the channel used by the bot and users
		if (currentChannel == null) {
			currentChannel = event.getChannel();
		}
		if(event.getAuthor().isBot()) {
			return;
		}
		if (account == null) {
			account = Account.openAccount(event.getAuthor().getName());
			if(account == null) {
				account = new Account(event.getAuthor().getName());
			}
		}
		
		// Store the message sent by the user
		Message message = event.getMessage();
		String content = message.getContentRaw();

		// Check which emotion was entered
		switch (content) {
		case "!ping":
			currentChannel.sendMessage("Pong " + event.getJDA().getPing()).queue();
			break;
		case "!help":
			currentChannel.sendMessage("Hey " + event.getAuthor().getName() + "!"
										+ " Here is a list of commands you can use:\n\n"
										+ "!ping to check your ping\n\n"
										+ "!happy, !reallyhappy, !content, !excited, "
										+ "!okay, !indifferent, !sad, !lonely, !anxious, !sick"
										+ ", !tired, !angry, !depressed, !stressed, !overwhelmed\n\n"
										+ "!mood to change the length of time your mood graph covers\n\n"
										+ "!therapist to find a therapist perfectly suited for you!").queue();
			break;
		case "!happy":
			account.add("!happy");
			happyConvo(currentChannel, event.getAuthor());
			break;
		case "!reallyhappy":
			account.add("!reallyhappy");
			happyConvo(currentChannel, event.getAuthor());
			break;
		case "!content":
			account.add("!content");
			happyConvo(currentChannel, event.getAuthor());
			break;
		case "!excited":
			account.add("!excited");
			happyConvo(currentChannel, event.getAuthor());
			break;
		case "!okay":
			account.add("!okay");
			okayConvo(currentChannel, event.getAuthor());
			break;
		case "!indifferent":
			account.add("!indifferent");
			okayConvo(currentChannel, event.getAuthor());
			break;
		case"!sad":
			account.add("!sad");
			sadConvo(currentChannel, event.getAuthor());
			break;
		case "!lonely":
			account.add("!lonely");
			sadConvo(currentChannel, event.getAuthor());
			break;
		case "!anxious":
			account.add("!anxious");
			anxiousConvo(currentChannel, event.getAuthor());
			break;
		case "!sick":
			account.add("!sick");
			sickConvo(currentChannel, event.getAuthor());
			break;
		case "!tired":
			account.add("!tired");
			tiredConvo(currentChannel, event.getAuthor());
			break;
		case "!angry":
			account.add("!angry");
			depressedConvo(currentChannel, event.getAuthor());
			break;
		case "!depressed":
			account.add("!depressed");
			depressedConvo(currentChannel, event.getAuthor());
			break;
		case "!stressed":
			account.add("!stressed");
			stressedConvo(currentChannel, event.getAuthor());
			break;
		case "!overwhelmed":
			account.add("!overwhelmed");
			stressedConvo(currentChannel, event.getAuthor());
			break;
		case "!mood":
			currentChannel.sendTyping().queue();
			currentChannel.sendMessage("This graph is customizable too." + "You can choose how"
					+ " long of a period to graph your mood on. By default, we graph your mood"
					+ " over a week. Type 'yes' if you want to change the time.").queueAfter(3, TimeUnit.SECONDS);
			waiter.waitForEvent(MessageReceivedEvent.class, 
					e -> e.getAuthor().equals(event.getAuthor()), 
					e -> setGraphTime(e.getChannel(), event.getAuthor(), e.getMessage().getContentRaw().toLowerCase()),
					RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
					("if you ever want to change it, simply type '!mood'.").queue());	
			break;
		case "!therapist":
			String s = null;
			try {
				String cmd = "C:\\Users\\mbeil\\Downloads\\anaconda2\\Scripts\\activate.bat";
				
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		            // read the output from the command
		            System.out.println("Here is the standard output of the command:\n");
		            while ((s = stdInput.readLine()) != null) {
		                System.out.println(s);
		            }
		            
		            // read any errors from the attempted command
		            System.out.println("Here is the standard error of the command (if any):\n");
		            while ((s = stdError.readLine()) != null) {
		                System.out.println(s);
		            }
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		default:
//			currentChannel.sendMessage("Sorry, I didn't recognize that emotion. Try using a"
//					+ "different spelling or different word.").queue();
			break;
		} // end of switch()

		
		// The third time the user enters a mood, inform them about the graph
		if(account.getCount() == 100) {
			currentChannel.sendTyping().queue();
			currentChannel.sendMessage("Hey " + event.getAuthor().getName() + "! I'm glad"
					+ " to see you using the mood response feature!" + " There's even more"
							+ " I can do, including tracking your moods and graphing them"
							+ " over time. This is a great way to see how your moods are"
							+ " fluctuating.").queueAfter(2, TimeUnit.SECONDS);
			currentChannel.sendMessage("This graph is customizable too." + "You can choose how"
					+ " long of a period to graph your mood on. By default, we graph your mood"
					+ " over a week. Type '!mood' to change this!").queueAfter(3, TimeUnit.SECONDS);
			
			waiter.waitForEvent(MessageReceivedEvent.class, 
					e -> e.getAuthor().equals(event.getAuthor()), 
					e -> setGraphTime(e.getChannel(), event.getAuthor(), e.getMessage().getContentRaw().toLowerCase()),
					RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
					("if you ever want to change it, simply type '!mood'.").queue());
		}
		
		// Save data to '[username].ser'
		try {
			account.saveAccountData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * @param channel: The MessageChannel used by the bot & users
	 * @param user: used to confirm the user sent a reply, not anyone else
	 * @param response: the actual message sent by the user
	 * 
	 * This method handles the user response regarding the period of time
	 * the mood graph covers. If the user says 'yes', the bot will ask them
	 * how long they wish their graph to cover. If they say no, the bot
	 * ends the interaction.
	 */
	private void setGraphTime(MessageChannel channel, User user, String response) {
		if(response.equals("yes") || response.equals("yea") || response.equals("yeah")) {
			channel.sendMessage("Okay great! What would you like to set the time period to?" +
								"Please choose '3 days', '1 week', '2 weeks', '1 month', "
								+ "'3 months', '6 months', or '1 year'").queue();
			waiter.waitForEvent(MessageReceivedEvent.class, 
					e -> e.getAuthor().equals(user), 
					e -> setGraphTime2(channel, user, e.getMessage().getContentRaw().toLowerCase()),
					RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
					("and what are some ways to recreate that success?").queue());
		}
		else {
			channel.sendMessage("Sounds good! Have a nice day!");
		}
	}

	private void setGraphTime2(MessageChannel channel, User user, String response) {
		if(response.contains("3 days")) {
			account.setDaysInGraph(3);
			channel.sendTyping().queue();
			channel.sendMessage("Great! Your graph will now show the last 3 days of moods!").queueAfter(1, TimeUnit.SECONDS);
		}
		else if(response.contains("1 week")) {
			account.setDaysInGraph(7);
			channel.sendTyping().queue();
			channel.sendMessage("Great! Your graph will now show the last week of moods!").queueAfter(1, TimeUnit.SECONDS);
		}
		else if(response.contains("2 weeks")) {
			account.setDaysInGraph(14);
			channel.sendTyping().queue();
			channel.sendMessage("Great! Your graph will now show the last 2 weeks of moods!").queueAfter(1, TimeUnit.SECONDS);
		}
		else if(response.contains("1 month")) {
			account.setDaysInGraph(30);
			channel.sendTyping().queue();
			channel.sendMessage("Great! Your graph will now show the last month of moods!").queueAfter(1, TimeUnit.SECONDS);
		}
		else if(response.contains("3 months")) {
			account.setDaysInGraph(91);
			channel.sendTyping().queue();
			channel.sendMessage("Great! Your graph will now show the last 3 months of moods!").queueAfter(1, TimeUnit.SECONDS);
		}
		else if(response.contains("6 months")) {
			account.setDaysInGraph(182);
			channel.sendTyping().queue();
			channel.sendMessage("Great! Your graph will now show the last 6 months of moods!").queueAfter(1, TimeUnit.SECONDS);
		}
		else if(response.contains("1 year")){
			account.setDaysInGraph(365);
			channel.sendTyping().queue();
			channel.sendMessage("Great! Your graph will now show the last year of moods!").queueAfter(1, TimeUnit.SECONDS);
		}
		else {
			channel.sendMessage("Sorry, please enter '3 days', '1 week', '2 weeks',"
					+ " '1 month', '3 months', '6 months' or '1 year'");
			waiter.waitForEvent(MessageReceivedEvent.class, 
					e -> e.getAuthor().equals(user), 
					e -> setGraphTime2(channel, user, e.getMessage().getContentRaw().toLowerCase()),
					RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
					("You can always set the graph period with the '!graph' command").queue());
		}
	}

	/**
	 * @param currentChannel: The MessageChannel used by the bot & users
	 * @param user: used to confirm the user sent a reply, not anyone else 
	 * 
	 * This method sends the initial response to '!happy' and '!content' and
	 * calls firstHappy() to continue the conversation. We are calling other functions
	 * because we want to wait for the user to respond to questions before
	 * the bot sends its next message.
	 */
	private void happyConvo(MessageChannel currentChannel, User user){
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("That's amazing to hear " + user.getName()
						  + "! It's always nice when things fall into place!").queueAfter(2, TimeUnit.SECONDS);
		currentChannel.sendMessage("What do you think has you feeling so good??").queueAfter(5, TimeUnit.SECONDS);
		
		// Wait for the user to respond to the bot's 1st question
		// Calls helper functions that facilitate flow of conversation
		// methods called: firstHappy(), secondHappy()
		waiter.waitForEvent(MessageReceivedEvent.class, 
							e -> e.getAuthor().equals(user), 
							e -> firstHappy(e.getChannel(), user),
							RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
							("and what are some ways to recreate that success?").queue());
	}
	
	/**
	 * @param currentChannel: The MessageChannel used by the bot & users
	 * @param user: used to confirm the user sent a reply, not anyone else 
	 * 
	 * This method is used during the '!happy' and '!content' condition.
	 * The following is the flow of conversation:
	 * 
	 * 		'!happy' -> [BOT RESPONSE/QUESTION] -> [USER RESPONSE] -> firstHappy() is called
	 */
	private void firstHappy(MessageChannel currentChannel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("amazingness! what are some ways to recreate that success?").queueAfter(2, TimeUnit.SECONDS);
		waiter.waitForEvent(MessageReceivedEvent.class, 
		e -> e.getAuthor().equals(user) , 
		e -> secondHappy(currentChannel, user),
		RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage("you must be busy, see you around!").queue());
		
	}
	
	/**
	 * @param currentChannel: The MessageChannel used by the bot & users
	 * @param user: used to confirm the user sent a reply, not anyone else 
	 * 
	 * This method is the second response for the '!happy' and '!content' condition.
	 * secondHappy() is called by firstHappy() to ensure that the bot waits
	 * for the user to respond to the first question before sending its own response.
	 */
	private void secondHappy(MessageChannel currentChannel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("good job reflecting!").queueAfter(2, TimeUnit.SECONDS);
		currentChannel.sendMessage("great talking with you! take care :)").queueAfter(4, TimeUnit.SECONDS);		
	}
	
	/**
	 * @param currentChannel: The MessageChannel used by the bot & users
	 * @param user: used to confirm the user sent a reply, not anyone else
	 * 
	 */
	private void okayConvo(MessageChannel currentChannel, User user) {
		
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("Could be worse and could be better�"
								+ " I�ve definitely been there before.").queueAfter(2, TimeUnit.SECONDS);
		currentChannel.sendMessage("What do you think could be better?").queueAfter(5, TimeUnit.SECONDS);
		
		waiter.waitForEvent(MessageReceivedEvent.class, 
				e -> e.getAuthor().equals(user), 
				e -> firstOkay(e.getChannel(), user),
				RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
				("try revisiting this later.").queue());
	}
	
	private void firstOkay(MessageChannel channel, User user) {

		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("That definitely sounds like a tricky one. :/")
									.queueAfter(2, TimeUnit.SECONDS);
		currentChannel.sendMessage("On the other hand, what's been going well?").queueAfter(5, TimeUnit.SECONDS);
		
		waiter.waitForEvent(MessageReceivedEvent.class, 
				e -> e.getAuthor().equals(user), 
				e -> secondOkay(e.getChannel(), user),
				RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
				("try revisiting this later.").queue());	
	}

	private void secondOkay(MessageChannel channel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("That�s awesome, " + user.getName() +
								"! It�s always good to reflect on the upsides").queueAfter(2, TimeUnit.SECONDS);
	}

	private void sadConvo(MessageChannel currentChannel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("Ugh that�s rough, " + user.getName()
				+ ". It�s never fun to feel that way.").queueAfter(2, TimeUnit.SECONDS);
		currentChannel.sendMessage("Is there someone you can "
				+ "call or text right now? Sometimes all it takes is"
				+ " being proactive in tough times.").queueAfter(5, TimeUnit.SECONDS);
		
		waiter.waitForEvent(MessageReceivedEvent.class, 
				e -> e.getAuthor().equals(user), 
				e -> firstSad(e.getChannel(), user, e.getMessage().getContentRaw().toLowerCase()),
				RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
				("try revisiting this later.").queue());
	}
	
	private void firstSad(MessageChannel currentChannel, User user, String response){
		if(response.equals("yes") || response.equals("yea") || response.equals("yeah")){
			currentChannel.sendTyping().queue();
			currentChannel.sendMessage("Great, reach out to them! I�m sure "
					+ "they�ll be glad to hear from you.").queueAfter(2, TimeUnit.SECONDS);
		}
		else{
			currentChannel.sendTyping().queue();
			currentChannel.sendMessage("That�s totally fine. I find staying"
					+ " busy can help alleviate these feelings too. "
					+ "Consider cleaning your room or cooking yourself "
					+ "a meal! Both are excellent uses of your time.").queueAfter(2, TimeUnit.SECONDS);
		}
	}
	
	private void anxiousConvo(MessageChannel currentChannel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("I�m so sorry, " + user.getName() +
				", that�s always rough. Remember there are a few simple "
				+ "techniques to help combat anxiousness. One of which "
				+ "is listening to calming music.").queueAfter(4, TimeUnit.SECONDS);
		currentChannel.sendMessage("Listening to this song has been shown "
				+ "to bring down heart rate and promote relaxation."
				+ " I'll link the song and an article about it here!").queueAfter(7, TimeUnit.SECONDS);
		currentChannel.sendMessage(
				"https://www.lifehacker.com.au/2016/11/this-song-is-scientifically-proven-to-reduce-anxiety-in-8-minutes/")
				.queueAfter(8, TimeUnit.SECONDS);
	}
	
	private void sickConvo(MessageChannel currentChannel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("Being sick is the worst, especially if you already "
						+ "have a lot on your plate.\nTry to rest as much as you can and"
						+ " be sure to drink lots of water.\nAnd if you�ve been sick "
						+ "for several days, consider seeing a medical professional.")
						.queueAfter(4, TimeUnit.SECONDS);
	}
	
	private void tiredConvo(MessageChannel currentChannel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("I�m sorry you�re feeling tired, " + user.getName()
				+ ". It�s never fun to be low on energy.").queueAfter(2, TimeUnit.SECONDS);
	}
	
	private void depressedConvo(MessageChannel currentChannel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("I�m so sorry, " + user.getName()
					+ ", that must be tough.").queueAfter(2, TimeUnit.SECONDS);
		currentChannel.sendMessage("What has you feeling this way?").queueAfter(4, TimeUnit.SECONDS);
		
		waiter.waitForEvent(MessageReceivedEvent.class, 
				e -> e.getAuthor().equals(user), 
				e -> firstDepressed(e.getChannel(), user),
				RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
				("try revisiting this later.").queue());
	}
	
	private void firstDepressed(MessageChannel channel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("I see, thanks for sharing that. Remember"
				+ " that others probably struggle with the same issue as you.\n" + 
				"If you feel up to it, reach out to a friend or family member "
				+ "with this problem. I wish you well.\n").queueAfter(3, TimeUnit.SECONDS);
	}

	private void stressedConvo(MessageChannel currentChannel, User user) {
		currentChannel.sendTyping().queue();
		currentChannel.sendMessage("It�s never easy feeling stressed or "
				+ "overwhelmed.").queueAfter(2, TimeUnit.SECONDS);
		currentChannel.sendMessage("Is there something you have to do"
				+ " that�s stressing you out?").queueAfter(4, TimeUnit.SECONDS);
		
		waiter.waitForEvent(MessageReceivedEvent.class, 
				e -> e.getAuthor().equals(user), 
				e -> firstStressed(e.getChannel(), user, e.getMessage().getContentRaw().toLowerCase()),
				RESPONSE_WAIT_TIME, TimeUnit.SECONDS, () -> currentChannel.sendMessage
				("try revisiting this later.").queue());
	}

	private void firstStressed(MessageChannel channel, User user, String response) {
		if(response.equals("yes") || response.equals("yea") || response.equals("yeah")){
			currentChannel.sendTyping().queue();
			currentChannel.sendMessage("Gotcha. If the task feels daunting or overly "
					+ "stressful, try breaking it down into more manageable mini-tasks. "
					+ "This can help make the process feel less stressful and give you "
					+ "a sense of accomplishment when you finish each task!\n").queueAfter(2, TimeUnit.SECONDS);
		}
		else{
			currentChannel.sendTyping().queue();
			currentChannel.sendMessage("Ah I see. Try talking with someone about whatever is "
					+ "stressing you out. Just talking about your feelings can "
					+ "be a huge relief!\n").queueAfter(2, TimeUnit.SECONDS);
		}
	}
}
