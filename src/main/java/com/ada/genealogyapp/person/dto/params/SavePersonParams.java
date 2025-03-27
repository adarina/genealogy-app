package com.ada.genealogyapp.person.dto.params;

import com.ada.genealogyapp.person.model.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SavePersonParams extends BasePersonParams {
    private Person person;
}