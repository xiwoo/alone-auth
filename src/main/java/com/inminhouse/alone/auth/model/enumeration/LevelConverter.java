package com.inminhouse.alone.auth.model.enumeration;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

public class LevelConverter implements AttributeConverter<Level, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Level level) {
        return level.getValue();
    }

    @Override
    public Level convertToEntityAttribute(Integer integer) {
        return Stream.of(Level.values())
                .filter(c -> c.getValue() == integer)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}