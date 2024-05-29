package org.example.Repositories;

import org.example.Infrastructure.DatabaseConfig;
import org.example.entities.Partner;
import org.example.entities.Volunteer;
import org.example.utils.Loggable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartnerRepository implements _BaseRepository<Partner>, Loggable<String> {
    public static final String ID_COLUMN = "ID";
    public static final String NOME_COLUMN = "NOME";
    public static final String EMAIL_COLUMN = "EMAIL";
    public static final String TELEFONE_COLUMN = "TELEFONE";
    public static final String CNPJ_COLUMN = "CNPJ";
    public static final String INDUSTRIA_COLUMN = "INDUSTRIA";
    public static final String ID_COLLABORATOR_COLUMN = "ID_COLLABORATOR";
    public static final String TB_NAME = "PARTNERS";


    public Optional<Partner> getPartenerById(int id) {
        String selectSQL = "select P.id, nome, email, telefone, id_collaborator, CNPJ, INDUSTRIA" +
                " from PARTNERS P inner join collaborator c on P.id_collaborator = c.id WHERE P.ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Partner(rs.getInt(ID_COLUMN),
                        rs.getString(NOME_COLUMN),
                        rs.getString(EMAIL_COLUMN),
                        rs.getString(TELEFONE_COLUMN),
                        rs.getInt(ID_COLLABORATOR_COLUMN),
                        rs.getString(CNPJ_COLUMN),
                        rs.getString(INDUSTRIA_COLUMN)
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
            String sql = "INSERT INTO " + TB_NAME + " (%s, %s, %s) VALUES (?, ?, ?)"
                    .formatted(ID_COLLABORATOR_COLUMN, CNPJ_COLUMN, INDUSTRIA_COLUMN);
            try (var connection = DatabaseConfig.getConnection();
                 var statement = connection.prepareStatement(sql)) {
                statement.setInt(1, collaboratorId.get());
                statement.setString(2, entity.getCnpj());
                statement.setString(3, entity.getIndustria());
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
        String selectAllSQL = "select P.id, nome, email, telefone, id_collaborator, CNPJ, INDUSTRIA\n" +
                "from PARTNERS P\n" +
                "inner join collaborator c\n" +
                "on P.id_collaborator = c.id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectAllSQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Partner partner = new Partner(rs.getInt(ID_COLUMN),
                        rs.getString(NOME_COLUMN),
                        rs.getString(EMAIL_COLUMN),
                        rs.getString(TELEFONE_COLUMN),
                        rs.getInt(ID_COLLABORATOR_COLUMN),
                        rs.getString(CNPJ_COLUMN),
                        rs.getString(INDUSTRIA_COLUMN)
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
        Optional<Partner> partner = getPartenerById(id);
        if (partner.isPresent()) {
            CollaboratorRepository collaboratorRepository = new CollaboratorRepository();
            collaboratorRepository.update(partner.get().getIdCollaborator(),
                    entity);

            String updateSQL = "UPDATE " + TB_NAME +
                    " SET CNPJ = ?, INDUSTRIA = ? WHERE id = ?";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setString(1, entity.getCnpj());
                pstmt.setString(2, entity.getIndustria());
                pstmt.setInt(3, id);

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
        Optional<Partner> partner = getPartenerById(id);
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