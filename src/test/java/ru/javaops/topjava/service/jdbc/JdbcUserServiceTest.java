package ru.javaops.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javaops.topjava.Profiles;
import ru.javaops.topjava.service.AbstractUserServiceTest;

@ActiveProfiles(Profiles.JDBC)
class JdbcUserServiceTest extends AbstractUserServiceTest {
}
