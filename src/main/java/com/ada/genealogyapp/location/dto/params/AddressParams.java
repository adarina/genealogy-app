package com.ada.genealogyapp.location.dto.params;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class AddressParams {

    private String city;

    private String state;

    private String country;
}

