package com.pluralsight.conference.repository;

import com.pluralsight.conference.model.Account;
import com.pluralsight.conference.model.VerificationToken;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private DataSource dataSource;

    @Override
    public Account create(Account account) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("INSERT INTO accounts (username, password, email, firstname, lastname) VALUES (?,?,?,?,?)",
                account.getUsername(),
                account.getPassword(),
                account.getEmail(),
                account.getFirstName(),
                account.getLastName());

        return account;
    }

    @Override
    public void saveToken(VerificationToken verificationToken) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("INSERT INTO verification_tokens (username , token, expiry_date) VALUES " +
                        "(?,?,?)", verificationToken.getUsername(),
                verificationToken.getToken(),
                verificationToken.calculateExpiryDate(verificationToken.EXPIRATION));

    }

//    @Override
    public VerificationToken findByToken(String token) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        VerificationToken verificationToken =
                template.queryForObject("select username, token, expiry_date from verification_tokens where token = ?",
                        new RowMapper<VerificationToken>() {
                            @Override
                            public VerificationToken mapRow(ResultSet resultSet, int i) throws SQLException {
                                VerificationToken rsToken = new VerificationToken();
                                rsToken.setUsername(resultSet.getString("username"));
                                rsToken.setToken(resultSet.getString("token"));
                                rsToken.setExpiryDate(resultSet.getTimestamp("expiry_date"));
                                return rsToken;
                            }
                        },
                        token);
        return verificationToken;
    }
}
