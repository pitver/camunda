package ru.vershinin.service;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.IdentityServiceImpl;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vershinin.model.NewGroup;
import ru.vershinin.model.NewUser;
import ru.vershinin.util.GetCurrentProcessEngine;
import ru.vershinin.util.GetIdentityService;

import java.util.*;
import java.util.logging.Logger;

@Service
public class CreateUsersServiceImpl implements CreateUsersService {
    @Autowired
    private GetCurrentProcessEngine currentProcessEngine;
    @Autowired
    private GetIdentityService getIdentityService;

    private static final Logger LOGGER = Logger.getLogger(CreateUsersServiceImpl.class.getName());
    private static final String TEXT_READ_ONLY = "Identity service provider is Read Only, not creating any users.";


    public CreateUsersServiceImpl() {
    }


    @Override
    public String createNewUser(NewUser nUser) {
        IdentityServiceImpl identityService = getIdentityService.getIdentityService();
        if (identityService.isReadOnly()) {
            LOGGER.info(TEXT_READ_ONLY);
            return TEXT_READ_ONLY;
        } else {
            User singleResult = identityService.createUserQuery().userId(nUser.getUserId()).singleResult();
            if (singleResult == null) {
                LOGGER.info("Generating data for " + nUser.getUserId());
                User user = identityService.newUser(nUser.getUserId());
                user.setFirstName(nUser.getFirstName());
                user.setLastName(nUser.getLastName());
                user.setPassword(nUser.getPassword());
                user.setEmail(nUser.getEmail());
                identityService.saveUser(user, true);
                return "пользователь  с userId " + nUser.getUserId() + " создан";
            } else {
                return "пользователь  с userId " + nUser.getUserId() + " уже создан";
            }
        }

    }

    @Override
    public String createNewGroup(NewGroup nGroup) {
        IdentityServiceImpl identityService = getIdentityService.getIdentityService();
        if (identityService.isReadOnly()) {
            LOGGER.info(TEXT_READ_ONLY);
            return TEXT_READ_ONLY;
        } else {
            Group singleResult = identityService.createGroupQuery().groupId(nGroup.getGroupId()).singleResult();
            if (singleResult == null) {
                Group groupCreate = identityService.newGroup(nGroup.getGroupId());
                groupCreate.setName(nGroup.getName());
                groupCreate.setType(nGroup.getType());
                identityService.saveGroup(groupCreate);
                return "группа  с groupId " + nGroup.getGroupId() + " создана";
            } else {
                return "группа  с groupId " + nGroup.getGroupId() + " уже создана";
            }
        }
    }

    @Override
    public List<String> assignGroup(String[] userId, String groupId) {
        IdentityServiceImpl identityService =getIdentityService.getIdentityService();
        List<String> result = new ArrayList<>();
        if (identityService.isReadOnly()) {
            LOGGER.info(TEXT_READ_ONLY);
            return Collections.singletonList(TEXT_READ_ONLY);
        } else {

            for (String userName : userId) {
                User singleUserResult = identityService.createUserQuery().userId(userName).singleResult();
                Group singleGroupResult = identityService.createGroupQuery().groupId(groupId).singleResult();
                if (singleGroupResult != null && singleUserResult != null) {
                    identityService.createMembership(userName, groupId);
                    result.add("пользователю " + userName + " назначена группа " + groupId);
                } else {
                    result.add("пользователь " + userName + " или группа " + groupId + " не найдены");
                }
            }
        }
        return result;
    }


