package ru.vershinin.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vershinin.model.NewGroup;
import ru.vershinin.service.CreateUsersService;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/creategroup")
public class CreateGroupController {

    private final CreateUsersService createUsersService;

    public CreateGroupController(CreateUsersService createUsersService) {
        this.createUsersService = createUsersService;
    }

    @PostMapping
    public String placeStartPOST(@RequestBody @Valid NewGroup nGroup) {

        return createUsersService.createNewGroup(nGroup);
    }


}
