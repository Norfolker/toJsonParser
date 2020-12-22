package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        final String CSV_PATH = "C://Users//User//IdeaProjects//CsvJsonParser//src//main//java//ru//netology//files//data.csv";
        final String JSON_PATH = "C://Users//User//IdeaProjects//CsvJsonParser//src//main//java//ru//netology//files//new_data.json";
        final String XML_PATH = "C://Users//User//IdeaProjects//CsvJsonParser//src//main//java//ru//netology//files//data.xml";

        File csv = new File(CSV_PATH);
        String[] employee = "1,John,Smith,USA,25".split(",");
        String[] employee2 = "2,Ivan,Petrov,RU,23".split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter(csv, true))) {
            writer.writeNext(employee);
            writer.writeNext(employee2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> employees = parseCSV(columnMapping, CSV_PATH);
        System.out.println(employees.toString());
        String json = listToJson(employees);
        System.out.println(json);
        writeString(json, JSON_PATH);

        List<Employee> employees2 = parseXML(XML_PATH);
        System.out.println(employees2.toString());
        String json2 = listToJson(employees2);
        System.out.println(json2);
        writeString(json2, JSON_PATH);
    }

    protected static List parseCSV(String[] columnMapping, String filename) {
        try (CSVReader csvReader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> staff = csv.parse();
            return staff;
        } catch (IOException e) {
            return Arrays.asList(e.getStackTrace());
        }
    }

    protected static String listToJson(List list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    protected static void writeString(String string, String path) {
        JSONParser parser = new JSONParser();
        try (FileWriter file = new FileWriter(path)){
            file.write(string);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected static List parseXML(String path) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(path));
            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            List<Employee> list = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node element = nodeList.item(i);
                NamedNodeMap map = element.getAttributes();
                list.add(new Employee(Integer.parseInt(map.item(0).getNodeName()), map.item(1).getNodeName(), map.item(2).getNodeName(),
                        map.item(3).getNodeName(), Integer.parseInt(map.item(4).getNodeName())));
            }
            return list;
        } catch (ParserConfigurationException e) {
            return Arrays.asList(e.getStackTrace());
        } catch (IOException e) {
            return Arrays.asList(e.getStackTrace());
        } catch (SAXException e) {
            return Arrays.asList(e.getStackTrace());
        }
    }

}
