package grimorio.t20.configs.comando.comandos;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import grimorio.t20.configs.comando.ComandoContext;
import grimorio.t20.configs.comando.IComando;
import grimorio.t20.database.IDatabaseGerenciar;
import grimorio.t20.struct.Condicao;
import grimorio.t20.struct.Magia;
import grimorio.t20.struct.Padroes;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ComandoCondicao implements IComando {

    public static final String NOME = "Condição";
    private EventWaiter waiter;

    public ComandoCondicao(EventWaiter waiter) { this.waiter = waiter; }

    @Override
    public void gerenciar(ComandoContext context) {
        List<String> args = context.getArgs();
        TextChannel canal = context.getChannel();

        String consultaCondicao = String.join(" ", args);

        Map<Integer, Condicao> mapCondicoes = IDatabaseGerenciar.INSTANCE.consultaCondicao(consultaCondicao);

        if (mapCondicoes.size() <= 0) {
            canal.sendMessageEmbeds(
                    Padroes.getMensagemErro(
                            "Condição não encontrada",
                            String.format("_Não existe uma consequência para `%s`.\n" +
                                            "Pare de me atormentar com bobagens._\n\n" +
                                            "(não foi possível encontrar uma condição que contenha `%s` em seu nome)"
                                    , consultaCondicao, consultaCondicao)
                    ).build()
            ).queue();
            return;
        }

        if (mapCondicoes.size() == 1) {
            Condicao condicao = (Condicao) mapCondicoes.values().toArray()[0];
            canal.sendMessageEmbeds(
                    Padroes.getMensagemCondicao(condicao).build()
            ).queue();
            return;
        }

        int i = 1;
        String condicoes = "";
        for (Condicao condicao: mapCondicoes.values()) {
            condicoes = condicoes.concat(String.format("\n**[%d]** %s", i++, condicao.getNome()));
        }

        canal.sendMessageEmbeds(
                        Padroes.getMensagemSucesso(
                                        "Escolha uma condição",
                                        String.format("Sua consulta às efermidades trouxeram muitos resultados.\n" +
                                                "Digite o número referente a condição que desejas verificar.\n" +
                                                "%s", condicoes))
                                .build())
                .queue((message -> {
                    this.waiter.waitForEvent(
                            MessageReceivedEvent.class,
                            (e) -> e.getAuthor().getIdLong() == context.getAuthor().getIdLong() && !e.getAuthor().isBot(),
                            (e) -> {
                                message.delete().queue();
                                String idStr = e.getMessage().getContentRaw();
                                if (idStr.matches("\\d+")) {
                                    e.getMessage().delete().queue();
                                    int id = Integer.parseInt(idStr);
                                    if (id <= mapCondicoes.size()) {
                                        Condicao condicao = (Condicao) mapCondicoes.values().toArray()[id - 1];
                                        if (condicao != null)
                                            canal.sendMessageEmbeds(
                                                    Padroes.getMensagemCondicao(condicao).build()
                                            ).queue();
                                        else
                                            canal.sendMessageEmbeds(Padroes.getMensagemOpcaoNaoExiste().build()).queue();
                                    } else {
                                        canal.sendMessageEmbeds(Padroes.getMensagemOpcaoNaoExiste().build()).queue();
                                    }
                                } else {
                                    canal.sendMessageEmbeds(Padroes.getMensagemOpcaoNaoExiste().build()).queue();
                                }
                            },
                            5L, TimeUnit.SECONDS,
                            () -> {
                                message.delete().queue();
                                canal.sendMessageEmbeds(
                                        Padroes.getMensagemErro(
                                                "Que infortúnio",
                                                "_Eu não tenho todo tempo do mundo, mortal.\nVolte quando souber " +
                                                        "o que procuras.\n\n" +
                                                        "(você não selecionou uma condição da lista)_"
                                        ).build()
                                ).queue();
                            }
                    );
                }));
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getAjuda() {
        return "_Mortais e seus problemas mundanos. Diga-me o que lhe aflige e eu direi as consequências disso._\n\n" +
                "(consulta uma condição)\n" +
                "Uso: `%s"+NOME.toLowerCase()+" <parte_do_nome_da_condição>`\n" +
                (getAliasesToString().length() > 0 ? "Tente também: " + getAliasesToString() + "\n": "") +
                "_Dica_: você pode consultar condições pelo tipo. Tente consutlar \"medo\", por exemplo.";
    }

    @Override
    public String getResumoComando() {
        return "\n`%s" + NOME.toLowerCase() + " <parte_do_nome_da_condição>`\nConsulta a condição informada, retornando " +
                "as informações dela ou uma lista de seleção com base no nome informado.\n";
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
    public List<String> getAliases() {
        return List.of("c", "cond", "condicao");
    }

}
