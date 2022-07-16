package grimorio.t20.configs.comando.comandos;

import grimorio.t20.configs.comando.IComando;
import grimorio.t20.configs.comando.IComandoContext;
import grimorio.t20.database.IDatabaseGerenciar;
import grimorio.t20.struct.Errata;
import grimorio.t20.struct.Magia;
import grimorio.t20.struct.Padroes;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Map;

public class ComandoErrata implements IComando {

    public static final String NOME = "Errata";

    @Override
    public void gerenciar(IComandoContext context) {
        List<String> args = context.getArgs();
        TextChannel canal = context.getChannel();
        Member member = context.getMember();

        String id = "";

        if (args.size() > 0) {
            id = args.get(0);
        }

        Map<Integer, Errata>  mapErratas = IDatabaseGerenciar.INSTANCE.consultaErrata(id);

        if (mapErratas.size() <= 0) {
            canal.sendMessageEmbeds(
                    Padroes.getMensagemErro(
                            "Errata não encontrada",
                            String.format("_Não existem correções com o índice `%s`.\n" +
                                            "O códice está intacto._\n\n" +
                                            "(não foi possível encontrar uma errata com o id `%s`)"
                                    , id, id)
                    ).build()
            ).queue();
            return;
        }

        Errata errata = new Errata();
        for (Map.Entry<Integer, Errata> e : mapErratas.entrySet()) {
            errata = e.getValue();
        }

        canal.sendMessageEmbeds(
                Padroes.getMensagemAjuda(
                        "Errata v" + errata.getVersao(),
                        String.format("%s", errata.getDescricao()))
                .setFooter(errata.getData() + " [" + errata.getId() + "]")
                .build()
            ).queue();

    }

    @Override
    public String getNome() { return NOME; }

    @Override
    public String getAjuda(boolean mostrarAliases) {
        return "_Exibe a lista de correções das quais os rituais precisaram ser submetidos._\n\n" +
                "(exibe uma lista das magias alteradas pelas erratas)\n" +
                "Uso: `%s"+NOME.toLowerCase()+" [id]`\n" +
                (mostrarAliases && getAliasesToString().length() > 0 ? "Tente também: " + getAliasesToString() : "");
    }

    @Override
    public String getResumoComando() {
        return "\n`%s" + NOME.toLowerCase() + " [id]`\nExibe o detalhamento da errata informada ou da última errata, caso nenhum " +
                "id seja informado.\n";
    }

    @Override
    public boolean isAdministrativo() { return false; }

    @Override
    public boolean isRestritoDesenvolvedor() { return false; }
}
