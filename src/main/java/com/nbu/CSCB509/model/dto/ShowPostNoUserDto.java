package com.nbu.CSCB509.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowPostNoUserDto {

    private Long id;

    private String title;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date endDate;

    private Double donates;

    private ShowUserDto user;

}
