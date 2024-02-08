import java.io.File;
import java.io.IOException;
import Sat.*;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
public class Driver {

    public static void main(String[] args) throws IOException, ContradictionException, ParseFormatException {
        //File file = new File("SudokuInputs/puzsim_1");
        //File file = new File("SudokuInputs/puz1");
        //File file = new File("SudokuInputs/puz2");
        //File file = new File("SudokuInputs/puz3");
        //File file = new File("SudokuInputs/puz4");
        File file = new File("SudokuInputs/unsolvable");

        System.out.println("now solving file: " + file.getName());
        Formulator formulator = new Formulator(file);
        Solver solver = new Solver(formulator);
    }
}
