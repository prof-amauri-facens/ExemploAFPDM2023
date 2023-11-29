package br.facens.aula.exemploaf;

public class Serie {
    private String nomeSerie;
    private int episodio;
    private String categoria;

    public Serie() {
        // Construtor vazio necessário para o Firebase
    }

    public Serie(String nomeSerie, int episodio, String categoria) {
        this.nomeSerie = nomeSerie;
        this.episodio = episodio;
        this.categoria = categoria;
    }

    // Getters e setters (não fornecidos para economizar espaço)


    public String getNomeSerie() {
        return nomeSerie;
    }

    public void setNomeSerie(String nomeSerie) {
        this.nomeSerie = nomeSerie;
    }

    public int getEpisodio() {
        return episodio;
    }

    public void setEpisodio(int episodio) {
        this.episodio = episodio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}

