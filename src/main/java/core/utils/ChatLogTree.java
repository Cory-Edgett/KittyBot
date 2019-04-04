package core.utils;

import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

@Component
@Scope("prototype")
public class ChatLogTree extends DiscordTree<ObservableList<String>> {
	
	
	public ChatLogTree() {
		setMap(new TreeMap<>());
	}

	@Override
	public void destroyAll() {
		getMap().values().forEach(
				map -> {
					map.values().forEach(chat -> chat.clear());
					map.clear();
				});
		getMap().clear();
	}
	
	public ObservableList<String> getChatLog(long guild, long channel){
		return getMap().get(guild).get(channel);
	}
	
	public void addMessage(long guild, long channel, String message) {
		if(!contains(guild, channel)) this.addLeaf(guild, channel, FXCollections.observableArrayList(new ArrayList<>()));
		Platform.runLater(() -> getChatLog(guild, channel).add(message));
	}
	
	public void addMessage(Guild guild, MessageChannel channel, String message) {
		addMessage(guild.getIdLong(), channel.getIdLong(), message);
	}
	
	/**
	 * Adds a guild's channels into the chat log if the channel is not yet logged
	 * Can be used to update channel lists
	 * @param guild
	 */
	public void addGuild(Guild guild) {
		guild.getTextChannels().forEach(channel -> {
			if(!this.contains(guild.getIdLong(), channel.getIdLong()) && channel.canTalk())
				this.addLeaf(guild.getIdLong()
						, channel.getIdLong()
						, FXCollections.observableArrayList(new ArrayList<String>()));
		});
	}

}
