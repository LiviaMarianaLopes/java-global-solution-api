package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.Alert;
import org.example.Infrastructure.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class AlertRepository implements Loggable<String>, _BaseRepository<Alert> {
    public static final HashMap<String, String> COLUMN_TYPE_NAMES = new HashMap<String, String>() {{
        put("ID_COLUMN", "ID");
        put("CEP_COLUMN", "CEP");
        put("NOME_LOCAL_COLUMN", "NOME_LOCAL");
        put("REFERENCIA_COLUMN", "REFERENCIA");
        put("DESCRICAO_COLUMN", "DESCRICAO");
        put("ID_COLLABORATOR_COLUMN", "ID_COLLABORATOR");
    }};
    public static final String TB_NAME = "VS_ALERTS";
    public Optional<Alert> getAlertById(int id) {
        String selectSQL = "select * FROM VS_ALERTS WHERE ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Alert(
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CEP_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("NOME_LOCAL_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN"))));
            }

        } catch (Exception e) {
            logError("Erro ao obter alerta: " + e.getMessage());
        }

        return Optional.empty();
    }
    @Override
    public void create(Alert entity) {
        String sql = "INSERT INTO " + TB_NAME + " (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)"
                .formatted(COLUMN_TYPE_NAMES.get("CEP_COLUMN"), COLUMN_TYPE_NAMES.get("NOME_LOCAL_COLUMN"), COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN"), COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN"), COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN"));

        try (var connection = DatabaseConfig.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getCep());
            statement.setString(2, entity.getNomeLocal());
            statement.setString(3, entity.getReferencia());
            statement.setString(4, entity.getDescricao());
            statement.setInt(5, entity.getIdColaborador());

            statement.executeUpdate();

        } catch (SQLException e) {
            logError("Erro ao inserir alerta: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Alert> readAll() {
        List<Alert> alertList = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM " + TB_NAME + " ORDER BY ID";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Alert alert = new Alert(
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CEP_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("NOME_LOCAL_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN")));
                alertList.add(alert);
            }

        } catch (Exception e) {
            logError("Erro ao listar alertas: " + e.getMessage());
        }
        return alertList;
    }

    @Override
    public void update(int id, Alert entity) {
        String updateSQL = "UPDATE " + TB_NAME + " SET cep = ?, nome_local = ?, referencia = ?, descricao = ?, id_collaborator = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, entity.getCep());
            pstmt.setString(2, entity.getNomeLocal());
            pstmt.setString(3, entity.getReferencia());
            pstmt.setString(4, entity.getDescricao());
            pstmt.setInt(5, entity.getIdColaborador());
            pstmt.setInt(6, id);

            pstmt.executeUpdate();
            logInfo("Alerta atualizado com sucesso!");

        } catch (Exception e) {
            logError("Erro ao atualizar Alerta: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String deleteSQL = "DELETE FROM " + TB_NAME + " WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            logInfo("Alerta excluído com sucesso!");

        } catch (Exception e) {
            logError("Erro ao excluir Alerta: " + e.getMessage());
        }
    }
}