package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.Volunteer;
import org.example.Infrastructure.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class VolunteerRepository implements _BaseRepository<Volunteer>, Loggable<String> {
    public static final HashMap<String, String> COLUMN_TYPE_NAMES = new HashMap<String, String>() {{
        put("ID_COLUMN", "ID");
        put("NOME_COLUMN", "NOME");
        put("EMAIL_COLUMN", "EMAIL");
        put("TELEFONE_COLUMN", "TELEFONE");
        put("DATA_NASCIMENTO_COLUMN", "DATA_DE_NASCIMENTO");
        put("ID_COLLABORATOR_COLUMN", "ID_COLLABORATORS");
    }};
    public static final String TB_NAME = "VS_VOLUNTEERS";


    public int getIdVolunteer(String email) {
        for (Volunteer l : readAll()) {

            if (l.getEmail().equals(email)) {

                return l.getId();

            }
        }
        return 0;
    }

    @Override

    public Optional<Volunteer> getById(int id) {
        String selectSQL = "SELECT v.ID," +
                " v.DATA_DE_NASCIMENTO," +
                " c.ID AS ID_COLLABORATORS," +
                " c.NOME," +
                " c.EMAIL," +
                " c.TELEFONE" +
                " FROM" +
                " VS_VOLUNTEERS V" +
                " JOIN" +
                " VS_COLLABORATORS c" +
                " ON" +
                " v.ID_COLLABORATORS = c.ID WHERE V.ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Volunteer(rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("NOME_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("EMAIL_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("TELEFONE_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN")),
                        rs.getDate(COLUMN_TYPE_NAMES.get("DATA_NASCIMENTO_COLUMN")).toLocalDate()
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
                    .formatted(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN"), COLUMN_TYPE_NAMES.get("DATA_NASCIMENTO_COLUMN"));

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
        String selectAllSQL = "SELECT \n" +
                "    v.ID,\n" +
                "    v.DATA_DE_NASCIMENTO,\n" +
                "    c.ID AS ID_COLLABORATORS,\n" +
                "    c.NOME,\n" +
                "    c.EMAIL,\n" +
                "    c.TELEFONE\n" +
                "FROM \n" +
                "    VS_VOLUNTEERS v\n" +
                "JOIN \n" +
                "    VS_COLLABORATORS c\n" +
                "ON \n" +
                "    v.ID_COLLABORATORS = c.ID";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Volunteer volunteer = new Volunteer(rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("NOME_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("EMAIL_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("TELEFONE_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN")),
                        rs.getDate(COLUMN_TYPE_NAMES.get("DATA_NASCIMENTO_COLUMN")).toLocalDate()
                );
                volunterrs.add(volunteer);
            }

        } catch (Exception e) {
            logError("Erro ao listar voluntários: " + e.getMessage());
        }
        return volunterrs;
    }

    @Override
    public void update(int id, Volunteer entity) {
        Optional<Volunteer> volunteer = getById(id);
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
        Optional<Volunteer> volunteer = getById(id);
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