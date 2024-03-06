package com.github.alwaysseen.merchsite.services;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class LocalDateToStringConverter implements AttributeConverter<LocalDate, String> {

    private static final Logger logger = LoggerFactory.getLogger(LocalDateToStringConverter.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalDateToStringConverter() {
        logger.info("Creating LocalDateToStringConverter");
    }

    @Override
    public String convertToDatabaseColumn(LocalDate localDate) {
        logger.info("CONVERTING TO DB COLUMN");
        return localDate != null ? localDate.format(FORMATTER) : null;
    }

    @Override
    public LocalDate convertToEntityAttribute(String dateString) {
        return dateString != null ? LocalDate.parse(dateString, FORMATTER) : null;
    }
}


