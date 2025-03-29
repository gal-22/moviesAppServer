package com.moviesapp.moviesapp.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

@Converter
public class GenreListConverter implements AttributeConverter<List<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        return new JSONArray(attribute).toString();
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty() || "[]".equals(dbData)) {
            return new ArrayList<>();
        }

        List<Integer> result = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(dbData);
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(jsonArray.getInt(i));
        }
        return result;
    }
}