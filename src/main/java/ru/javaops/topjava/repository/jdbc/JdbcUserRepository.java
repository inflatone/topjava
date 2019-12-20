package ru.javaops.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.jdbc.core.OneToManyResultSetExtractor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.javaops.topjava.model.Role;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.repository.UserRepository;
import ru.javaops.topjava.util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private static final ResultSetExtractor<List<User>> USERS_EXTRACTOR = new OneToManyResultSetExtractor<User, Role, Integer>(
            BeanPropertyRowMapper.newInstance(User.class),
            (rs, rowNum) -> Role.valueOf(rs.getString("role"))
    ) {
        @Override
        protected Integer mapPrimaryKey(ResultSet rs) throws SQLException {
            return rs.getInt("id");
        }

        @Override
        protected Integer mapForeignKey(ResultSet rs) throws SQLException {
            return rs.getInt("user_id");
        }

        @Override
        protected void addChild(User user, Role role) {
            if (user.getRoles() == null) {
                user.setRoles(null);
            }
            user.getRoles().add(role);
        }
    };

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
        ValidationUtil.validate(user);

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
        return DataAccessUtils.singleResult(
                jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles r ON users.id = r.user_id WHERE id=?",
                        USERS_EXTRACTOR, id));
    }

    @Override
    public User getByEmail(String email) {
        return DataAccessUtils.singleResult(
                jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles r ON users.id = r.user_id WHERE email=?",
                        USERS_EXTRACTOR, email));
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles r ON users.id = r.user_id ORDER BY name, email", USERS_EXTRACTOR);
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