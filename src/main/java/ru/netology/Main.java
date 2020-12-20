package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

        File csv = new File("C://Users//User//IdeaProjects//CsvJsonParser//src//Main//java//ru//netology",
                "data.csv");
        String[] employee = "1,John,Smith,USA,25".split(",");
        String[] employee2 = "2,Ivan,Petrov,RU,23".split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter(csv, true))) {
            writer.writeNext(employee);
            writer.writeNext(employee2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "C://Users//User//IdeaProjects//CsvJsonParser//src//Main//java//ru//netology//data.csv";
        List<Employee> employees = parseCSV(columnMapping, fileName);
        System.out.println(employees.toString());
        String json = listToJson(employees);
        System.out.println(json);
        writeString(json);

        List<Employee> employees2 = parseXML("C://Users//User//IdeaProjects//CsvJsonParser//src//Main//java//ru//netology//data.xml");
        System.out.println(employees2.toString());
        String json2 = listToJson(employees2);
        System.out.println(json2);
        writeString(json2);
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

    protected static void writeString(String string) {
        JSONParser parser = new JSONParser();
        try (FileWriter file = new FileWriter("C://Users//User//IdeaProjects//CsvJsonParser//src//Main//java//ru//netology//new_data.json")){
            JSONArray list = new JSONArray();
            list.add(parser.parse(string));
            file.write(list.toJSONString());
            file.flush();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    protected static List parseXML(String path) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(path);
            NodeList nodeList = doc.getDocumentElement().getElementsByTagName("employee");
            List<Employee> list = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                NamedNodeMap map = element.getAttributes();
                int id = Integer.parseInt(map.getNamedItem("id").getNodeValue());
                list.add(new Employee(Integer.parseInt(map.getNamedItem("id").getNodeValue()), map.getNamedItem("firstName").getNodeValue(), map.getNamedItem("lastName").getNodeValue(),
                        map.getNamedItem("country").getNodeValue(), Integer.parseInt(map.getNamedItem("age").getNodeValue())));
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
