package grimorio.t20;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import grimorio.t20.database.IDatabaseGerenciar;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComandoListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComandoListener.class);
    private final ComandoGerenciar gerenciador;

    public ComandoListener(EventWaiter waiter) {
        this.gerenciador = new ComandoGerenciar(waiter);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        final long guildId = event.getGuild().getIdLong();
        String prefixo = VeryBadDesign.PREFIXES.computeIfAbsent(guildId, IDatabaseGerenciar.INSTANCE::getPrefixo);
        String raw = event.getMessage().getContentRaw();

        if (raw.startsWith(prefixo)) {
            gerenciador.gerenciar(event, prefixo);
        }
    }

}
