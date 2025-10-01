package com.ada.genealogyapp.location.dto.params;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetLocationsParams extends BaseParams {

    private String filter;
}
