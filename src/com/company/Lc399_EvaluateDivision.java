package com.company;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Lc399_EvaluateDivision {
    public double[] calcEquation(String[][] equations, double[] values, String[][] queries) {
        //Construct graph: a/b = v => <a, <b, v>> AND <b, <a, 1/v>>
        Map<String, Map<String, Double>> graph = new HashMap<>();
        for (int i = 0; i < equations.length; i++) {
            String d1 = equations[i][0];
            String d2 = equations[i][1];
            Map<String, Double> d1_nbr = graph.get(d1) == null ? new HashMap<>() : graph.get(d1);
            Map<String, Double> d2_nbr = graph.get(d2) == null ? new HashMap<>() : graph.get(d2);

            d1_nbr.put(d2, values[i]);
            d2_nbr.put(d1, 1 / values[i]);

            graph.put(d1, d1_nbr);
            graph.put(d2, d2_nbr);
        }

        //Do queries
        double[] rtn = new double[queries.length];
        for (int i = 0; i < queries.length; i++) {
            rtn[i] = findValue(queries[i][0], queries[i][1], graph);
        }

        return rtn;
    }

    private double findValue(String d1, String d2, Map<String, Map<String, Double>> graph) {
        if (d1.equals(d2)) {
            if (graph.containsKey(d1)) {
                return 1.0;
            } else {
                return -1.0;
            }
        }

        double rtn = 0;
        Set<String> visited = new HashSet<>(); //record the vertices that have been visted
        Deque<Map.Entry> stack = new ArrayDeque<>(); //DFS
        stack.push(new HashMap.SimpleEntry(d1, 1.0));
        visited.add(d1);

        while (!stack.isEmpty()) {
            Map.Entry<String, Double> curNode = stack.pop();

            Map<String, Double> nbr = graph.get(curNode.getKey());
            if (nbr != null) {
                if (nbr.containsKey(d2)) {
                    //Found a path from d1 to d2
                    rtn = curNode.getValue() * nbr.get(d2);
                    return rtn;
                } else {
                    for (Map.Entry<String, Double> entry : nbr.entrySet()) {
                        if (!visited.contains(entry.getKey())) {
                            visited.add(entry.getKey());
                            stack.push(new HashMap.SimpleEntry(entry.getKey(), entry.getValue() * curNode.getValue()));
                        }
                    }
                }
            }
        }

        return -1;
    }
}
