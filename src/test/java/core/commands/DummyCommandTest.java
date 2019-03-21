package core.commands;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import core.BotConnectionTest;
import core.KittyBot;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DummyCommandTest extends BotConnectionTest {
	@Mock private KittyBot bot;
	@Mock private MessageReceivedEvent e;
	@Mock private User user;
	@Mock private Message message;
	private Command command;
	
	
	@BeforeEach 
	public void initDummyCommand() {
		command = new DummyCommand();
		command.setPrefix(prefix);
	}
	
	@BeforeEach
	public void buildDummyCommandMocks() {
		when(user.isBot()).thenReturn(false);
		doReturn(user).when(e).getAuthor();
		doReturn(message).when(e).getMessage();
		doReturn(false).when(message).isFromType(ChannelType.PRIVATE);
	}
	
}
