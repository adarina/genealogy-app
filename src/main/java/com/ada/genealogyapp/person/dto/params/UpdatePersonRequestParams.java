package com.ada.genealogyapp.person.dto.params;

import com.ada.genealogyapp.person.dto.PersonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdatePersonRequestParams extends BasePersonParams {
    private PersonRequest personRequest;
}