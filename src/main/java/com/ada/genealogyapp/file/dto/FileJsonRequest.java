package com.ada.genealogyapp.file.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FileJsonRequest extends FileRequest {

    private String id;

    private String filename;
}
