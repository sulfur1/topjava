package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /*@Transactional
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        return user;
    }*/

    @Transactional
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);/* {
            @Override
            public Object getValue(String paramName) throws IllegalArgumentException {
                Object value = super.getValue(paramName);
                if (value instanceof Enum) {
                    return value.toString();
                }
                return value;
            }
        };*/
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            batchInsertRole(user, false);
        } else {
            namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource);
            batchInsertRole(user, true);
        }
        return user;

    }

    private int batchInsertRole(User user, boolean isUpdate) {
        List<Role> roles = new ArrayList<>(user.getRoles());

        if (isUpdate) {
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.id());
        }

            String sql = """
                        INSERT INTO user_role (user_id,role) VALUES (?,?)
                    """;

            return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, user.id());
                    ps.setString(2, roles.get(i).name());
                }

                @Override
                public int getBatchSize() {
                    return roles.size();
                }
            }).length;

    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        Set<Role> roles = new HashSet<>(jdbcTemplate.query("SELECT * FROM user_role WHERE user_id=?", new RowMapper<Role>() {

            @Override
            public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Enum.valueOf(Role.class, rs.getString(2));
            }
        }, id));
        if (!roles.isEmpty()) {
            users.get(0).setRoles(roles);
        }
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        if (!users.isEmpty()) {
            Set<Role> roles = new HashSet<>(jdbcTemplate.query("SELECT * FROM user_role WHERE user_id=?", new RowMapper<Role>() {

                @Override
                public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return Enum.valueOf(Role.class, rs.getString(2));
                }
            }, users.get(0).id()));

            if (!roles.isEmpty()) {
                users.get(0).setRoles(roles);
            }
        }
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                                    SELECT id, name, email, password, registered, enabled, calories_per_day, role FROM users u LEFT OUTER JOIN user_role ur on u.id = ur.user_id
                                    """, (ResultSetExtractor<List<User>>) rs -> {
                                        Map<Integer, User> userMap = new HashMap<>();

                                        while (rs.next()) {
                                            User user = new User();
                                            user.setId(rs.getInt(1));
                                            user.setName(rs.getString(2));
                                            user.setEmail(rs.getString(3));
                                            user.setPassword(rs.getString(4));
                                            user.setRegistered(rs.getDate(5));
                                            user.setEnabled(rs.getBoolean(6));
                                            user.setCaloriesPerDay(rs.getInt(7));
                                            String role = rs.getString(8);

                                            Role r = role != null
                                                    ? Role.USER.name().equalsIgnoreCase(role)
                                                    ? Role.USER : Role.ADMIN
                                                    : null;

                                            User u = userMap.get(user.getId());
                                            if (r != null) {
                                                if (u != null) {
                                                    u.getRoles().add(r);
                                                } else {
                                                    Set<Role> newRoleSet = new HashSet<>();
                                                    newRoleSet.add(r);
                                                    user.setRoles(newRoleSet);
                                                    userMap.put(user.getId(), user);
                                                }
                                            } else {
                                                user.setRoles(Collections.emptySet());
                                                userMap.put(user.getId(), user);
                                            }
                                        }
                                        return new ArrayList<>(userMap.values());
                                    });
    }
}
