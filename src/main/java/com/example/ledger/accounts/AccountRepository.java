package com.example.ledger.accounts;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountRepository {
    private static final String COLUMNS = "id, owner_id, holder_name, type, currency, status, allow_negative, posted_balance, held_amount, version, created_at, updated_at";
    private final JdbcClient jdbcClient;

    AccountRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    void insert(UUID id, NewAccount a){
        jdbcClient.sql("""
                INSERT INTO accounts (id, owner_id, holder_name, type, currency, allow_negative) VALUES (:id, :owner_id, :holder_name, :type, :currency, :allow_negative)
                """)
                .param("id", id)
                .param("owner_id", a.ownerId())
                .param("holder_name", a.holderName())
                .param("type", a.type().name())
                .param("currency", a.currency())
                .param("allowNegative", a.allowNegative()).update();
    }

    Optional<Account> findById(UUID id){
        return jdbcClient.sql("SELECT "+ COLUMNS + " FROM accounts WHERE id = :id")
                .param("id", id)
                .query((rs,n) -> map(rs))
                .optional();
    }
    Optional<Account> findByIdForUpdate(UUID id){
        return jdbcClient.sql("SELECT "+ COLUMNS + " FROM accounts WHERE id = :id FOR UPDATE")
                .param("id", id)
                .query((rs,n) -> map(rs))
                .optional();
    }

    void updateStatus(UUID id, AccountStatus target){
        jdbcClient.sql("UPDATE accounts SET status = :status, updated_at = now() WHERE id = :id")
                .param("status",target.name())
                .param("id", id)
                .update();
    }


    private static Account map(ResultSet rs) throws SQLException{
        return new Account(
                rs.getObject("id", UUID.class),
                rs.getObject("owner_id", UUID.class),
                rs.getString("holder_name"),
                AccountType.valueOf(rs.getString("type")),
                rs.getString("currency"),
                AccountStatus.valueOf(rs.getString("status")),
                rs.getBoolean("allow_negative"),
                rs.getLong("posted_balance"),
                rs.getLong("held_amount"),
                rs.getLong("version"),
                rs.getObject("created_at", OffsetDateTime.class).toInstant(),
                rs.getObject("updated_at", OffsetDateTime.class).toInstant()
        );
    }
}
