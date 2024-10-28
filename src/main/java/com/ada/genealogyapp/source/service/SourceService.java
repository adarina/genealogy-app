package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.model.Source;

import java.util.UUID;

public interface SourceService {

    Source findSourceByIdOrThrowNodeNotFoundException(UUID sourceId);
}
