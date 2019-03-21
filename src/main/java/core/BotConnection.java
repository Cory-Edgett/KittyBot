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
	 * Sets the given command as an event listener for the bot.
	 * Note: Every command must be it's own listener, so if you have N commands,
	 * 			every event will be handled N times.
	 * 
	 * @param registerCommand
	 * 		command to register as an event listener
	 */
	public void addEventListener(CommandListener listener) {
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
