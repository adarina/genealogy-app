package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.person.type.GenderType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreatedResponse {

    private String firstname;

    private String lastname;

    private GenderType gender;

    private LocalDateTime createTime;
}
