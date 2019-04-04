package core.utils;

import java.util.Map;
import java.util.TreeMap;

public abstract class DiscordTree<T>{	
	private Map<Long, Map<Long, T>> map;
	
	public abstract void destroyAll();
	
	public T removeLeaf(long guild, long channel) {
		return getMap().get(guild).remove(channel);
	}	
	
	public T addLeaf(long guild, long channel, T chat) {
		if(!getMap().containsKey(guild)) addNode(guild);
		return getMap().get(guild).put(channel, chat);

	}	
	
	public Map<Long, T> removeNode(long guild) {
		return getMap().remove(guild);
	}	
	
	public Map<Long, T> addNode(long guild) {
		return getMap().put(guild, new TreeMap<>());
	}

	public boolean contains(long guild, long channel) {
		if(!getMap().containsKey(guild) || !getMap().get(guild).containsKey(channel))
			return false;
		return true;
	}	
	
	public Map<Long, Map<Long, T>> getMap() {
		return map;
	}
	
	public void setMap(Map<Long, Map<Long, T>> map) {
		this.map = map;
	}
}
