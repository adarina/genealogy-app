package com.ada.genealogyapp.event.dto.params;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseEventParams {
    private String userId;
    private String treeId;
    private String eventId;
}