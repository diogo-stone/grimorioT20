package grimorio.t20.configs.comando.comandos;

import grimorio.t20.ComandoGerenciar;
import grimorio.t20.configs.Config;
import grimorio.t20.configs.comando.ComandoContext;
import grimorio.t20.configs.comando.IComando;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;
import java.util.Locale;

public class ComandoAjuda implements IComando {

    public static final String NOME = "help";

    private final ComandoGerenciar gerenciador;

    public ComandoAjuda(ComandoGerenciar gerenciador) {
        this.gerenciador = gerenciador;
    }

    @Override
    public void gerenciar(ComandoContext context) {
        List<String> args = context.getArgs();
        TextChannel canal = context.getChannel();

        EmbedBuilder msg = new EmbedBuilder().setColor(new Color(255,79,111));

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            gerenciador.getListaComandos().stream().map(IComando::getNome).forEach(
                    (it) -> builder.append("```")
                                    .append(Config.get("prefixo"))
                                    .append(it)
                                    .append("```")
            );

            msg.setTitle("Lista de Comandos").setDescription(builder.toString());

            canal.sendMessageEmbeds(msg.build()).queue();
            return;
        }

        String consulta = args.get(0);
        IComando cmd = gerenciador.getComando(consulta);

        if (cmd == null) {
            msg.setTitle("Até a magia tem limites")
                .setDescription("Comando `"+consulta+"` não encontrado.");
        } else {
            msg.setTitle(cmd.getNome())
                    .setDescription(cmd.getAjuda());
        }

        canal.sendMessageEmbeds(msg.build()).queue();
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getAjuda() {
        return "Exibe a lista de comandos místicos aceitos por mim.\n" +
                "Uso: `"+ Config.get("prefixo")+"help [comando]`";
    }

    @Override
    public List<String> getAliases() {
        return List.of("cmds", "comandos", "listacomandos");
    }
}
