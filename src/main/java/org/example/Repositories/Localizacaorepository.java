package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.Infrastructure.Loggable;
import org.example.entities.Localizacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Localizacaorepository implements Loggable<String> {
    public static final HashMap<String, String> COLUMN_TYPE_NAMES = new HashMap<String, String>() {{
        put("ID_COLUMN", "ID");
        put("CEP_COLUMN", "CEP");
        put("ESTADO_COLUMN", "ESTADO");
        put("CIDADE_COLUMN", "CIDADE");
        put("LOGRADOURO_COLUMN", "LOGRADOURO");
        put("REFERENCIA_COLUMN", "REFERENCIA");
    }};
    public static final String TB_NAME = "VS_LOCATION";

    public Optional<Integer> create(Localizacao entity) {
        String sql = "INSERT INTO " + TB_NAME + " (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)"
                .formatted(COLUMN_TYPE_NAMES.get("CEP_COLUMN"), COLUMN_TYPE_NAMES.get("ESTADO_COLUMN"),
                        COLUMN_TYPE_NAMES.get("CIDADE_COLUMN"), COLUMN_TYPE_NAMES.get("LOGRADOURO_COLUMN"),
                        COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN"));

        try (var connection = DatabaseConfig.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getCep());
            statement.setString(2, entity.getEstado());
            statement.setString(3, entity.getCidade());
            statement.setString(4, entity.getLogradouro());
            statement.setString(5, entity.getReferencia());

            statement.executeUpdate();
            return getId(entity);

        } catch (SQLException e) {
            logError("Erro ao inserir localização: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(int id, Localizacao entity) {
        String updateSQL = "UPDATE " + TB_NAME + " SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE id = ?"
                .formatted(COLUMN_TYPE_NAMES.get("CEP_COLUMN"), COLUMN_TYPE_NAMES.get("ESTADO_COLUMN"),
                        COLUMN_TYPE_NAMES.get("CIDADE_COLUMN"), COLUMN_TYPE_NAMES.get("LOGRADOURO_COLUMN"),
                        COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN"));

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, entity.getCep());
            pstmt.setString(2, entity.getEstado());
            pstmt.setString(3, entity.getCidade());
            pstmt.setString(4, entity.getLogradouro());
            pstmt.setString(5, entity.getReferencia());
            pstmt.setInt(6, id);

            pstmt.executeUpdate();
            logInfo("Localização atualizada com sucesso!");

        } catch (Exception e) {
            logError("Erro ao atualizar Localização: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String deleteSQL = "DELETE FROM " + TB_NAME + " WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            logInfo("Localização excluída com sucesso!");

        } catch (Exception e) {
            logError("Erro ao excluir Localização: " + e.getMessage());
        }
    }

    public List<Localizacao> readAll() {
        List<Localizacao> localizacaoList = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM " + TB_NAME + " ORDER BY ID";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Localizacao localizacao = new Localizacao(
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CEP_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("ESTADO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CIDADE_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("LOGRADOURO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN")));
                localizacaoList.add(localizacao);
            }

        } catch (Exception e) {
            logError("Erro ao listar localizações: " + e.getMessage());
        }
        return localizacaoList;
    }

    public Optional<Integer> getId(Localizacao entity) {
        StringBuilder selectSQL = new StringBuilder("SELECT ID FROM " + TB_NAME + " WHERE 1=1");
        List<Object> values = new ArrayList<>();

        if (entity.getCep() != null && !entity.getCep().isEmpty()) {
            selectSQL.append(" AND ").append(COLUMN_TYPE_NAMES.get("CEP_COLUMN")).append(" = ?");
            values.add(entity.getCep());
        }
        if (entity.getEstado() != null && !entity.getEstado().isEmpty()) {
            selectSQL.append(" AND ").append(COLUMN_TYPE_NAMES.get("ESTADO_COLUMN")).append(" = ?");
            values.add(entity.getEstado());
        }
        if (entity.getCidade() != null && !entity.getCidade().isEmpty()) {
            selectSQL.append(" AND ").append(COLUMN_TYPE_NAMES.get("CIDADE_COLUMN")).append(" = ?");
            values.add(entity.getCidade());
        }
        if (entity.getLogradouro() != null && !entity.getLogradouro().isEmpty()) {
            selectSQL.append(" AND ").append(COLUMN_TYPE_NAMES.get("LOGRADOURO_COLUMN")).append(" = ?");
            values.add(entity.getLogradouro());
        }

        if (entity.getReferencia() != null && !entity.getReferencia().isEmpty()) {
            selectSQL.append(" AND ").append(COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN")).append(" = ?");
            values.add(entity.getReferencia());
        }

        Integer id = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL.toString())) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
            return Optional.ofNullable(id);

        } catch (Exception e) {
            logError("Erro ao obter Localização: " + e.getMessage());
            throw new IllegalArgumentException("Erro ao obter Localização");
        }
    }

}