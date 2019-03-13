package core.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import core.utils.ExceptionHandler;
import core.utils.TheCatAPI;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand extends Command {
    private static final String NO_NAME = "No name provided for this command. Sorry!";
    private static final String NO_DESCRIPTION = "No description has been provided for this command. Sorry!";
    private static final String NO_USAGE = "No usage instructions have been provided for this command. Sorry!";

   

	private TreeMap<String, Command> commands;

    public HelpCommand()
    {
        commands = new TreeMap<>();
    }
    
    public Command registerCommand(Command command)
    {
        commands.put(command.getAliases().get(0), command);
        return command;
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args)
    {
        if(!e.isFromType(ChannelType.PRIVATE))
        {
            e.getTextChannel().sendMessage(new MessageBuilder()
                    .append(e.getAuthor())
                    .append(": Help information was sent as a private message. (I work in DMs too!)")
                    .build()).queue();
        }
        
        //send help info along with cat picture! :D
        try {
			sendImage(sendPrivate(e.getAuthor().openPrivateChannel().complete(), args), TheCatAPI.getCat(null));
		} catch (IOException e1) {
			ExceptionHandler.printErr(e1);
		}
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList(
        		prefix +"help"
        		, prefix +"commands");
    }

    @Override
    public String getDescription()
    {
        return "Command that helps use all other commands!";
    }

    @Override
    public String getName()
    {
        return "Help Command";
    }

    @Override
    public List<String> getUsageInstructions()
    {
        return Collections.singletonList(
        		prefix +"help   **OR**  "+ prefix +"help *<command>*\n"
        		+ prefix + "help - returns the list of commands along with a simple description of each.\n"
        		+ prefix + "help <command> - returns the name, description, aliases and usage information of a command.\n"
                + "   - This can use the aliases of a command as input as well.\n"
                + "__Example:__ "+ prefix +"help kitkat");
    }

    private PrivateChannel sendPrivate(PrivateChannel channel, String[] args)
    {
        if (args.length < 2)
        {
            StringBuilder s = new StringBuilder();
            for (Command c : commands.values())
            {
                String description = c.getDescription();
                description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

                s.append("**").append(c.getAliases().get(0)).append("** - ");
                s.append(description).append("\n");
            }

            channel.sendMessage(new MessageBuilder()
                    .append("The following commands are supported by the bot\n")
                    .append(s.toString())
                    .build()).queue();
        }
        else
        {
            String command = args[1].startsWith(prefix) ? args[1] : prefix + args[1];    //If there is not a preceding . attached to the command we are search, then prepend one.
            for (Command c : commands.values())
            {
                if (c.getAliases().contains(command))
                {
                    String name = c.getName();
                    String description = c.getDescription();
                    List<String> usageInstructions = c.getUsageInstructions();
                    name = (name == null || name.isEmpty()) ? NO_NAME : name;
                    description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;
                    usageInstructions = (usageInstructions == null || usageInstructions.isEmpty()) ? Collections.singletonList(NO_USAGE) : usageInstructions;

                    //TODO: Replace with a PrivateMessage
                    channel.sendMessage(new MessageBuilder()
                            .append("**Name:** " + name + "\n")
                            .append("**Description:** " + description + "\n")
                            .append("**Alliases:** " + String.join(", ", c.getAliases()) + "\n")
                            .append("**Usage:** ")
                            .append(usageInstructions.get(0))
                            .build()).queue();
                    for (int i = 1; i < usageInstructions.size(); i++)
                    {
                        channel.sendMessage(new MessageBuilder()
                            .append("__" + name + " Usage Cont. (" + (i + 1) + ")__\n")
                            .append(usageInstructions.get(i))
                            .build()).queue();
                    }
                    return channel;
                }
            }
            channel.sendMessage(new MessageBuilder()
                    .append("The provided command '**" + args[1] + "**' does not exist. Use "+prefix+"help to list all commands.")
                    .build()).queue();
        }
        return channel;
        
    }
}
