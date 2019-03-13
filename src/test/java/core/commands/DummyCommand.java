package core.commands;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DummyCommand extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(prefix + "dummy");
	}

	@Override
	public String getDescription() {
		return "dummy command for testing purposes";
	}

	@Override
	public String getName() {
		return "dummy";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Arrays.asList("not for use");
	}

}
