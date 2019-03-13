package core;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.security.auth.login.LoginException;

import core.commands.Command;
import core.commands.HelpCommand;
import core.commands.LearnCommand;
import core.commands.QuitCommand;
import core.commands.RandomCatCommand;
import core.commands.UpdateCommand;
import core.commands.VectorMapCommand;
import core.commands.VersionCommand;
import core.utils.ExceptionHandler;
import core.utils.KittyBotInfo;
import core.BotConnection;
import net.dv8tion.jda.core.JDA;

public class KittyBot {
	
	//----autowired----//
	private BotConnection connection;
	private HelpCommand helpCommand;
	private RandomCatCommand randomCatCommand;
	private UpdateCommand updateCommand;
	private QuitCommand quitCommand;
	private VersionCommand versionCommand;
	private LearnCommand learnCommand;
	private VectorMapCommand vectorMapCommand;

	//-----------------//
	
	/* values found in Config.properties */
	private String presenceMessage;
	private String ownerId;
	
	public void setPresenceMessage(String presenceMessage) {
		this.presenceMessage = presenceMessage;
	}
	
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getOwnerId() {
		return this.ownerId;
	}

	public void setHelpCommand(HelpCommand help) {
		this.helpCommand = help;
	}

	public void setRandomCatCommand(RandomCatCommand randomCat) {
		this.randomCatCommand = randomCat;
	}
	
	public void setUpdateCommand(UpdateCommand update) {
		this.updateCommand = update;
	}
	
	public void setQuitCommand(QuitCommand quit) {
		this.quitCommand = quit;
	}
	
	public void setVersionCommand(VersionCommand v) {
		this.versionCommand = v;
	}
	
	public void setLearnCommand(LearnCommand learnCommand) {
		this.learnCommand = learnCommand;
	}
	
	public void setVectorMapCommand(VectorMapCommand vectorMapCommand) {
		this.vectorMapCommand = vectorMapCommand;
	}
	

	public BotConnection getConnection() {
		return connection;
	}
	

	public void setConnection(BotConnection connection) {
		this.connection = connection;
	}
	
/*-----------------------------------------------------------------------------------------------------------*/
	/**
	 * Performs all the startup tasks for the bot.
	 * 
	 * register commands -> login -> awaitReady -> set game message
	 * 
	 * @throws LoginException
	 * 			In case login fails.
	 * @throws InterruptedException
	 * 			In case the bot is interrupted while waiting for ready.
	 */
	public void setupBot() throws LoginException, InterruptedException {
		registerListeners();
		connection.login();
		connection.awaitReady();
		connection.setGame(presenceMessage);
		System.out.println("Bot setup with version#" + KittyBotInfo.getCurrentVersion());
	}
	
	/**
	 * Adds all commands to the help command tree-map, and then registers
	 * 		those commands as event listeners for the bot.
	 */
	private void registerListeners() {
		//TODO: Automate listener registration with spring if possible.

		connection.addEventListener(helpCommand.registerCommand(helpCommand));
		connection.addEventListener(helpCommand.registerCommand(randomCatCommand));
		connection.addEventListener(helpCommand.registerCommand(updateCommand));
		connection.addEventListener(helpCommand.registerCommand(quitCommand));
		connection.addEventListener(helpCommand.registerCommand(versionCommand));
		connection.addEventListener(helpCommand.registerCommand(learnCommand));
		connection.addEventListener(helpCommand.registerCommand(vectorMapCommand));
	}
	
	/**
	 * Prints a list of connected guilds to standard out.
	 * @throws InterruptedException
	 */
	public void printGuilds() throws InterruptedException {
		JDA api = connection.getApi();
		api.awaitReady();
		System.out.println("In " + api.getGuilds().size() + " guilds: ");
		api.getGuilds().forEach( guild -> System.out.println(guild.getName()));
	}
	
	/**
	 * Restarts the bot with the latest version.
	 */
	public void restart() {
		try {
			System.out.printf("Updating bot: %s -> %s\n", KittyBotInfo.getCurrentVersion(), KittyBotInfo.findNewVersion());
			this.start(KittyBotInfo.getNewestJarPath());
		} catch (IOException | URISyntaxException e) {
			ExceptionHandler.printErr(e);
			System.err.println("Restart failed.");
		}
		this.quit(0);
	}
	
	/**
	 * Starts the jar of the given jar as a new process.
	 * @param jarPath
	 * 		The ABSOLUTE path to the given jar file 
	 * 		(kittyBot can automatically find it's latest version with 
	 * {@link core.utils.KittyBotInfo#getNewestJarPath() KittyBotInfo.getNewestJarPath})
	 * @throws IOException
	 */
	public void start(String jarPath) throws IOException {
		System.out.println(jarPath);
		String[] command = new String[] {"cmd.exe", "/c", "start", "java", "-jar", jarPath };
		Runtime.getRuntime().exec(command);
	}
	
	/**
	 * 
	 * @param status
	 * 		0 - Normal
	 * 	   !0 - TODO: Implement exit statuses.
	 */
	public void quit(int status) {
		this.connection.logout();
		System.exit(0);
	}
}
