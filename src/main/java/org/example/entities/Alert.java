package org.example.entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

public class Alert extends Localizacao {

    private String descricao;
    private int idLocalizacao;
    private int idColaborador;

    public Alert() {
    }

    public Alert(int id, String cep, String estado, String cidade, String logradouro, String referencia, String descricao, int idLocalizacao, int idColaborador) {
        super(id, cep, estado, cidade, logradouro, referencia);
        this.descricao = descricao;
        this.idLocalizacao = idLocalizacao;
        this.idColaborador = idColaborador;
    }

    public Alert(int id, String cep, String estado, String cidade, String logradouro, String referencia) {
        super(id, cep, estado, cidade, logradouro, referencia);
    }

    public Alert(int id, String descricao, int idLocalizacao, int idColaborador) {
        super(id);
        this.descricao = descricao;
        this.idLocalizacao = idLocalizacao;
        this.idColaborador = idColaborador;
    }

    public Alert(String descricao, int idLocalizacao, int idColaborador) {
        this.descricao = descricao;
        this.idLocalizacao = idLocalizacao;
        this.idColaborador = idColaborador;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public int getIdLocalizacao() {
        return idLocalizacao;
    }

    public void setIdLocalizacao(int idLocalizacao) {
        this.idLocalizacao = idLocalizacao;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }

    @Override
    public Map<Boolean, ArrayList<String>> validate() {
        ArrayList<String> errors = new ArrayList<>();
        if (super.validate().containsKey(false)) {
            errors.addAll(super.validate().get(false));
        }
        if (descricao == null || descricao.isBlank())
            errors.add("Descrição pode estar vazia");
        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Alert.class.getSimpleName() + "[", "]")
                .add("descricao='" + descricao + "'")
                .add("idLocalizacao=" + idLocalizacao)
                .add("idColaborador=" + idColaborador)
                .toString();
    }
}
