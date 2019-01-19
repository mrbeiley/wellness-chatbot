import java.io.File;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class myEventListener extends ListenerAdapter{

	private EventWaiter waiter;
	public MessageChannel currentChannel = null;
	
	public myEventListener(EventWaiter waiter) {
		this.waiter = waiter;
	}
	
	public void onMessageReceived(MessageReceivedEvent event) {
		if (currentChannel == null) {
			currentChannel = event.getChannel();
		}

		if(event.getAuthor().isBot())
			return;
		
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
			
			Predicate<MessageReceivedEvent> p = new Pred();
			Consumer<MessageReceivedEvent> c = new Cons();
		
			System.out.println("got here");
			waiter.waitForEvent(MessageReceivedEvent.class, p, c);
//			waiter.waitForEvent(MessageReceivedEvent.class, p, c);
//			waiter.waitForEvent(MessageReceivedEvent.class, 
//								e -> event.getChannel().equals(e.getChannel()), 
//								e -> currentChannel.sendMessage("hello user"), 5, TimeUnit.SECONDS, 
//							    () -> currentChannel.sendMessage("took too long"));
			System.out.println("and here");
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
}
