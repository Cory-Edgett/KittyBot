package core.commands;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Component
public class CommandListener extends ListenerAdapter {
	 private List<Command> commands;
	 
	 @Value("${bot.prefix}")
	 private String prefix;
	 
	 public CommandListener() {
		 commands = new ArrayList<>();
	 }
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	    

	@Override
	public void onMessageReceived(MessageReceivedEvent e){
	  
	    if(!e.getMessage().getContentRaw().startsWith(prefix))
	    	return;
	        
	    if (e.isFromType(ChannelType.PRIVATE)) {
	     	System.out.printf("%s[%s] DM'd me: %s\n"
	       			, e.getAuthor().getName()
	       			, e.getAuthor().getId()
	       			, e.getMessage().getContentDisplay());
	    }
	    
	    for (Command command : commands) {
	     	if (command.containsCommand(e.getMessage())){
	      		command.onCommand(e, command.commandArgs(e.getMessage()));
	       	}
	    }
	}
	
	public Command registerCommand(Command command){
        commands.add(command);
        return command;
    }
}
