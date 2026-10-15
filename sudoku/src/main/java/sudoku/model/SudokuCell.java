package sudoku.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SudokuCell {

    private int value; // 0 -> no val ie empty
    private final boolean given;

    public SudokuCell(int value){
        this.value = value;
        this.given = value != 0; //0 means not give, else a number means it by defualt there
    }

    public boolean isEmpty() {
        return value == 0;
    }
    public boolean isGiven(){return given;}

}