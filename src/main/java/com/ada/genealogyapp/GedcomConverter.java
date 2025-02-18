package com.ada.genealogyapp;

import org.folg.gedcom.parser.JsonParser;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.ModelParser;


public class GedcomConverter {
    public void convertGedcomToJson(String gedcomFilePath, String jsonOutputPath) throws SAXParseException, IOException {

            ModelParser modelParser = new ModelParser();
            Gedcom gedcom = modelParser.parseGedcom(new File(gedcomFilePath));
            JsonParser jsonParser = new JsonParser();
            String json = jsonParser.toJson(gedcom);
            Files.writeString(Path.of(jsonOutputPath), json);


    }
}
