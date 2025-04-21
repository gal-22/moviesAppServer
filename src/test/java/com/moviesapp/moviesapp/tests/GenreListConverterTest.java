package com.moviesapp.moviesapp.tests;

import com.moviesapp.moviesapp.utils.GenreListConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class GenreListConverterTest {

    private final GenreListConverter converter = new GenreListConverter();

    @Test
    void convertToDatabaseColumn_emptyList_returnsEmptyJson() {
        assertEquals("[]", converter.convertToDatabaseColumn(Collections.emptyList()));
    }

    @Test
    void convertToDatabaseColumn_nonEmptyList_returnsJsonString() {
        String json = converter.convertToDatabaseColumn(Arrays.asList(5, 12, 18));
        assertTrue(json.contains("5"));
        assertTrue(json.contains("12"));
        assertTrue(json.contains("18"));
    }

    @Test
    void convertToEntityAttribute_emptyString_returnsEmptyList() {
        List<Integer> list = converter.convertToEntityAttribute("[]");
        assertTrue(list.isEmpty());
    }

    @Test
    void convertToEntityAttribute_jsonString_returnsList() {
        List<Integer> list = converter.convertToEntityAttribute("[7,8,9]");
        assertEquals(Arrays.asList(7,8,9), list);
    }
}
