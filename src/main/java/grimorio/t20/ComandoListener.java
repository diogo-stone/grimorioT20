package grimorio.t20;

import grimorio.t20.configs.Config;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComandoListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComandoListener.class);
    private final ComandoGerenciar gerenciador = new ComandoGerenciar();


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String prefix = Config.get("prefixo");
        String raw = event.getMessage().getContentRaw();

        if (raw.startsWith(prefix)) {
            gerenciador.gerenciar(event);
        }
    }
}
