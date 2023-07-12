import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws IOException {
        while (args.length > 0) {
            if(args[0].toLowerCase().equals("help")){
                System.out.println("Problem description is given in the help.txt...");
                showHelpMessage();
                break;
            }
            if(args[0].toLowerCase().equals("generatedata")){
                System.out.println("Problem data is shown in CSV files...");
                System.out.println(" Please type run in command screen to see the solution of the problem.");
                DataHub dh = new DataHub(args[1]);
                dh.getData();
                dh.produceData();
                dh.writeToCSV();
                break;
            }
            if(args[0].toLowerCase().equals("run")){
                DataHub currentData = new DataHub(args[1]);
                currentData.readFromCSV();
                Heuristic heuristic = new Heuristic(currentData);
                heuristic.Simulate();
                break;
            }
            if(args[0].toLowerCase().equals("ekol")){
                System.out.println(" To get more information about our problem, you can type the following commands; help or generatedata.");
                System.out.println(" Help for business problem , Generatedata for viewing randomly generated output.");
                break;
            }else{
                System.out.println("To more information about our problem please write ekol.");
                break;
            }
        }

    }
    private static void showHelpMessage() {
        File file = new File("help.txt");

        try {

            Scanner scanner = new Scanner(file);


            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }


            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
}