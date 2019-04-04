package core.utils;

import java.util.TreeMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ThreadTree extends DiscordTree<Thread> {
	

	public ThreadTree() {
		setMap(new TreeMap<>());
	}
	
	@Override
	public void destroyAll() {
		getMap().values().forEach(
				channelThreadMap -> channelThreadMap.values().forEach(
						thread -> thread.interrupt()));
	}

	@Override
	public boolean contains(long guild, long channel) {
		if(!getMap().containsKey(guild) || !getMap().get(guild).containsKey(channel))
			return false;
		return true;
	}
	
}
