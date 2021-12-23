package grimorio.t20.struct;

public enum EImagemCustoPM {

    CUSTO_MAGIA_1_CIRCULO(1, "https://imgur.com/rNEUduT.png"),
    CUSTO_MAGIA_2_CIRCULO(2, "https://imgur.com/pzuP6Rj.png"),
    CUSTO_MAGIA_3_CIRCULO(3, "https://imgur.com/2a14FRC.png"),
    CUSTO_MAGIA_4_CIRCULO(4, "https://imgur.com/428Werh.png"),
    CUSTO_MAGIA_5_CIRCULO(5, "https://imgur.com/brHCVF3.png");

    private final int nivelMagia;
    private final String url;

    EImagemCustoPM(int nivelMagia, String url) {
        this.nivelMagia = nivelMagia;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int getNivelMagia() {
        return nivelMagia;
    }

    public static String getUrlPeloNivelMagia(int nivelMagia) {
        for (EImagemCustoPM img : values()) {
            if (img.getNivelMagia() == nivelMagia)
                return img.getUrl();
        }
        return "";
    }

}
