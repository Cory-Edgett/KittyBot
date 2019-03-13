package core.commands;

import java.util.Arrays;
import java.util.List;

import core.utils.KittyBotInfo;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
/**
 * UPDATE PROCESS: 
 * 		In root project folder, run "mvn package."
 * 		In discord, use update command. TODO: Allow updates to be initiated from CLI.
 * 
 * @author Cortlan Edgett
 *
 */
public class UpdateCommand extends Command {
	
	
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if(!isOwner(e)) {
			sendMessage(e, "You are not authorized to use that command.");
			return;
		}
		sendMessage(e, "Updating bot " +KittyBotInfo.getCurrentVersion()+" -> " + KittyBotInfo.findNewVersion());
		bot.restart();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(prefix + "update");
	}

	@Override
	public String getDescription() {
		return "Restarts bot with new version, if available.";
	}

	@Override
	public String getName() {
		return "update";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Arrays.asList(prefix + getName());
	}

}
