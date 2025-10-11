package io.github.skeffy.flagguesser.dao;

import io.github.skeffy.flagguesser.exception.DaoException;
import io.github.skeffy.flagguesser.model.Flag;
import javax.sql.DataSource;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcFlagDao implements FlagDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcFlagDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Flag getFlag(int id) {
        Flag flag = null;
        String sql = "SELECT * FROM country WHERE country_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                flag = this.mapRowToFlag(results);
            }

            return flag;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }

    public int dbLength() {
        String sql = "SELECT country_id FROM country;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            results.last();
            return results.getRow();
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }

    public List<String> getList() {
        List<String> namesList = new ArrayList<>();
        String sql = "SELECT name FROM country;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                namesList.add(results.getString("name"));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return namesList;
    }

    private Flag mapRowToFlag(SqlRowSet results) {
        Flag flag = new Flag();
        flag.setId(results.getInt("country_id"));
        flag.setName(results.getString("name"));
        flag.setCode(results.getString("code"));
        flag.setCapital(results.getString("capital"));
        flag.setContinent(results.getString("continent"));
        flag.setFlagImage(results.getString("flag_image"));
        return flag;
    }
}