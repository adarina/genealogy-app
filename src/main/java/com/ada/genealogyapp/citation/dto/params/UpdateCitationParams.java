package com.ada.genealogyapp.citation.dto.params;

import com.ada.genealogyapp.citation.model.Citation;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateCitationParams extends BaseCitationParams {
    private Citation citation;
}
