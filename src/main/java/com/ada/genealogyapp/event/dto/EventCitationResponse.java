package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.source.model.Source;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventCitationResponse {

    private String id;

    private String page;

    private String date;

    private String name;

    private Set<File> files = new HashSet<>();

    private Source source;
}
