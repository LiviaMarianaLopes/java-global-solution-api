package org.example.entities;

import jakarta.json.bind.annotation.JsonbDateFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public class Volunteer extends Collaborator {
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dataDeNascimento;

    public Volunteer() {
    }


    public Volunteer(int id, String nome, String email, String telefone, int idCollaborator, LocalDate dataDeNascimento) {
        super(id, nome, email, telefone, idCollaborator);
        this.dataDeNascimento = dataDeNascimento;
    }


    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
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
