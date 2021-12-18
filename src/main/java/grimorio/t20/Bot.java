package grimorio.t20;

import grimorio.t20.configs.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class Bot {

    public static void main(String[] args) throws LoginException {
        String token = Config.get("token");
        if (token.length() < 1) {
            System.err.println("Token não informado.");
            return;
        }
        JDABuilder jda = JDABuilder.createDefault(token);
                jda.addEventListeners(new ComandoListener())
                .setActivity(Activity.watching("Prefixo: !@"))
                .build();

    }

}
