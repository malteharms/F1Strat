package de.malte.f1strat.handler;

import de.malte.f1strat.structure.DataStructure;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;

public class JsonHandler {

    public DataStructure loadData(String file) {
        DataStructure data;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            data = new Gson().fromJson(br, DataStructure.class);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

}
