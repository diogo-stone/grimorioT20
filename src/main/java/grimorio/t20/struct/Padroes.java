package grimorio.t20.struct;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Padroes {

    public static final Color COR_AJUDA = new Color(255,79,111);

    public static final Color COR_MAGIA_ARCANA = new Color(0, 128, 192);

    public static final Color COR_MAGIA_DIVINA = new Color(220, 210, 35);

    public static final Color COR_MAGIA_UNIVERSAL = new Color(0, 128, 0);

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
                .setFooter(magia.getFonte())
                .setDescription(magia.getDescricaoFormatada());
    }

}