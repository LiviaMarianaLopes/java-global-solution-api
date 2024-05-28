package org.example.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Event extends _BaseEntity{
    private String titulo;
    private String descricao;
    private Timestamp data;
    private int idAlerta;

    public Event() {
    }

    public Event(int id, String titulo, String descricao, Timestamp data, int idAlerta) {
        super(id);
        this.titulo = titulo;
        this.descricao = descricao;
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
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (titulo == null || titulo.isBlank())
            errors.add("O campo título não pode estar vazio");
        if (descricao == null || descricao.isBlank())
            errors.add("Descrição não pode estar vazio");
        if (data == null)
            errors.add("Data do evento não pode ser null");
        if (idAlerta < 1) {
            errors.add("Id do alerta esta inválido");
        }
        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }
    @Override
    public String toString() {
        return new StringJoiner(", ", Event.class.getSimpleName() + "[", "]")
                .add("titulo='" + titulo + "'")
                .add("descricao='" + descricao + "'")
                .add("data=" + data)
                .add("idAlerta=" + idAlerta)
                .toString();
    }
}