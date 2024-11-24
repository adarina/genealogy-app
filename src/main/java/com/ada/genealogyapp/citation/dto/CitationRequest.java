package com.ada.genealogyapp.citation.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CitationRequest {

    public String page;

    public LocalDate date;
}
