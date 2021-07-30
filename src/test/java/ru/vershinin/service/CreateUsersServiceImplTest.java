package ru.vershinin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.test.ProcessEngineRule;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vershinin.model.NewGroup;
import ru.vershinin.model.NewUser;
import ru.vershinin.util.ProvidedProcessEngineRule;

@RunWith(SpringRunner.class)
public class CreateUsersServiceImplTest {
    public CreateUsersServiceImplTest() {
    }

    @Rule
    public ProcessEngineRule engineRule = new ProvidedProcessEngineRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected IdentityService identityService;


    @Before
    public void init() {
        identityService = engineRule.getIdentityService();
    }

    @After
    public void cleanUp() {
        for (User user : identityService.createUserQuery().list()) {
            identityService.deleteUser(user.getId());
        }
        for (Group group : identityService.createGroupQuery().list()) {
            identityService.deleteGroup(group.getId());
        }
    }


    @Test
    public void testIsReadOnly() {
        assertFalse(identityService.isReadOnly());
    }

    @Test
    public void testUserInfo() {
        User user = identityService.newUser("testuser");
        identityService.saveUser(user);

        identityService.setUserInfo("testuser", "myinfo", "myvalue");
        assertEquals("myvalue", identityService.getUserInfo("testuser", "myinfo"));

        identityService.setUserInfo("testuser", "myinfo", "myvalue2");
        assertEquals("myvalue2", identityService.getUserInfo("testuser", "myinfo"));

        identityService.deleteUserInfo("testuser", "myinfo");
        assertNull(identityService.getUserInfo("testuser", "myinfo"));

        identityService.deleteUser(user.getId());
    }


    @Test
    public void createNewUser() {

        NewUser nUser = new NewUser("x", "x", "x", "x", "t@r.ru");

        User user = identityService.newUser(nUser.getUserId());
        user.setFirstName(nUser.getFirstName());
        user.setLastName(nUser.getLastName());
        user.setPassword(nUser.getPassword());
        user.setEmail(nUser.getEmail());
        identityService.saveUser(user);

        User nUserResult = identityService.createUserQuery().userId("x").singleResult();
        assertEquals("x", nUserResult.getFirstName());
        assertEquals("x", nUserResult.getLastName());
        assertEquals("t@r.ru", user.getEmail());
        identityService.deleteUser(nUserResult.getId());


    }

    @Test
    public void createNewGroup() {

        NewGroup nGroup = new NewGroup("test", "operator", "WORKFLOW");

        Group groupCreate = identityService.newGroup(nGroup.getGroupId());
        groupCreate.setName(nGroup.getName());
        groupCreate.setType(nGroup.getType());
        identityService.saveGroup(groupCreate);

        Group nGroupResult = identityService.createGroupQuery().groupId("test").singleResult();
        assertEquals("operator", nGroupResult.getName());
        assertEquals("WORKFLOW", nGroupResult.getType());

        identityService.deleteGroup(nGroupResult.getId());
    }
}