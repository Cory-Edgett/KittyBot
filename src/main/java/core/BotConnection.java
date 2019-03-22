package core;

import javax.security.auth.login.LoginException;

import core.commands.CommandListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class BotConnection {
	private JDA api;
	private JDABuilder builder;
	
	
	public JDABuilder getBuilder() {
		return builder;
	}

	public void setBuilder(JDABuilder builder) {
		this.builder = builder;
	}
	
	/**
	 * Builds the JDA object, which in turn logs the bot in to discord
	 * 
	 * @throws LoginException
	 */
	public void login() throws LoginException {
		this.api = builder.build();
	}
	
	/**
	 * Performs a soft shutdown of the bot.
	 * Before shutting down, all queued actions are performed.
	 * 
	 */
	public void logout() {
		this.api.shutdown();
	}
	
	/**
	 * Performs a hard-shutdown of the bot.
	 * Any queued actions are discarded.
	 * 
	 * Use for exceptional behavior.
	 */
	public void forceLogout() {
		this.api.shutdownNow();
	}
	
	public JDA getApi() {
		return api;
	}
	
	/**
	 * Sets the given implementation of {@link ListenerAdapter} as an event listener for the bot.
	 * @param listener	
	 */
	public void addEventListener(ListenerAdapter listener) {
		builder.addEventListener(listener);
	}
	
	/**
	 * Sets the "game" of the bot.
	 * 
	 * @param message
	 * 		The string to set as the game.
	 */
	public void setGame(String message) {
		api.getPresence().setGame(Game.of(GameType.WATCHING, message));
	}
	
	/**
	 * Stops thread until the bot is ready.
	 * 
	 * @throws InterruptedException
	 */
	public void awaitReady() throws InterruptedException {
		api.awaitReady();
	}
	

}
