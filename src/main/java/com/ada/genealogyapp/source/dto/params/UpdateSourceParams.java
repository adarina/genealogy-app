package com.ada.genealogyapp.source.dto.params;


import com.ada.genealogyapp.source.model.Source;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateSourceParams extends BaseSourceParams {
    private Source source;
}