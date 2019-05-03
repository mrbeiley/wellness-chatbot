import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.Timer;
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
	private static AccountTable accounts;
	
	public static void main(String[] args) throws LoginException, FileNotFoundException, IOException, ClassNotFoundException {
		
		// Declare JDA object and EventWaiter object
		api = new JDABuilder(AccountType.BOT).setToken("NTIyNTI5MTgzMDc5OTg5MjU4.DvMVSQ._uxPAtQ8Jsfut1NyzEkKFbkV_M0").build();
		EventWaiter waiter = new EventWaiter();
		api.addEventListener(waiter);
		
		// Add EventWaiter object to JDA object
		eventListener = new MyEventListener(waiter);
		api.addEventListener(eventListener);
		
		sendMessageToUser(); // method is declared below
		
	}
	
	// Variables used for sending periodic health reminders to the user
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final static HealthReminders healthReminders = new HealthReminders();
	private final static Random rand = new Random();
	
	/**
	 * Sends health reminders to the user. This method rotates through an 
	 * ArrayList of reminders and displays a random reminder every 10 seconds.
	 */
	public static void sendMessageToUser() {
	     
		final Runnable beeper = new Runnable() {   
			public void run() {
				if(eventListener.currentChannel != null) {
					int randIndex = rand.nextInt(healthReminders.size());
				//	eventListener.currentChannel.sendMessage((CharSequence) healthReminders.get(randIndex)).queue();
					createMoodGraph();
	    	    }
			}
	    };
	    
	    // Schedules the reminders every 10 seconds
	    final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, 10, TimeUnit.SECONDS);
	    
	    // Cancels the reminders after 100 seconds
	    scheduler.schedule(new Runnable() {
	       public void run() { beeperHandle.cancel(true); }
	     }, 100, TimeUnit.SECONDS);
   }
	
	public static void createMoodGraph() {
		MoodGraph graph = new MoodGraph("Mood Graph", "Moods over last " + eventListener.account.getDaysInGraph() + " days", eventListener.account);
		
	}
} // end of Main.java
