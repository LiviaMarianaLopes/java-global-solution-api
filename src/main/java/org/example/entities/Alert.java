package org.example.entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Alert extends _BaseEntity {

    private String cep;
    private String nomePraia;
    private String referencia;
    private String descricao;
    private int idColaborador;

    public Alert() {
    }

    public Alert(int id, String cep, String nomePraia, String referencia, String descricao, int idColaborador) {
        super(id);
        this.cep = cep;
        this.nomePraia = nomePraia;
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
        return nomePraia;
    }

    public void setNomePraia(String nomePraia) {
        this.nomePraia = nomePraia;
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
        if (nomePraia == null || nomePraia.isBlank())
            errors.add("O campo nome da praia não pode estar vazio");
        if (descricao == null || descricao.isBlank())
            errors.add("Descrição não pode estar vazio");
        if (referencia == null || referencia.isBlank())
            errors.add("O campo referêcia não pode estar vazio");
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
                .add("nomePraia='" + nomePraia + "'")
                .add("referencia='" + referencia + "'")
                .add("descricao='" + descricao + "'")
                .add("idColaborador=" + idColaborador)
                .toString();
    }
}
