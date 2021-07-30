package ru.vershinin.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vershinin.model.MemberShip;
import ru.vershinin.service.CreateUsersService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CreateMembershipController {

    private final CreateUsersService createUsersService;

    public CreateMembershipController(CreateUsersService createUsersService) {
        this.createUsersService = createUsersService;
    }

    @PostMapping("/rest/createmembership")
    public List<String> createMembership(@RequestBody @Valid MemberShip memberShip){

        return  createUsersService.assignGroup(memberShip.getUsersId(), memberShip.getGroupId());
    }

}
