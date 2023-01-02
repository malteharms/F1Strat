package de.malte.f1strat.handler;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonHandler {

    public JSONObject loadData(String file) {
        Object obj;
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(file)) {
            //Read JSON file
            obj = jsonParser.parse(reader);

            JSONObject data = (JSONObject) obj;
            System.out.println(data);

            //Iterate over employee array
            //data.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );

            return data;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    return null;
    }

    private void parseEmployeeObject(JSONObject employee)
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) employee.get("employee");

        //Get employee first name
        String firstName = (String) employeeObject.get("firstName");
        System.out.println(firstName);

        //Get employee last name
        String lastName = (String) employeeObject.get("lastName");
        System.out.println(lastName);

        //Get employee website name
        String website = (String) employeeObject.get("website");
        System.out.println(website);
    }

}
