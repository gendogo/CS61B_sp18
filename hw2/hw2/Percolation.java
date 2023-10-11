package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size; //size of grid
    private WeightedQuickUnionUF quickUnion;
    private boolean[][] sites; // size * size grid
    private int topSite = size * size; //Virtual top site connected to all open items in top row
    //private int bottomSite = size * size + 1;
    private int _numberOfOpenSites;
    private int[] openBottomColumn;
    private boolean percolationProperty = false;
    private int openBottomSize;

    public Percolation(int N) { // create N-by-N grid, with all sites initially blocked
        if (N <= 0) {
            throw new IllegalArgumentException("N should not smaller than 1!");
        }
        size = N;
        _numberOfOpenSites = 0;
        quickUnion = new WeightedQuickUnionUF(size * size + 1);
        openBottomColumn = new int[size];
        openBottomSize = 0;

        // all sites initially blocked
        sites = new boolean[N][N];
        for (int i = 0; i < sites.length; i++) {
            for (int j = 0; j < sites[i].length; j++) {
                sites[i][j] = false;
            }
        }

        //connect all sites in top row with virtual top site
        for (int i = 0; i < size; i++) {
            quickUnion.union(topSite, xyTo1D(0, i));
        }

    }

    //return one dimensional coordinate used in Disjoint Set
    public int xyTo1D(int r, int c) {
        if (r < 0 || r >= size || c < 0 || c >= size) {
            throw new IllegalArgumentException("Index outbounded!");
        }
        return size * r + c;
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Index outbounded!");
        }
        if (isOpen(row, col)) {
            return;
        }
        sites[row][col] = true;
        _numberOfOpenSites++;
        if (row == size - 1) {
            openBottomColumn[openBottomSize] = col;
            openBottomSize++;
        }
        // connect this site with the open sites around itself
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        for (int i = 0; i < 4; i++) {
            int r = row + dx[i];
            int c = col + dy[i];
            if (r < 0 || r >= size || c < 0 || c >= size) {
                continue;
            }
            if (isOpen(r, c)) {
                quickUnion.union(xyTo1D(row, col), xyTo1D(r, c));
            }
        }
        // if virtual top site is connected with any bottom opened site
        if (!percolationProperty) {
            for (int i = 0; i < openBottomSize; i++) {
                if (quickUnion.connected(topSite, xyTo1D(size - 1, openBottomColumn[i]))) {
                    percolationProperty = true;
                }
            }
        }
    }


    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Index outbounded!");
        }
        return sites[row][col];
    }

    // is the site connected with virtual top site
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Index outbounded!");
        }
        return isOpen(row, col) && quickUnion.connected(xyTo1D(row,col), topSite);
    }


    public int numberOfOpenSites() {
        return _numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolationProperty;
    }

    public static void main(String[] args) {
        int size = 3;
        Percolation p = new Percolation(size);
        for (int i = 0; i < size; i++) {
            p.open(i, 1);
        }

        System.out.println(p.numberOfOpenSites());
        System.out.println(p.isOpen(0, 0));
        System.out.println(p.isOpen(1, 1));
        System.out.println(p.percolates());
    }
}
