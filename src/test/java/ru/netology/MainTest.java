package ru.netology;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MainTest {

    @Test
    public void parseXMLTest() {

        List<Employee> expected = new ArrayList<>();
        expected.add(new Employee(1, "John", "Smith", "USA", 25));
        expected.add(new Employee(2,"Ivan", "Petrov", "RU", 23));

        String argument = "C://Users//User//IdeaProjects//CsvJsonParser//src//main//java//ru//netology//files//data.xml";

        List<Employee> actual = Main.parseXML(argument);

        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void listToJsonTest() {

        String expected = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Ivan\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";

        List<Employee> argument = new ArrayList<>();
        argument.add(new Employee(1, "John", "Smith", "USA", 25));
        argument.add(new Employee(2,"Ivan", "Petrov", "RU", 23));

        String actual = Main.listToJson(argument);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void parseXML_NO_NULL() {

        String argument = "C://Users//User//IdeaProjects//CsvJsonParser//src//main//java//ru//netology//files//data.xml";
        List<Employee> actual = Main.parseXML(argument);

        Assert.assertNotNull(actual);
    }
}
