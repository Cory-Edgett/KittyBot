package core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.security.auth.login.LoginException;

import core.commands.CommandListener;
import core.commands.HelpCommand;
import core.commands.QuitCommand;
import core.commands.RandomCatCommand;
import core.commands.UpdateCommand;
import core.commands.VersionCommand;
import core.BotConnection;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import utils.ExceptionHandler;
import utils.KittyBotInfo;

public class KittyBot {
	
	//----autowired----//
	private BotConnection connection;
	private HelpCommand helpCommand;
	private RandomCatCommand randomCatCommand;
	private UpdateCommand updateCommand;
	private QuitCommand quitCommand;
	private VersionCommand versionCommand;
	private KittyBotInfo info;
	private CommandListener listener;
	private ThreadManager threadManager;
	//-----------------//
	/* values found in Config.properties */
	private String presenceMessage;
	private String ownerId;
	
	public void setThreadManager(ThreadManager threadManager) {
		this.threadManager = threadManager;
	}
	
	public void setListener(CommandListener listener) {
		this.listener = listener;
	}
	
	public void setInfo(KittyBotInfo info) {
		this.info = info;
	}
	
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
	

	public BotConnection getConnection() {
		return connection;
	}
	

	public void setConnection(BotConnection connection) {
		this.connection = connection;
	}
	
/*-------------------------------------------------------------------*/
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
		registerListener();
		connection.login();
		connection.awaitReady();
		connection.setGame(presenceMessage);
		System.out.println("Bot setup with version#" + info.getCurrentVersion());
	}
	
	/**
	 * Registers the event listener.
	 */
	private void registerListener() {
		connection.addEventListener(registerCommands(listener));
	}
	
	/**
	 * Adds all commands to the help command tree-map and command listener
	 * 		command-list.
	 */
	private CommandListener registerCommands(CommandListener listener) {
		//TODO: Automate command registration with spring if possible.
		helpCommand.registerCommand(listener.registerCommand(helpCommand));
		helpCommand.registerCommand(listener.registerCommand(quitCommand));
		helpCommand.registerCommand(listener.registerCommand(randomCatCommand));
		helpCommand.registerCommand(listener.registerCommand(updateCommand));
		helpCommand.registerCommand(listener.registerCommand(versionCommand));
		
		return listener;
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
	 * 
	 * @return The list of connected guilds.
	 * @throws InterruptedException 
	 */
	public List<Guild> getGuilds() throws InterruptedException {
		connection.awaitReady();
		return connection.getApi().getGuilds();
	}
	
	/**
	 * 
	 * @param guild
	 * @return The list of channels in guild.
	 */
	public List<TextChannel> getTextChannels(Guild guild){
		return guild.getTextChannels();
	}
	
	public User getSelf() {
		return connection.getApi().getSelfUser();
	}
	
	public String getVersion() {
		return info.getCurrentVersion();
	}
	
	/**
	 * Restarts the bot with the latest version.
	 */
	public void restart() {
		try {
			System.out.printf("Updating bot: %s -> %s\n", info.getCurrentVersion(), info.findNewVersion());
			this.start(info.getNewestJarPath());
		} catch (IOException | URISyntaxException e) {
			ExceptionHandler.printErr(e);
			System.err.println("Restart failed.");
		}
		this.shutdown(0);
	}
	
	/**
	 * Starts the jar of the given jar as a new process.
	 * @param jarPath
	 * 		The ABSOLUTE path to the given jar file 
	 * 		(kittyBot can automatically find it's latest version with 
	 * {@link utils.KittyBotInfo#getNewestJarPath() KittyBotInfo.getNewestJarPath})
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
	public void shutdown(int status) {
		threadManager.destroyAll();
		try {
			ThreadManager.waitForThreads();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.connection.logout();
		System.exit(0);
	}
}