    public void create() {

        ProcessEngine processEngine = currentProcessEngine.getDefaultProcessEngine();
        IdentityServiceImpl identityService = (IdentityServiceImpl) processEngine.getIdentityService();
        if (identityService.isReadOnly()) {
            LOGGER.info(TEXT_READ_ONLY);
        } else {


            /* identityService.createMembership("test", "testing");*/

            AuthorizationService authorizationService = processEngine.getAuthorizationService();
            //////предоставление разрешения на доступ к определенному ресурсу созданному объекту авторизации
            //////APPLICATION////////////
            Authorization testingTasklistAuth = authorizationService.createNewAuthorization(1);
            testingTasklistAuth.setGroupId("testing");
            testingTasklistAuth.addPermission(Permissions.ACCESS);
            testingTasklistAuth.setResourceId("tasklist");
            testingTasklistAuth.setResource(Resources.APPLICATION);
            authorizationService.saveAuthorization(testingTasklistAuth);
            ///////PROCESS_DEFINITION - READ,UPDATE,CREATE,DELETE,READ_TASK,UPDATE_TASK,CREATE_INSTANCE,
            // READ_INSTANCE,UPDATE_INSTANCE,DELETE_INSTANCE,READ_HISTORY ,DELETE_HISTORY,TASK_WORK,TASK_ASSIGN,MIGRATE_INSTANCE///
            Authorization testingReadProcessDefinition = authorizationService.createNewAuthorization(1);
            testingReadProcessDefinition.setGroupId("testing");
            testingReadProcessDefinition.addPermission(Permissions.READ);
            testingReadProcessDefinition.addPermission(Permissions.UPDATE);
            testingReadProcessDefinition.addPermission(Permissions.UPDATE_TASK);
            testingReadProcessDefinition.addPermission(Permissions.READ_HISTORY);
            testingReadProcessDefinition.setResource(Resources.PROCESS_DEFINITION);
            testingReadProcessDefinition.setResourceId("Sample");
            authorizationService.saveAuthorization(testingReadProcessDefinition);
            ////////TASK - READ, UPDATE, CREATE, DELETE ,TASK_ASSIGN, TASK_WORK, READ_HISTORY////////////////////////
            Authorization testingTestAuth = authorizationService.createNewAuthorization(1);
            testingTestAuth.setGroupId("testing");
            testingTestAuth.addPermission(Permissions.READ);
            testingTestAuth.addPermission(Permissions.UPDATE);
            testingTestAuth.setResource(Resources.TASK);
            testingTestAuth.setResourceId("test");
            authorizationService.saveAuthorization(testingTestAuth);

            //////Filter - READ, UPDATE, CREATE, DELETE /////
            FilterService filterService = processEngine.getFilterService();
            Map<String, Object> filterProperties = new <String, Object> HashMap();
            TaskService taskService = processEngine.getTaskService();

            filterProperties.put("description", "Tasks assigned to me");
            filterProperties.put("priority", -10);

            TaskQuery query = taskService.createTaskQuery().taskCandidateUserExpression("${currentUser()}");
            Filter myTasksFilter = filterService.newTaskFilter().setName("My Tasks").setProperties(filterProperties).setOwner("demo").setQuery(query);
            filterService.saveFilter(myTasksFilter);
            filterProperties.clear();

            filterProperties.put("description", "Tasks assigned to my Groups");
            filterProperties.put("priority", -10);
            /*TaskQuery */
            query = taskService.createTaskQuery().taskCandidateGroupInExpression("${currentUserGroups()}").taskUnassigned();
            Filter groupTasksFilter = filterService.newTaskFilter().setName("My Group Tasks").setProperties(filterProperties).setOwner("demo").setQuery(query);
            filterService.saveFilter(groupTasksFilter);

            Authorization globalMyTaskFilterRead = authorizationService.createNewAuthorization(0);
            globalMyTaskFilterRead.setResource(Resources.FILTER);
            globalMyTaskFilterRead.setResourceId(myTasksFilter.getId());
            globalMyTaskFilterRead.addPermission(Permissions.READ);
            globalMyTaskFilterRead.addPermission(Permissions.UPDATE);
            authorizationService.saveAuthorization(globalMyTaskFilterRead);

            Authorization globalGroupFilterRead = authorizationService.createNewAuthorization(0);
            globalGroupFilterRead.setResource(Resources.FILTER);
            globalGroupFilterRead.setResourceId(groupTasksFilter.getId());
            globalGroupFilterRead.addPermission(Permissions.READ);
            globalGroupFilterRead.addPermission(Permissions.UPDATE);
            authorizationService.saveAuthorization(globalGroupFilterRead);
            filterProperties.clear();
            filterProperties.put("description", "Tasks for Group Testing");
            filterProperties.put("priority", -3);
            query = taskService.createTaskQuery().taskCandidateGroupIn(Arrays.asList("testing")).taskUnassigned();

            Filter candidateGroupTasksFilter = filterService.newTaskFilter().setName("Testing").setProperties(filterProperties).setOwner("demo").setQuery(query);
            filterService.saveFilter(candidateGroupTasksFilter);
            Authorization managementGroupFilterRead = authorizationService.createNewAuthorization(1);
            managementGroupFilterRead.setResource(Resources.FILTER);
            managementGroupFilterRead.setResourceId(candidateGroupTasksFilter.getId());
            managementGroupFilterRead.addPermission(Permissions.READ);
            managementGroupFilterRead.addPermission(Permissions.UPDATE);
            managementGroupFilterRead.setGroupId("testing");
            authorizationService.saveAuthorization(managementGroupFilterRead);
            filterProperties.clear();
            filterProperties.put("description", "Tasks assigned to Test");
            filterProperties.put("priority", -1);
            query = taskService.createTaskQuery().taskAssignee("test");

            Filter testsTasksFilter = filterService.newTaskFilter().setName("Test's Tasks").setProperties(filterProperties).setOwner("demo").setQuery(query);
            filterService.saveFilter(testsTasksFilter);
            filterProperties.clear();

            filterProperties.put("description", "All Tasks - Not recommended to be used in production :)");
            filterProperties.put("priority", 10);
            query = taskService.createTaskQuery();
            Filter allTasksFilter = filterService.newTaskFilter().setName("All Tasks").setProperties(filterProperties).setOwner("demo").setQuery(query);
            filterService.saveFilter(allTasksFilter);


            // }


        }
    }





}
