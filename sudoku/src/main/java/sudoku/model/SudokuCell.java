package sudoku.model;

import lombok.Data;

@Data
public class SudokuCell {

    private int value; // 0 -> no val ie empty

    public SudokuCell(int value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == 0;
    }
}