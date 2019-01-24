package com.gradingapp.service.util;

import com.gradingapp.domain.Submissions;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.PrintWriter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteCsvToResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteCsvToResponse.class);

    public static void writeSubmissionList(PrintWriter writer, List<Submissions> submissionsList)  {

        try {

            ColumnPositionMappingStrategy mapStrategy
                = new ColumnPositionMappingStrategy();

            mapStrategy.setType(Submissions.class);
            mapStrategy.generateHeader();

            String[] columns = new String[]{"fdaiNumber", "name", "course", "subject", "exercises"};
            mapStrategy.setColumnMapping(columns);

            StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(mapStrategy)
                .withSeparator(',')
                .build();

            btcsv.write(submissionsList);

        } catch (CsvException ex) {

            LOGGER.error("Error mapping Bean to CSV", ex);
        }
    }

    public static void writeSubmissions(PrintWriter writer, Submissions submissions) {

        try {

            ColumnPositionMappingStrategy mapStrategy
                = new ColumnPositionMappingStrategy();

            mapStrategy.setType(Submissions.class);

            String[] columns = new String[]{"fdaiNumber", "name", "course", "subject", "exercises"};
            mapStrategy.setColumnMapping(columns);

            StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(mapStrategy)
                .withSeparator(',')
                .build();

            btcsv.write(submissions);

        } catch (CsvException ex) {

            LOGGER.error("Error mapping Bean to CSV", ex);
        }
    }
}
