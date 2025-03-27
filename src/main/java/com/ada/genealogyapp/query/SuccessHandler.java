package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SUCCESS")
public class SuccessHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
    }
}