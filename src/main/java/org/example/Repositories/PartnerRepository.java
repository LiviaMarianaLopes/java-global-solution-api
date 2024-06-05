package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.Partner;
import org.example.Infrastructure.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PartnerRepository implements _BaseRepository<Partner>, Loggable<String> {
    public static final HashMap<String, String> COLUMN_TYPE_NAMES = new HashMap<String, String>() {{
        put("ID_COLUMN", "ID");
        put("NOME_COLUMN", "NOME");
        put("EMAIL_COLUMN", "EMAIL");
        put("TELEFONE_COLUMN", "TELEFONE");
        put("CNPJ_COLUMN", "CNPJ");
        put("ID_COLLABORATOR_COLUMN", "ID_COLLABORATOR");
    }};
    public static final String TB_NAME = "PARTNERS";

    @Override
    public Optional<Partner> getById(int id) {
        String selectSQL = "select P.id, nome, email, telefone, id_collaborator, CNPJ" +
                " from PARTNERS P inner join collaborator c on P.id_collaborator = c.id WHERE P.ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Partner(rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("NOME_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("EMAIL_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("TELEFONE_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CNPJ_COLUMN"))
                ));
            }

        } catch (Exception e) {
            logError("Erro ao obter parceiro: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public void create(Partner entity) {
        // Primeiro, cria o colaborador
        CollaboratorRepository collaboratorRepository = new CollaboratorRepository();
        Optional<Integer> collaboratorId = collaboratorRepository.create(entity);
        if (collaboratorId.isPresent()) {
            // Em seguida, cria o parceiro usando o ID do colaborador
            String sql = "INSERT INTO " + TB_NAME + " (%s, %s) VALUES (?, ?)"
                    .formatted(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN"), COLUMN_TYPE_NAMES.get("CNPJ_COLUMN"));
            try (var connection = DatabaseConfig.getConnection();
                 var statement = connection.prepareStatement(sql)) {
                statement.setInt(1, collaboratorId.get());
                statement.setString(2, entity.getCnpj());
                int result = statement.executeUpdate();
                logInfo("Insert realizado com sucesso, linhas afetadas: " + result);
            } catch (SQLException e) {
                logError("Erro ao inserir patrocinador: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            logError("Erro ao criar colaborador");
        }
    }

    @Override
    public List<Partner> readAll() {
        List<Partner> partners = new ArrayList<>();
        String selectAllSQL = "select P.id, nome, email, telefone, id_collaborator, CNPJ\n" +
                "from PARTNERS P\n" +
                "inner join collaborator c\n" +
                "on P.id_collaborator = c.id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Partner partner = new Partner(rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("NOME_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("EMAIL_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("TELEFONE_COLUMN")),
                        rs.getInt(COLUMN_TYPE_NAMES.get("ID_COLLABORATOR_COLUMN")),
                        rs.getString(COLUMN_TYPE_NAMES.get("CNPJ_COLUMN"))
                );
                partners.add(partner);
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar parceiros: " + e.getMessage());
        }
        return partners;
    }

    @Override
    public void update(int id, Partner entity) {
        Optional<Partner> partner = getById(id);
        if (partner.isPresent()) {
            CollaboratorRepository collaboratorRepository = new CollaboratorRepository();
            collaboratorRepository.update(partner.get().getIdCollaborator(),
                    entity);

            String updateSQL = "UPDATE " + TB_NAME +
                    " SET CNPJ = ? WHERE id = ?";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setString(1, entity.getCnpj());
                pstmt.setInt(2, id);

                pstmt.executeUpdate();
                logInfo("Parceiro atualizado com sucesso!");

            } catch (Exception e) {
                logError("Erro ao atualizar Parceiro: " + e.getMessage());
            }
        } else {
            logError("Erro ao indentificar colaborador");
            throw new IllegalArgumentException("Erro ao indentificar colaborador");
        }
    }


    @Override
    public void delete(int id) {
        Optional<Partner> partner = getById(id);
        if (partner.isPresent()) {
            String deleteSQL = "DELETE FROM " + TB_NAME + " WHERE id = ?";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

                pstmt.setInt(1, id);

                pstmt.executeUpdate();
                logInfo("Parceiro excluído com sucesso!");

                CollaboratorRepository collaboratorRepository = new CollaboratorRepository();
                collaboratorRepository.delete(partner.get().getIdCollaborator());
            } catch (Exception e) {
                logError("Erro ao excluir Parceiro: " + e.getMessage());
            }
        } else {
            logError("Erro ao indentificar colaborador");
            throw new IllegalArgumentException("Erro ao indentificar colaborador");
        }
    }
}