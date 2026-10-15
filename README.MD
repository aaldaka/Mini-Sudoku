# Mini Sudoku Solver

A Spring Boot console app that reads Sudoku puzzles from text files, solves them, prints each board before and after, and saves the solutions to new files.

## How it works

1. Finds every `.txt` file in `uploads/puzzles/`
2. Reads each puzzle and prints it
3. Solves it using backtracking with MRV
4. Prints the solved puzzle
5. Saves it to `uploads/solutions/` (for example `puzzle1.txt` → `puzzle1-solution.txt`)

If a file is missing or invalid, the app prints an error and moves on to the next puzzle.

## Project structure

```
src/main/java/sudoku/
├── SudokuApplication.java      runs the program
├── exception/
│   ├── InvalidCharacterException.java
│   └── SudokuFileNotFoundException.java
├── model/
│   ├── Sudoku.java             the 9x9 board, rule checking and printing
│   └── SudokuCell.java         one cell on the board
├── solver/
│   └── SudokuSolver.java       solves the puzzle
└── utils/
    └── SudokuFileUtils.java    reads puzzle files and saves solutions
```

## Puzzle file format

- 9 rows of 9 numbers
- `0` means an empty cell
- Spaces, `|` and `-----` lines are ignored

```
0  0  0  |  1  5  0  |  0  7  0
1  0  6  |  0  0  0  |  8  2  0
3  0  0  |  8  6  0  |  0  4  0
----------------------------------
...
```

## Output

The console shows each board in ASCII, with `.` for empty cells:

```
        puzzle1
+-------+-------+-------+
| . . . | 1 5 . | . 7 . |
| 1 . 6 | . . . | 8 2 . |
| 3 . . | 8 6 . | . 4 . |
+-------+-------+-------+
...
```

The saved solution files use the same format as the puzzle files.

## How the solver works

The solver uses backtracking (a depth-first search) with the MRV (Minimum Remaining Values) heuristic. Instead of filling the next empty cell in order, it always picks the empty cell with the fewest possible values. This finds dead ends earlier: if any empty cell has no possible values left, the solver backtracks straight away. That cuts down the number of guesses it has to try.

1. Look at every empty cell and pick the one with the fewest possible numbers
2. If that cell has no possible numbers, go back and change the last guess
3. Otherwise, try each possible number in that cell and move on
4. When there are no empty cells left, the puzzle is solved

## Errors

| Exception | When |
|---|---|
| `SudokuFileNotFoundException` | the puzzle file doesn't exist |
| `InvalidCharacterException` | the file has a character that isn't 0-9, or isn't 9 rows of 9 numbers |

## How to run

1. Put the puzzle files in `uploads/puzzles/`. The `uploads/puzzles/` and `uploads/solutions/` folders are created automatically if they don't exist.
2. Run `SudokuApplication` in IntelliJ, with the working directory set to the folder that contains `pom.xml`
3. The solutions should appear in `uploads/solutions/`

## Built with

- Java 17
- Spring Boot
- Maven

## Sources

1. [Sudoku solving algorithms (Wikipedia)](https://en.wikipedia.org/wiki/Sudoku_solving_algorithms)
2. [Sudoku and Backtracking (HackerNoon)](https://hackernoon.com/sudoku-and-backtracking-6613d33229af)
3. [Minimum number of clues to fully specify any sudoku? (Computer Science)] (https://cs.stackexchange.com/questions/163/minimum-number-of-clues-to-fully-specify-any-sudoku)
4. [YouDoSudoku (API)] https://www.youdosudoku.com/