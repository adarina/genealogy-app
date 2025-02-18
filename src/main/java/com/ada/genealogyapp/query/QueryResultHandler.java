package com.ada.genealogyapp.query;

import java.util.Map;

public interface QueryResultHandler {
    void handleResult(Map<String, String> context);
}
