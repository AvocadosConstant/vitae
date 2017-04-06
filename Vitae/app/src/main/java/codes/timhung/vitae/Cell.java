package codes.timhung.vitae;

import java.util.HashSet;

public class Cell {

    private final int x;
    private final int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public HashSet<Cell> getAllNeighbors(HashSet<Cell> cells) {
        HashSet<Cell> neighbors = new HashSet<>(8);
        // Loop through neighbors
        for(int i = this.x - 1; i <= this.x + 1; i++) {
            for(int j = this.y - 1; j <= this.y + 1; j++) {
                // Verify not counting itself
                if(i != this.x || j != this.y) {
                    if(cells.contains(new Cell(i, j))) neighbors.add(new Cell(i, j));
                    else neighbors.add(new Cell(i, j));
                }
            }
        }
        return neighbors;
    }

    public HashSet<Cell> getDeadNeighbors(HashSet<Cell> cells) {
        HashSet<Cell> neighbors = new HashSet<>(8);
        // Loop through neighbors
        for(int i = this.x - 1; i <= this.x + 1; i++) {
            for(int j = this.y - 1; j <= this.y + 1; j++) {
                // Verify not counting itself
                if(i != this.x || j != this.y) {
                    if(!cells.contains(new Cell(i, j))) neighbors.add(new Cell(i, j));
                }
            }
        }
        return neighbors;
    }

    public int countNeighbors(HashSet<Cell> cells) {
        int neighborCount = 0;
        // Loop through neighbors
        for(int i = this.x - 1; i <= this.x + 1; i++) {
            for(int j = this.y - 1; j <= this.y + 1; j++) {
                // Verify not counting itself
                if(i != this.x || j != this.y) {
                    if(cells.contains(new Cell(i, j))) neighborCount++;
                }
            }
        }
        return neighborCount;
    }

    @Override
    /**
     * Hashes a cell
     *
     * First 16 bits of the int are filled with the cell's x value
     * left truncated, and the last 16 bits are filled with the y
     * value left truncated.
     *
     * e.g. x = 3, y = 7
     *
     * retVal = 00000000 00000011 00000000 00000111
     *                         3                 7
     */
    public int hashCode() {
        // Left truncation not handled, but size won't get close to 2^16 so whatever
        return (this.x << 16) + this.y;
    }

    @Override
    public boolean equals(Object o) {
        // Check for edge cases (null or wrong classtype)
        if(o == null) return false;
        if(getClass() != o.getClass()) return false;

        Cell other = (Cell) o;
        return (this.x == other.getX() && this.y == other.getY());
    }

    @Override
    public String toString() {
        return "Cell at (" + this.x + ", " + this.y + ")";
    }
}
