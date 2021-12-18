package grimorio.t20;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class Bot extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        if (args.length < 1) {
            System.err.println("Token não informado.");
            return;
        }
        JDABuilder jda = JDABuilder.createDefault(args[0]);
                jda.addEventListeners(new Bot())
                .setActivity(Activity.watching("Prefixo: !@"))
                .build();

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
//        super.onMessageReceived(event);
        if (event.getMessage().getContentRaw().startsWith("!@ma")) {
            System.out.println("Msg: " + event.getMessage().getContentDisplay());
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.orange);
            eb.setTitle("MAGIAAAAA");
            eb.setFooter("Fonte: Tormenta 20 v1.1");
            eb.setDescription("_Universal 2_\n**Execução**: padrão; **Alcance**: toque; **Duração**: cena;" +
                    "\n**Resistência**: Vontade anula.\n\nEssa é a descrição da maghdsa halk asa kasj jfkas kasfk fjakfjaskj a jaf sajfçksaj ia. kk.\n\n**+1 PM**: bagulho é loko.");

            event.getChannel().sendMessageEmbeds(eb.build()).queue();
        }
    }

}
