package buildmaze;

import edu.princeton.cs.algs4.StdDraw;

public class MazeBuilder {
    
    class Wall {                            // Wall class holds
        int cell1;                          // the cells on each side
        int cell2;                          // of the wall
        int node1;                          // as well as the nodes
        int node2;                          // of the wall
        
        Wall(int a, int b) {
            cell1 = a;
            cell2 = b;
            node1 = 0;
            node2 = 0;
        }
    }
    
    private int[] cells;                    // array of cell group IDs
    private Wall[] mazeWalls;               // the "list" of walls
    private int groups;                     // # of component groups
    final private int m, n;                 // width and height of grid
    final private double unitSize;          // unit size of each cell
    
    // Constructor takes dimensions of the grid
    public MazeBuilder(int x, int y) {
        m = x;      // cell width of maze
        n = y;      // cell height of maze
        
        if(m > n) unitSize = 500.0 / m;     // Divide "canvas size" by either
        else unitSize = 500.0 / n;          // width of height, whichever is
                                            // greater
        int gridsize = m * n;
        
        cells = new int[gridsize];
        mazeWalls = new Wall[2 * gridsize - x - y];
        groups = gridsize;
        
        initCells();
        initWalls(m,n);
    }
    
    // Initialize each cell to it's own group ID
    private void initCells() {
        for(int i=0; i<cells.length; i++)
            cells[i] = i;
    }
    
    // Prepares the list of walls using the width and height of grid
    private void initWalls(int m, int n) {
        System.out.println("Preparing a list of walls...");
        
        // loop limits
        int horizontals = m * (n - 1);
        int verticals = m * n - 1;
        int index;
        int below, after;         // values of cell #2, to simplify calculations
        
        // Add horizontal walls to the list
        for(int i=0; i<horizontals; i++) {
            below = i + m;                    
            mazeWalls[i] = new Wall(i, below);
            mazeWalls[i].node1 = below / m + below;
            mazeWalls[i].node2 = mazeWalls[i].node1 + 1;
        }
        
        index = horizontals;    // Continue where we left off with
                                // a separate index
        
        // Add vertical walls to the list
        for(int j=0; j<verticals; j++) {
            after = j+1;
            mazeWalls[index] = new Wall(j, after);
            mazeWalls[index].node1 = after / m + after;
            mazeWalls[index].node2 = mazeWalls[index].node1 + m + 1;
            index++;
            if(j % m == (m-2)) j++;     // Skip accordingly
        }
        
        // Shuffle the list
        System.out.println("Shuffling list...");
        
        for(int k=0; k<mazeWalls.length; k++) {
            Wall temp;
            int r;
            r = (int) (Math.random() * mazeWalls.length);
            temp = mazeWalls[k];
            mazeWalls[k] = mazeWalls[r];
            mazeWalls[r] = temp;
        }
        System.out.println("Done.");
    }
    
    public void DrawGrid() {                    
        StdDraw.setXscale(0.0, 500.5);          // Setup calls for StdDraw
        StdDraw.setYscale(-0.1, 500.0);         
        StdDraw.setPenRadius(0.002);
        StdDraw.setPenColor(StdDraw.BLACK);
        
        // Horizontal lines for grid
        for(int i=0; i<=n; i++)
            StdDraw.line(0.0, i*unitSize, m*unitSize, i*unitSize);
        // Vertical lines
        for(int j=0; j<=m; j++)
            StdDraw.line(j*unitSize, 0.0, j*unitSize, n*unitSize);
        
        StdDraw.setPenRadius(0.004);
        StdDraw.setPenColor(StdDraw.WHITE);
    }
    
    public boolean connected(int p, int q) {        // Method not used
        return find(p) == find(q);
    }
    
    private int find(int p) {
        // Find the root of p
        while( p != cells[p]) p = cells[p];
        return p;
    }
    
    public boolean union(int p, int q) {
        int i = find(p);
        int j = find(q);
        
        if(i == j) return false;
         
        cells[j] = i;               // Make q a member of p's group  
        
        groups--;
        System.out.println(p + " and " + q + " connected");
        return true;
    }
    
    private int count() {
        return groups;    // should equal 1 after maze is built
    }
    
    public void LinkCells() {
        int ct = 0;
        
        System.out.println("\nBuilding " + m + "x" + n + " maze...");
        for(int i=0; count() != 1; i++) {
                            
            // If cells are connected, knock down the wall between them
            if( union(mazeWalls[i].cell1, mazeWalls[i].cell2) ) {
                EraseWall(mazeWalls[i]);
                ct++;
            }
        }
        System.out.println("Maze completed after " + ct + " connections.");
    }
    
    private void EraseWall(Wall w) {
        double x0, y0, x1, y1;
        
        // Calculate coordinates of each node of the wall
        x0 = unitSize * (w.node1 % (m+1));
        y0 = unitSize * (w.node1 / (m+1));
        x1 = unitSize * (w.node2 % (m+1));
        y1 = unitSize * (w.node2 / (m+1));
        
        StdDraw.line(x0, y0, x1, y1);
    }
    
    public static void main(String[] args) {
        
        // Dimensions must each be >= 1
        MazeBuilder maze = new MazeBuilder(32, 32);
        
        maze.DrawGrid();
        
        maze.LinkCells();
    }
}

/*
Preparing a list of walls...
Shuffling list...
Done.
Building 32x32 maze...
264 and 265 connected
377 and 378 connected
727 and 728 connected
66 and 98 connected
512 and 513 connected
730 and 762 connected
495 and 527 connected
777 and 809 connected
47 and 79 connected
    ...                         // Excessive output     
821 and 822 connected
659 and 691 connected
996 and 997 connected
1009 and 1010 connected
Maze completed after 1023 connections.
*/