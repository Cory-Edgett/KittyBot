package core.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import core.utils.TheCatAPI;

public class RandomCatCommand extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if(!getAliases().contains(args[0])) {
			return;
		}
		
		String settings = null;
		try {
			String imageUrl = TheCatAPI.getCat(settings);
			sendImage(e.getChannel(), imageUrl);
		} catch (IOException e1) {
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
