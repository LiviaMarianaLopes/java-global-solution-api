package org.example.entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Partner extends Collaborator {
    private String cnpj;
    private String industria;

    public Partner() {
    }

    public Partner(int id, String nome, String email, String telefone, String cnpj, String industria) {
        super(id, nome, email, telefone);
        this.cnpj = cnpj;
        this.industria = industria;
    }

    public Partner(int id, String nome, String email, String telefone, int idCollaborator, String cnpj, String industria) {
        super(id, nome, email, telefone, idCollaborator);
        this.cnpj = cnpj;
        this.industria = industria;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getIndustria() {
        return industria;
    }

    public void setIndustria(String industria) {
        this.industria = industria;
    }

    @Override
    public Map<Boolean, ArrayList<String>> validate() {
        ArrayList<String> errors = new ArrayList<>();
        if (super.validate().containsKey(false)) {
            errors.addAll(super.validate().get(false));
        }
        if (cnpj == null || cnpj.isBlank())
            errors.add("O campo CNPJ não pode estar vazio");
        if (cnpj.length() < 14 || cnpj.length() > 18)
            errors.add("CNPJ inválido");
        if (industria == null || industria.isBlank())
            errors.add("O campo industria não pode estar vazio");
        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Partner.class.getSimpleName() + "[", "]")
                .add("cnpj='" + cnpj + "'")
                .add("industria='" + industria + "'")
                .toString();
    }
}

