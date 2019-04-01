import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

public class lexicalAnalysis {
    static private Map<Integer, Map<String, TreeSet<Integer>>> Transition = new TreeMap<>();

    static private void FillReservedWords(TreeSet<String>ReservedWords){
        ReservedWords.add("const");
        ReservedWords.add("func");
        ReservedWords.add("return");
    }

    static private void FillSeperators(TreeSet<String>Seperators){
        Seperators.add("/n");
        Seperators.add("/t");
        Seperators.add(" ");
    }

    static private void FillAlphabet(HashSet<TreeSet<String>> Alphabet,TreeSet<String> Lexem){
        Alphabet.add(Lexem);
    }

    static void add_Edge(int from,int to,String str){
        if(!Transition.containsKey(from)){
            Transition.put(from,new TreeMap<>());
        }
        Map <String,TreeSet<Integer>> edge_of_state = Transition.get(from);
        if (!edge_of_state.containsKey(str)){
            edge_of_state.put(str,new TreeSet<Integer>());
        }
        edge_of_state.get(str).add(to);
    }

    static void show_Edges(HashSet<TreeSet<String>>Alphabet) {
        Integer i = 1;
        for (TreeSet<String> tmp : Alphabet){
            if (i==1){
                for (String TmpStr : tmp ){
                    add_Edge(i-1,i-1,TmpStr);
                    System.out.printf("(" + (i-1) + ",%s)-> " + (i-1) + " \n",TmpStr );
                }
            } else {
                for (String TmpStr : tmp ){
                    add_Edge(i-2,i-1,TmpStr);
                    System.out.printf("(" + (i-2) + ",%s)-> " + (i-1) + " \n",TmpStr );
                }
            }
            i++;
        }
    }

//    static void add_edge(int from,int to,String str){
//
//    }

    public static void main(String [] args) throws Exception {
        TreeSet<String> ReservedWords = new TreeSet<>();
        FillReservedWords(ReservedWords);

        TreeSet<String> Separators = new TreeSet<>();
        FillSeperators(Separators);

        HashSet<TreeSet<String>> Alphabet = new LinkedHashSet<>();
        FillAlphabet(Alphabet,Separators);
        FillAlphabet(Alphabet,ReservedWords);

        show_Edges(Alphabet);

        File file = new File("/home/cyberstar/IdeaProjects/sys_programming_3/src/main.go");

        try {

            Scanner sc = new Scanner(file);
            String LineWithWords = "";

            while(sc.hasNextLine()){

                LineWithWords=sc.nextLine();

                while (LineWithWords.length()!=0) {

                    String TargetWord = "";
                    Integer index = 0;
                    Integer LexemNum = 0;

                    TreeSet<Integer> S0 = new TreeSet<>();
                    S0.add(0);

                    for (TreeSet<String> lexem : Alphabet) {

                        for (String WordFromLexem : lexem) {
                            index = LineWithWords.indexOf(WordFromLexem);
                            if (index == 0) {
                                TreeSet<Integer> S = new TreeSet<>();
                                TargetWord = WordFromLexem;

                                for (Integer elem : S0) {
                                    TreeSet<Integer> tmp = Transition.get(elem).get(WordFromLexem);
                                    if (tmp == null) {
                                        continue;
                                    }
                                    for (Integer tmpelem : tmp) {
                                        S.add(tmpelem);
                                    }
                                }

                                System.out.printf(" Word: %s \n",WordFromLexem);
                                System.out.print(" States:");
                                for (Integer elem:S){
                                    System.out.printf(" %d", elem);
                                }
                                System.out.print("\n");
                                S0=S;
                                System.out.print(" Final States:");
                            for (Integer elem:S0){
                                System.out.printf(" %d \n", elem);
                            }

                            String TextLexem = "";

                            switch (LexemNum){
                                case 1: TextLexem="This word " + WordFromLexem + " is reserved word\n";
                                break;
                                case 0: TextLexem="This word " + WordFromLexem + " is separator\n";
                                break;
                            }

                            System.out.print(TextLexem);

                            }
                        }

                        LexemNum++;
                    }

                    Integer FirstIndexOfNewLine=TargetWord.length();
                    LineWithWords=LineWithWords.substring(FirstIndexOfNewLine);
                }

            }

        } catch (FileNotFoundException e) {
            System.out.format("Could not find file '%s'.%n", file);
            System.exit(1);
        }

    }

}
