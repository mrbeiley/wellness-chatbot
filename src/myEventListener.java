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
			currentChannel.sendTyping().queue();
			currentChannel.sendMessage("That's amazing to hear " + event.getAuthor().getName()
							  + "! It's always nice when things fall into place!").queueAfter(2, TimeUnit.SECONDS);
			currentChannel.sendMessage("What do you think has you feeling so good??").queueAfter(5, TimeUnit.SECONDS);
			
//			int i = 0;
									
			// Wait for the user to respond to the bot's 1st question
			waiter.waitForEvent(MessageReceivedEvent.class, 
								e -> event.getChannel().equals(e.getChannel()) && e.getAuthor().equals(event.getAuthor()), 
								e -> firstHappy(e.getChannel()),
								15, TimeUnit.SECONDS, () -> currentChannel.sendMessage("and what are some ways to recreate that success?").queue());

//			waiter.waitForEvent(MessageReceivedEvent.class, 
//					e -> event.getChannel().equals(e.getChannel()) && e.getAuthor().equals(event.getAuthor()) , 
//					e -> currentChannel.sendMessage("awesome, i'm glad you shared that").queueAfter(2, TimeUnit.SECONDS),
//					30, TimeUnit.MINUTES, () -> currentChannel.sendMessage("you must be busy, see you around!").queue());

			
		}
		if(content.startsWith("!okay") || content.startsWith("!indifferent")) {

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
	 * @param The MessageChannel used by the bot & users
	 * 
	 * This method is used during the '!happy' and '!content' condition.
	 * The following is the flow of conversation:
	 * 
	 * 		'!happy' -> [BOT RESPONSE/QUESTION] -> [USER RESPONSE] -> firstHappy() is called
	 */
	private void firstHappy(MessageChannel currentChannel) {
			
		currentChannel.sendMessage("amazingness! what are some ways to recreate that success?").queueAfter(2, TimeUnit.SECONDS);
	}
}
