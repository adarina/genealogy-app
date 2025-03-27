package com.ada.genealogyapp.query;

import java.util.Map;

public interface QueryResultHandler {
    void handleResult(Map<IdType, String> context);
}
