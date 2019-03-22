package core;

import java.util.concurrent.atomic.AtomicInteger;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import utils.DiscordTree;

public class ThreadManager {
	private DiscordTree<Thread> threadTree;
	private static AtomicInteger threadCount = new AtomicInteger(0);
	
	public void setThreadTree(DiscordTree<Thread> threadTree) {
		this.threadTree = threadTree;
	}
	
	public void addThread(Guild guild, MessageChannel channel, Thread thread) {
		threadTree.addLeaf(guild.getIdLong(), channel.getIdLong(), thread);
	}
	
	public void destroyThread(Guild guild, MessageChannel channel) {
		removeThread(guild, channel).interrupt();
	}
	
	private Thread removeThread(Guild guild, MessageChannel channel) {
		return threadTree.removeLeaf(guild.getIdLong(), channel.getIdLong());
	}
	
	public void destroyAll() {
		threadTree.destroyAll();
	}
	
	public boolean contains(Guild guild, MessageChannel channel) {
		return threadTree.contains(guild.getIdLong(), channel.getIdLong());
	}
	
	public static void incrementThreadCount() {
		threadCount.incrementAndGet();
	}
	
	public static void decrementThreadCount() {
		threadCount.decrementAndGet();
	}
	

	
	public static void waitForThreads() throws InterruptedException {
		while(!threadCount.compareAndSet(0, 0)) Thread.sleep(1000);
	}
	
	
}
