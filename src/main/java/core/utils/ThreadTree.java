package core.utils;

import java.util.Map;
import java.util.TreeMap;

public class ThreadTree implements DiscordTree<Thread> {
	private Map<Long, Map<Long, Thread>> threadMap;

	public ThreadTree() {
		this.threadMap = new TreeMap<>();
	}
	
	@Override
	public Thread addLeaf(long guild, long channel, Thread thread) {
		if(!threadMap.containsKey(guild)) addNode(guild);
		return threadMap.get(guild).put(channel, thread);
	}
	
	@Override
	public Map<Long, Thread> addNode(long guild) {
		return threadMap.put(guild, new TreeMap<>());
	}

	@Override
	public Thread removeLeaf(long guild, long channel) {
		return threadMap.get(guild).remove(channel);
	}

	@Override
	public Map<Long, Thread> removeNode(long guild) {
		return threadMap.remove(guild);

	}
	
	@Override
	public void destroyAll() {
		this.threadMap.values().forEach(
				channelThreadMap -> channelThreadMap.values().forEach(
						thread -> thread.interrupt()));
	}

	@Override
	public boolean contains(long guild, long channel) {
		if(!threadMap.containsKey(guild) || !threadMap.get(guild).containsKey(channel))
			return false;
		return true;
	}
	
}
