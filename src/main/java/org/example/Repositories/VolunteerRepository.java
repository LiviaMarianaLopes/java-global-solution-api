package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.Volunteer;
import org.example.utils.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VolunteerRepository implements _BaseRepository<Volunteer>, Loggable<String> {
    public static final String ID_COLUMN = "ID";
    public static final String NOME_COLUMN = "NOME";
    public static final String EMAIL_COLUMN = "EMAIL";
    public static final String TELEFONE_COLUMN = "TELEFONE";
    public static final String DATA_NASCIMENTO_COLUMN = "DATA_DE_NASCIMENTO";

    public static final String ID_COLLABORATOR_COLUMN = "ID_COLLABORATOR";
    public static final String TB_NAME = "VOLUNTEERS";


    public int getIdVolunteer(String email) {
        for (Volunteer l : readAll()) {

            if (l.getEmail().equals(email)) {

                return l.getId();

            }
        }
        return 0;
    }

    public Optional<Volunteer> getVolunteerById(int id) {
        String selectSQL = "select v.id, nome, email, telefone, data_de_nascimento, id_collaborator\n" +
                "from volunteers v\n" +
                "inner join collaborator c\n" +
                "on v.id_collaborator = c.id\n" +
                "where v.id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Volunteer(rs.getInt(ID_COLUMN),
                        rs.getString(NOME_COLUMN),
                        rs.getString(EMAIL_COLUMN),
                        rs.getString(TELEFONE_COLUMN),
                        rs.getInt(ID_COLLABORATOR_COLUMN),
                        rs.getDate(DATA_NASCIMENTO_COLUMN).toLocalDate()
                ));
            }

        } catch (Exception e) {
            logError("Erro ao obter voluntário: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public void create(Volunteer entity) {
        // Primeiro, cria o colaborador
        CollaboratorRepository collaboratorRepository = new CollaboratorRepository();
        Optional<Integer> collaboratorId = collaboratorRepository.create(entity);
        if (collaboratorId.isPresent()) {


            // Em seguida, cria o voluntário usando o ID do colaborador
            String sql = "INSERT INTO " + TB_NAME + " (%s, %s) VALUES (?, TO_DATE(?, 'YYYY-MM-DD'))"
                    .formatted(ID_COLLABORATOR_COLUMN, DATA_NASCIMENTO_COLUMN);

            try (var connection = DatabaseConfig.getConnection();
                 var statement = connection.prepareStatement(sql)) {

                statement.setInt(1, collaboratorId.get());
                statement.setString(2, entity.getDataDeNascimento().toString());
                int result = statement.executeUpdate();
                logInfo("Insert realizado com sucesso, linhas afetadas: " + result);
            } catch (SQLException e) {
                logError("Erro ao inserir voluntário: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            logError("Erro ao criar colaborador");
            throw new IllegalArgumentException("Erro ao criar colaborador");
        }
    }

    @Override
    public List<Volunteer> readAll() {
        List<Volunteer> volunterrs = new ArrayList<>();
        String selectAllSQL = "select v.id, data_de_nascimento, id_collaborator, nome, email, telefone\n" +
                "from volunteers v\n" +
                "inner join collaborator c\n" +
                "on v.id_collaborator = c.id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Volunteer volunteer = new Volunteer(rs.getInt(ID_COLUMN),
                        rs.getString(NOME_COLUMN),
                        rs.getString(EMAIL_COLUMN),
                        rs.getString(TELEFONE_COLUMN),
                        rs.getInt(ID_COLLABORATOR_COLUMN),
                        rs.getDate(DATA_NASCIMENTO_COLUMN).toLocalDate()
                );
                volunterrs.add(volunteer);
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar voluntários: " + e.getMessage());
        }
        return volunterrs;
    }

    @Override
    public void update(int id, Volunteer entity) {
        Optional<Volunteer> volunteer = getVolunteerById(id);
        if (volunteer.isPresent()) {
            CollaboratorRepository collaboratorRepository = new CollaboratorRepository();
            collaboratorRepository.update(volunteer.get().getIdCollaborator(),
                    entity);

            String updateSQL = "UPDATE " + TB_NAME +
                    " SET data_de_nascimento = TO_DATE(?, 'YYYY-MM-DD') WHERE id = ?";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                String formattedDate = String.valueOf(entity.getDataDeNascimento());
                pstmt.setString(1, formattedDate);
                pstmt.setInt(2, id);

                pstmt.executeUpdate();
                logInfo("Voluntário atualizado com sucesso!");

            } catch (Exception e) {
                logError("Erro ao atualizar Voluntário: " + e.getMessage());
            }
        } else {
            logError("Erro ao indentificar colaborador");
            throw new IllegalArgumentException("Erro ao indentificar colaborador");
        }
    }


    @Override
    public void delete(int id) {
        Optional<Volunteer> volunteer = getVolunteerById(id);
        if (volunteer.isPresent()) {
            String deleteSQL = "DELETE FROM " + TB_NAME + " WHERE id = ?";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

                pstmt.setInt(1, id);

                pstmt.executeUpdate();
                logInfo("Voluntário excluído com sucesso!");

                CollaboratorRepository collaboratorRepository = new CollaboratorRepository();
                collaboratorRepository.delete(volunteer.get().getIdCollaborator());
            } catch (Exception e) {
                logError("Erro ao excluir Voluntário: " + e.getMessage());
            }
        } else {
            logError("Erro ao indentificar colaborador");
            throw new IllegalArgumentException("Erro ao indentificar colaborador");
        }
    }
}