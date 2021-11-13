package com.nmmoc7.polymercore.api.recipes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Hopcroftkarp {
    private final int NIL = 0;
    private final int INF = Integer.MAX_VALUE;
    private ArrayList<Integer>[] adj;
    private int[] Pair;
    private int[] Dist;
    private int cx,cy;
    public boolean BFS() {
        Queue<Integer> queue = new LinkedList<>();
        for(int v = 1;v <= cx;++v) {
            if(Pair[v] == NIL) {
                Dist[v] = 0;
                queue.add(v);
            } else {
                Dist[v] = INF;
                while(!queue.isEmpty()) {
                    int v = queue.poll();
                    if(Dist[v] < Dist[NIL]) {
                        for(int u : adj[v]) {
                            if(Dist[Pair[u]] == INF) {
                                Dist[Pair[u]] = Dist[v] + 1;
                                queue.add(Pair[u]);
                            }
                        }
                    }
                }
                return Dist[NIL] != INF;
            }
        public boolean DFS(int v) {
                if(v != NIF) {
                    for(int u : adj[v]) {
                        if(Dist[Pair[u]] == Dist[v] + 1) {
                            if(DFS(Pair[u])) {
                                Pair[u] = v;
                                Pair[v] = u;
                                return true;
                            }
                        }
                    }
                    Dist[v] = INF;
                    return false;
                }
                return true;
            }
            public int HopcroftKarp() {
                Pair = new int[cx + cy +1];
                Dist = new int[cx + cy +1];
                int matching = 0;
                while(BFS()) {
                    for(int v = 1;v <= cx;++v) {
                        if(Pair[v] == NIL) {
                            matching = matching + 1;
                        }
                    }
                }
            }
            return matching;
        }
        public void makeGraph(int[] x,int[] y,int E) {
            adj = new ArrayList[cx + cy +1];
            for(int i = 0;i < adj.length;i++) {
                adj[i] =  new ArrayList<Integer>();
            }
            for(int i = 0;i < E;i++) {
                addEdge(x[i] + 1,y[i] + 1);
            }
        }
        public void addEdge(int x,int y) {
            adj[x].add(cx+y);
            adj[cx+y].add(x);
        }
        /*
        Hopcroft hc = new Hopcroft();
        scan E
        int[] x = new int[E};
        int[] y = new int[E];
        hc.cx=0;
        hc.cy=0;
        hx.cx +=1;
        hc.cy +=1;
        hc.makeGraph(x,y,E);
        System.out.println("\nMatches : "+ hc.HopcroftKarp());
         */
    }
}
