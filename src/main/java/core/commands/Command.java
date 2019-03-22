package core.commands;

import java.util.List;

import core.KittyBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.KittyBotInfo;

public abstract class Command extends ListenerAdapter {
    public abstract void onCommand(MessageReceivedEvent e, String[] args);
    public abstract List<String> getAliases();
    public abstract String getDescription();
    public abstract String getName();
    public abstract List<String> getUsageInstructions();
    protected String prefix;
	protected KittyBot bot;
	protected KittyBotInfo info;

	public void setInfo(KittyBotInfo info) {
		this.info = info;
	}
	
    public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
    
	public void setBot(KittyBot bot) {
		this.bot = bot;
	}
	
	protected boolean containsCommand(Message message) {
        return getAliases().contains(commandArgs(message)[0]);
    }

    protected String[] commandArgs(Message message) {
        return commandArgs(message.getContentDisplay().toLowerCase());
    }

    protected String[] commandArgs(String string) {
        return string.split(" ");
    }
    
    protected void sendMessage(MessageChannel channel, Message message) {
    	channel.sendMessage(message).queue();
    }
    
	protected void sendMessage(MessageChannel channel, String string) {
		channel.sendMessage(string).complete();
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
    
	protected boolean isOwner(MessageReceivedEvent e) {
		return e.getAuthor().getId().equals(bot.getOwnerId());
	}
}
