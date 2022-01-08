package grimorio.t20.configs.comando.comandos;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import grimorio.t20.configs.comando.ComandoContext;
import grimorio.t20.configs.comando.IComando;
import grimorio.t20.database.IDatabaseGerenciar;
import grimorio.t20.struct.Magia;
import grimorio.t20.struct.Padroes;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ComandoMagia implements IComando {
//
    public static final String NOME = "Magia";
    private final EventWaiter waiter;

    public ComandoMagia(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void gerenciar(ComandoContext context) {
        List<String> args = context.getArgs();
        TextChannel canal = context.getChannel();

        String consultaMagia = String.join(" ", args);

        Map<Integer, Magia> mapMagias = IDatabaseGerenciar.INSTANCE.consultaMagia(consultaMagia);

        if (mapMagias.size() <= 0) {
            canal.sendMessageEmbeds(
                    Padroes.getMensagemErro(
                            "Magia não encontrada",
                            String.format("_Meu acervo não dispõe de nenhum feitiço similar a `%s`.\n" +
                                    "Tem certeza que é isso que buscavas?_\n\n" +
                                    "(não foi possível encontrar uma magia que contenha `%s` em seu nome)"
                            , consultaMagia, consultaMagia)
                    ).build()
            ).queue();
            return;
        }

        if (mapMagias.size() == 1) {
            Magia magia = (Magia) mapMagias.values().toArray()[0];
            canal.sendMessageEmbeds(
                    Padroes.getMensagemMagia(magia).build()
            ).queue();
            return;
        }

        int i = 1;
        String magias = "";
        for (Magia magia: mapMagias.values()) {
            magias = magias.concat(String.format("\n**[%d]** %s", i++, magia.getNome()));
        }

        canal.sendMessageEmbeds(
                Padroes.getMensagemSucesso(
                    "Escolha uma magia",
                    String.format("Sua consulta ao acervo retornou muitos resultados.\nDigite o número referente a magia " +
                            "que desejas verificar.\n%s", magias))
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
                                if (id <= mapMagias.size()) {
                                    Magia magia = (Magia) mapMagias.values().toArray()[id - 1];
                                    if (magia != null)
                                        canal.sendMessageEmbeds(
                                                Padroes.getMensagemMagia(magia).build()
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
                        10L, TimeUnit.SECONDS,
                        () -> {
                            message.delete().queue();
                            canal.sendMessageEmbeds(
                                    Padroes.getMensagemErro(
                                            "Que infortúnio",
                                            "_Eu não tenho todo tempo do mundo, mortal.\nVolte quando souber " +
                                                    "o que procuras.\n\n" +
                                                    "(você não selecionou uma magia da lista)_"
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
        return "_Vamos, mortal. Diga-me qual feitiço procuras e eu lhe enaltecerei com conheicmento._\n\n" +
                "(consulta uma magia)\n" +
                "Uso: `%s"+NOME.toLowerCase()+" <parte_do_nome_da_magia>`\n" +
                (getAliasesToString().length() > 0 ? "Tente também: " + getAliasesToString() + "\n" : "") +
                "_Dica_: não é preciso colocar aspas para nomes que contenham espaços.";
    }

    @Override
    public String getResumoComando() {
        return "\n`%s" + NOME.toLowerCase() + " <parte_do_nome_da_magia>`\nConsulta a magia informada, retornando as informações " +
                "dela ou uma lista de seleção com base no nome informado.\n";
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
        return List.of("m", "ma", "mag");
    }

}
