package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.EventVolunteer;
import org.example.Infrastructure.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventsVolunteersRepository implements Loggable<String> {

    private static final String TB_NAME = "VS_EVENTS_VOLUNTEERS";
    public static final HashMap<String, String> COLUMN_TYPE_NAMES = new HashMap<String, String>() {{
        put("ID_COLUMN", "ID");
        put("ID_VOLUNTEER_COLUMN", "ID_VOLUNTEERS");
        put("ID_EVENT_COLUMN", "ID_EVENTS");
    }};

    public void registerVolunteerInEvent(EventVolunteer eventVolunteer) {
        String sql = "INSERT INTO " + TB_NAME + " (%s, %s) VALUES (?, ?)"
                .formatted(COLUMN_TYPE_NAMES.get("ID_VOLUNTEER_COLUMN"), COLUMN_TYPE_NAMES.get("ID_EVENT_COLUMN") );

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
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_VOLUNTEER_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_EVENT_COLUMN")));
                eventVolunteerList.add(eventVolunteer);
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar tabela EventsVolunteers: " + e.getMessage());
        }
        return eventVolunteerList;
    }

}

