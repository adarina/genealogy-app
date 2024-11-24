package com.ada.genealogyapp.citation.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CitationRequest {

    public UUID id;

    public String page;

    public LocalDate date;
}
