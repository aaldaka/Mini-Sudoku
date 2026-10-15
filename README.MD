
/


























Readme · MD
# Mini Sudoku Solver

A Spring Boot console app that reads Sudoku puzzles from text files, solves them, prints each board before and after, and saves the solutions to new files. It also has a play mode where you solve a puzzle yourself, with difficulty levels, a timer, and new puzzles downloaded from an online API.

## How it works

When the app starts, it shows a menu:

```
1 = Solve all puzzle files
2 = Play
```

### 1. Solve all puzzle files

1. Finds every `.txt` file in `uploads/puzzles/`
2. Reads each puzzle and prints it
3. Solves it using backtracking with MRV
4. Prints the solved puzzle
5. Saves it to `uploads/solutions/` (for example `puzzle1.txt` → `puzzle1-solution.txt`)
   If a file is missing or invalid, the app prints an error and moves on to the next puzzle.

### 2. Play

1. Choose a difficulty: `easy`, `medium` or `hard`
2. The game downloads a new puzzle of that difficulty from the [YouDoSudoku API](#online-puzzles-api) and saves it as `puzzleN-online.txt`. If that fails, it picks a local puzzle of the same difficulty instead
3. Fill in the board. Numbers you place are shown in lavender, starting numbers in the normal color
4. When the puzzle is solved, the time is shown and the result is saved to `uploads/solutions/`
   | Command | What it does |
   |---|---|
   | `row col value` | Puts a number in a cell, e.g. `1 3 4` puts a 4 in row 1, column 3 (rows and columns are 1-9) |
   | `row col 0` | Clears a cell |
   | `?` | Shows the options: continue playing, let the computer solve it, or quit |

`solve` and `quit` can also be typed directly as shortcuts.

Moves are checked before they are placed: the input must be three numbers in range, starting numbers can't be changed, and a number can't repeat in its row, column or 3x3 box.

## Project structure

```
src/main/java/sudoku/
├── SudokuApplication.java      the menu: solve all files or play
├── exception/
│   ├── InvalidCharacterException.java
│   └── SudokuFileNotFoundException.java
├── model/
│   ├── Sudoku.java             the 9x9 board, rule checking, difficulty and printing
│   └── SudokuCell.java         one cell on the board (value + whether it's a starting number)
├── solver/
│   ├── SudokuSolver.java       solves the puzzle
│   └── SudokuGame.java         play mode: difficulty, moves and timer
└── utils/
    ├── SudokuFileUtils.java    reads puzzle files and saves puzzles and solutions
    └── SudokuApiClient.java    downloads puzzles from the YouDoSudoku API
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

Solution files in `uploads/solutions/` have a title, the solved board and some stats:

```
puzzle1 Solved
==================================
4  2  8  |  1  5  9  |  6  7  3
1  9  6  |  3  7  4  |  8  2  5
3  7  5  |  8  6  2  |  9  4  1
----------------------------------
...
==================================
Difficulty: medium
Solved by: the computer
Solved in: 2 ms
```

## Difficulty levels

The difficulty of a local puzzle is based on how many starting numbers (clues) it has. The fewer clues, the harder the puzzle.

| Clues | Difficulty |
|---|---|
| 40 or more | Easy |
| 32 to 39 | Medium |
| 31 or fewer | Hard |

## Online puzzles (API)

In play mode, the game downloads a new puzzle from the [YouDoSudoku API](https://www.youdosudoku.com/) using the difficulty the player chose. If that fails (no internet, no API key, or the site is down), it falls back to a local puzzle of the same difficulty from `uploads/puzzles/`.

Downloaded puzzles are saved to `uploads/puzzles/` as `puzzle1-online.txt`, `puzzle2-online.txt` and so on, so they can be reused later and solved with option 1.

### Request

```
POST https://youdosudoku.com/api
```

Headers:

```
Content-Type: application/json
x-api-key: <your API key>
```

Body:

```json
{
  "difficulty": "hard",
  "solution": false,
  "array": false
}
```

| Field | Meaning |
|---|---|
| `difficulty` | `easy`, `medium` or `hard` (the level the player chose) |
| `solution` | `false`, because the app's own solver handles solving |
| `array` | `false`, so the puzzle comes back as one 81-digit string |

### Response

A real response the app received:

```json
{
  "difficulty": "hard",
  "puzzle": "000000050000610002100084900800950730000006015000000800030002067005090000020300000"
}
```

The `puzzle` string holds the 81 cells row by row (characters 0-8 are row 1, 9-17 are row 2, and so on), with `0` meaning an empty cell. `SudokuApiClient` turns it into a 9x9 grid, checking that it is exactly 81 digits. This puzzle has 25 clues, so the app's own difficulty check agrees that it is hard.

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
| `InvalidCharacterException` | the file (or the API response) has a character that isn't 0-9, or isn't 9 rows of 9 numbers |

## How to run

### 1. Get an API key (for online puzzles)

1. Go to [youdosudoku.com](https://www.youdosudoku.com/) and generate a free API key in the API section
2. In IntelliJ, open **Run → Edit Configurations → SudokuApplication**
3. In **Environment variables**, add:
```
   SUDOKU_API_KEY=your-key-here
```
Write the key without quotes or spaces
4. Click **Apply** and **OK**

The key is read from this environment variable so it is never stored in the code or on GitHub. Without a key, play mode still works and uses the local puzzles instead.

### 2. Run the app

1. Put the puzzle files in `uploads/puzzles/` (next to `pom.xml`). The `uploads/puzzles/` and `uploads/solutions/` folders are created automatically if they don't exist
2. Make sure the run configuration's **Working directory** is the folder that contains `pom.xml`
3. Run `SudokuApplication`
4. Click inside the Run window and type `1` (solve all files) or `2` (play)
5. Solutions appear in `uploads/solutions/`, and downloaded puzzles in `uploads/puzzles/`
## Bonuses

- **Interactivity:** play mode with a menu, typed moves and a `?` options menu
- **Difficulty levels:** easy, medium and hard, from the API or by clue count for local puzzles
- **Timer:** shows how long the puzzle took and saves it with the solution
- **Input validation:** moves must be numbers in range, can't change starting numbers, and can't break a rule
## Built with

- Java 17
- Spring Boot
- Maven
## Sources

1. [Sudoku solving algorithms (Wikipedia)](https://en.wikipedia.org/wiki/Sudoku_solving_algorithms)
2. [Sudoku and Backtracking (HackerNoon)](https://hackernoon.com/sudoku-and-backtracking-6613d33229af)
3. [Minimum number of clues to fully specify any sudoku? (Computer Science Stack Exchange)](https://cs.stackexchange.com/questions/163/minimum-number-of-clues-to-fully-specify-any-sudoku)
4. [YouDoSudoku API](https://www.youdosudoku.com/)