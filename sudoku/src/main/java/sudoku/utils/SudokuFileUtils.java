package sudoku.utils;

import sudoku.exception.InvalidCharacterException;
import sudoku.exception.SudokuFileNotFoundException;
import sudoku.model.Sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SudokuFileUtils {

    private static final String PUZZLE_DIR = "uploads/puzzles/";
    private static final String SOLUTION_DIR = "uploads/solutions/";

    public static int[][] readPuzzle(String fileName) {
        new File(PUZZLE_DIR).mkdirs();
        int[][] numbers = new int[9][9];
        int row = 0;

        try (Scanner scanner = new Scanner(new File(PUZZLE_DIR + fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().replace("|", " ").trim();

                if (line.isEmpty() || line.startsWith("-")) {
                    continue;
                }

                if (row == 9) { // 0-8
                    throw new InvalidCharacterException("The puzzle has more than 9 rows");
                }

                String[] parts = line.split("\\s+"); // remove the spaces if theres any
                if (parts.length != 9) {
                    throw new InvalidCharacterException("Row " + (row + 1) + " should have 9 numbers but has " + parts.length);
                }

                for (int col = 0; col < 9; col++) {
                    numbers[row][col] = toNumber(parts[col], row);
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            throw new SudokuFileNotFoundException("Can't find the file " + fileName);
        }

        if (row != 9) {
            throw new InvalidCharacterException("The puzzle only has " + row + " rows");
        }
        return numbers;
    }

    private static int toNumber(String text, int row) {
        int number;
        try {
            number = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new InvalidCharacterException("Invalid character '" + text + "' in row " + (row + 1));
        }

        if (number < 0 || number > 9) {
            throw new InvalidCharacterException("Invalid number " + number + " in row " + (row + 1));
        }
        return number;
    }

    public static String saveSolution(Sudoku sudoku, String fileName) {
        new File(SOLUTION_DIR).mkdirs();
        String solutionPath = SOLUTION_DIR + fileName.replace(".txt", "-solution.txt");

        try (PrintWriter writer = new PrintWriter(solutionPath)) {
            writer.print(sudoku.boardToString()); // used in printing to console + saving to txt
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't save the solution to " + solutionPath);
        }
        return solutionPath;
    }
}