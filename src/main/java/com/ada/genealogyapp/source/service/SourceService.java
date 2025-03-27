package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.dto.params.*;

public interface SourceService {

    void saveSource(SaveSourceParams params);

    void deleteSource(DeleteSourceParams params);

    void updateSource(UpdateSourceParams params);
}
