package com.example.urlchecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Hash {
    private HashMap<Integer, Integer> values;

    public Hash(String data) {
        values = new HashMap<>();
        for (char c : data.toCharArray()) {
            if (values.containsKey((int)c))
                values.put((int)c, values.get((int)c) + 1);
            else
                values.put((int)c, 1);
        }
    }

    private static <T> Set<T> difference(final Set<T> setOne, final Set<T> setTwo) {
        Set<T> result = new HashSet<T>(setOne);
        result.removeAll(setTwo);
        return result;
    }

    public double compareTo(Hash other) {
        //ArrayList<Double> percentages = new ArrayList<>();
        double total = 0;
        int count = 0;
        for (int key : values.keySet()) {
            if (other.values.containsKey(key)) {
                double val1 = values.get(key).doubleValue();
                double val2 = other.values.get(key).doubleValue();
                double percent = (Math.max(val1, val2) - Math.min(val1, val2)) / Math.max(val1, val2);
                if (percent > 1)
                    total += 1 / percent;
                else total += percent;
                //percentages.add(values.get(key).doubleValue() / other.values.get(key).doubleValue());
            } else {
                total++;
                //percentages.add(values.get(key).doubleValue());
            }
            count++;
        }

        for (int key : difference(other.values.keySet(), values.keySet())) {
            total++;
            count++;
        }

        return total / count;
    }

}
