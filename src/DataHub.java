import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class DataHub {
    //variables to read data
    int[][] countBounds,chargeBounds,lifetimeBounds,chargeTimeBounds,taskCountBounds,taskLengthBounds;
    int shift1mins, shift2mins;
    int start1_m,start2_m,end1_m,end2_m,start1_h,start2_h,end1_h,end2_h;
    int[] stationBounds,employeeBounds;
    ArrayList<String> nameList;
    //variables to produce data
    int[] counts,taskCounts;
    ArrayList<Integer>[] initialCharges,lifetimes,chargeTimes,taskLengths;
    int workingHours,numStations,numWorkers;
    String[] names;
    String outname;

    public DataHub(String out) throws IOException {
        counts = new int[3];
        initialCharges = new ArrayList[3];
        lifetimes = new ArrayList[3];
        chargeTimes = new ArrayList[3];
        workingHours = 0;
        taskCounts = new int[3];
        taskLengths = new ArrayList[3];
        numStations = 0;
        numWorkers = 0;
        names = new String[3];
        outname = out;
        for (int i = 0; i < 3; i++) {
            initialCharges[i] = new ArrayList<Integer>();
            lifetimes[i] = new ArrayList<Integer>();
            chargeTimes[i] = new ArrayList<Integer>();
            taskLengths[i] = new ArrayList<Integer>();
        }

    }
    public void getData() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File("Config.yaml"));
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);
        nameList = (ArrayList<String>) data.get("types");
        Map<String,Map<String,Integer>> equipmentData = (Map<String, Map<String,Integer>>) data.get("equipmentConfig");
        countBounds = new int[3][2];
        countBounds[0][0] = equipmentData.get("equipment0").get("countLowerBound");
        countBounds[0][1] = equipmentData.get("equipment0").get("countUpperBound");
        countBounds[1][0] = equipmentData.get("equipment1").get("countLowerBound");
        countBounds[1][1] = equipmentData.get("equipment1").get("countUpperBound");
        countBounds[2][0] = equipmentData.get("equipment2").get("countLowerBound");
        countBounds[2][1] = equipmentData.get("equipment2").get("countUpperBound");
        chargeBounds = new int[3][2];
        chargeBounds[0][0] = equipmentData.get("equipment0").get("chargeLowerBound");
        chargeBounds[0][1] = equipmentData.get("equipment0").get("chargeUpperBound");
        chargeBounds[1][0] = equipmentData.get("equipment1").get("chargeLowerBound");
        chargeBounds[1][1] = equipmentData.get("equipment1").get("chargeUpperBound");
        chargeBounds[2][0] = equipmentData.get("equipment2").get("chargeLowerBound");
        chargeBounds[2][1] = equipmentData.get("equipment2").get("chargeUpperBound");
        lifetimeBounds = new int[3][2];
        lifetimeBounds[0][0] = equipmentData.get("equipment0").get("usageTimeLowerBound");
        lifetimeBounds[0][1] = equipmentData.get("equipment0").get("usageTimeUpperBound");
        lifetimeBounds[1][0] = equipmentData.get("equipment1").get("usageTimeLowerBound");
        lifetimeBounds[1][1] = equipmentData.get("equipment1").get("usageTimeUpperBound");
        lifetimeBounds[2][0] = equipmentData.get("equipment2").get("usageTimeLowerBound");
        lifetimeBounds[2][1] = equipmentData.get("equipment2").get("usageTimeUpperBound");
        chargeTimeBounds = new int[3][2];
        chargeTimeBounds[0][0] = equipmentData.get("equipment0").get("chargingTimeLowerBound");
        chargeTimeBounds[0][1] = equipmentData.get("equipment0").get("chargingTimeUpperBound");
        chargeTimeBounds[1][0] = equipmentData.get("equipment1").get("chargingTimeLowerBound");
        chargeTimeBounds[1][1] = equipmentData.get("equipment1").get("chargingTimeUpperBound");
        chargeTimeBounds[2][0] = equipmentData.get("equipment2").get("chargingTimeLowerBound");
        chargeTimeBounds[2][1] = equipmentData.get("equipment2").get("chargingTimeUpperBound");
        Map<String,Map<String,Integer>> workingHourData = (Map<String, Map<String,Integer>>) data.get("workingHourConfig");
        start1_h =workingHourData.get("shift1").get("startHour");
        end1_h = workingHourData.get("shift1").get("endHour");
        start1_m=workingHourData.get("shift1").get("startMinute");
        end1_m=workingHourData.get("shift1").get("endMinute");
        start2_h =workingHourData.get("shift2").get("startHour");
        end2_h = workingHourData.get("shift2").get("endHour");
        start2_m=workingHourData.get("shift2").get("startMinute");
        end2_m=workingHourData.get("shift2").get("endMinute");
        shift1mins = (end1_h-start1_h)*60;
        shift1mins += end1_m-start1_m;
        shift2mins= (end2_h-start2_h)*60;
        shift2mins += end2_m-start2_m;
        Map<String,Map<String,Integer>> taskData = (Map<String, Map<String,Integer>>) data.get("taskConfig");
        taskCountBounds = new int[3][2];
        taskCountBounds[0][0] = taskData.get("task0").get("countLowerBound");
        taskCountBounds[0][1] = taskData.get("task0").get("countUpperBound");
        taskCountBounds[1][0] = taskData.get("task1").get("countLowerBound");
        taskCountBounds[1][1] = taskData.get("task1").get("countUpperBound");
        taskCountBounds[2][0] = taskData.get("task2").get("countLowerBound");
        taskCountBounds[2][1] = taskData.get("task2").get("countUpperBound");
        taskLengthBounds = new int[3][2];
        taskLengthBounds[0][0] = taskData.get("task0").get("lengthLowerBound");
        taskLengthBounds[0][1] = taskData.get("task0").get("lengthUpperBound");
        taskLengthBounds[1][0] = taskData.get("task1").get("lengthLowerBound");
        taskLengthBounds[1][1] = taskData.get("task1").get("lengthUpperBound");
        taskLengthBounds[2][0] = taskData.get("task2").get("lengthLowerBound");
        taskLengthBounds[2][1] = taskData.get("task2").get("lengthUpperBound");
        Map<String,Integer> stationData = (Map<String, Integer>) data.get("chargeStationConfig");
        stationBounds = new int[2];
        stationBounds[0] = stationData.get("countLowerBound");
        stationBounds[1] = stationData.get("countUpperBound");
        Map<String,Integer> employeeData = (Map<String, Integer>) data.get("employeeConfig");
        employeeBounds = new int[2];
        employeeBounds[0] = employeeData.get("countLowerBound");
        employeeBounds[1] = employeeData.get("countUpperBound");

    }
    public void produceData()
    {
        Random random=new Random();
        random.setSeed(12);
        for (int i = 0; i < 3; i++) {
            counts[i] = random.nextInt(countBounds[i][1]-countBounds[i][0]) + countBounds[i][0];
            taskCounts[i] = random.nextInt(taskCountBounds[i][1]-taskCountBounds[i][0]) + taskCountBounds[i][0];
        }
        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < counts[i]; j++) {
                initialCharges[i].add(random.nextInt(chargeBounds[i][1]-chargeBounds[i][0])+chargeBounds[i][0]);
                lifetimes[i].add(random.nextInt(lifetimeBounds[i][1]-lifetimeBounds[i][0])+lifetimeBounds[i][0]);
                chargeTimes[i].add(random.nextInt(chargeTimeBounds[i][1]-chargeTimeBounds[i][0])+chargeTimeBounds[i][0]);

            }
            for (int j = 0; j <taskCounts[i] ; j++) {
                taskLengths[i].add(random.nextInt(taskLengthBounds[i][1]-taskLengthBounds[i][0])+taskLengthBounds[i][0]);
            }
        }
        workingHours = shift1mins+shift2mins;
        numStations = random.nextInt(stationBounds[1]-stationBounds[0]) + stationBounds[0];
        numWorkers = random.nextInt(employeeBounds[1]-employeeBounds[0]) + employeeBounds[0];
        names[0] = nameList.get(0);
        names[1] = nameList.get(1);
        names[2] = nameList.get(2);
    }
    public void writeToCSV() throws IOException {
        File dir = new File("./out_"+outname);
        dir.mkdirs();
        File csv1 = new File(dir,"/generic.csv");
        FileWriter writer1 = new FileWriter(csv1);
        writer1.write("WorkMinutes,Shift1StartHour,Shift1StartMin,Shift1EndHour,Shift1EndMin,");
        writer1.write("Shift2StartHour,Shift2StartMin,Shift2EndHour,Shift2EndMin,NumWorkers,NumStations\n");
        writer1.write(Integer.toString(workingHours)+","+Integer.toString(start1_h)+','+Integer.toString(start1_m)+',');
        writer1.write(Integer.toString(end1_h)+","+Integer.toString(end1_m)+','+Integer.toString(start2_h)+',');
        writer1.write(Integer.toString(start2_m)+","+Integer.toString(end2_h)+','+Integer.toString(end2_m)+',');
        writer1.write(Integer.toString(numWorkers) + ","+Integer.toString(numStations)+"\n");
        writer1.write(names[0]+","+names[1]+","+names[2]);
        writer1.close();
        File csv2 = new File(dir,"/equipment.csv");
        FileWriter writer2 = new FileWriter(csv2);
        writer2.write("Type,ID,InitialCharge,ChargeTime,LifeTime\n");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < counts[i]; j++) {
                writer2.write(Integer.toString(i)+","+Integer.toString(j)+","+Integer.toString(initialCharges[i].get(j))+",");
                writer2.write(Integer.toString(chargeTimes[i].get(j))+","+Integer.toString(lifetimes[i].get(j))+"\n");
            }
        }
        writer2.close();
        File csv3 = new File(dir,"/task.csv");
        FileWriter writer3 = new FileWriter(csv3);
        writer3.write("Type,ID,TaskLength\n");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < taskCounts[i]; j++) {
                writer3.write(Integer.toString(i)+","+Integer.toString(j)+","+Integer.toString(taskLengths[i].get(j))+"\n");
            }
        }
        writer3.close();
    }
    public void readFromCSV() throws IOException {
        File csv1 = new File("./out_"+outname+"/generic.csv");
        Scanner reader1 = new Scanner(csv1);
        String title = reader1.nextLine();
        String[] genericData = reader1.nextLine().split(",");
        workingHours = Integer.parseInt(genericData[0]);
        start1_h = Integer.parseInt(genericData[1]);
        start1_m = Integer.parseInt(genericData[2]);
        end1_h = Integer.parseInt(genericData[3]);
        end1_m = Integer.parseInt(genericData[4]);
        start2_h = Integer.parseInt(genericData[5]);
        start2_m = Integer.parseInt(genericData[6]);
        end2_h = Integer.parseInt(genericData[7]);
        end2_m = Integer.parseInt(genericData[8]);
        numWorkers = Integer.parseInt(genericData[9]);
        numStations = Integer.parseInt(genericData[10]);
        names = reader1.nextLine().split(",");
        File csv2 = new File("./out_"+outname+"/equipment.csv");
        Scanner reader2 = new Scanner(csv2);
        reader2.nextLine();
        while(reader2.hasNextLine())
        {
            String[] equipmentData = reader2.nextLine().split(",");
            int which_type = Integer.parseInt(equipmentData[0]);
            int mach_num = Integer.parseInt(equipmentData[1]);
            int init_charge = Integer.parseInt(equipmentData[2]);
            int charge_time = Integer.parseInt(equipmentData[3]);
            int life_time = Integer.parseInt(equipmentData[4]);
            counts[which_type]+=1;
            initialCharges[which_type].add(init_charge);
            chargeTimes[which_type].add(charge_time);
            lifetimes[which_type].add(life_time);
        }
        File csv3 = new File("./out_"+outname+"/task.csv");
        Scanner reader3 = new Scanner(csv3);
        reader3.nextLine();
        while(reader3.hasNextLine())
        {
            String[] taskData = reader3.nextLine().split(",");
            int which_type = Integer.parseInt(taskData[0]);
            int task_num = Integer.parseInt(taskData[1]);
            int task_length = Integer.parseInt(taskData[2]);
            taskCounts[which_type]+=1;
            taskLengths[which_type].add(task_length);
        }
    }
}


