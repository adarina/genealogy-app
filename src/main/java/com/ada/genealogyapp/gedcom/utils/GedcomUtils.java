package com.ada.genealogyapp.gedcom.utils;

import com.ada.genealogyapp.gedcom.dto.Extensions;
import com.ada.genealogyapp.gedcom.dto.MoreTag;
import com.ada.genealogyapp.gedcom.type.LocationGedcomType;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.folg.gedcom.model.Gedcom;
import org.folg.gedcom.parser.JsonParser;
import org.folg.gedcom.parser.ModelParser;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GedcomUtils {
    public static String convertGedcomToJson(InputStream gedcomInputStream) throws SAXParseException, IOException {
        ModelParser modelParser = new ModelParser();
        Gedcom gedcom = modelParser.parseGedcom(gedcomInputStream);
        JsonParser jsonParser = new JsonParser();
        return jsonParser.toJson(gedcom);
    }

    public static Double parseCoordinateFromExtensions(Extensions extensions, String tag) {
        if (isNull(extensions) || isNull(extensions.getMoreTags())) return null;
        for (MoreTag moreTag : extensions.getMoreTags()) {
            if (LocationGedcomType.MAP.name().equals(moreTag.getTag()) && nonNull(moreTag.getChildren())) {
                for (MoreTag child : moreTag.getChildren()) {
                    if (tag.equals(child.getTag())) {
                        return parseCoordinates(child.getValue());
                    }
                }
            }
        }
        return null;
    }

    private static Double parseCoordinates(String value) {
        if (isNull(value) || value.length() < 2) {
            return null;
        }
        return Try.of(() -> {
                    char prefix = value.charAt(0);
                    double coordinate = Double.parseDouble(value.substring(1));
                    return (prefix == 'S' || prefix == 'W') ? -coordinate : coordinate;
                }).onFailure(e -> log.error("Error parsing coordinate value: {}", value, e))
                .getOrNull();
    }
}
