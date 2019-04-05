import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

public class lexicalAnalysis {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_MAGENTA = "\u001B[35m";

    static private Map<Integer, Map<String, TreeSet<Integer>>> Transition = new TreeMap<>();

    static private void FillFinalStates(TreeSet<Integer> FinalStates){
        FinalStates.add(0);
        FinalStates.add(1);
        FinalStates.add(2);
        FinalStates.add(4);
        FinalStates.add(6);
        FinalStates.add(8);
        FinalStates.add(10);
        FinalStates.add(11);
        FinalStates.add(12);
        FinalStates.add(13);
    }

    static private void FillReservedWords(LinkedHashSet<String> ReservedWords) {
        ReservedWords.add("const");
        ReservedWords.add("func");
        ReservedWords.add("var");
        ReservedWords.add("int");
        ReservedWords.add("char");
    }

    static private void FillSeperators(LinkedHashSet<String> Seperators) {
        Seperators.add("\n");
        Seperators.add("\t");
        Seperators.add(" ");
    }

    static private void FillOperators(LinkedHashSet<String> Operators) {
        Operators.add(":=");
        Operators.add("-");
        Operators.add("+");
        Operators.add(">=");
        Operators.add("<=");
        Operators.add("==");
        Operators.add("++");
        Operators.add("--");
    }

    static private void FillNumbers(LinkedHashSet<String> Numbers){
        for (int i=0;i<10;i++){
            String number = String.valueOf(i);
            Numbers.add(number);
        }
    }

    static private void FillFloatNumbers(LinkedHashSet<String> FloatNumbers){
        FloatNumbers.add(".");
        for (int i=0;i<10;i++){
            String number = String.valueOf(i);
            FloatNumbers.add(number);
        }
    }

    static private void FillIdentifiers(LinkedHashSet<String> Identifiers) {
        Identifiers.add("_");
        for (int i=0;i<26;i++){
            String big=String.valueOf((char)('A'+i));
            String small=String.valueOf((char)('a'+i));
            Identifiers.add(big);
            Identifiers.add(small);
        }
    }

    static private void FillComment1(LinkedHashSet<String> Comment) {
        Comment.add("//");
        for (int i=0;i<26;i++){
            String big=String.valueOf((char)('A'+i));
            String small=String.valueOf((char)('a'+i));
            Comment.add(big);
            Comment.add(small);
        }
        Comment.add(":=");
        Comment.add("-");
        Comment.add("+");
        Comment.add(">=");
        Comment.add("<=");
        Comment.add("==");
        Comment.add("++");
        Comment.add("--");
        Comment.add("\t");
        Comment.add(" ");
        Comment.add("#");
        Comment.add("&");
        Comment.add("_");
        Comment.add("\n");
    }

    static private void FillComment2(LinkedHashSet<String> Comment) {
        Comment.add("/*");
        Comment.add("const");
        Comment.add("func");
        Comment.add(" ");
        Comment.add("\t");
        Comment.add("_");
        Comment.add("a");
        Comment.add("c");
        Comment.add("b");
        Comment.add("=");
        Comment.add("-");
        Comment.add("+");
        Comment.add("*/");
    }

    static private void FillString(LinkedHashSet <String> string ){
        string.add("\"");
        string.add("_");
        for (int i=0;i<26;i++){
            String big= String.valueOf((char)('A'+i));
            String small= String.valueOf((char)('a'+i));
            string.add(big);
            string.add(small);
        }
    }

    static private void FillCharacter(LinkedHashSet <String> character ){
        character.add("'");
        character.add("_");
        for (int i=0;i<26;i++){
            String big= String.valueOf((char)('A'+i));
            String small= String.valueOf((char)('a'+i));
            character.add(big);
            character.add(small);
        }
    }

    static private void FillAlphabet(HashSet<LinkedHashSet<String>> Alphabet, LinkedHashSet<String> Lexem) {
        Alphabet.add(Lexem);
    }

