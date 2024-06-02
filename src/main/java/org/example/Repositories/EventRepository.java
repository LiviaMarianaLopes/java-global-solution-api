package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.Event;
import org.example.utils.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventRepository implements Loggable<String> {
    public static final String ID_COLUMN = "ID";
    public static final String TITULO_COLUMN = "TITULO";
    public static final String DESCRICAO_COLUMN = "DESCRICAO";
    public static final String DATA_COLUMN = "DATA";

    public static final String LOCAL_COLUMN = "LOCAL";

    public static final String ID_ALERT_COLUMN = "ID_ALERT";
    public static final String TB_NAME = "VS_EVENTS";

    public Optional<Event> getEventById(int id) {
        String selectSQL = "select * FROM VS_EVENTS WHERE ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Event(
                        rs.getInt(ID_COLUMN),
                        rs.getString(TITULO_COLUMN),
                        rs.getString(DESCRICAO_COLUMN),
                        rs.getTimestamp(DATA_COLUMN),
                        rs.getString(LOCAL_COLUMN),
                        rs.getInt(ID_ALERT_COLUMN)));
            }

        } catch (Exception e) {
            logError("Erro ao obter evento: " + e.getMessage());
        }

        return Optional.empty();
    }

    public void create(Event entity) {
        String sql = "INSERT INTO " + TB_NAME + " (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)"
                .formatted(TITULO_COLUMN, DESCRICAO_COLUMN, DATA_COLUMN, LOCAL_COLUMN, ID_ALERT_COLUMN);

        try (var connection = DatabaseConfig.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getTitulo());
            statement.setString(2, entity.getDescricao());
            statement.setTimestamp(3, entity.getData());
            statement.setString(4, entity.getLocal());
            statement.setInt(5, entity.getIdAlerta());

            statement.executeUpdate();

        } catch (SQLException e) {
            logError("Erro ao inserir evento: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public List<Event> readAll(String orderby) {
        List<Event> eventList = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM " + TB_NAME + " ORDER BY "+ orderby ;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Event alert = new Event(
                        rs.getInt(ID_COLUMN),
                        rs.getString(TITULO_COLUMN),
                        rs.getString(DESCRICAO_COLUMN),
                        rs.getTimestamp(DATA_COLUMN),
                        rs.getString(LOCAL_COLUMN),
                        rs.getInt(ID_ALERT_COLUMN));
                eventList.add(alert);
            }

        } catch (Exception e) {
            logError("Erro ao listar eventos: " + e.getMessage());
        }
        return eventList;
    }

    public void update(int id, Event entity) {
        String updateSQL = "UPDATE " + TB_NAME + " SET %s = ?, %S = ?, %s = ?, %s = ?, %s = ? WHERE id = ?"
                .formatted(TITULO_COLUMN, DESCRICAO_COLUMN, DATA_COLUMN, LOCAL_COLUMN, ID_ALERT_COLUMN);

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, entity.getTitulo());
            pstmt.setString(2, entity.getDescricao());
            pstmt.setTimestamp(3, entity.getData());
            pstmt.setString(4, entity.getLocal());
            pstmt.setInt(5, entity.getIdAlerta());
            pstmt.setInt(6, id);

            pstmt.executeUpdate();
            logInfo("Evento atualizado com sucesso!");

        } catch (Exception e) {
            logError("Erro ao atualizar Evento: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String deleteSQL = "DELETE FROM " + TB_NAME + " WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            logInfo("Evento excluído com sucesso!");

        } catch (Exception e) {
            logError("Erro ao excluir Evento: " + e.getMessage());
        }
    }
}