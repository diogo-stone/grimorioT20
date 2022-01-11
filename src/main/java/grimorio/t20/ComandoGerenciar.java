package grimorio.t20;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import grimorio.t20.configs.comando.ComandoContext;
import grimorio.t20.configs.comando.IComando;
import grimorio.t20.configs.comando.comandos.ComandoAjuda;
import grimorio.t20.configs.comando.comandos.ComandoCondicao;
import grimorio.t20.configs.comando.comandos.ComandoListarMagias;
import grimorio.t20.configs.comando.comandos.ComandoMagia;
import grimorio.t20.configs.comando.comandos.admin.ComandoPrefixo;
import grimorio.t20.configs.comando.comandos.admin.ComandoResetDatabaseCondicoes;
import grimorio.t20.configs.comando.comandos.admin.ComandoResetDatabaseMagias;
import me.duncte123.botcommons.StringUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ComandoGerenciar {

    private final List<IComando> listaComandos = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ComandoGerenciar.class);

    ComandoGerenciar(EventWaiter waiter) {
        addComando(new ComandoAjuda(this));
        // Outros comandos vão aqui
        addComando(new ComandoMagia(waiter));
        addComando(new ComandoListarMagias());
        addComando(new ComandoCondicao(waiter));
        // Comandos administrativos
        addComando(new ComandoPrefixo());
        addComando(new ComandoResetDatabaseMagias());
        addComando(new ComandoResetDatabaseCondicoes());
    }

    private void addComando(IComando comando) {
        boolean jaExiste = this.listaComandos.stream().anyMatch((it) -> it.getNome().equalsIgnoreCase(comando.getNome()));

        if (jaExiste)
            throw new IllegalArgumentException("Esse comando já existe.");

        listaComandos.add(comando);

    }

    public List<IComando> getListaComandos() {
        return listaComandos;
    }

    public IComando getComando(String consulta) {
        String consultaLower = consulta.toLowerCase();
        for (IComando cmd : listaComandos) {
            if (cmd.getNome().toLowerCase().equals(consultaLower) || cmd.getAliases().contains(consultaLower))
                return cmd;
        }

        return null;
    }

    void gerenciar(MessageReceivedEvent event, String prefixo) {
        String split[] = event.getMessage().getContentRaw()
            .replaceFirst("(?i)" + Pattern.quote(prefixo), "")
            .split("\\s+");

        String nomeComando = split[0].toLowerCase();

        IComando cmd = this.getComando(nomeComando);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);
            ComandoContext contexto = new ComandoContext(event, args);
            cmd.gerenciar(contexto);
        }
    }

}
