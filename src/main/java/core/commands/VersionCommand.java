package core.commands;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Component
public class VersionCommand extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		sendMessage(e, "Current version: " + info.getCurrentVersion());
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(prefix + "version", prefix + "v", prefix + "ver");
	}

	@Override
	public String getDescription() {
		return "Sends current version number.";
	}

	@Override
	public String getName() {
		return "Version";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Arrays.asList(prefix + "version");
	}

}
