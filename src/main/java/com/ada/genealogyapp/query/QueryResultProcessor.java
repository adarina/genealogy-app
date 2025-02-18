package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class QueryResultProcessor {
    private final Map<String, QueryResultHandler> handlers;

    public QueryResultProcessor(Map<String, QueryResultHandler> handlers) {
        this.handlers = handlers;
    }

    public void process(String result, Map<String, String> context) {
        QueryResult queryResult = QueryResult.valueOf(result);
        QueryResultHandler handler = handlers.get(queryResult.toString());
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for result: " + queryResult);
        }
        handler.handleResult(context);
    }
}