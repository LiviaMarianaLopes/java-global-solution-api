package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.Collaborator;
import org.example.Infrastructure.Loggable;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CollaboratorRepository implements Loggable<String> {
    public static final HashMap<String, String> COLUMN_TYPE_NAMES = new HashMap<String, String>() {{
        put("ID_COLUMN", "ID");
        put("NOME_COLUMN", "NOME");
        put("EMAIL_COLUMN", "EMAIL");
        put("TELEFONE_COLUMN", "TELEFONE");
    }};
    public static final String TB_NAME = "COLLABORATOR";

    public Optional<Integer> create(Collaborator entity) {
        String sql = "INSERT INTO " + TB_NAME + " (%s, %s, %s) VALUES (?, ?, ?)"
                .formatted(COLUMN_TYPE_NAMES.get("NOME_COLUMN"),COLUMN_TYPE_NAMES.get("EMAIL_COLUMN") ,COLUMN_TYPE_NAMES.get("TELEFONE_COLUMN") );

        try (var connection = DatabaseConfig.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getNome());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getTelefone());
            statement.executeUpdate();
            return getId(entity.getEmail());

        } catch (SQLException e) {
            logError("Erro ao inserir colaborador: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(int id, Collaborator entity) {
        String updateSQL = "UPDATE " + TB_NAME + " SET nome = ?, email = ?, telefone = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, entity.getNome());
            pstmt.setString(2, entity.getEmail());
            pstmt.setString(3, entity.getTelefone());
            pstmt.setInt(4, id);

            pstmt.executeUpdate();
            logInfo("Colaborador atualizado com sucesso!");

        } catch (Exception e) {
            logError("Erro ao atualizar Colaborador: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String deleteSQL = "DELETE FROM " + TB_NAME + " WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            logInfo("Colaborador excluído com sucesso!");

        } catch (Exception e) {
            logError("Erro ao excluir Colaborador: " + e.getMessage());
        }
    }
    public List<Collaborator> readAll() {
        List<Collaborator> collaboratorList = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM " + TB_NAME + " ORDER BY ID";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Collaborator collaborator = new Collaborator(
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("NOME_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("EMAIL_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("TELEFONE_COLUMN")));
                collaboratorList.add(collaborator);
            }

        } catch (Exception e) {
            logError("Erro ao listar colaboradores: " + e.getMessage());
        }
        return collaboratorList;
    }

    public Optional<Integer> getId(String email) {
        String selectSQL = "SELECT ID FROM " + TB_NAME + " WHERE EMAIL = ?";
        Integer id = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
            return Optional.of(id);

        } catch (Exception e) {
            logError("Erro ao obter Colaborador: " + e.getMessage());
        }

        return Optional.empty();
    }
}
