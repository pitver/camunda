package ru.vershinin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInstanceInfo {

    String businessKey;
    String processInstanceId;


}
