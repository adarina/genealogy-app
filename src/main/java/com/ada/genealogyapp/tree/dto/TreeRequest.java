package com.ada.genealogyapp.tree.dto;


import lombok.*;



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




