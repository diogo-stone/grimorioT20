package grimorio.t20.configs.comando;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class MessageComandoContext implements IComandoContext {

    private final MessageReceivedEvent event;
    private final List<String> args;

    public MessageComandoContext(MessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    @Override
    public Guild getGuild() {
        return event.getGuild();
    }

    @Override
    public MessageReceivedEvent getEvent() {
        return this.event;
    }

    @Override
    public List<String> getArgs() {
        return args;
    }
}
