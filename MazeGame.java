package maze;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class MazeGame 
{
    public static final int HEIGHT = 19; 
    public static final int WIDTH = 39;  
    private final int COL = 1;
    private final int ROW = 0;

    private Scanner playerInput;
    private boolean[][] blocked;
    private boolean[][] visited;
    private int[] player = new int[2];
    private int[] goal = new int[2];
    private int[] start = new int[2];

    public MazeGame(String mazeFile) throws FileNotFoundException {
        this(mazeFile, new Scanner(System.in));
    }

    public MazeGame(String mazeFile, Scanner playerInput) throws FileNotFoundException {
        this.playerInput = playerInput;
        this.blocked = new boolean[HEIGHT][WIDTH];
        this.visited = new boolean[HEIGHT][WIDTH];
        loadMaze(mazeFile);
    }

    private void loadMaze(String mazeFile) throws FileNotFoundException {
        blocked = new boolean[HEIGHT][WIDTH];
        visited = new boolean[HEIGHT][WIDTH];
        player = new int[2];
        start = new int[2];
        goal = new int[2];

        Scanner fileScanner = new Scanner(new File(mazeFile));

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                char c = fileScanner.next().charAt(0);

                switch (c) {
                    case '1':
                        blocked[row][col] = true;
                        break;
                    case '0':
                        blocked[row][col] = false;
                        break;
                    case 'S':
                        start[ROW] = row;
                        start[COL] = col;
                        player[ROW] = row;
                        player[COL] = col;
                        blocked[row][col] = false; 
                        break;
                    case 'G':
                        goal[ROW] = row;
                        goal[COL] = col;
                        blocked[row][col] = false; 
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid character in maze file: " + c);
                }
            }
        }

        fileScanner.close();
    }

    public void playGame() {
        String input; 
    
        do {
            prompt();
    
            input = playerInput.nextLine();
    
        } while (!makeMove(input)); 
    
        if (playerAtGoal()) {
            System.out.println("You Won!");
        } else {
            System.out.println("Goodbye!");
        }
    }
    

    public void printMaze() {
        System.out.print("*");
        for (int col = 0; col < WIDTH; col++) {
            System.out.print("-");
        }
        System.out.println("*");

        for (int row = 0; row < HEIGHT; row++) {
            System.out.print("|");

            for (int col = 0; col < WIDTH; col++) {
                if (player[ROW] == row && player[COL] == col) {
                    System.out.print("@"); 
                } else if (start[ROW] == row && start[COL] == col) {
                    System.out.print("S"); 
                } else if (goal[ROW] == row && goal[COL] == col) {
                    System.out.print("G"); 
                } else if (visited[row][col]) {
                    System.out.print("."); 
                } else if (blocked[row][col]) {
                    System.out.print("X"); 
                } else {
                    System.out.print(" "); 
                }
            }

            System.out.println("|");
        }

        System.out.print("*");
        for (int col = 0; col < WIDTH; col++) {
            System.out.print("-");
        }
        System.out.println("*");
    }

    public int getPlayerRow() {
        return player[ROW];
    }

    public int getPlayerCol() {
        return player[COL];
    }

    public int getGoalRow() {
        return goal[ROW];
    }

    public int getGoalCol() {
        return goal[COL];
    }

    public int getStartRow() {
        return start[ROW];
    }

    public int getStartCol() {
        return start[COL];
    }

    public boolean[][] getBlocked() {
        return copyTwoDimBoolArray(blocked);
    }

    public boolean[][] getVisited() {
        return copyTwoDimBoolArray(visited);
    }

    public Scanner getPlayerInput() {
        return playerInput;
    }

    public void setPlayerRow(int row) {
        if (row >= 0 && row < HEIGHT) {
            player[ROW] = row;
        }
    }

    public void setPlayerCol(int col) {
        if (col >= 0 && col < WIDTH) {
            player[COL] = col;
        }
    }

    public void setGoalRow(int row) {
        if (row >= 0 && row < HEIGHT) {
            goal[ROW] = row;
        }
    }

    public void setGoalCol(int col) {
        if (col >= 0 && col < WIDTH) {
            goal[COL] = col;
        }
    }

    public void setStartRow(int row) {
        if (row >= 0 && row < HEIGHT) {
            start[ROW] = row;
        }
    }

    public void setStartCol(int col) {
        if (col >= 0 && col < WIDTH) {
            start[COL] = col;
        }
    }

    public void setBlocked(boolean[][] blocked) {
        this.blocked = copyTwoDimBoolArray(blocked);
    }

    public void setVisited(boolean[][] visited) {
        this.visited = copyTwoDimBoolArray(visited);
    }

    public void setPlayerInput(Scanner playerInput) {
        this.playerInput = playerInput;
    }

    private boolean[][] copyTwoDimBoolArray(boolean[][] arrayToCopy) {
        boolean[][] copy = new boolean[arrayToCopy.length][];
        for (int i = 0; i < arrayToCopy.length; i++) {
            copy[i] = arrayToCopy[i].clone();
        }
        return copy;
    }

    private void prompt() {
        printMaze();

        System.out.print("Enter your move (up, down, left, right, or q to quit): ");
    }

    private boolean playerAtGoal() {
        return player[ROW] == goal[ROW] && player[COL] == goal[COL];
    }

    private boolean valid(int row, int col) {
        return row >= 0 && row < HEIGHT && col >= 0 && col < WIDTH && !blocked[row][col];
    }

    private void visit(int row, int col) {
        visited[row][col] = true;
    }

    private boolean makeMove(String move) {
        char direction = move.toLowerCase().charAt(0);

        int newRow = player[ROW];
        int newCol = player[COL];

        switch (direction) {
            case 'q':
                return true;

            case 'd':
                newRow++;
                break;

            case 'u':
                newRow--;
                break;

            case 'l':
                newCol--;
                break;

            case 'r':
                newCol++;
                break;

            default:
                System.out.println("Invalid move! Please enter 'u', 'd', 'l', 'r', or 'q'.");
                return false;
        }

        if (valid(newRow, newCol)) {
            setPlayerRow(newRow);
            setPlayerCol(newCol);

            visit(newRow, newCol);

            return playerAtGoal();
        } else {
            System.out.println("Move is not valid. Try a different direction.");
        }

        return false;
    }
}
