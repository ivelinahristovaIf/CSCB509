package com.nbu.CSCB509.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
public class ShowCommentDto {

    private Long id;

    private String comment;

    private Long postId;

    private ShowUserDto user;

}
