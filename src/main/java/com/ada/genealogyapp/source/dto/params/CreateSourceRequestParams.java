package com.ada.genealogyapp.source.dto.params;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.source.dto.SourceRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateSourceRequestParams extends BaseParams {
    private SourceRequest sourceRequest;
}