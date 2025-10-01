package com.ada.genealogyapp.file.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileExportResponse {

    private String name;

    private String type;

    private String path;

    private String id;

    private String filename;

}
