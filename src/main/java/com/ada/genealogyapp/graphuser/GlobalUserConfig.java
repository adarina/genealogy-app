package com.ada.genealogyapp.graphuser;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GlobalUserConfig {

    @Value("${app.user.id}")
    private String userId;

}
