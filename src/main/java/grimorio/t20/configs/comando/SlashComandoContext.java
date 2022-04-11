package grimorio.t20.configs.comando;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;

public class SlashComandoContext implements IComandoContext {

    private final SlashCommandEvent event;
    private final List<String> args;

    public SlashComandoContext(SlashCommandEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    @Override
    public Guild getGuild() {
        return event.getGuild();
    }

    @Override
    public SlashCommandEvent getEvent() { return this.event; }

    @Override
    public List<String> getArgs() {
        return args;
    }
}
