package grimorio.t20;

import grimorio.t20.configs.comando.ComandoContext;
import grimorio.t20.configs.comando.IComando;
import grimorio.t20.configs.comando.comandos.ComandoAjuda;
import grimorio.t20.configs.comando.comandos.admin.ComandoPrefixo;
import grimorio.t20.configs.comando.comandos.admin.ComandoResetDatabaseMagias;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ComandoGerenciar {

    private final List<IComando> listaComandos = new ArrayList<>();

    ComandoGerenciar() {
        addComando(new ComandoAjuda(this));
        // Outros comandos vão aqui

        // Comandos administrativos
        addComando(new ComandoPrefixo());
        addComando(new ComandoResetDatabaseMagias());
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
            if (cmd.getNome().equals(consultaLower) || cmd.getAliases().contains(consultaLower))
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
