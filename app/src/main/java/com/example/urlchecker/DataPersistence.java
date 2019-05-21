package com.example.urlchecker;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataPersistence {
    protected DataPersistence() {
    }

    private static File file;
    private static Gson gson = new Gson();

    static void Init(File fileDir) throws IOException {
        file = new File(fileDir, "stuff.json");
        if (!file.exists())
            file.createNewFile();
    }

    static List<UrlChecker> Read() throws IOException {

        UrlChecker[] savedList = gson.fromJson(new FileReader(file), UrlChecker[].class);
        if (savedList == null)
            return new ArrayList<>();
        return Arrays.asList(savedList);
    }

    static void Add(UrlChecker newUrl) throws IOException {
        List<UrlChecker> currentList = new ArrayList<>(Read());
        currentList.add(newUrl);

        String json = gson.toJson(currentList);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(json);
        bufferedWriter.close();
    }

    static void Delete(UrlChecker url) throws IOException {
        List<UrlChecker> currentList = new ArrayList<>(Read());
        currentList.remove(url);

        String json = gson.toJson(currentList);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(json);
        bufferedWriter.close();
    }

    static boolean Replace(UrlChecker toFind, UrlChecker toReplace) throws IOException {

        UrlChecker[] savedList = gson.fromJson(new FileReader(file), UrlChecker[].class);
        if (savedList == null)
            return false;
        boolean found = false;
        for (int i = 0; i < savedList.length; i++) {
            if (savedList[i].equals(toFind)) {
                savedList[i] = toReplace;
                found = true;
            }
        }

        if (found) {
            String json = gson.toJson(savedList);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(json);
            bufferedWriter.close();
            return true;
        }
        return false;
    }

    static void Debug() {
        file.delete();
    }
}
