package core.commands;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	 private List<Command> commands;
	 private String prefix;
	 public CommandListener() {
		 commands = new ArrayList<>();
	 }
	 
	public void setRespondToBots(Boolean bool) {
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
