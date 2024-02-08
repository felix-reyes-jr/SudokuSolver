package Sat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Felix Reyes
 * @date 2/27/22
 * a class that represents a clause in a boolean formula
 */
public class Clause {

    /**
     * map of the variable numbers for the clause and a boolean value dictating whether the variable is negated
     */
    public Map<Integer,Boolean> clauseVars = new HashMap<>();

    /**
     * list of all variable numbers for the clause, strictly used for output
     */
    public List<Integer> varList = new ArrayList<>();

    /**
     * adds a variable number n to the clauseVars list and given a boolean value to denote if negated
     * adds a variable number to the varList
     * @param n the variable number to be added
     */
    public void addVariable(int n){
        if(n< 0) {
            clauseVars.put(Math.abs(n), true);
            varList.add(n);
        }
        else {
            clauseVars.put(n, false);
            varList.add(n);
        }
    }

    /**
     *
     * @param list the list of assignments to be passed against clauseVars
     * @return true if the assignment to the corresponing variable is 1(true) and is not negated or
     * if the assignment to the corresponing variable is 0(false) and is negated
     * @return false iff both of the above are not true
     */
    public boolean isSat(List<Integer>  list) {
        for (Map.Entry<Integer, Boolean> set : clauseVars.entrySet()) {
            int varNum = set.getKey();
            int assignment = list.get(varNum-1);
            boolean negated = set.getValue();
            if(negated == false && assignment == 1){
                return true;
            }
            if(negated == true && assignment == 0){
                return true;
            }
        }
        return false;
    }


    /**
     * a toString method for the clause class
     * @return the formatted clause in (#x V #x) format as a string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        String separator = "";

        for (Integer binaryVariable : varList) {
            sb.append(separator);
            separator = String.valueOf(" ");

            sb.append(binaryVariable + "");
        }

        return sb.append("").toString();
    }

}