    static void add_Edge(int from, int to, String str) {
        if (!Transition.containsKey(from)) {
            Transition.put(from, new TreeMap<>());
        }
        Map<String, TreeSet<Integer>> edge_of_state = Transition.get(from);
        if (!edge_of_state.containsKey(str)) {
            edge_of_state.put(str, new TreeSet<Integer>());
        }
        edge_of_state.get(str).add(to);
    }

    static private void show_Edges(HashSet<LinkedHashSet<String>> Alphabet) {
        int i = 0,c = 0;
        for (LinkedHashSet<String> ClassLexem : Alphabet) {
            if (i == 0) {
                for (String word : ClassLexem) {
                    add_Edge(0, 0, word);
                    System.out.printf("(" + 0 + ",%s)-> " + 0 + " \n", word);
                }
                i=i+1;
            } else if (i == 3 || i==5 ) {
                for (String word : ClassLexem) {
                    int size = ClassLexem.size();
                    if (c == 0) {
                        add_Edge(0, i, word);
                        System.out.printf("(" + 0 + ",%s)-> " + i + " \n", word);
                        c=c+1;
                    } else if (c == size-1) {
                        add_Edge(i, i + 1, word);
                        System.out.printf("(" + i + ",%s)-> " + (i + 1) + " \n", word);
                    } else {
                        add_Edge(i, i, word);
                        System.out.printf("(" + i + ",%s)-> " + i + " \n", word);
                        c=c+1;
                    }
                }
                i=i+2;
                c=0;
            } else if (i == 7 || i==9 ) {
                String close ="";
                for (String word : ClassLexem) {
                    int size = ClassLexem.size();
                    if (c == 0) {
                        close=word;
                        add_Edge(0, i, word);
                        System.out.printf("(" + 0 + ",%s)-> " + i + " \n", word);
                        c=c+1;
                    } else if (c == size-1) {
                        add_Edge(i, i + 1, close);
                        System.out.printf("(" + i + ",%s)-> " + (i + 1) + " \n", close);
                    } else {
                        add_Edge(i, i, word);
                        System.out.printf("(" + i + ",%s)-> " + i + " \n", word);
                        c=c+1;
                    }
                }
                i=i+2;
                c=0;
            } else if (i==11){
                String point="";
                for (String word : ClassLexem){
                    if(c==0){
                        add_Edge(i, i+1, word);
                        System.out.printf("(" + i + ",%s)-> " + (i+1) + " \n", word);
                        c=c+1;
                    }else {
                        add_Edge(0,i,word);
                        add_Edge(i,i,word);
                        add_Edge(i+1,i+1,word);
                        System.out.printf("(%d,%s)-> %d\n",0, word,i);
                        System.out.printf("(" + i + ",%s)-> " + i + " \n", word);
                        System.out.printf("(" + (i + 1) + ",%s)-> " + (i + 1) + " \n", word);
                        c=c+1;
                    }
                }
                i=i+2;
                c=0;
            } else {
                for (String word : ClassLexem) {
                    add_Edge(0, i, word);
                    System.out.printf("(" + 0 + ",%s)-> " + i + " \n", word);
                }
                i=i+1;
            }
        }
    }

