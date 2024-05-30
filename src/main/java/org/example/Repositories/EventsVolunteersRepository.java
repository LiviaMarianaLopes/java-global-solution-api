package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.EventVolunteer;
import org.example.utils.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventsVolunteersRepository implements Loggable<String> {

    private static final String TB_NAME = "VS_EVENTS_VOLUNTEERS";
    private static final String ID_COLUMN = "id";

    private static final String ID_VOLUNTEER_COLUMN = "VS_VOLUNTEER_ID";
    private static final String ID_EVENT_COLUMN = "VS_EVENT_ID";

    public void registerVolunteerInEvent(EventVolunteer eventVolunteer) {
        String sql = "INSERT INTO " + TB_NAME + " (%s, %s) VALUES (?, ?)"
                .formatted(ID_VOLUNTEER_COLUMN, ID_EVENT_COLUMN);

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventVolunteer.getIdVolnteer());
            statement.setInt(2, eventVolunteer.getIdEvent());
            int result = statement.executeUpdate();
            logInfo("Voluntário registrado no evento com sucesso, linhas afetadas: " + result);
        } catch (SQLException e) {
            logError("Erro ao registrar voluntário no evento: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<EventVolunteer> readAll() {
        List<EventVolunteer> eventVolunteerList = new ArrayList<>();
        String selectAllSQL = "SELECT * FROM " + TB_NAME + " ORDER BY ID";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EventVolunteer eventVolunteer = new EventVolunteer(
                        rs.getInt(ID_COLUMN),
                        rs.getInt(ID_VOLUNTEER_COLUMN),
                        rs.getInt(ID_EVENT_COLUMN));
                eventVolunteerList.add(eventVolunteer);
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
        return eventVolunteerList;
    }

}

