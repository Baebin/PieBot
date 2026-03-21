package com.piebin.piebot.model.converter;

import jakarta.persistence.AttributeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {
    private String DELIMITER = ", ";

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        if (attribute == null || attribute.isEmpty())
            return "";
        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.equals(""))
            return new ArrayList<>();
        return Arrays.stream(dbData.split(DELIMITER))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