    public static void main(String [] args) throws Exception {
        TreeSet<Integer>FinalStates = new TreeSet<>();
        FillFinalStates(FinalStates);

        LinkedHashSet<String> Separators = new LinkedHashSet<>();
        FillSeperators(Separators);

        LinkedHashSet<String> Comment1 = new LinkedHashSet<>();
        FillComment1(Comment1);
        LinkedHashSet<String> Comment2 = new LinkedHashSet<>();
        FillComment2(Comment2);

//        LinkedHashSet<String> Numbers = new LinkedHashSet<>();
//        FillNumbers(Numbers);

        LinkedHashSet<String> ReservedWords = new LinkedHashSet<>();
        FillReservedWords(ReservedWords);

        LinkedHashSet<String > Operators = new LinkedHashSet<>();
        FillOperators(Operators);

        LinkedHashSet<String> Identifiers = new LinkedHashSet<>();
        FillIdentifiers(Identifiers);

        LinkedHashSet<String> string = new LinkedHashSet<>();
        FillString(string);

        LinkedHashSet<String> character = new LinkedHashSet<>();
        FillCharacter(character);

        LinkedHashSet<String> floatNumbers = new LinkedHashSet<>();
        FillFloatNumbers(floatNumbers);

        HashSet<LinkedHashSet<String>> Alphabet = new LinkedHashSet<>();

        FillAlphabet(Alphabet,Separators);
        FillAlphabet(Alphabet,ReservedWords);
//        FillAlphabet(Alphabet,Numbers);
        FillAlphabet(Alphabet,Operators);
        FillAlphabet(Alphabet,Comment1);
        FillAlphabet(Alphabet,Comment2);
        FillAlphabet(Alphabet,string);
        FillAlphabet(Alphabet,character);
        FillAlphabet(Alphabet,floatNumbers);
        FillAlphabet(Alphabet,Identifiers);

        show_Edges(Alphabet);
        File file = new File("/home/cyberstar/IdeaProjects/sys_programming_3/src/text.go");
        try {
            Scanner sc = new Scanner(file);
            String text ="";
            while (sc.hasNextLine()){
                text=text.concat(sc.nextLine());
                text=text.concat("\n");
            }

            ArrayList <String> edges = new ArrayList<>();
            int index =0;
            String tmp ="";
            while (text.length()!=0) {
                for (LinkedHashSet<String> lexem : Alphabet) {
                    for (String WordFromLexem : lexem) {
                        index = text.indexOf(WordFromLexem);
                        if (index == 0) {
                            tmp = WordFromLexem;
                            edges.add(tmp);
                            break;
                        }
                    }
                    int FirstIndexOfNewLine = tmp.length();
                    tmp="";
                    text = text.substring(FirstIndexOfNewLine);
                    if (index==0) {break;}
                }
            }

            TreeSet <Integer> S0 = new TreeSet<>();
            S0.add(0);
            for (String edge : edges) {
                TreeSet<Integer> S = new TreeSet<>();
                for (Integer state : S0) {
                    TreeSet<Integer> t = Transition.get(state).get(edge);
                    if (t == null) {
                        System.out.print(" Oy, I can't colored lexem ");
                        continue;
                    }
                    S.addAll(t);
                }
//                System.out.printf("Word: %s \n", edge);
//                System.out.print("States:");
                for (Integer elem : S) {
//                    System.out.printf(" %d", elem);
//                    System.out.print("\n");
                    String message=" ";
                    switch(elem){
                        case 0: message=edge;
                            break;
                        case 1: message= ANSI_RED + edge + ANSI_RESET ;
                            break;
                        case 2: message= ANSI_GREEN + edge + ANSI_RESET ;
                            break;
                        case 3: message= ANSI_CYAN + edge + ANSI_RESET ;
                            break;
                        case 4: message= ANSI_CYAN + edge + ANSI_RESET;
                            break;
                        case 5: message= ANSI_CYAN + edge + ANSI_RESET ;
                            break;
                        case 6: message= ANSI_CYAN + edge + ANSI_RESET ;
                            break;
                        case 7: message= ANSI_MAGENTA + edge + ANSI_RESET;
                            break;
                        case 8: message= ANSI_MAGENTA + edge + ANSI_MAGENTA;
                            break;
                        case 9: message= ANSI_MAGENTA + edge + ANSI_MAGENTA;
                            break;
                        case 10: message= ANSI_MAGENTA + edge + ANSI_MAGENTA;
                            break;
                        case 11: message= ANSI_YELLOW + edge + ANSI_MAGENTA;
                            break;
                        case 12: message= ANSI_YELLOW + edge + ANSI_MAGENTA;
                            break;
                        case 13: message= ANSI_BLUE + edge + ANSI_MAGENTA;
                            break;

                    }
                    System.out.print(message);
                }
                for (Integer be_a_final : S){
                    if (FinalStates.contains(be_a_final)){
                        S0.clear();
                        S0.add(0);
                    }else{
                        S0=S;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.format("Could not find file '%s'.%n", file);
            System.exit(1);
        }

    }


}