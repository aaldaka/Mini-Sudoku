## Algorithm Idea:
The solver uses backtracking (a depth-first search) with the MRV (Minimum Remaining Values) heuristic. Instead of filling the next empty cell in order, it always picks the empty cell with the fewest possible values. This finds dead ends earlier: if any empty cell has no possible values left, the solver backtracks straight away. That cuts down the number of guesses it has to try.

# Mini Sudoku Solver

A Spring Boot console app that reads a Sudoku puzzle from a text file, solves it, prints the board before and after, and saves the solution to a new file.

## How it works

1. Reads a puzzle from `uploads/puzzles/` (for example `puzzle1.txt`)
2. Prints the puzzle
3. Solves it using backtracking + MRV
4. Prints the solved puzzle
5. Saves it to `uploads/solutions/` as `puzzle1-solution.txt`

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

## How the solver works
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

1. Put the puzzle files in `uploads/puzzles/`
3. Run `SudokuApplication` in IntelliJ
4. The solutions appear in `uploads/solutions/`

## Sources
1. https://en.wikipedia.org/wiki/Sudoku_solving_algorithms
2. https://hackernoon.com/sudoku-and-backtracking-6613d33229af