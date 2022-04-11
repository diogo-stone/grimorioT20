package grimorio.t20.configs.comando.comandos.admin;

import grimorio.t20.VeryBadDesign;
import grimorio.t20.configs.comando.IComando;
import grimorio.t20.configs.comando.IComandoContext;
import grimorio.t20.database.IDatabaseGerenciar;
import grimorio.t20.struct.Padroes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class ComandoPrefixo implements IComando {

    public static final String NOME = "prefixo";

    @Override
    public void gerenciar(IComandoContext context) {
        final TextChannel canal = context.getChannel();
        final List<String> args = context.getArgs();
        final Member member = context.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder eb = Padroes.getMensagemErro(
                    "Mortal tolo!",
                    "_Você não é um Arquimago, não pode solicitar este ritual._\n\n" +
                            "(é necessário ter a permissão de \"Gerente do Servidor\" para executar este comando)");
            canal.sendMessageEmbeds(eb.build()).queue();
            return;
        }

        if (args.isEmpty()) {
            EmbedBuilder eb = Padroes.getMensagemErro(
                    "Atenção, mortal!",
                    "_Você não fez o ritual completo, é muito perigoso continuar assim._\n\n" +
                            "(é necessário informar o novo prefixo como parâmetro)");
            canal.sendMessageEmbeds(eb.build()).queue();
            return;
        }

        final String novoPrefixo = String.join("", args);
        updatePrefixo(context.getGuild().getIdLong(), novoPrefixo);

        EmbedBuilder eb = Padroes.getMensagemSucesso(
                "O ritual foi concluído!",
                "_Nova sequência arcana de ativação definida._\n\n" +
                        "(novo prefixo definido: `" + novoPrefixo + "`)");
        canal.sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getAjuda(boolean mostrarAliases) {
        long guildId =0;
        return "_Este ritual permite definir minha sequência arcana de ativação._\n\n" +
                "(define o prefixo do bot no servidor)\n" +
                "Uso: `%s"+NOME.toLowerCase()+" <novo_prefixo>`\n" +
                (getAliasesToString().length() > 0 ? "Tente também: " + getAliasesToString() : "");
    }

    @Override
    public String getResumoComando() {
        return "\n`%s" + NOME.toLowerCase() + " <novo_prefixo>`\nDefine o prefixo do bot no servidor.\n" +
                "**NÃO DISPONÍVEL POR COMANDOS DE BARRA**";
    }

    @Override
    public boolean isAdministrativo() {
        return true;
    }

    @Override
    public boolean isRestritoDesenvolvedor() {
        return false;
    }

    @Override
    public List<String> getAliases() {
        return List.of("setprefixo");
    }

    private void updatePrefixo(long guildId, String novoPrefixo) {
        VeryBadDesign.PREFIXES.put(guildId, novoPrefixo);
        IDatabaseGerenciar.INSTANCE.setPrefixo(guildId, novoPrefixo);
    }
}
