package ru.javaops.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert userInsert;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        userInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        var parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = userInsert.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user.getRoles(), user.getId());
        } else {
            if (namedParameterJdbcTemplate.update(
                    "  UPDATE users SET " +
                            "            name=:name, " +
                            "           email=:email, " +
                            "        password=:password, " +
                            "      registered=:registered, " +
                            "         enabled=:enabled, " +
                            "calories_per_day=:caloriesPerDay " +
                            "        WHERE id=:id",
                    parameterSource) == 0
            ) {
                return null;
            }
            updateRoles(user);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles r ON users.id = r.user_id WHERE id=?",
                this::extractUser, id);
    }

    @Override
    public User getByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles r ON users.id = r.user_id WHERE email=?",
                this::extractUser, email);
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        var users = new ArrayList<User>();
        var roles = new ArrayList<Role>();
        int rowNum = 0;
        while (resultSet.next()) {
            users.add(ROW_MAPPER.mapRow(resultSet, rowNum++));
            roles.add(Role.valueOf(resultSet.getString("role")));
        }
        var result = DataAccessUtils.singleResult(users.stream().distinct().collect(Collectors.toList()));
        if (result != null) {
            result.setRoles(roles);
        }
        return result;
    }

    @Override
    public List<User> getAll() {
        var roleMap = new HashMap<Integer, Set<Role>>();
        jdbcTemplate.query("SELECT * FROM user_roles", rs -> {
            roleMap.computeIfAbsent(
                    rs.getInt("user_id"), userId -> EnumSet.noneOf(Role.class)
            ).add(Role.valueOf(rs.getString("role")));
        });
        var users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        users.forEach(u -> u.setRoles(roleMap.get(u.getId())));
        return users;
    }

    // gets user roles from DB and compare them with user.roles (assume that roles are changed rarely)
    // If roles are changed, calculate difference in java and delete/insert them
    private void updateRoles(User user) {
        var persisted = EnumSet.noneOf(Role.class);
        jdbcTemplate.query("SELECT * FROM user_roles r WHERE r.user_id=?", rs -> {
            persisted.add(Role.valueOf(rs.getString("role")));
        }, user.getId());
        checkRoles(user.getId(), persisted, user.getRoles());
    }

    private void checkRoles(int userId, Set<Role> persisted, Set<Role> actual) {
        Set<Role> deleted = new HashSet<>();
        Set<Role> added = new HashSet<>(actual);
        persisted.forEach(r -> {
            if (!added.remove(r)) {
                deleted.add(r);
            }
        });
        removeRoles(deleted, userId);
        insertRoles(added, userId);
    }

    private void removeRoles(Set<Role> roles, int userId) {
        jdbcTemplate.update("DELETE FROM user_roles r WHERE r.user_id=?", userId);
    }

    private void insertRoles(Set<Role> roles, int userId) {
        if (!CollectionUtils.isEmpty(roles)) {
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", roles, roles.size(),
                    (ps, role) -> {
                        ps.setInt(1, userId);
                        ps.setString(2, role.name());
                    });
        }
    }
}