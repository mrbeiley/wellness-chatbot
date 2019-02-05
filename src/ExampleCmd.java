import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ExampleCmd extends Command
{
    public ExampleCmd()
    {
        this.name = "example";
        this.aliases = new String[]{"test","demo"};
        this.help = "gives an example of commands do";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        event.reply("Hey look! This would be the bot's reply if this was a command!");
    }

}