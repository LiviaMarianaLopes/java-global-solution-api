package org.example.entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;

public abstract class Collaborator extends _BaseEntity {
    private String nome;
    private String email;
    private String telefone;

    public Collaborator() {
    }

    public Collaborator(int id, String nome, String email, String telefone) {
        super(id);
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Map<Boolean, ArrayList<String>> validate() {
        var errors = new ArrayList<String>();
        if (nome == null || nome.isBlank())
            errors.add("O campo nome não pode estar vazio");
        if (email == null || email.isBlank())
            errors.add("O campo email não pode estar vazio");
        // verifica se o email contem '@' e '.'
        if (!email.contains("@") || !email.contains("."))
            errors.add("Email inválido");
        if (telefone == null || telefone.isBlank())
            errors.add("Telefone não pode estar vazio");
        //verifica se o telefone tem entre 11 á 14 digitos
        if (telefone.length() < 11 || telefone.length() > 14) // variações possiveis: 11999999999 ou (11)99999-9999
            errors.add("Telefone inválido");
        return !errors.isEmpty() ?
                Map.of(false, errors) :
                Map.of(true, errors);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Collaborator.class.getSimpleName() + "[", "]")
                .add("nome='" + nome + "'")
                .add("email='" + email + "'")
                .add("telefone='" + telefone + "'")
                .toString();
    }
}
