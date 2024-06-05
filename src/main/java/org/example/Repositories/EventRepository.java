package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.Alert;
import org.example.entities.Event;
import org.example.Infrastructure.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EventRepository implements Loggable<String> {
    public static final HashMap<String, String> COLUMN_TYPE_NAMES = new HashMap<String, String>() {{
        put("ID_COLUMN", "ID");
        put("CEP_COLUMN", "CEP");
        put("ESTADO_COLUMN", "ESTADO");
        put("CIDADE_COLUMN", "CIDADE");
        put("LOGRADOURO_COLUMN", "LOGRADOURO");
        put("REFERENCIA_COLUMN", "REFERENCIA");
        put("DESCRICAO_ALERT_COLUMN", "ALERT_DESCRICAO");
        put("ID_LOCALIZACAO_COLUMN", "ID_LOCALIZACAO");
        put("ID_COLLABORATOR_COLUMN", "ID_COLLABORATOR");
        put("TITULO_COLUMN", "TITULO");
        put("DESCRICAO_COLUMN", "DESCRICAO");
        put("DATA_COLUMN", "DATA");
        put("ID_ALERT_COLUMN", "ID_ALERTS");
    }};
    public static final String TB_NAME = "VS_EVENTS";

    public Optional<Event> getById(int id) {
        String selectSQL = " SELECT \n" +
                "    ve.id,\n" +
                "    ve.titulo,\n" +
                "    ve.descricao,\n" +
                "    ve.data,\n" +
                "    va.id AS id_alerts,\n" +
                "    va.id_collaborators AS id_collaborator,\n" +
                "    va.descricao AS alert_descricao,\n" +
                "    l.id AS id_localizacao,\n" +
                "    l.cep,\n" +
                "    l.estado,\n" +
                "    l.cidade,\n" +
                "    l.logradouro,\n" +
                "    l.referencia  \n" +
                "FROM \n" +
                "    vs_events ve\n" +
                "JOIN \n" +
                "    vs_alerts va ON ve.id_alerts = va.id\n" +
                "JOIN \n" +
                "    VS_LOCATION l ON va.ID_LOCATION = l.id " +
                "WHERE ve.id = ? ";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Event(
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CEP_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("ESTADO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CIDADE_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("LOGRADOURO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("DESCRICAO_ALERT_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_LOCALIZACAO_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("TITULO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN")),
                        rs.getTimestamp(COLUMN_TYPE_NAMES.get("DATA_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_ALERT_COLUMN"))));

            }

        } catch (Exception e) {
            logError("Erro ao obter evento: " + e.getMessage());
        }

        return Optional.empty();
    }

    public void create(Event entity) {
        // Primeiro, cria o alerta
        AlertRepository alertRepository = new AlertRepository();
        Optional<Integer> alertId = alertRepository.createId(entity);
        if (alertId.isPresent()) {
            // Em seguida, cria o evento
            String sql = "INSERT INTO " + TB_NAME + " (%s, %s, %s, %s) VALUES (?, ?, ?, ?)"
                    .formatted(COLUMN_TYPE_NAMES.get("TITULO_COLUMN"), COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN"), COLUMN_TYPE_NAMES.get("DATA_COLUMN"), COLUMN_TYPE_NAMES.get("ID_ALERT_COLUMN"));
            try (var connection = DatabaseConfig.getConnection();
                 var statement = connection.prepareStatement(sql)) {
                statement.setString(1, entity.getTitulo());
                statement.setString(2, entity.getDescricao());
                statement.setTimestamp(3, entity.getData());
                statement.setInt(4, alertId.get());

                int result = statement.executeUpdate();
                logInfo("Insert realizado com sucesso, linhas afetadas: " + result);
            } catch (SQLException e) {
                logError("Erro ao inserir evento: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            logError("Erro ao criar evento");
            throw new IllegalArgumentException("Erro ao criar evento");
        }
    }


    public List<Event> readAll(String orderby) {
        List<Event> eventList = new ArrayList<>();
        String selectAllSQL = " SELECT \n" +
                "    ve.id,\n" +
                "    ve.titulo,\n" +
                "    ve.descricao,\n" +
                "    ve.data,\n" +
                "    va.id AS id_alerts,\n" +
                "    va.id_collaborators AS id_collaborator,\n" +
                "    va.descricao AS alert_descricao,\n" +
                "    l.id AS id_localizacao,\n" +
                "    l.cep,\n" +
                "    l.estado,\n" +
                "    l.cidade,\n" +
                "    l.logradouro,\n" +
                "    l.referencia  \n" +
                "FROM \n" +
                "    vs_events ve\n" +
                "JOIN \n" +
                "    vs_alerts va ON ve.id_alerts = va.id\n" +
                "JOIN \n" +
                "    VS_LOCATION l ON va.ID_LOCATION = l.id ORDER BY " + orderby;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Event alert = new Event(
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CEP_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("ESTADO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CIDADE_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("LOGRADOURO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("REFERENCIA_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("DESCRICAO_ALERT_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_LOCALIZACAO_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("TITULO_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN")),
                        rs.getTimestamp(COLUMN_TYPE_NAMES.get("DATA_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_ALERT_COLUMN")));
                eventList.add(alert);
            }

        } catch (Exception e) {
            logError("Erro ao listar eventos: " + e.getMessage());
        }
        return eventList;
    }

    public void update(int id, Event entity) {
        Optional<Event> event = getById(id);
        if (event.isPresent()) {
            AlertRepository alertRepository = new AlertRepository();
            alertRepository.update(event.get().getIdAlerta(),
                    entity);

            String updateSQL = "UPDATE " + TB_NAME + " SET %s = ?, %S = ?, %s = ? WHERE id = ?"
                    .formatted(COLUMN_TYPE_NAMES.get("TITULO_COLUMN"), COLUMN_TYPE_NAMES.get("DESCRICAO_COLUMN"), COLUMN_TYPE_NAMES.get("DATA_COLUMN"));

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

                pstmt.setString(1, entity.getTitulo());
                pstmt.setString(2, entity.getDescricao());
                pstmt.setTimestamp(3, entity.getData());
                pstmt.setInt(4, id);

                pstmt.executeUpdate();
                logInfo("Evento atualizado com sucesso!");

            } catch (Exception e) {
                logError("Erro ao atualizar Evento: " + e.getMessage());
            }
        }
    }

    public void delete(int id) {
        Optional<Event> event = getById(id);
        if (event.isPresent()) {
            String deleteSQL = "DELETE FROM " + TB_NAME + " WHERE id = ?";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

                pstmt.setInt(1, id);

                pstmt.executeUpdate();
                logInfo("Evento excluído com sucesso!");
                AlertRepository alertRepository = new AlertRepository();
                alertRepository.delete(event.get().getIdAlerta());

            } catch (Exception e) {
                logError("Erro ao excluir Evento: " + e.getMessage());
            }
        } else {
            logError("Erro ao indentificar evento");
            throw new IllegalArgumentException("Erro ao indentificar evento");
        }
    }
}