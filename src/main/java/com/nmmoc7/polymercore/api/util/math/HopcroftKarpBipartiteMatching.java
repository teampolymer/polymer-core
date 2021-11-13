package com.nmmoc7.polymercore.api.util.math;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.ints.IntPriorityQueue;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.*;

/*
 * @author Chuck Jonas (charlie@callaway.cloud) and Dustin Kovac
 *
 * Derived from: JGraphT
 * (C) Copyright 2017-2020, by Joris Kinable and Contributors.
 * https://github.com/jgrapht
 */
public class HopcroftKarpBipartiteMatching {
    private final Set<String> partition1;
    private final Set<String> partition2;

    /* Ordered list of vertices */
    private List<String> vertices;
    /* Mapping of a vertex to their unique position in the ordered list of vertices */
    private Object2IntMap<String> vertexIndexMap;

    /* Number of matched vertices i partition 1. */
    private int matchedVertices = 0;

    /* Dummy vertex. All vertices are initially matched against this dummy vertex */
    private final int DUMMY = 0;

    private static final String LOOPS_NOT_ALLOWED = "loops not allowed";
    private static final String INVALID_VERTEX = "Invalid Vertex";

    /* Infinity */
    private final int INF = 999999999;

    /* Array keeping track of the matching. */
    private int[] matching;
    /* Distance array. Used to compute shoretest augmenting paths */
    private int[] dist;

    /* queue used for breadth first search */
    private IntPriorityQueue queue;

    //graph data
    private final Set<String> edgeSet;
    private final Map<String, Set<String>> vertexMap;

    /**
     * Constructor
     *
     * @param partition1 Set 1 of two disjoint sets
     * @param partition2 Set 2 of two disjoint sets
     * @param edgeMap    Map containing all possible matchings. Edges are undirected (you only need to include one direction)
     */
    public HopcroftKarpBipartiteMatching(
        Set<String> partition1,
        Set<String> partition2,
        Map<String, Set<String>> edgeMap
    ) {
        this.vertexMap = new HashMap<>();
        for (String part : partition1) {
            addVertex(part);
        }
        for (String part : partition2) {
            addVertex(part);
        }
        this.edgeSet = new HashSet<>();

        for (String vert1 : edgeMap.keySet()) {
            for (String vert2 : edgeMap.get(vert1)) {
                addEdge(vert1, vert2);
            }
        }

        if (partition1.size() <= partition2.size()) {
            this.partition1 = partition1;
            this.partition2 = partition2;
        } else {
            // else, swap
            this.partition1 = partition2;
            this.partition2 = partition1;
        }
    }

