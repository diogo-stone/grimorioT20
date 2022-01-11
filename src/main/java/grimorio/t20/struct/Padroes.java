package grimorio.t20.struct;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class Padroes {

    public static final Color COR_AJUDA = new Color(255,79,111);

    public static final Color COR_MAGIA_ARCANA = new Color(0, 128, 192);

    public static final Color COR_MAGIA_DIVINA = new Color(220, 210, 35);

    public static final Color COR_MAGIA_UNIVERSAL = new Color(0, 128, 0);

    public static final Color COR_CONDICAO = new Color(255,120,50);

    public static final Color COR_ERRO = new Color(190,20,20);

    public static final Color COR_SUCESSO = new Color(125, 250, 125);

    public static EmbedBuilder getMensagemErro(String titulo, String descricao) {
        return new EmbedBuilder()
                .setColor(Padroes.COR_ERRO)
                .setTitle(titulo)
                .setDescription(descricao);
    }

    public static EmbedBuilder getMensagemSucesso(String titulo, String descricao) {
        return new EmbedBuilder()
                .setColor(Padroes.COR_SUCESSO)
                .setTitle(titulo)
                .setDescription(descricao);
    }

    public static EmbedBuilder getMensagemAjuda(String titulo, String descricao) {
        return new EmbedBuilder()
                .setColor(Padroes.COR_AJUDA)
                .setTitle(titulo)
                .setDescription(descricao);
    }

    public static EmbedBuilder getMensagemMagia(Magia magia) {
        return new EmbedBuilder()
                .setColor(magia.getTipoMagia().equalsIgnoreCase("universal") ?
                        COR_MAGIA_UNIVERSAL : (magia.getTipoMagia().equalsIgnoreCase("arcana") ?
                        COR_MAGIA_ARCANA : COR_MAGIA_DIVINA))
                .setTitle(magia.getNome())
                .setFooter(magia.getFonte() + " [" + magia.getId() + "]")
                .setThumbnail(EImagemCustoPM.getUrlPeloNivelMagia(magia.getNivel()))
                .setDescription(magia.getDescricaoFormatada());
    }

    public static EmbedBuilder getMensagemListaMagia(List<Magia> listMagias, boolean isArcana, boolean isDivina) {
        StringBuilder texto = new StringBuilder();
        String escola = "";
        int nivel = 0;

        listMagias.sort(Comparator.comparing(Magia::getNivel)
                .thenComparing(Magia::getEscola)
                .thenComparing(Magia::getNome)
            );

        for (Magia m: listMagias) {
            if (m.getNivel() != nivel) {
                texto.append(String.format("%s\n**%d\u00B0 CÍRCULO**\n\n", nivel == 0 ? "" : "```", m.getNivel()));
                escola = "";
            }

            if (!m.getEscola().equalsIgnoreCase(escola))
                texto.append(String.format("%s_%s_\n```", escola.isEmpty() ? "" : "```", m.getEscola()));

            texto.append(String.format("  %s\n", m.getResumo()));

            nivel = m.getNivel();
            escola = m.getEscola();
        }

        if (!texto.isEmpty())
            texto.append("```");

        return new EmbedBuilder()
                .setColor(isArcana && isDivina ?
                        COR_MAGIA_UNIVERSAL : isArcana ?
                        COR_MAGIA_ARCANA : COR_MAGIA_DIVINA)
                .setTitle(String.format("Magias %s", isArcana && isDivina ?
                                        "Universais" : isArcana ?
                                            "Arcanas" : "Divinas"
                                        ))
//                .setThumbnail(EImagemCustoPM.getUrlPeloNivelMagia(magia.getNivel()))
                .setDescription(texto.toString());
    }

    public static EmbedBuilder getMensagemCondicao(Condicao condicao) {
        return new EmbedBuilder()
                .setColor(COR_CONDICAO)
                .setTitle(condicao.getNome())
                .setFooter(condicao.getFonte() + " [" + condicao.getId() + "]")
                .setDescription(condicao.getDescricaoFormatada());
    }

    public static EmbedBuilder getMensagemOpcaoNaoExiste() {
        return Padroes.getMensagemErro(
                        "Que infortúnio",
                        "_Essa opção não existe, mortal.\nVolte quando souber " +
                                "o que procuras._\n\n" +
                                "(você não informou um valor válido)"
                );
    }

}
