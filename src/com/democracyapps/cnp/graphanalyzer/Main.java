package com.democracyapps.cnp.graphanalyzer;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Main {

    public static void main(String[] args) {
        System.out.println("GraphAnalyzer - starting up ...");
        try {
            if (args.length < 1) {
                System.err.println("GraphAnalyzer usage:  graphanalyzer run-directory");
                System.exit(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(" Got run directory: " + args[0]);
        String filePath = args[0] + "/ga.json";
        FileReader fr = null;
        try {
            fr = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        BufferedReader reader = new BufferedReader(fr);
        String contents = "";
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                contents += line;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(contents);
        JSONParser parser = new JSONParser();

        try {

            Object obj;
            JSONArray array;

            obj = parser.parse(contents);
            JSONObject o2 = (JSONObject) obj;
            array = (JSONArray) o2.get("sets");
            System.out.println("My name is " + o2.get("name"));
            System.out.println("And here's the array" + array.get(0));

        }
        catch (Exception e) {
            e.printStackTrace();
        }


	// write your code here
    }
}
