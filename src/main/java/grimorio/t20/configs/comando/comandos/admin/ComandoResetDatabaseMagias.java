package grimorio.t20.configs.comando.comandos.admin;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import grimorio.t20.configs.Config;
import grimorio.t20.configs.comando.ComandoContext;
import grimorio.t20.configs.comando.IComando;
import grimorio.t20.database.IDatabaseGerenciar;
import grimorio.t20.struct.Aprimoramento;
import grimorio.t20.struct.Magia;
import grimorio.t20.struct.Padroes;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.spi.CharsetProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComandoResetDatabaseMagias implements IComando {

    public static final String NOME = "resetDatabaseMagias";

    @Override
    public void gerenciar(ComandoContext context) {
        final TextChannel canal = context.getChannel();
        final List<String> args = context.getArgs();
        final Member member = context.getMember();

        if (member.getIdLong() != Long.parseLong(Config.get("owner_id"))) {
            canal.sendMessageEmbeds(
                    Padroes.getMensagemErro(
                        "Quem você pensa que é?",
                        "_Você acha que pode me enganar?\n" +
                                "Você não é o meu mestre, mortal._\n\n" +
                                "(você não tem permissão para executar esse comando)"
                ).build()
            ).queue();
            return;
        }
        canal.sendMessageEmbeds(
                Padroes.getMensagemSucesso(
                        "Seja bem-vindo",
                        "_Que bom ver que você retornou, mestre.\n" +
                                "Todos esses mortais me cansam com suas perguntas incesantes e rituais pífios.\n" +
                                "Por favor, diga-me que você trouxe conhecimento novo._\n\n" +
                                "(o banco de dados de magia está sendo atualizado. Isso pode levar algum tempo, aguardem)"
                ).build()
        ).queue();

        ArrayList<Magia> listaMagias = null;

        try {
            BufferedReader json = new BufferedReader(new InputStreamReader(new FileInputStream("raw/magias.json")));
            listaMagias = new Gson().fromJson(json, new TypeToken<List<Magia>>(){}.getType());
            json.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listaMagias == null) {
            canal.sendMessageEmbeds(
                    Padroes.getMensagemErro(
                            "Não entendi",
                            "_Perdoe-me grande Arquimago das Pedras, mas eu não consegui compreender " +
                                    "o conheicmento que trouxeste até mim._\n\n" +
                                    "(o banco de dados de magias **não** foi atualizado. O arquivo estava incorreto)"
                    ).build()
            ).queue();
            return;
        }

        ArrayList<Aprimoramento> listaAprimoramentos = new ArrayList<>();
//        int id = 1, idApr;
        for (Magia magia : listaMagias) {
            listaAprimoramentos.addAll(magia.getListaAprimoramentos());
        }
//            idApr = 1;
//            magia.setId(id);
//            for (Aprimoramento apr : magia.getListaAprimoramentos()) {
//                apr.setIdMagia(id);
//                apr.setId(idApr);
//                listaAprimoramentos.add(apr);
//                idApr++;
//            }
//            id++;
//        }

//        try {
//            File f = new File("raw/magiasId.json");
//            if (!f.exists())
//                f.createNewFile();
//
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(new Gson().toJson(listaMagias).getBytes("windows-1252"));
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        IDatabaseGerenciar.INSTANCE.addListaMagias(listaMagias);
        IDatabaseGerenciar.INSTANCE.addListaAprimoramentos(listaAprimoramentos);

        canal.sendMessageEmbeds(
                Padroes.getMensagemSucesso(
                        "Até breve",
                        "_Obigado por me enaltecer com este acervo arcano, Arquimago das Pedras.\n" +
                                "Retorne quando houver mais conhecimento para compartilhar comigo, mestre._\n\n" +
                                "(o banco de dados de magias foi atualizado com sucesso)"
                ).build()
        ).queue();
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getAjuda() {
        return "_Este poderoso ritual só pode ser invocado pelo meu mestre, o grande Arquimago das Pedras.\n" +
                "Mortais podem apenas fazer perguntas incesantes e rituais pífios._\n\n" +
                "(atualiza o banco de dados de magias do bot)\n" +
                "Uso: `%s" + NOME.toLowerCase() + "`\n" +
                (getAliasesToString().length() > 0 ? "Tente também: " + getAliasesToString() : "");
    }

    @Override
    public String getResumoComando() {
        return "\n`%s" + NOME.toLowerCase() + "`\nImporta novamente as magias a partir do arquivo base.\n";
    }

    @Override
    public boolean isAdministrativo() {
        return true;
    }
}
