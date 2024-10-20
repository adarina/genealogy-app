package com.ada.genealogyapp.citation.dto;

import com.ada.genealogyapp.citation.model.Citation;
import lombok.*;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CitationRequest {

    public String page;

    public LocalDate creationDate;

    public static Function<CitationRequest, Citation> dtoToEntityMapper() {
        return request -> Citation.builder()
                .page(request.getPage())
                .creationDate(request.getCreationDate())
                .build();
    }
}
