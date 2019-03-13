package core.commands;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import core.KittyBot;
import core.nlp.Trainer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class Command extends ListenerAdapter {
    public abstract void onCommand(MessageReceivedEvent e, String[] args);
    public abstract List<String> getAliases();
    public abstract String getDescription();
    public abstract String getName();
    public abstract List<String> getUsageInstructions();
    protected String prefix;
	protected KittyBot bot;
	protected Trainer trainer;

    public void setTrainer(Trainer trainer) {
    	this.trainer = trainer;
    }
    
    public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
    
	public void setBot(KittyBot bot) {
		this.bot = bot;
	}
    

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getAuthor().isBot() && !respondToBots())
            return;
        
        if (e.isFromType(ChannelType.PRIVATE)) {
        	System.out.printf("%s[%s] DM'd me: %s\n"
        			, e.getAuthor().getName()
        			, e.getAuthor().getId()
        			, e.getMessage().getContentDisplay());
        }
		if (containsCommand(e.getMessage())){
            onCommand(e, commandArgs(e.getMessage()));
        }
        int numOfCat = containsCat(e.getMessage());

		if (numOfCat>0) {
        	addCatReact(e, numOfCat);
        }
        
    }
    
    /**
     * Reacts numOfCat times to the given message.
     * @param e
     * 		the MessageReceivedEvent
     * @param numOfCat
     * 		the number of "cat words" in the message,
     * 			determined by {@link #containsCat(Message) containsCat}
     */
    protected void addCatReact(MessageReceivedEvent e, int numOfCat) {
    	for(String react : Arrays.asList("🐱", "😹", "🐈", "😸")) {
    		e.getMessage().addReaction(react).complete();
    		numOfCat--;
    		if (numOfCat ==0) break;
    	}
		
	}
    
    /**
     * Searches a given string for occurrences of "cat words"
     * @param message
     * 		The message to search.
     * @return the number of occurrences
     */
	protected int containsCat(Message message) {
		int count = 0;
		for(String str : message.getContentRaw().toLowerCase().replaceAll("[^a-z ]", "").split(" ")) {
			if(trainer.closeToCat(str)) count++;
		}
		return count;
		
	}
	
	protected boolean containsCommand(Message message) {
        return getAliases().contains(commandArgs(message)[0]);
    }

    protected String[] commandArgs(Message message) {
        return commandArgs(message.getContentDisplay());
    }

    protected String[] commandArgs(String string) {
        return string.split(" ");
    }
    
    protected void sendMessage(MessageChannel channel, Message message) {
    	channel.sendMessage(message).queue();
    }

    protected void sendMessage(MessageReceivedEvent e, Message message) {
        sendMessage(e.getChannel(), message);
    }

    protected void sendMessage(MessageReceivedEvent e, String message) {
        sendMessage(e, new MessageBuilder().append(message).build());
    }
    
    protected void sendImage(MessageChannel channel, String url) {
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setImage(url);
    	sendMessage(channel, new MessageBuilder().setEmbed(eb.build()).build());
    }
    
    protected void deleteMessage(MessageReceivedEvent e) {
    	if(e.isFromType(ChannelType.PRIVATE)) 
    		return;
    	try {
    		e.getChannel().deleteMessageById(e.getMessageIdLong()).complete();
    	} catch (IllegalArgumentException e1) {
    		System.err.printf("Invalid messageId for event %s when attempting to delete\n", e.toString());
    	} catch (InsufficientPermissionException e2) {
    		System.err.printf("Unable to delete messages in Guild: %s, Channel: %s\n", e.getGuild().getName(), e.getChannel().getName());
    		sendMessage(e, "ERROR: Missing permissions; please allow me to delete command messages!");
    	}
    }
    
    protected boolean respondToBots() {
        return false;
    }
    
    protected boolean isOwner(MessageReceivedEvent e) {
    	return e.getAuthor().getId().equals(bot.getOwnerId());
    }
}
