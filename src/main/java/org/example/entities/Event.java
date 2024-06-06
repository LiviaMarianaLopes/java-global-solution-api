package org.example.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Event extends Alert {
    private String titulo;
    private String descricaoEvento;
    private Timestamp data;
    private int idAlerta;

    public Event() {
    }

    public Event(int id, String cep, String estado, String cidade, String logradouro, String referencia, String descricao, int idLocalizacao, int idColaborador, String titulo, String descricaoEvento, Timestamp data, int idAlerta) {
        super(id, cep, estado, cidade, logradouro, referencia, descricao, idLocalizacao, idColaborador);
        this.titulo = titulo;
        this.descricaoEvento = descricaoEvento;
        this.data = data;
        this.idAlerta = idAlerta;
    }

    public Event(int id, String cep, String estado, String cidade, String logradouro, String referencia, String titulo, String descricaoEvento, Timestamp data, int idAlerta) {
        super(id, cep, estado, cidade, logradouro, referencia);
        this.titulo = titulo;
        this.descricaoEvento = descricaoEvento;
        this.data = data;
        this.idAlerta = idAlerta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricaoEvento;
    }

    public void setDescricao(String descricaoEvento) {
        this.descricaoEvento = descricaoEvento;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    @Override
    public Map<Boolean, ArrayList<String>> validate() {
        ArrayList<String> errors = new ArrayList<>();
        if (super.validate().containsKey(false)) {
            errors.addAll(super.validate().get(false));
        }
        if (titulo == null || titulo.isBlank())
            errors.add("O campo título não pode estar vazio");
        if (data == null)
            errors.add("Data do evento não pode ser null");
        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Event.class.getSimpleName() + "[", "]")
                .add("titulo='" + titulo + "'")
                .add("descricaoEvento='" + descricaoEvento + "'")
                .add("data=" + data)
                .add("idAlerta=" + idAlerta)
                .toString();
    }
}
