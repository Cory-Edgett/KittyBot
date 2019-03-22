package utils;

import java.util.Map;

public interface DiscordTree<T>{	

	public T removeLeaf(long key1, long key2);
	public T addLeaf(long key1, long key2, T t);
	public Map<Long, T> removeNode(long key1);
	public Map<Long, T> addNode(long key1);
	public boolean contains(long key1, long key2);
	public void destroyAll();
}
