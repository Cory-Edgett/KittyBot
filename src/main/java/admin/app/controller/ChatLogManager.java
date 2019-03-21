package admin.app.controller;

import java.util.ArrayList;

import core.KittyBot;
import core.utils.ChatLogTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChatLogManager extends ListenerAdapter {
	private ChatLogTree chatLogs;
	private KittyBot bot;
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e){
		chatLogs.addMessage(
				e.getGuild()
				, e.getChannel()
				, e.getAuthor().getName() + ": "+ e.getMessage().getContentDisplay());

	}
	
	public void init() throws InterruptedException {
		bot.getConnection().getApi().addEventListener(this);
    	
    	bot.getGuilds().forEach(guild -> {
    		guild.getTextChannels().forEach(channel -> {
    			if(channel.canTalk()) 
    				chatLogs.addLeaf(guild.getIdLong(), channel.getIdLong(), FXCollections.observableArrayList(new ArrayList<>()));;
    		});
    	});
	}
	
	/**
	 * 
	 * @param guild {@link Guild}
	 * @param channel {@link MessageChannel}
	 * @return The chat log for the given {@link MessageChannel} in the given {@link Guild}
	 */
	public ObservableList<String> getChatLog(Guild guild, MessageChannel channel){
		return getChatLog(guild.getIdLong(), channel.getIdLong());
	}
	
	private ObservableList<String> getChatLog(long guildId, long channelId){
		if(chatLogs.contains(guildId, channelId)) 
			return chatLogs.getChatLog(guildId, channelId) ;
		return chatLogs.addLeaf(guildId, channelId, FXCollections.observableArrayList(new ArrayList<>()));
	}
	
	public void setChatLogs(ChatLogTree chatLogs) {
		this.chatLogs = chatLogs;
	}

	public void setBot(KittyBot bot) {
		this.bot = bot;
	}


}
