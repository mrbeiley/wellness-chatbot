import java.io.File;

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
public class myEventListener extends ListenerAdapter{

	private EventWaiter waiter;
	public MessageChannel currentChannel = null;
	public static final int RESPONSE_WAIT_TIME = 15; // seconds
	
	public myEventListener(EventWaiter waiter) {
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

		if(event.getAuthor().isBot())
			return;
		
		// Store the message sent by the user
		Message message = event.getMessage();
		String content = message.getContentRaw();
		
		if(content.startsWith("!ping")) {
			currentChannel.sendMessage("Pong " + event.getJDA().getPing()).queue();
		}
		if(content.startsWith("!happy") || content.startsWith("!content")) {
			happyConvo(currentChannel, event.getAuthor());
		}
		if(content.startsWith("!okay") || content.startsWith("!indifferent")) {
			okayConvo(currentChannel, event.getAuthor());
		}
		if(content.startsWith("!sad") || content.startsWith("!lonely")) {
			
		}
		if(content.startsWith("!anxious")) {
			
		}
		if(content.startsWith("!sick")) {
			
		}
		if(content.startsWith("!tired")) {
			
		}
		if(content.startsWith("!angry")) {
			
		}
		if(content.startsWith("!depressed")) {
			
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
		currentChannel.sendMessage("That sounds like a difficult situation. "
								   + "Don�t be afraid to reach out to friends or family. "
								  + "I�m sure they would be more than happy to help.").queueAfter(2, TimeUnit.SECONDS);
		currentChannel.sendMessage("Now, what's been going well?").queueAfter(5, TimeUnit.SECONDS);
		
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
				+ "It�s never fun to feel that way.").queueAfter(2, TimeUnit.SECONDS);
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
				+ "is listening to calming music. Listening to this "
				+ "song has been shown to bring down heart rate and "
				+ "promote relaxation. If you�re curious, read more"
				+ " about it here! "
				+ "https://www.lifehacker.com.au/2016/11/this-song-is-scientifically-proven-to-reduce-anxiety-in-8-minutes/")
				.queueAfter(4, TimeUnit.SECONDS);
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
		
	}
	
	private void angryConvo(MessageChannel currentChannel, User user) {
		
	}
	
	private void depressedConvo(MessageChannel currentChannel, User user) {
		
	}
}
