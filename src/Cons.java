import java.util.function.Consumer;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Cons implements Consumer<MessageReceivedEvent>{

	@Override
	public void accept(MessageReceivedEvent t) {

		System.out.println("Inside Cons");
		MessageChannel channel = t.getChannel();
		channel.sendMessage("send pls").queue();
	}

}
