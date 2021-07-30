package ru.vershinin.service;

import ru.vershinin.model.NewGroup;
import ru.vershinin.model.NewUser;

import java.util.List;

public interface CreateUsersService {
    String createNewUser(NewUser user);
    String createNewGroup(NewGroup nGroup);
    List<String> assignGroup(String[] userId, String groupId);
}