    /**
     * This method uses the Hopcroft-Karp matching algorithm to return a
     * matches with maximal cardinality.
     *
     * @return Map<String, String> of all matches (include vertices from both partitions)
     */
    public Map<String, String> getMatching() {
        this.init();
        this.warmStart();

        while (matchedVertices < partition1.size() && bfs()) {
            // Greedily search for vertex disjoint augmenting paths
            for (
                int v = 1;
                v <= partition1.size() &&
                    matchedVertices < partition1.size();
                v++
            ) {
                if (
                    matching[v] == DUMMY // v is unmatched
                ) {
                    if (dfs(v)) {
                        matchedVertices++;
                    }
                }
            }
        }
        assert (matchedVertices <= partition1.size());

        Map<String, String> edges = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            if (matching[i] != DUMMY) {
                edges.put(vertices.get(i), vertices.get(matching[i]));
            }
        }
        return edges;
    }

    /**
     * Initialize data structures
     */
    private void init() {
        vertices = new ArrayList<>();
        vertices.add(null);
        vertices.addAll(partition1);
        vertices.addAll(partition2);
        vertexIndexMap = new Object2IntOpenHashMap<>();
        for (int i = 0; i < vertices.size(); i++)
            vertexIndexMap.put(vertices.get(i), i);

        matching = initializeFixedIntArray(vertices.size() + 1);
        dist = initializeFixedIntArray(partition1.size() + 1);
        queue = new IntArrayFIFOQueue(vertices.size());
    }

    /**
     * Greedily compute an initial feasible matching
     */
    private void warmStart() {
        for (String uOrig : partition1) {
            int u = vertexIndexMap.getInt(uOrig);
            for (String vOrig : neighborListOf(uOrig)) {
                int v = vertexIndexMap.getInt(vOrig);
                if (matching[v] == DUMMY) {
                    matching[v] = u;
                    matching[u] = v;
                    matchedVertices++;
                    break;
                }
            }
        }
    }

    //MultiGraph Methods
    private void addEdge(String sourceVertex, String targetVertex) {
        assertVertexExist(sourceVertex);
        assertVertexExist(targetVertex);

        if (Objects.equals(sourceVertex, targetVertex)) {
            throw new IllegalArgumentException(LOOPS_NOT_ALLOWED);
        }

        String edgeKey = sourceVertex + ':' + targetVertex;

        if (!edgeSet.contains(edgeKey)) {
            edgeSet.add(edgeKey);
            vertexMap.get(sourceVertex).add(targetVertex);
            vertexMap.get(targetVertex).add(sourceVertex);
        }
    }

    private Collection<String> neighborListOf(String vertex) {
        if (!vertexMap.containsKey(vertex)) {
            throw new IllegalArgumentException(INVALID_VERTEX);
        }

        return Collections.unmodifiableCollection(vertexMap.get(vertex));
    }

    private boolean addVertex(String v) {
        if (v == null) {
            throw new NullPointerException();
        } else if (vertexMap.containsKey(v)) {
            return false;
        } else {
            vertexMap.computeIfAbsent(v, k -> new HashSet<>());
            return true;
        }
    }

    private boolean assertVertexExist(String v) {
        if (vertexMap.containsKey(v)) {
            return true;
        } else if (v == null) {
            throw new NullPointerException();
        } else {
            throw new IllegalArgumentException("no such vertex in graph: " + v);
        }
    }

    //Breadth-First Search Algorithm
    private boolean bfs() {
        queue.clear();

        for (int u = 1; u <= partition1.size(); u++)
            if (matching[u] == DUMMY) {
                // Add all unmatched vertices to the queue and set their
                // distance to 0
                dist[u] = 0;
                queue.enqueue(u);

            } else
                // Set distance of all matched vertices to INF
                dist[u] = INF;
        dist[DUMMY] = INF;

        while (!queue.isEmpty()) {
            int u = queue.dequeueInt();
            if (dist[u] < dist[DUMMY])
                for (String vOrig : neighborListOf(vertices.get(u))) {
                    int v = vertexIndexMap.getInt(vOrig);
                    if (dist[matching[v]] == INF) {
                        dist[matching[v]] = dist[u] + 1;
                        queue.enqueue(matching[v]);
                    }
                }
        }
        return dist[DUMMY] != INF; // Return true if an augmenting path is found
    }

    //Depth-First Search Algorithm
    private boolean dfs(int u) {
        if (u != DUMMY) {
            for (String vOrig : neighborListOf(vertices.get(u))) {
                int v = vertexIndexMap.getInt(vOrig);
                if (dist[matching[v]] == dist[u] + 1)
                    if (dfs(matching[v])) {
                        matching[v] = u;
                        matching[u] = v;
                        return true;
                    }
            }
            // No augmenting path has been found. Set distance of u to INF to ensure that u isn't
            // visited again.
            dist[u] = INF;
            return false;
        }
        return true;
    }

    //Initializes fixed length list
    private static int[] initializeFixedIntArray(int len) {
        int[] iArr = new int[len];
        for (int i = 0; i < len; i++) {
            iArr[i] = 0;
        }
        return iArr;
    }


}
