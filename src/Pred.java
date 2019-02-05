import java.util.function.Predicate;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Pred implements Predicate<MessageReceivedEvent> {

	@Override
	public boolean test(MessageReceivedEvent arg0) {

		System.out.println("got into pred");
		if(arg0.getMessage().getContentRaw().startsWith("!happy")){
			return false;
		}
		return true;
	}

}
