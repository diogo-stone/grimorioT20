package grimorio.t20.configs.comando.comandos;

import grimorio.t20.configs.comando.ComandoContext;
import grimorio.t20.configs.comando.IComando;
import net.dv8tion.jda.api.JDA;

public class ComandoPing implements IComando {

    public static final String NOME = "ping";

    @Override
    public void gerenciar(ComandoContext context) {
        JDA jda = context.getJDA();

        jda.getRestPing().queue(
                (ping) -> context.getChannel()
                        .sendMessageFormat("Rest Ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing())
                        .queue()
        );
    }

    @Override
    public String getAjuda() {
        return "Exibe o ping";
    }

    @Override
    public String getNome() {
        return NOME;
    }
}
