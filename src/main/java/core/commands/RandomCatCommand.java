package core.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import core.ThreadManager;
import core.utils.TheCatAPI;

@Component
public class RandomCatCommand extends Command {
	
	@Autowired
	private ThreadManager catStreamManager;
	private static final long TIMEOUT = 1000000;
	
	public void setCatStreamManager(ThreadManager catStreamManager) {
		this.catStreamManager = catStreamManager;
	}
	
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if(!getAliases().contains(args[0].toLowerCase())) {
			return;
		}
		if(handleStreamArg(e, args)) return;
		sendCat(e.getChannel());
	}
	/**
	 * 
	 * @return true: Cat stream started
	 */
	private boolean handleStreamArg(MessageReceivedEvent e, String[] args) {
		Guild guild = e.getGuild();
		MessageChannel channel = e.getChannel();
		if(args.length > 1 && args[1].toLowerCase().contentEquals("stream")) {
			if(!catStreamManager.contains(guild, channel)) {
				catStreamManager.addThread(guild, channel, startCatStream(e));
			} else {
				catStreamManager.destroyThread(guild, channel);
			}
			return true;
		}
		return false;
	}
	
	private Thread startCatStream(MessageReceivedEvent e) {
		Thread catThread = new Thread( () -> {
			MessageChannel channel = e.getChannel();
			ThreadManager.incrementThreadCount();
			sendMessage(channel,"Starting stream...");
			while(true) {
				try {
					sendCat(channel);
					Thread.sleep(TIMEOUT);
				} catch (InterruptedException e1) {
					break;
				}
			}
			sendMessage(channel, "Stream stopped.");
			ThreadManager.decrementThreadCount();
		});
		
		catThread.start();
		return catThread;
	}
	


	public void sendCat(MessageChannel channel) {
		String imageUrl = "";
		try {
			while((imageUrl = TheCatAPI.getCat(null)).equals(""));
			sendImage(channel, imageUrl);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(prefix + "cat"
							, prefix + "kitcat"
							, prefix + "kitkat"
							, prefix + "kitty");
	}

	@Override
	public String getDescription() {
		return "Command that sends a random cat picture!";
	}

	@Override
	public String getName() {
		return "Random Cat Command";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Collections.singletonList(prefix + "cat");
	}
	
	
}
