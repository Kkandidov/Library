package org.astashonok.library.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class SqlReader {

    public static ArrayList<String> read(String fileName) {
        ArrayList<String> strings = new ArrayList<>();
        try (BufferedReader stream =
                     new BufferedReader(
                             new FileReader(fileName)
                     )) {
            String temp;
            while ((temp = stream.readLine()) != null) {
                strings.add(temp);
              }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return strings;
    }

}
