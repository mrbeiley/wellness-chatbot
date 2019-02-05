import java.util.Random;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Webhook;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;

/**
 * 
 * @author Michael Beiley
 * 
 * 1/19/19
 * 
 * Main file for chatbot/API declarations. Declares the JDA, EventListener,
 * and EventWaiter objects.
 */
public class Main {
	private static JDA api;
	private static MyEventListener eventListener;
	
	public static void main(String[] args) throws LoginException {
		
//		CommandClientBuilder builder = new CommandClientBuilder();
//		builder.useDefaultGame();
//		builder.setPrefix("!");
//		builder.addCommand(new ExampleCmd());
//		builder.setOwnerId("522529183079989258");
//		
	//	CommandClient client = builder.build();
		
		// Declare JDA object and EventWaiter object
		api = new JDABuilder(AccountType.BOT).setToken("NTIyNTI5MTgzMDc5OTg5MjU4.DvMVSQ._uxPAtQ8Jsfut1NyzEkKFbkV_M0").build();
		EventWaiter waiter = new EventWaiter();
		api.addEventListener(waiter);

		// Add EventWaiter object to JDA object
		eventListener = new MyEventListener(waiter);
		api.addEventListener(eventListener);
//		api.addEventListener(client);
		
//		sendMessageToUser(); // method is declared below
	}
	
	// Variables used for sending periodic health reminders to the user
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final static HealthReminders healthReminders = new HealthReminders();
	private final static Random rand = new Random();
	
	/**
	 * Can send the user a message at fixed intervals for a fixed amount of time. Currently,
	 * it sends the message "hi" every 10 seconds for 1 hour.
	 * 
	 * Eventually, this should send health reminders to the user. I'll probably implement
	 * some ArrayList of reminders and rotate through them randomly.
	 */
	public static void sendMessageToUser() {
	     
		final Runnable beeper = new Runnable() {   
			public void run() {
				if(eventListener.currentChannel != null) {
					int randIndex = rand.nextInt(healthReminders.size());
					eventListener.currentChannel.sendMessage((CharSequence) healthReminders.get(randIndex)).queue();
	    	    }
			}
	    };
	     
	    final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, 10, TimeUnit.SECONDS);
	    scheduler.schedule(new Runnable() {
	       public void run() { beeperHandle.cancel(true); }
	     }, 60 * 60, TimeUnit.SECONDS);
   }
} // end of Main.java
