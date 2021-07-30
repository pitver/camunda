package ru.vershinin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.vershinin.model.NewGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yaml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CreateGroupControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private CreateGroupController createGroupController;

    @Test
    public void test() throws Exception {
        assertThat(createGroupController).isNotNull();
    }

    /**
     * Создание новой группы
     *
     * @throws Exception
     */
    @Order(1)
    @Test
    public void testCreateNewGroup() throws Exception {
        NewGroup nGroup = new NewGroup("test", "operator", "WORKFLOW");

        this.mockMvc.perform(post("/rest/creategroup")
                .content(objectMapper.writeValueAsString(nGroup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("группа  с groupId " + nGroup.getGroupId() + " создана")))
                .andDo(print());

    }

    /**
     * Создание новой группы, когда его groupId совпадает
     * с уже созданный ранее groupId дргугой группы
     * работает только после выполнения {@link #testCreateNewGroup()} ()}.
     *
     * @throws Exception
     */
    @Order(2)
    @Test
    public void testCreatingAlreadyCreatedGroup() throws Exception {

        NewGroup nGroup = new NewGroup("test", "operator", "WORKFLOW");

        this.mockMvc.perform(post("/rest/creategroup")
                .content(objectMapper.writeValueAsString(nGroup))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("группа  с groupId " + nGroup.getGroupId() + " уже создана")))
                .andDo(print());

    }


}