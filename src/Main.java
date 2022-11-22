import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("example dfa.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        Vector<Vector<String>> tableVect = new Vector<>();
        String startState = "";
        String st;
        while ((st = br.readLine()) != null) {
            String[] currencies = st.split(" ");
            Vector<String> tempArray = new Vector<>();
            for(int i = 0; i < currencies.length; i++){

                if(currencies[i].contains("S")){
                    startState = currencies[i];
                    tempArray.add(currencies[i].replace("S", ""));
                } else{
                    tempArray.add(currencies[i]);
                }
            }
            tableVect.add(tempArray);
        }

        System.out.println("Start State:");
        System.out.println(startState);
        System.out.println();

        // Original Table without Start State
        System.out.println("Table:");
        System.out.println(tableVect);
        System.out.println();

        // Setup Table
        Vector<Vector<String>> xyTable = tableSetup(tableVect);

        // Print Table
        printTable(xyTable);

        // Mark Table
        markTable(xyTable);

        printTable(xyTable);

        // Find the value of each cord in the table
        Vector<Vector<String>> valueRows = new Vector<>();
        for(int j = 1; j < tableVect.get(0).size(); j++){
            Vector<String> eachRow = new Vector<>();
            for(int i = 0; i < tableVect.size(); i++){
                eachRow.add(tableVect.get(i).get(j));
            }
            valueRows.add(eachRow);
        }

        System.out.println(valueRows);
        System.out.println();

        // Find the following values until no more changes occur
        for(int i = 1; i < xyTable.size(); i++){
            for(int j = 1; j < xyTable.size(); j++){
                if(!Objects.equals(xyTable.get(i).get(j), "unm")
                        && !Objects.equals(xyTable.get(i).get(j), "rp")
                        && !Objects.equals(xyTable.get(i).get(j), "del")){

                    String values = xyTable.get(i).get(j);
                    String temp = values.substring(values.indexOf('{') + 1, values.indexOf('}'));
                    String[] currencies = temp.split(", ");

                    Vector<String> pointValue = new Vector<>();
                    for(int k = 0; k < valueRows.size(); k++){
                        pointValue.add(currencies[k]);
                    }

                    System.out.println(pointValue);
                    Vector<Vector<String>> tempT = new Vector<>();
                    for(int k = 0; k < valueRows.size(); k++){
                        Vector<String> tempHold = new Vector<>();
                        for(int z = 0; z < currencies.length; z++){
                            int strTemp = 0;
                            if(currencies[z].contains("F")){
                                strTemp = Integer.parseInt(currencies[z].replace("F", ""));
                            } else {
                                strTemp = Integer.parseInt(currencies[z]);
                            }

                            String tempStr = valueRows.get(k).get(strTemp + 1);
                            tempHold.add(tempStr);
                        }
                        tempT.add(tempHold);
                    }

                    if(currencies[1] == "2"){
                        System.out.println();
                    }

                    for(int x = 0; x < tempT.size(); x++){
                        Vector<Integer> intTemp = new Vector<>();
                        int numFinal = 0;
                        for(int z = 0; z < tempT.get(0).size(); z++){
                            if(tempT.get(x).get(z).contains("F")){
                            intTemp.add(
                                    Integer.parseInt(tempT.get(x).get(z).replace("F", "")));
                                numFinal++;
                            } else {
                            intTemp.add(Integer.parseInt(tempT.get(x).get(z)));
                            }
                        }
                        String tempStr = xyTable.get(intTemp.get(0)).get(intTemp.get(1));
                        String fi = "when";

                        if(tempStr == "del"){
                            xyTable.get(Integer.parseInt(currencies[0]))
                                    .get(Integer.parseInt(currencies[1])).replace(values, "del");
                        }
                        if(numFinal > 0 && numFinal != currencies.length){
                            xyTable.get(Integer.parseInt(currencies[0]))
                                    .get(Integer.parseInt(currencies[1])).replace(values, "del");
                        }
                    }

                    System.out.println(tempT);
                    System.out.println();

                }
            }
        }

        printTable(xyTable);


    }

    public static Vector<Vector<String>> markTable(Vector<Vector<String>> xyTable){
        for(int i = 1; i < xyTable.size(); i++){
            for(int j = 1; j < xyTable.size(); j++){
                if(!Objects.equals(xyTable.get(i).get(j), "unm")
                        && !Objects.equals(xyTable.get(i).get(j), "rp")){
                    String values = xyTable.get(i).get(j);
                    String temp = values.substring(values.indexOf('{') + 1, values.indexOf('}'));
                    String[] currencies = temp.split(", ");
                    int numFinal = 0;
                    for(int z = 0; z < currencies.length; z++){
                        if(currencies[z].contains("F")){
                            numFinal++;
                        }
                    }

                    if(numFinal > 0 && numFinal != currencies.length){
                        temp = "del";
                        Vector<String> locateCord = xyTable.get(i);
                        locateCord.set(j, temp);
                        xyTable.set(i, locateCord);
                    }

                }
            }
        }
        return xyTable;
    }

    public static void tablePairs(Vector<Vector<String>> xyTable){
        for(int x = 1; x < xyTable.size(); x++){
            Vector<String> cordsTable = new Vector<>();
            cordsTable.add(xyTable.get(x).get(0));
            for(int y = 1; y < xyTable.size(); y++){
                if(Objects.equals(xyTable.get(0).get(y), xyTable.get(x).get(0))){
                    String cord = "unm";
                    cordsTable.add(cord);
                    y++;
                    for(int z = y; y < xyTable.size(); y++){
                        cord = "rp";
                        cordsTable.add(cord);
                    }
                } else{
                    String cord = "{" + xyTable.get(0).get(y) + ", " + xyTable.get(x).get(0) + "}";
                    cordsTable.add(cord);
                }
            }
            xyTable.set(x, cordsTable);
        }

    }

    public static Vector<Vector<String>> tableSetup(Vector<Vector<String>> tableVect){
        Vector<Vector<String>> xyTable = new Vector<>();

        Vector<String> intValues = new Vector<>();
        for(int i = 0; i < tableVect.size(); i++){
            intValues.add(tableVect.get(i).get(0));
        }

        xyTable.add(intValues);
        for(int i = 1; i < intValues.size(); i++){
            Vector<String> yValues = new Vector<>();
            yValues.add(intValues.get(i));

            xyTable.add(yValues);
        }

        tablePairs(xyTable);
        return xyTable;
    }

    public static void printTable(Vector<Vector<String>> xyTable){
        for(int i = 0; i < xyTable.size(); i++){
            for(int j = 0; j < xyTable.get(i).size(); j++){
                System.out.print(xyTable.get(i).get(j) + "\t\t");
            }
            System.out.println();
        }
        System.out.println();
    }

}
