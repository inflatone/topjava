package ru.javaops.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.javaops.topjava.web.AbstractControllerTest;
import ru.javaops.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava.UserTestData.ADMIN;
import static ru.javaops.topjava.UserTestData.ADMIN_ID;

class AdminRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Test
    void getAdmin() throws Exception {
        mockMvc.perform(get(REST_URL + ADMIN_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeIgnoreProperties(ADMIN, "registered")))
        ;
    }
}
