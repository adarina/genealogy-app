package com.ada.genealogyapp.citation.model;

import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.nonNull;


@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Citation {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String page;

    private LocalDate date;

    @Relationship(type = "HAS_CITATION", direction = Relationship.Direction.INCOMING)
    private Tree tree;

    @Relationship(type = "HAS_CITATION_SOURCE", direction = Relationship.Direction.OUTGOING)
    private Source source;

    @Relationship(type = "HAS_CITATION_FILE", direction = Relationship.Direction.OUTGOING)
    private Set<File> files = new HashSet<>();

    public boolean hasSource(Source source) {
        return nonNull(this.source) && this.source.equals(source);
    }

    public void addSource(Source source) {
        this.source = source;
    }

    public void removeSource() {
        this.source = null;
    }

    public boolean hasFile(File file) {
        return files.contains(file);
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void removeFile(File file) {
        files.remove(file);
    }

}
