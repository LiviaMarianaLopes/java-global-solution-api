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
        put("ESTADO_COLUMN", "ESTADO");
        put("CIDADE_COLUMN", "CIDADE");
        put("LOGRADOURO_COLUMN", "LOGRADOURO");
        put("REFERENCIA_COLUMN", "REFERENCIA");
        put("DESCRICAO_COLUMN", "DESCRICAO");
        put("ID_LOCALIZACAO_COLUMN", "ID_LOCATION");
        put("ID_COLLABORATOR_COLUMN", "ID_COLLABORATORS");
    }};
    public static final String TB_NAME = "VS_ALERTS";

    @Override
    public Optional<Alert> getById(int id) {
        String selectSQL = " SELECT A.ID, CEP, ESTADO, CIDADE, LOGRADOURO, REFERENCIA, DESCRICAO, ID_LOCATION, ID_COLLABORATORS\n" +
                " FROM VS_ALERTS A\n" +
                " INNER JOIN VS_LOCATION L\n" +
                " ON A.ID_LOCATION = L.ID\n" +
                " WHERE A.ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Alert(
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CEP_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("ESTADO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CIDADE_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("LOGRADOURO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_LOCALIZACAO_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN"))));
            }

        } catch (Exception e) {
            logError("Erro ao obter alerta: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public void create(Alert entity) {
        // Primeiro, cria a localização
        Localizacaorepository localizacaoRepository = new Localizacaorepository();
        Optional<Integer> localizacaoId = localizacaoRepository.create(entity);
        if (localizacaoId.isPresent()) {
            // Em seguida, cria o alerta usando o ID da localização
            String sql = "INSERT INTO " + TB_NAME + " (%s, %s, %s) VALUES (?, ?, ?)"
                    .formatted(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN"), COLUMN_TYPE_NAMES.get("ID_LOCALIZACAO_COLUMN"), COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN"));
            try (var connection = DatabaseConfig.getConnection();
                 var statement = connection.prepareStatement(sql)) {
                statement.setString(1, entity.getDescricao());
                statement.setInt(2, localizacaoId.get());
                statement.setInt(3, entity.getIdColaborador());

                int result = statement.executeUpdate();
                logInfo("Insert realizado com sucesso, linhas afetadas: " + result);
            } catch (SQLException e) {
                logError("Erro ao inserir alerta: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            logError("Erro ao criar alerta");
            throw new IllegalArgumentException("Erro ao criar alerta");
        }
    }

    @Override
    public List<Alert> readAll() {
        List<Alert> alertList = new ArrayList<>();
        String selectAllSQL = " SELECT A.ID, CEP, ESTADO, CIDADE, LOGRADOURO, REFERENCIA, DESCRICAO, ID_LOCATION, ID_COLLABORATORS\n" +
                " FROM VS_ALERTS A\n" +
                " INNER JOIN VS_LOCATION L\n" +
                " ON A.ID_LOCATION = L.ID";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Alert alert = new Alert(
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CEP_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("ESTADO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CIDADE_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("LOGRADOURO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_LOCALIZACAO_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN"))
                );
                alertList.add(alert);
            }

        } catch (Exception e) {
            logError("Erro ao listar alertas: " + e.getMessage());
        }
        return alertList;
    }

    @Override
    public void update(int id, Alert entity) {
        Optional<Alert> alert = getById(id);
        if (alert.isPresent()) {
            Localizacaorepository localizacaoRepository = new Localizacaorepository();
            localizacaoRepository.update(alert.get().getIdLocalizacao(),
                    entity);
            String updateSQL = "UPDATE " + TB_NAME + " SET %s = ?, %s = ? WHERE id = ?"
                    .formatted(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN"), COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN"));

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

                pstmt.setString(1, entity.getDescricao());
                pstmt.setInt(2, entity.getIdColaborador());
                pstmt.setInt(3, id);

                pstmt.executeUpdate();
                logInfo("Alerta atualizado com sucesso!");

            } catch (Exception e) {
                logError("Erro ao atualizar Alerta: " + e.getMessage());
            }
        }
    }

    @Override
    public void delete(int id) {
        Optional<Alert> alert = getById(id);
        if (alert.isPresent()) {
            String deleteSQL = "DELETE FROM " + TB_NAME + " WHERE id = ?";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

                pstmt.setInt(1, id);

                pstmt.executeUpdate();
                logInfo("Alerta excluído com sucesso!");
                Localizacaorepository localizacaoRepository = new Localizacaorepository();
                localizacaoRepository.delete(alert.get().getIdLocalizacao());

            } catch (Exception e) {
                logError("Erro ao excluir Alerta: " + e.getMessage());
            }
        } else {
            logError("Erro ao indentificar alerta");
            throw new IllegalArgumentException("Erro ao indentificar alerta");
        }
    }

    public Optional<Integer> getId(Alert entity, Optional<Integer> localizacaoId) {
        StringBuilder selectSQL = new StringBuilder("SELECT ID FROM " + TB_NAME + " WHERE ID_LOCATION = ? AND ID_COLLABORATORS = ?");
        List<Object> values = new ArrayList<>();

        // Adicionando o valor de localizacaoId desempacotado
        if (localizacaoId.isPresent()) {
            values.add(localizacaoId.get());
        } else {
            values.add(null);
            throw new IllegalArgumentException("id localização não encontrado");// Ou lançar uma exceção se localizacaoId for obrigatório
        }

        values.add(entity.getIdColaborador());

        if (entity.getDescricao() != null && !entity.getDescricao().isEmpty()) {
            selectSQL.append(" AND ").append(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN")).append(" = ?");
            values.add(entity.getDescricao());
        }

        Integer id = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL.toString())) {
            for (int i = 0; i < values.size(); i++) {
                // Definindo o tipo correto para cada valor
                if (values.get(i) == null) {
                    pstmt.setNull(i + 1, java.sql.Types.INTEGER); // Ou o tipo correspondente
                } else if (values.get(i) instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values.get(i));
                } else if (values.get(i) instanceof String) {
                    pstmt.setString(i + 1, (String) values.get(i));
                } else {
                    pstmt.setObject(i + 1, values.get(i)); // Fallback para setObject
                }
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
            return Optional.ofNullable(id);

        } catch (SQLException e) {
            logError("Erro ao obter alerta: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Erro ao obter alerta");
        }
    }

    public Optional<Integer> createId(Alert entity) {
        // Primeiro, cria a localização
        Localizacaorepository localizacaoRepository = new Localizacaorepository();
        Optional<Integer> localizacaoId = localizacaoRepository.create(entity);
        if (localizacaoId.isPresent()) {
            // Em seguida, cria o alerta usando o ID da localização
            String sql = "INSERT INTO " + TB_NAME + " (%s, %s, %s) VALUES (?, ?, ?)"
                    .formatted(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN"), COLUMN_TYPE_NAMES.get("ID_LOCALIZACAO_COLUMN"), COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN"));
            try (var connection = DatabaseConfig.getConnection();
                 var statement = connection.prepareStatement(sql)) {
                statement.setString(1, entity.getDescricao());
                statement.setInt(2, localizacaoId.get());
                statement.setInt(3, entity.getIdColaborador());

                int result = statement.executeUpdate();
                logInfo("Insert realizado com sucesso, linhas afetadas: " + result);
                return getId(entity, localizacaoId);
            } catch (SQLException e) {
                logError("Erro ao inserir alerta: " + e.getMessage());
                throw new RuntimeException(e);
            }

        } else {
            logError("Erro ao criar alerta");
            throw new IllegalArgumentException("Erro ao criar alerta");
        }
    }
}