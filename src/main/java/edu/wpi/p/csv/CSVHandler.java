package edu.wpi.p.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A static class for parsing a CSV file
 */
public class CSVHandler {

    /**
     * Splits the csv file line up by comma and trims whitespace
     * @param line The line of the csv file
     * @return List of strings that were separated by commas
     */
    private static List<String> splitLine(String line) {
        List<String> lines = Arrays.asList(line.split(","));
        return lines.stream().map(String::trim).collect(Collectors.toList());
    }

    /**
     * Checks if all the lines are of equal length
     * @param tokenizedLines A stream of the split lines
     * @throws Exception If the lines are not all the same length, throw an exception
     */
    private static void testLinesEqual(Stream<List<String>> tokenizedLines) throws Exception {
        long distinctCount =  tokenizedLines.map(List::size).distinct().count();
        if (distinctCount > 1) {
            throw new Exception("The lines of the csv file are not all the same length");
        }
    }

    /**
     * Parses the given csv at the given filename
     * @param filename Name/Path to the csv file
     * @return CSVData instance with the same name as the filename, and with the data from the file
     * @throws Exception If there is an issue with the csv file, like the rows not all being the same size, an exception is thrown
     */
    public static CSVData readCSVFile(String filename) throws Exception {
        // get the name from between the last backlash in the path, and the last '.'
        String name = filename.substring(filename.lastIndexOf('\\') + 1, filename.lastIndexOf('.'));
        CSVData csvData = new CSVData(name);

        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            readLineStreamIntoCSVData(lines, csvData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvData;
    }

    /**
     * Parses the given csv at the given filename
     * @param csv String of the csv data
     * @return CSVData instance with the same name as the filename, and with the data from the file
     * @throws Exception If there is an issue with the csv file, like the rows not all being the same size, an exception is thrown
     */
    public static CSVData readCSVString(String csv) throws Exception {
        // get the name from between the last backlash in the path, and the last '.'
        String name = "csv from string";//filename.substring(filename.lastIndexOf('\\') + 1, filename.lastIndexOf('.'));
        CSVData csvData = new CSVData(name);

        // this code can almost assuredly be improved, it's currently a weird mix of lists and streams
        try (Stream<String> lines = Arrays.stream(csv.split("\n"))) {
            readLineStreamIntoCSVData(lines, csvData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvData;
    }

    /**
     * Does the actual work of parsing the csv file, that has already been turned into a stream of liens
     * @param lines A stream with each of the line of the csv file
     * @param csvData A CSVData instance to add the data from the csv file into
     * @throws Exception If the lines aren't the same length
     */
    private static void readLineStreamIntoCSVData(Stream<String> lines, CSVData csvData) throws Exception {
        // tokenize all the lines
        List<List<String>> tokenizedLines = lines.map(CSVHandler::splitLine).collect(Collectors.toList());
        // raise and exception if the rows aren't all the same length, exits the method
        testLinesEqual(tokenizedLines.stream());

        // for every column of data, make it into a single list and put it in the CSVData
        for (int i = 0; i < tokenizedLines.get(0).size(); i++) {
            final int index = i;
            List<String> columnValues = tokenizedLines.stream().map(s -> s.get(index)).collect(Collectors.toList());
            // first element is the name, the rest are the values
            csvData.addColumnFromStringData(columnValues.get(0), columnValues.subList(1, columnValues.size()));
        }
    }

    public static void writeCSVData(CSVData data, String fileName) {
        try {
            FileWriter output = new FileWriter(fileName, false);

            List<Column> columns = data.getAllColumns();
            List<String> columnNames = columns.stream().map(Column::getName).collect(Collectors.toList());
            output.write(String.join(", ", columnNames) + "\n");
            int length = columns.get(0).getData().size();
            for(int i = 0; i < length; i++) {
                final int indx = i;
                List<String> values = columns.stream().map(c -> c.getData().get(indx) + "").collect(Collectors.toList());
                output.write(String.join(", ",  values) + "\n");
            }
            output.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }


}
