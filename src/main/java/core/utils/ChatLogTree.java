package core.utils;

import java.util.Map;
import java.util.TreeMap;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

public class ChatLogTree implements DiscordTree<ObservableList<String>> {
	
	private Map<Long, Map<Long, ObservableList<String>>> chatMap;
	
	public ChatLogTree() {
		this.chatMap = new TreeMap<>();
	}

	@Override
	public ObservableList<String> removeLeaf(long guild, long channel) {
		return chatMap.get(guild).remove(channel);
	}

	@Override
	public ObservableList<String> addLeaf(long guild, long channel, ObservableList<String> chat) {
		if(!chatMap.containsKey(guild)) addNode(guild);
		return chatMap.get(guild).put(channel, chat);

	}

	@Override
	public Map<Long, ObservableList<String>> removeNode(long guild) {
		return chatMap.remove(guild);
	}

	@Override
	public Map<Long, ObservableList<String>> addNode(long guild) {
		return chatMap.put(guild, new TreeMap<>());
	}

	@Override
	public boolean contains(long guild, long channel) {
		if(!chatMap.containsKey(guild) || !chatMap.get(guild).containsKey(channel))
			return false;
		return true;
	}

	@Override
	public void destroyAll() {
		chatMap.values().forEach(
				map -> {
					map.values().forEach(chat -> chat.clear());
					map.clear();
				});
		this.chatMap.clear();
	}
	
	public ObservableList<String> getChatLog(long guild, long channel){
		return chatMap.get(guild).get(channel);
	}
	
	public void addMessage(long guild, long channel, String message) {
		Platform.runLater(() -> getChatLog(guild, channel).add(message));
	}
	
	public void addMessage(Guild guild, MessageChannel channel, String message) {
		addMessage(guild.getIdLong(), channel.getIdLong(), message);
	}

}
