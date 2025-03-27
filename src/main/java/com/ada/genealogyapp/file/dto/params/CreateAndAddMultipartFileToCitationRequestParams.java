package com.ada.genealogyapp.file.dto.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateAndAddMultipartFileToCitationRequestParams extends CreateMultipartFileRequestParams {
    private String citationId;
}