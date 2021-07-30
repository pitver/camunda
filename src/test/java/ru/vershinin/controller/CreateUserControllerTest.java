package ru.vershinin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.vershinin.model.NewUser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yaml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateUserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private CreateUserController createUserController;

    @Test
    public void test() throws Exception {
        assertThat(createUserController).isNotNull();
    }

    /**
     * Создание нового пользователя
     *
     * @throws Exception
     */
    @Order(1)
    @Test
    public void testCreateNewUser() throws Exception {
        NewUser nUser = new NewUser("x", "x", "x", "x", "t@r.ru");

        this.mockMvc.perform(post("/rest/createuser")
                .content(objectMapper.writeValueAsString(nUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("пользователь  с userId " + nUser.getUserId() + " создан")))
                .andDo(print());

    }

    /**
     * Создание нового пользователя, когда его userId совпадает
     * с уже созданный ранее userId дргуго пользователя
     * работает только после выполнения {@link #testCreateNewUser()}.
     *
     * @throws Exception
     */
    @Order(2)
    @Test
    public void testCreatingAlreadyCreatedUser() throws Exception {
        NewUser nUser = new NewUser("x", "x", "x", "x", "t@r.ru");

        this.mockMvc.perform(post("/rest/createuser")
                .content(objectMapper.writeValueAsString(nUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("пользователь  с userId " + nUser.getUserId() + " уже создан")))
                .andDo(print());

    }


}