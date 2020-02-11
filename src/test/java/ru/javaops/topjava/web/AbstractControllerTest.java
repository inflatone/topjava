package ru.javaops.topjava.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.javaops.topjava.AllActiveProfileResolver;
import ru.javaops.topjava.TimingExtension;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.util.exeption.ErrorType;
import ru.javaops.topjava.web.json.JsonUtil;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.javaops.topjava.web.AbstractControllerTest.RequestWrapper.wrap;

@Transactional
@ActiveProfiles(resolver = AllActiveProfileResolver.class)
@WebAppConfiguration
@ExtendWith(TimingExtension.class)
@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
public abstract class AbstractControllerTest {
    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding(UTF_8.name());
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    private MockMvc mockMvc;

    private final String url;

    public AbstractControllerTest(String url) {
        this.url = url + '/';
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected MessageUtil messageUtil;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    public ResultActions perform(RequestWrapper wrapper) throws Exception {
        return perform(wrapper.builder);
    }

    public ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected RequestWrapper doGet(String urlTemplatePad, Object... uriVars) {
        return wrap(get(url + urlTemplatePad, uriVars));
    }

    protected RequestWrapper doGet() {
        return wrap(get(url));
    }

    protected RequestWrapper doGet(String urlTemplatePad) {
        return wrap(get(url + urlTemplatePad));
    }

    protected RequestWrapper doGet(int id) {
        return doGet("{id}", id);
    }

    protected RequestWrapper doDelete() {
        return wrap(delete(url));
    }

    protected RequestWrapper doDelete(int id) {
        return wrap(delete(url + "{id}", id));
    }

    protected RequestWrapper doPut() {
        return wrap(put(url));
    }

    protected RequestWrapper doPut(int id) {
        return wrap(put(url + "{id}", id));
    }

    protected RequestWrapper doPost(String pad) {
        return wrap(MockMvcRequestBuilders.post(url + pad));
    }

    protected RequestWrapper doPost() {
        return wrap(post(url));
    }

    protected RequestWrapper doPatch(int id) {
        return wrap(patch(url + "{id}", id));
    }

    public static class RequestWrapper {
        private final MockHttpServletRequestBuilder builder;

        private RequestWrapper(MockHttpServletRequestBuilder builder) {
            this.builder = builder;
        }

        public static RequestWrapper wrap(MockHttpServletRequestBuilder builder) {
            return new RequestWrapper(builder);
        }

        public MockHttpServletRequestBuilder unwrap() {
            return builder;
        }

        public <T> RequestWrapper jsonBody(T body) {
            builder.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(body));
            return this;
        }

        public RequestWrapper jsonUserWithPassword(User user) {
            builder.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeWithAdditionalProperty(user, "password", user.getPassword()));
            return this;
        }

        public RequestWrapper basicAuth(User user) {
            builder.with(httpBasic(user.getEmail(), user.getPassword()));
            return this;
        }

        public RequestWrapper auth(User user) {
            builder.with(authentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())));
            return this;
        }
    }

    public ResultMatcher errorType(ErrorType type) {
        return jsonPath("$.type").value(type.name());
    }

    public ResultMatcher detailMessage(String code, String... params) {
        var args = Arrays.stream(params).map(arg -> getMessage(arg)).toArray(String[]::new);
        return jsonPath("$.details").value(getMessage(code, args));
    }

    private String getMessage(String code, String... args) {
        return messageUtil.getMessage(code, MessageUtil.RU_LOCALE, args);
    }
}
