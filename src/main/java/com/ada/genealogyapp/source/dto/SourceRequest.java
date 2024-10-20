package com.ada.genealogyapp.source.dto;

import com.ada.genealogyapp.source.model.Source;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class SourceRequest {

    public String name;

    public static Function<SourceRequest, Source> dtoToEntityMapper() {
        return request -> Source.builder()
                .name(request.getName())
                .build();
    }
}