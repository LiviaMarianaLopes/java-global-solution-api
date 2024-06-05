package org.example.entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Partner extends Collaborator {
    private String cnpj;

    public Partner() {
    }

    public Partner(int id, String nome, String email, String telefone, String cnpj) {
        super(id, nome, email, telefone);
        this.cnpj = cnpj;

    }

    public Partner(int id, String nome, String email, String telefone, int idCollaborator, String cnpj) {
        super(id, nome, email, telefone, idCollaborator);
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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
        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Partner.class.getSimpleName() + "[", "]")
                .add("cnpj='" + cnpj + "'")
                .toString();
    }
}

