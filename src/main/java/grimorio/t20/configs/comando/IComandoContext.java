package grimorio.t20.configs.comando;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.List;

public interface IComandoContext {

    Guild getGuild();

    Event getEvent();

    List<String> getArgs();

    default boolean isMessageEvent() { return (getEvent() instanceof MessageReceivedEvent); }

    default boolean isSlashEvent() { return (getEvent() instanceof SlashCommandEvent); }

    default TextChannel getChannel() {
        if (isMessageEvent())
            return ((MessageReceivedEvent) this.getEvent()).getTextChannel();
        else if (isSlashEvent()) {
            return ((SlashCommandEvent) this.getEvent()).getTextChannel();
        } else {
            return null;
        }
    }

    default Message getMessage() {
        if (isMessageEvent())
            return ((MessageReceivedEvent) this.getEvent()).getMessage();
        else if (isSlashEvent()) {
            return null;
        } else {
            return null;
        }
    }

    default User getAuthor() {
        if (isMessageEvent())
            return ((MessageReceivedEvent) this.getEvent()).getAuthor();
        else if (isSlashEvent()) {
            return ((SlashCommandEvent) this.getEvent()).getUser();
        } else {
            return null;
        }
    }

    default Member getMember() {
        if (isMessageEvent())
            return ((MessageReceivedEvent) this.getEvent()).getMember();
        else if (isSlashEvent()) {
            return ((SlashCommandEvent) this.getEvent()).getMember();
        } else {
            return null;
        }
    }

    default JDA getJDA() {
        return this.getEvent().getJDA();
    }

    default ShardManager getShardManager() {
        return this.getJDA().getShardManager();
    }

    default User getSelfUser() {
        return this.getJDA().getSelfUser();
    }

    default Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }

}
