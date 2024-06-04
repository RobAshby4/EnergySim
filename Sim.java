import java.util.*;
import java.util.stream.Collectors;
import java.lang.Thread;

class Sim {
    private static int getNeighborsSum(int x, int y, ArrayList<Coordinate> coords) {
        ArrayList<Coordinate> neighbors = coords.stream()
                                            .filter(coord -> Math.abs(coord.x - x) <= 1 && Math.abs(coord.y - y) <= 1)
                                            .collect(Collectors.toCollection(ArrayList::new));
        int sum = 0;
        for (Coordinate neighbor : neighbors) {
            if (neighbor.x == x && neighbor.y == y) {
                continue;
            }
            sum += neighbor.val;
        }
        return sum;
    }

    private static ArrayList<Coordinate> updateGrid(ArrayList<Coordinate> coords) {
        ArrayList<Coordinate> newCoords = new ArrayList<Coordinate>();
        for (Coordinate coord : coords) {
            Coordinate newCoord = new Coordinate(coord.x, coord.y, coord.val, coord.energy);
            int neighborSum = getNeighborsSum(coord.x, coord.y, coords);
            if (coord.val == 1) {
                if (neighborSum <= 2) {
                    newCoord.energy += 10;
                    if (newCoord.energy > 100) {
                        newCoord.energy = 100;
                    }
                } else if (neighborSum > 5){
                    newCoord.energy -= 10 * neighborSum;
                }
                if (newCoord.energy <= 0) {
                    newCoord.energy = 0;
                    newCoord.val = 0;
                }
            } else {
                if (neighborSum <= 3) {
                    newCoord.val = 1;
                    newCoord.energy = 100;
                }
            }
            newCoords.add(newCoord);
        }
        return newCoords;
    }

    private static void printGrid(ArrayList<Coordinate> coords) {
        for (Coordinate coord : coords) {
            if (coord.val == 0) {
                System.out.print(" ");
            } else {
                char[] outputEnergy = String.format("%d", (coord.energy / 10) % 10).toCharArray();
                System.out.print(outputEnergy[outputEnergy.length - 1]);
            }
            if (coord.x == 79) {
                System.out.println();
            }
        }
    }

    public static void main(String args[]) {
        ArrayList <Coordinate> coords = new ArrayList<Coordinate>();
        Random rand = new Random();
        for (int y = 0; y < 24; y++) {
            for (int x = 0; x < 80; x++) {
                int val = rand.nextInt(2);
                int energy = 0;
                if (val == 1) {
                    energy = rand.nextInt(100) + 1;
                }
                coords.add(new Coordinate(x, y, val, energy));
            }
        }
        System.out.println();
        printGrid(coords);
        for (int i = 0; i < 10; i++) {
            coords = updateGrid(coords);
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println();
            printGrid(coords);
        }
    }
}
