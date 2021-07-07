package com.inminhouse.alone.auth.model.enumeration;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

public class GenderConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        return gender.getValue();
    }

    @Override
    public Gender convertToEntityAttribute(Integer integer) {
        return Stream.of(Gender.values())
                .filter(c -> c.getValue() == integer)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}