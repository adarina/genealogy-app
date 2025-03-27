package com.ada.genealogyapp.citation.dto.params;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateAndAddCitationToEventRequestParams extends CreateCitationRequestParams {
    private String eventId;
}