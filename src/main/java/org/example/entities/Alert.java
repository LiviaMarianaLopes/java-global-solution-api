package org.example.entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Alert extends _BaseEntity {

    private String cep;
    private String nomeLocal;
    private String referencia;
    private String descricao;
    private int idColaborador;

    public Alert() {
    }

    public Alert(int id, String cep, String nomeLocal, String referencia, String descricao, int idColaborador) {
        super(id);
        this.cep = cep;
        this.nomeLocal = nomeLocal;
        this.referencia = referencia;
        this.descricao = descricao;
        this.idColaborador = idColaborador;
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNomePraia() {
        return nomeLocal;
    }

    public void setNomePraia(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (nomeLocal == null || nomeLocal.isBlank())
            errors.add("O campo nome da praia não pode estar vazio");
        if (cep == null || cep.isBlank())
            errors.add("CEP não pode estar vazio");
        if (cep.length() < 8 || cep.length() > 9)
            errors.add("CEP inválido");
        if (idColaborador < 1) {
            errors.add("Id do colaborador esta inválido");
        }
        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Alert.class.getSimpleName() + "[", "]")
                .add("cep='" + cep + "'")
                .add("nomeLocal='" + nomeLocal + "'")
                .add("referencia='" + referencia + "'")
                .add("descricao='" + descricao + "'")
                .add("idColaborador=" + idColaborador)
                .toString();
    }
}
