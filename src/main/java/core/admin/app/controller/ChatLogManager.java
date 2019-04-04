package core.admin.app.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import core.KittyBot;
import core.utils.ChatLogTree;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Component
public class ChatLogManager extends ListenerAdapter {
	@Autowired
	private ChatLogTree chatLogs;
	@Autowired
	private KittyBot bot;
	@Autowired
	private ApplicationController controller;
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e){
		if(e.isFromType(ChannelType.PRIVATE))
			return;

		chatLogs.addMessage(
				e.getGuild()
				, e.getChannel()
				, e.getAuthor().getName() + ": "+ e.getMessage().getContentDisplay());

	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent e) {
		System.out.println("hit");
		Platform.runLater(() -> controller.updateGuilds());
		chatLogs.addGuild(e.getGuild());
		Platform.runLater(() -> controller.updateGuilds());

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
	
	@PostConstruct
	public void init() throws InterruptedException {
		bot.getConnection().getApi().addEventListener(this);
    	buildChatLogs();
	}
	
	private void buildChatLogs() throws InterruptedException {
		bot.getGuilds().forEach(guild -> this.chatLogs.addGuild(guild));
	}
	
	public void setChatLogs(ChatLogTree chatLogs) {
		this.chatLogs = chatLogs;
	}

	public void setBot(KittyBot bot) {
		this.bot = bot;
	}

	public void addGuild(Guild guild) {
		chatLogs.addGuild(guild);
	}

	public void setController(ApplicationController controller) {
		this.controller = controller;
	}


}
