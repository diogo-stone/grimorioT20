package grimorio.t20.configs.comando.comandos;

import grimorio.t20.configs.comando.IComando;
import grimorio.t20.configs.comando.IComandoContext;
import grimorio.t20.database.IDatabaseGerenciar;
import grimorio.t20.struct.Magia;
import grimorio.t20.struct.Padroes;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComandoListarMagias implements IComando {

    public static final String NOME = "ListarMagias";

    @Override
    public void gerenciar(IComandoContext context) {
        List<String> args = context.getArgs();
        TextChannel canal = context.getChannel();
        List<String> listEscolas = new ArrayList<>();
        List<Integer> listNiveis = new ArrayList<>();
        boolean isArcana = false, isDivina = false;
        String parm;

        for (int i = 0; i < args.size(); i++) {
            parm = args.get(i);
            // Se tiver só dois caracteres, verifica se é um parâmetro válido, se não, considera uma escola da busca
            if (parm.length() == 2) {
                // Se iniciar com o sinal de menos "-" então é qualificado para ser um parâmetro, se não, considera uma escola da busca
                if (parm.charAt(0) == '-') {
                    // Se for um dígito, o parâmetro informado é um nível de magia
                    if (Character.isDigit(parm.charAt(1))) {
                        listNiveis.add(Integer.parseInt(String.valueOf(parm.charAt(1))));
                    } else
                        switch (parm.charAt(1)) {
                            case 'a':
                            case 'A':
                                isArcana = true;
                                break;
                            case 'd':
                            case 'D':
                                isDivina = true;
                                break;
                        }
                } else
                    listEscolas.add(args.get(i));
            } else
                listEscolas.add(args.get(i));
        }

        Map<Integer, Magia> mapMagias = IDatabaseGerenciar.INSTANCE.ListarMagias(listEscolas, listNiveis, isArcana, isDivina);

        if (mapMagias.size() <= 0) {
            canal.sendMessageEmbeds(
                    Padroes.getMensagemErro(
                            "Magias não encontradas",
                            String.format("_Meu acervo não dispõe de nenhum feitiço similar com os parâmetros " +
                                            "informados.\nTem certeza que buscaste o que desejavas corretamente?_\n\n" +
                                            "(não foi possível encontrar magias com os parâmetros informados)")
                    ).build()
            ).queue();
            return;
        }

        List<Magia> listMagiasArcanas = new ArrayList<>(),
                    listMagiasDivinas = new ArrayList<>();

        for (Magia magia: mapMagias.values()) {
            if (magia.isArcana() && (isArcana || (!isArcana && !isDivina)))
                listMagiasArcanas.add(magia);
            if (magia.isDivina() && (isDivina || (!isArcana && !isDivina)))
                listMagiasDivinas.add(magia);
        }

        if (listMagiasArcanas.size() > 0)
            canal.sendMessageEmbeds(
                    Padroes.getMensagemListaMagia(
                                    listMagiasArcanas,
                                    true,
                                    false)
                            .build()
            ).queue();

        if (listMagiasDivinas.size() > 0)
            canal.sendMessageEmbeds(
                    Padroes.getMensagemListaMagia(
                                    listMagiasDivinas,
                                    false,
                                    true)
                            .build()
            ).queue();
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getAjuda(boolean mostrarAliases) {
        String ajuda = "_Vamos, mortal. Diga-me quais tipos de feitiço procuras e eu lhe enaltecerei com conheicmento._\n\n" +
                "(consulta uma lista de magias baseado nos parâmetros informados)\n" +
                "Uso: `%s"+NOME.toLowerCase()+" <parametros>`\n\n";

        if (mostrarAliases) {
            ajuda += "_Parâmetros disponíveis_:\n" +
                    "`-1`, `-2`, `-3`, `-4` e `-5` filtram os círculos das magias que serão listadas. Não informar um " +
                    "círculo listará magias de todos os círculos que cumpram o restante das condições. É possível informar " +
                    "mais de um círculo ao mesmo tempo, por exemplo: `%s" + NOME.toLowerCase() + " -2 -5` lista todas as magias " +
                    "arcanas e divinas de 2º e 5º círculo.\n\n" +
                    "`-a` define que apenas magias arcanas devem ser exibidas.\n" +
                    "`-d` define que apenas magias divinas devem ser exibidas.\n" +
                    "Informar `-a` e `-d` lista tanto as magias divinas como as magias arcanas, que é o mesmo que não " +
                    "informar qualquer um deles.\n\n" +
                    "`<parte_do_nome_da_escola>` filtra as escolas das magias. Você pode informar mais de uma, separando-as " +
                    "com espaços. Se nenhuma for informada, todas que cumpram as outras condições serão exibidas. Por " +
                    "exemplo: `%s" + NOME.toLowerCase() + " ilusao adiv -2` exibe todas as magias de ilusão e adivinhação de " +
                    "2º círculo, arcanas e divinas.\n\n" +
                    (mostrarAliases && getAliasesToString().length() > 0 ? "Tente também: " + getAliasesToString() + "\n" : "");
        } else {
            ajuda += "_Parâmetros_:\n" +
                    "\u2022 niveis: Os valores aceitos são `-1`, `-2`, `-3`, `-4` e `-5`.\nOs valores representam os círculos " +
                    "das magias que serão listadas.\nNão informar um círculo listará magias de todos os círculos que " +
                    "cumpram o restante das condições.\nÉ possível informar mais de um círculo ao mesmo tempo, por exemplo: " +
                    "`-2 -5` lista todas as de 2º e 5º círculo que cumpram o restante das condições.\n\n" +

                    "\u2022 escolas: filtra as magias pelas suas escolas.\nVocê pode informar mais de uma, separando-as " +
                    "com espaços.\nSe nenhuma for informada, todas que cumpram as outras condições serão exibidas, por " +
                    "exemplo: `ilusao adiv -2` exibe todas as magias de ilusão e adivinhação de 2º círculo, arcanas e divinas.\n\n" +

                    "\u2022 origem: Define se as magias listadas serão arcanas, divinas ou de qualquer tipo.\n" +
                    "Magias `Universais` aparecem independente da opção selecionada.\n\n";
        }
        ajuda += "_Dica_: não é preciso colocar o nome das escolas por completo. \"ilu\" já suficiente para encontrar magias " +
                "de ilusão, por exemplo.";
        return ajuda;
    }

    @Override
    public String getResumoComando() {
        return "\n`%s" + NOME.toLowerCase() + " <parâmetros>`\nConsulta uma lista de magias baseado nos parâmetros " +
                "informados, separando-as por nível, escola e origem (arcana ou divina).\n";
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
        return List.of("lm", "listm", "listmag");
    }

}
