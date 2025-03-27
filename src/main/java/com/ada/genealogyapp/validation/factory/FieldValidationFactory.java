package com.ada.genealogyapp.validation.factory;

import java.util.List;

public interface FieldValidationFactory<T, T1> {

    List<T1> getValidators(T fieldType);
}
