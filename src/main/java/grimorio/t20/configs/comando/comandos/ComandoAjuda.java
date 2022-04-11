package grimorio.t20.configs.comando.comandos;

import grimorio.t20.ComandoGerenciar;
import grimorio.t20.configs.Config;
import grimorio.t20.configs.comando.IComando;
import grimorio.t20.configs.comando.IComandoContext;
import grimorio.t20.database.IDatabaseGerenciar;
import grimorio.t20.struct.Padroes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class ComandoAjuda implements IComando {

    public static final String NOME = "Ajuda";

    private final ComandoGerenciar gerenciador;

    public ComandoAjuda(ComandoGerenciar gerenciador) {
        this.gerenciador = gerenciador;
    }

    @Override
    public void gerenciar(IComandoContext context) {
        List<String> args = context.getArgs();
        TextChannel canal = context.getChannel();
        Member member = context.getMember();

        EmbedBuilder eb = Padroes.getMensagemAjuda("T", "");

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            gerenciador.getListaComandos()/*.map(IComando::getResumoComando)*/.forEach(
                    (it) -> {
                        String prefixo = Config.get("PREFIXO");
                        if (context.isMessageEvent())
                            prefixo = IDatabaseGerenciar.INSTANCE.getPrefixo(context.getGuild().getIdLong());
                        else if (context.isSlashEvent())
                            prefixo = "/";

                        if (
                            (it.isAdministrativo() && member.hasPermission(Permission.MANAGE_SERVER) && !it.isRestritoDesenvolvedor()) ||
                            (it.isRestritoDesenvolvedor() && member.getIdLong() == Long.parseLong(Config.get("owner_id")) )||
                            (!it.isAdministrativo() && !it.isRestritoDesenvolvedor())
                        ) {
                            builder.append(String.format(it.getResumoComando(), prefixo));
                            if (it.isAdministrativo())
                                builder.append("**Comando administrativo**\n");
                            if (it.isRestritoDesenvolvedor())
                                builder.append("**Comando restrito ao desenvolvedor**\n");
                        }
                    }
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
            String prefixo = Config.get("PREFIXO");
            if (context.isMessageEvent())
                prefixo = IDatabaseGerenciar.INSTANCE.getPrefixo(context.getGuild().getIdLong());
            else if (context.isSlashEvent())
                prefixo = "/";
            eb.setTitle(cmd.getNome())
                    .setDescription(cmd.getAjuda(context.isMessageEvent()).replace("%s", prefixo));
        }

        canal.sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getAjuda(boolean mostrarAliases) {
        return "_Exibe a lista de rituais e segredos arcanos que eu conheço._\n\n" +
                "(exibe a lista de comandos ou mais informações sobre um comando informado)\n" +
                "Uso: `%s"+NOME.toLowerCase()+" [comando]`\n" +
                (mostrarAliases && getAliasesToString().length() > 0 ? "Tente também: " + getAliasesToString() : "");
    }

    @Override
    public boolean isAdministrativo() {
        return false;
    }

    @Override
    public boolean isRestritoDesenvolvedor() {
        return false;
    }

    @Override
    public String getResumoComando() {
        return "\n`%s" + NOME.toLowerCase() + " [comando]`\nExibe o detalhamento do comando informado ou essa ajuda, caso nenhum " +
                "comando seja informado.\n";
    }

    @Override
    public List<String> getAliases() {
        return List.of("cmd", "cmds", "comandos", "help");
    }

}
