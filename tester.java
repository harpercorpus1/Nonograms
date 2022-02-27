import java.util.ArrayList;
import java.util.Arrays;

public class tester{
    public static String pad(int BOARD_SIZE, String arg){
        return String.valueOf('0').repeat(BOARD_SIZE - arg.length()) + arg;
    }

    public static ArrayList<String> all_possible(int BOARD_SIZE){
        ArrayList<String> retList = new ArrayList<String>();
        for(int i = 0; i <  Math.pow(2, BOARD_SIZE); i++){
            retList.add(pad(BOARD_SIZE, Integer.toBinaryString(i)));
        }
        return retList;
    }

    public static ArrayList<Integer> get_pattern(String arg){
        int curSum = 0;
        ArrayList<Integer> ar = new ArrayList<Integer>();
        for(int i = 0; i < arg.length(); i++){
            if(arg.charAt(i) == '1'){
                curSum++;
            }else{
                if(curSum != 0){
                    ar.add(curSum);
                    curSum = 0;
                }
            }
        }
        if(curSum != 0){
            ar.add(curSum);
        }
        if(ar.size() == 0){
            ar.add(0);
        }
        return ar;
    }

    /**
     * @param potential
     * @param current_pattern - contains '?' characters
     * @return
     */

    public static boolean contains (String potential, String current_pattern){
        if(potential.length() != current_pattern.length()) return false;
        for(int i = 0; i < current_pattern.length(); i++){

            if(current_pattern.charAt(i) != '?' && current_pattern.charAt(i) != potential.charAt(i)){

                return false;
            }
        }
        return true;
    }

    public static ArrayList<String> get_all_matching_pattern(int[] panel_pattern, String current_pattern, int BOARD_SIZE){
        ArrayList<String> all_solutions = all_possible(BOARD_SIZE);

        ArrayList<String> all_contains = new ArrayList<>();

        for(int i = 0; i < all_solutions.size(); i++){
            if(contains(all_solutions.get(i), "??11???????10??")){
                all_contains.add(all_solutions.get(i));
            }
        }

        ArrayList<String> sol = new ArrayList<>();
        for(int i = 0; i < all_contains.size(); i++){
            ArrayList<Integer> test = get_pattern(all_contains.get(i));
            if(test.size() != panel_pattern.length) continue;
            for(int j = 0; j < panel_pattern.length; j++){
                if(test.get(j) != panel_pattern[j]){
                    break;
                }
                if(j == (panel_pattern.length-1)){
                    sol.add(all_contains.get(i));
                }
            }
        }

        return sol;
    }

    public static String get_guaranteed(ArrayList<String> possible, int BOARD_SIZE){
        if(possible.size() == 0){
            return String.valueOf('?').repeat(BOARD_SIZE);
        }
        int[] counts = new int[BOARD_SIZE];
        for(int i = 0; i < possible.size(); i++){
            for(int j = 0; j < possible.get(i).length(); j++){
                if(possible.get(i).charAt(j) == '1'){
                    counts[j]++;
                }
            }
        }
        String retString = "";
        for(int i = 0; i < counts.length; i++){
            if(counts[i] == 0){
                retString += '0';
            }else if(counts[i] == possible.size()){
                retString += '1';
            }else{
                retString += '?';
            }
        }
        return retString;
    }



    public static void main(String[] args){   
        int BOARD_SIZE = 15;
        int[] pattern = {3, 2, 2, 1, 1};
        ArrayList<String> as = get_all_matching_pattern(pattern, "???11??????10??", BOARD_SIZE);

        for(int i = 0; i < as.size(); i++){
            System.out.println(as.get(i));
        }

        System.out.println(Integer.toString(as.size()) + " solutions");

        String guaranteed = get_guaranteed(as, BOARD_SIZE);

        System.out.println("Guaranteed: " + guaranteed);

        // ArrayList<String> all_solutions = all_possible(BOARD_SIZE);

        // ArrayList<String> all_contains = new ArrayList<>();

        // for(int i = 0; i < all_solutions.size(); i++){
        //     if(contains(all_solutions.get(i), "??11???????10??")){
        //         all_contains.add(all_solutions.get(i));
        //     }
        // }

        // for(int i = 0; i < all_contains.size(); i++){
        //     System.out.println(all_contains.get(i));
        // }
        // System.out.println(Integer.toString(all_contains.size()) + " Solutions");

        // ArrayList<String> sol = new ArrayList<>();
        // for(int i = 0; i < all_contains.size(); i++){
        //     ArrayList<Integer> test = get_pattern(all_contains.get(i));
        //     // System.out.print(all_contains.get(i));
        //     // System.out.print("\t");
        //     // System.out.println(test);
        //     if(test.size() != pattern.length) continue;
        //     for(int j = 0; j < pattern.length; j++){
        //         if(test.get(j) != pattern[j]){
        //             break;
        //         }
        //         if(j == (pattern.length-1)){
        //             sol.add(all_contains.get(i));
        //         }
        //     }
        // }

        // System.out.println(Integer.toString(sol.size()) + " Solutions Complete");

        // ArrayList<String> solutions = new ArrayList<>();
        // for(int i = 0; i < all_contains.size(); i++){
        //     ArrayList<Integer> test = get_pattern(all_contains.get(i));
        //     for(int j = 0; j < test.size(); j++){
        //         if(test.get(j) != pattern[j]){
        //             // System.out.print(test.get(j));
        //             // System.out.print(" != ");
        //             // System.out.println(pattern[j]);
        //             break;
        //         }
        //         if(j == (all_contains.size()-1)){
        //             solutions.add(all_contains.get(i));
        //         }
        //     }
        // }
        // for(int i = 0; i < solutions.size(); i++){
        //     System.out.println(solutions.get(i));
        // }
        // System.out.println(solutions.size());
        // if(contains("011101101101001", "??11???????10??")){
        //     System.out.println("True");
        // }else{
        //     System.out.println("False");
        // }

        // ArrayList<Integer> pat = get_pattern("011101101101001");
        // System.out.println(pat);
    }

}
