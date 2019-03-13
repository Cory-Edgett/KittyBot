package core.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VectorMapCommand extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if(args.length < 2) return;
		e.getChannel().sendTyping().complete();
		try {
			Collection<String> results = trainer.findClosest(args[1]);
			for(String str : results) {
				sendMessage(e, str);
			}
		} catch(NullPointerException e1){
			sendMessage(e, "Sorry, I don't know that word yet :(");
			return;
		}
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(prefix + "vector", prefix + "v", prefix + "similiar");
	}

	@Override
	public String getDescription() {
		return "Gets the top 10 words closest to your word";
	}

	@Override
	public String getName() {
		return "vector";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Arrays.asList(prefix + "vector");
	}

}
