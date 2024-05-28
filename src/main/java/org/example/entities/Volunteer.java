package org.example.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.StringJoiner;

public class Volunteer extends Collaborator {
    private Date dataDeNascimento;

    public Volunteer() {
    }

    public Volunteer(int id, String nome, String email, String telefone, Date dataDeNascimento) {
        super(id, nome, email, telefone);
        this.dataDeNascimento = dataDeNascimento;
    }

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    @Override
    public Map<Boolean, ArrayList<String>> validate() {
        ArrayList<String> errors = new ArrayList<>();
        if (super.validate().containsKey(false)) {
            errors.addAll(super.validate().get(false));
        }
        if (dataDeNascimento == null)
            errors.add("O campo data de nascimento não pode estar vazio");
        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Volunteer.class.getSimpleName() + "[", "]")
                .add("dataDeNascimento=" + dataDeNascimento)
                .toString();
    }
}
