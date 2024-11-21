package com.ada.genealogyapp.tree.dto;

import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;

import java.util.function.Function;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class TreeRequest {

    private String name;

    private Long userId;

}




