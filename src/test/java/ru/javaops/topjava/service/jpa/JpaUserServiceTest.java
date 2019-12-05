package ru.javaops.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javaops.topjava.Profiles;
import ru.javaops.topjava.service.AbstractUserServiceTest;

@ActiveProfiles(Profiles.JPA)
class JpaUserServiceTest extends AbstractUserServiceTest {
}
