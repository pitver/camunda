package ru.vershinin.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vershinin.model.NewUser;
import ru.vershinin.service.CreateUsersService;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/createuser")
public class CreateUserController {

    private final CreateUsersService createUsersService;

    public CreateUserController(CreateUsersService createUsersService) {
        this.createUsersService = createUsersService;
    }


    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid NewUser nUser) {

        return ResponseEntity.status(HttpStatus.OK).body(createUsersService.createNewUser(nUser));
    }


}
