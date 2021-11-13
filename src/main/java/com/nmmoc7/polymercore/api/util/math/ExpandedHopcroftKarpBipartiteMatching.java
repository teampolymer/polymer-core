package com.nmmoc7.polymercore.api.util.math;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class that make it easy to use the H-K matching to run "Resource Allocation" matching.
 * Each vertex gets an integer which represents the number of times it can be matched.
 * NOTE: Optimized for "developer friendly-ness".  Not optimal performance.
 *
 * @author Charlie Jonas (charlie@callaway.cloud)
 */
public class ExpandedHopcroftKarpBipartiteMatching {
    private final HopcroftKarpBipartiteMatching matching;

    // used to track and return useful results
    private final Object2IntMap<String> partition1;
    private final Object2IntMap<String> partition2;
    private final Map<String, String> reverseVertexLookupMap = new HashMap<>();

    /**
     * Constructor
     *
     * @param partition1 Map where key is the vertex and value is the number of times it should be added to the graph
     * @param partition2 Map where key is the vertex and value is the number of times it should be added to the graph
     * @param edgeMap    Map containing all possible matchings. Edges are undirected (you only need to include one direction)
     */
    public ExpandedHopcroftKarpBipartiteMatching(
        Object2IntMap<String> partition1,
        Object2IntMap<String> partition2,
        Map<String, Set<String>> edgeMap
    ) {
        this.partition1 = partition1;
        this.partition2 = partition2;

        Map<String, Set<String>> rightVertexExpansionMap = new HashMap<>();
        Set<String> rightPartition = new HashSet<>();
        for (String part : partition2.keySet()) {
            Set<String> expanded = new HashSet<>();
            for (int i = 0; i < partition2.getInt(part); i++) {
                String vertexKey = part + '-' + i;
                reverseVertexLookupMap.put(vertexKey, part);
                expanded.add(vertexKey);
            }
            rightPartition.addAll(expanded);
            rightVertexExpansionMap.put(part, expanded);
        }

        Map<String, Set<String>> expandedEdgeMap = new HashMap<>();
        for (String part : partition1.keySet()) {
            Set<String> edges = edgeMap.get(part);
            for (int i = 0; i < partition1.getInt(part); i++) {
                String vertexKey = part + '-' + i;
                reverseVertexLookupMap.put(vertexKey, part);
                Set<String> expandedEdges = new HashSet<>();
                if (edges != null) {
                    for (String edge : edges) {
                        Set<String> expEdges = rightVertexExpansionMap.get(edge);
                        if (expEdges != null) {
                            expandedEdges.addAll(expEdges);
                        }
                    }
                }
                expandedEdgeMap.put(vertexKey, expandedEdges);
            }
        }

        matching = new HopcroftKarpBipartiteMatching(
            expandedEdgeMap.keySet(),
            rightPartition,
            expandedEdgeMap
        );
    }

    /**
     * @return a result which contains both the matches and the unmatched vertices
     */
    public Result getMatching() {
        Map<String, String> matches = matching.getMatching();
        Result result = new Result();
        result.matches = new HashMap<>();
        result.unmatched = new Object2IntOpenHashMap<>();

        result.unmatched.putAll(partition1);
        result.unmatched.putAll(partition2);

        for (String match : matches.keySet()) {
            String v1 = reverseVertexLookupMap.get(match);
            String v2 = reverseVertexLookupMap.get(matches.get(match));

            if (result.matches.containsKey(v1)) {
                Object2IntMap<String> edgeCounts = result.matches.get(v1);
                int count = edgeCounts.getInt(v2);
                edgeCounts.put(v2, count + 1);
            } else {
                Object2IntMap<String> matchVal = new Object2IntOpenHashMap<>();
                matchVal.put(v2, 1);
                result.matches.put(v1, matchVal);
            }
            result.unmatched.put(v1, result.unmatched.getInt(v1) - 1);
            if (result.unmatched.getInt(v1) == 0) {
                result.unmatched.removeInt(v1);
            }
        }
        return result;
    }

    /**
     * Class for returning results
     */
    public static class Result {
        /**
         * matches: Vertex => Vertex => number of matches
         */
        public Map<String, Object2IntMap<String>> matches;
        /**
         * unmatched: Vertex => remainder
         */
        public Object2IntMap<String> unmatched;
    }
}