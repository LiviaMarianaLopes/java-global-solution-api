package org.example.entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Localizacao extends _BaseEntity {
    private String cep;
    private String estado;
    private String cidade;
    private String logradouro;
    private String referencia;

    public Localizacao() {
    }

    public Localizacao(int id, String cep, String estado, String cidade, String logradouro, String referencia) {
        super(id);
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.logradouro = logradouro;
        this.referencia = referencia;
    }

    public Localizacao(String cep, String estado, String cidade, String logradouro, String referencia) {
        this.cep = cep;
        this.estado = estado;
        this.cidade = cidade;
        this.logradouro = logradouro;
        this.referencia = referencia;
    }

    public Localizacao(int id) {
        super(id);
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (estado == null || estado.isBlank())
            errors.add("O campo estado não pode estar vazio");
        if (cidade == null || cidade.isBlank())
            errors.add("O campo cidade não pode estar vazio");
        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Localizacao.class.getSimpleName() + "[", "]")
                .add("cep='" + cep + "'")
                .add("estado='" + estado + "'")
                .add("cidade='" + cidade + "'")
                .add("logradouro='" + logradouro + "'")
                .add("referencia='" + referencia + "'")
                .toString();
    }
}
