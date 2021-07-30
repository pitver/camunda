package ru.vershinin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {

    String userId;
    String firstName;
    String lastName;
    String password;
    String email;

}
