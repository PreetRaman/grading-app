package com.gradingapp.web.rest.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Preet on 02/02/2019.
 */
@Component
public class ReadCSV {
    private final Logger log = LoggerFactory.getLogger(ReadCSV.class);
    public ReadCSV()
    {}
    public <T> List<T> loadObjectList(Class<T> type, File file) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            MappingIterator<T> readValues =
                mapper.reader(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file " + file.getName(), e);
            return Collections.emptyList();
        }
    }
}
