package com.ada.genealogyapp.event.dto.params;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.event.dto.EventRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateEventRequestParams extends BaseParams {
    private EventRequest eventRequest;
}