import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.*;
import java.io.*;
import java.util.Random;

public class Sadler_CFG {
    public static void main (String args[]) {

        File inputFile = null;
        if (args.length == 2) {
            inputFile = new File(args[0]);
        }
        else {
            System.out.println("Incorrect numbers of arguments.");
            System.exit(0);
        }

        //General variables needed for the program
        int numStrings = Integer.parseInt(args[1]);
        List<String> nonTerminals = new ArrayList<String>();
        List<String> terminals = new ArrayList<String>();
        String startSymbol = "";
        List<String> productions = new ArrayList<String>();
        List<CFGgenerator.Production> productionObjects = new ArrayList<CFGgenerator.Production>();

        //Reading in file and setting variable values
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String tempNonTerms = reader.readLine();
            nonTerminals = Arrays.asList(tempNonTerms.split(","));
            String tempTerms = reader.readLine();
            terminals = Arrays.asList(tempTerms.split(","));

            String tempLine;
            while((tempLine = reader.readLine()) != null && tempLine.contains("->")) {
                productions.add(tempLine);
            }

            startSymbol = tempLine;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //Creating production objects and adding them to list of all of the productions
        for (int i = 0; i < productions.size(); i++) {
            String start;
            String result;

            String[] tempProd = productions.get(i).split("->");
            start = tempProd[0];
            start = start.substring(0, start.length() - 1);
            result = tempProd[1];
            result = result.substring(1, result.length());

            CFGgenerator.Production tempProduction = new CFGgenerator.Production(start, result);
            productionObjects.add(tempProduction);
        }

        GenerateCFG(numStrings, startSymbol, productionObjects, terminals, nonTerminals);
    }

    public static void GenerateCFG(int numStrings, String startSymbol, List<CFGgenerator.Production> productions, List<String> terminals, List<String> nonTerminals) {
        String currentString = startSymbol;
        boolean isTerminal;
        List<String> resultStrings = new ArrayList<String>();

        for (int i = 0; i < numStrings; i++) {
            currentString = startSymbol;
            List<String> resultOptions = new ArrayList<String>();
            String[] currStrParts = currentString.split(" ");

            for (int j = 0; j < currStrParts.length; j++) {
                isTerminal = false;
                while (!isTerminal) {

                    for (int x = 0; x < terminals.size(); x++) {
                        if (currStrParts[j].equals(terminals.get(x))) {
                            isTerminal = true;
                        }
                    }

                    if (isTerminal) {
                        break;
                    }

                    currStrParts = currentString.split(" ");

                    resultOptions.clear();
                    for (int k = 0; k < productions.size(); k++) {
                        if (currStrParts[j].equals(productions.get(k).start)) {
                            resultOptions.add(productions.get(k).result);
                        }
                    }

                    for (int y = 0; y < resultOptions.size(); y++) {
                        if (resultOptions.get(y).equals("_EMPTY_")) {
                            resultOptions.remove(resultOptions.get(y));
                        }
                    }

                    Random rand = new Random();
                    int optionNum = rand.nextInt(resultOptions.size());

                    currStrParts[j] = resultOptions.get(optionNum);
                    currentString = String.join(" ", currStrParts);
                    currStrParts = currentString.split(" ");
                }
            }

            boolean exists = false;
            for (int j = 0; j < resultStrings.size(); j++) {
                if (resultStrings.get(j).equals(currentString)) {
                    exists = true;
                }
            }

            if (exists) {
                i = i - 1;
            }
            else {
                resultStrings.add(currentString);
            }
        }

        for (int i = 0; i < resultStrings.size(); i++) {
            System.out.println("(" + (i+1) + ") " + resultStrings.get(i));
        }
    }
}