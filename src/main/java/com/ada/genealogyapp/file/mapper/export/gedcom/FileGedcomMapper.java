package com.ada.genealogyapp.file.mapper.export.gedcom;

import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.export.gedcom.GedcomMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FileGedcomMapper extends GedcomMapper<File> {

    @Override
    public Class<File> getEntityType() {
        return File.class;
    }

    @Override
    public String generateGedcomContent(File file, String gedcomId, Map<String, String> personGedcomIds) {
        StringBuilder builder = new StringBuilder();

        builder.append("0 ").append(gedcomId).append(" OBJE\n");
        builder.append("1 FORM ").append(file.getType()).append("\n");
        builder.append("1 TITL ").append(file.getName()).append("\n");
        builder.append("1 FILE ").append(file.getPath()).append("\n");

        return builder.toString();
    }

    @Override
    public String getEntityId(File file) {
        return file.getId();
    }

    @Override
    public String getEntityTypePrefix() {
        return "M";
    }
}

