package core.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.nlp.Trainer;
import core.utils.ExceptionHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LearnCommand extends Command {
	private final int DEFAULT_AMOUNT = 100;
	private int amount = DEFAULT_AMOUNT;
	
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if(args.length > 1) parseAmount(args[1]);
		List<Message> messages = e.getChannel().getHistory().retrievePast(amount).complete();
		List<String> rawMessages = new ArrayList<String>();
		messages.forEach(msg -> rawMessages.add(msg.getContentRaw()));
		trainer.setDataSource(rawMessages);
		System.out.println("Calling trainer.train");
		trainer.train(e);
		deleteMessage(e);
		
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(prefix + "learn");
	}

	@Override
	public String getDescription() {
		return "Feeds the previous N messages to KittyBot so it can learn your human words!";
	}

	@Override
	public String getName() {
		return "learn";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Arrays.asList(prefix + "learn [N <= 100 past messages]", prefix + "learn (default " + DEFAULT_AMOUNT + " messages)");
	}
	
	public void parseAmount(String arg) {
		try {
			int inputAmount = Integer.parseInt(arg);
			amount = inputAmount > 0 && inputAmount < 100 ? inputAmount : DEFAULT_AMOUNT;
		} catch (NumberFormatException exc) {
			ExceptionHandler.printErr(exc);
		}
	}
	
	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

}
