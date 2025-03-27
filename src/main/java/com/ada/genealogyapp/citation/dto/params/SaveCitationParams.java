package com.ada.genealogyapp.citation.dto.params;

import com.ada.genealogyapp.citation.model.Citation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaveCitationParams extends BaseCitationParams {
    private Citation citation;
}
