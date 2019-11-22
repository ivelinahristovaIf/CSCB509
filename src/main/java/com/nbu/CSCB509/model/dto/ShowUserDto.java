package com.nbu.CSCB509.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShowUserDto {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String imageUrl;

}
