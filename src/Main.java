import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("example dfa.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));


        String st;
        Vector<Vector<String>> tableVect = new Vector<>();
        while ((st = br.readLine()) != null) {
            System.out.println(st);
            String[] currencies = st.split(" ");
            Vector<String> tempArray = new Vector<>();
            for(int i = 0; i < currencies.length; i++){
                tempArray.add(currencies[i]);
            }
            tableVect.add(tempArray);
        }
        System.out.println(tableVect);

    }
}
