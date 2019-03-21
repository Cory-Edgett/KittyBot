package core.commands;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class QuitCommand extends Command {
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if(!isOwner(e)) {
			sendMessage(e, "You are not authorized to use that command.");
			return;
		}
		sendMessage(e, "Shutting down...");
		bot.shutdown(0);
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(prefix + getName());
	}

	@Override
	public String getDescription() {
		return "Shuts down the bot.";
	}

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Arrays.asList(prefix + getName());
	}

}
