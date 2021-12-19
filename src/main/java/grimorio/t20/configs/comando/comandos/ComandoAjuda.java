package grimorio.t20.configs.comando.comandos;

import grimorio.t20.ComandoGerenciar;
import grimorio.t20.VeryBadDesign;
import grimorio.t20.configs.Config;
import grimorio.t20.configs.comando.ComandoContext;
import grimorio.t20.configs.comando.IComando;
import grimorio.t20.database.IDatabaseGerenciar;
import grimorio.t20.struct.Padroes;
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

        EmbedBuilder eb = Padroes.getMensagemAjuda("T", "");

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            String prefixo = VeryBadDesign.PREFIXES.get(context.getGuild().getIdLong());

            gerenciador.getListaComandos().stream().map(IComando::getNome).forEach(
                    (it) -> builder.append("```")
                                    .append(prefixo)
                                    .append(it)
                                    .append("```")
            );

            eb.setTitle("Lista de Comandos").setDescription(builder.toString());

            canal.sendMessageEmbeds(eb.build()).queue();
            return;
        }

        String consulta = args.get(0);
        IComando cmd = gerenciador.getComando(consulta);

        if (cmd == null) {
            eb.setTitle("Até a magia tem limites")
                .setDescription(String.format("_Este ritual não existe._\n\n" +
                        "(comando `%s` não encontrado)", consulta));
        } else {
            eb.setTitle(cmd.getNome())
                    .setDescription(String.format(cmd.getAjuda(), IDatabaseGerenciar.INSTANCE.getPrefixo(context.getGuild().getIdLong())));
        }

        canal.sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getAjuda() {
        return "_Exibe a lista de rituais e segredos arcanos que eu conheço._\n\n" +
                "(exibe a lista de comandos ou mais informações sobre um comando informado)\n" +
                "Uso: `%shelp [comando]`";
    }

    @Override
    public List<String> getAliases() {
        return List.of("cmds", "comandos", "listacomandos");
    }
}
