import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Heuristic {
    DataHub data;
    ArrayList<Task>[] tasks;
    ArrayList<Machine>[] machines;
    Station[] stations;

    public Heuristic(DataHub data) {
        this.data = data;
        this.tasks = new ArrayList[3];
        for (int i = 0; i <3 ; i++) {
            this.tasks[i] = new ArrayList<Task>();
            for (int j = 0; j <data.taskCounts[i] ; j++) {
                this.tasks[i].add(new Task(j,data.taskLengths[i].get(j),i));
            }
        }
        this.machines = new ArrayList[3];
        for (int i = 0; i <3 ; i++) {
            this.machines[i] = new ArrayList<Machine>();
            for (int j = 0; j < data.counts[i]; j++) {
                this.machines[i].add(new Machine(j,data.initialCharges[i].get(j),data.chargeTimes[i].get(j),data.lifetimes[i].get(j),i));
            }
        }
        this.stations = new Station[data.numStations];
        for (int i = 0; i < data.numStations; i++) {
            this.stations[i] = new Station(i);
        }
    }

    public int getNextTask(Task[] t_list, Machine[] m_list,int currTime)
    {
        Task[] t_copy = t_list.clone();
        Machine[] m_copy = m_list.clone();
        Arrays.sort(t_copy,new SortByLength());
        Arrays.sort(m_copy,new SortByCharge());
        for (int i = 0; i < t_copy.length; i++) {
            if(t_copy[i].isInQueue() && currTime<=data.workingHours-t_copy[i].getTaskLength())
            {
                for (int j = 0; j < m_list.length; j++) {
                    if(m_copy[j].isAvailable())
                    {
                        if(m_copy[j].isChargedEnough(t_copy[i].getTaskLength()))
                        {
                            return t_copy[i].getTaskID();
                        }
                    }
                }
            }
        }
        return -1;
    }

    public ArrayList<Task> taskQueue(ArrayList<Task>[] t)
    {

        ArrayList<Task> copy_list1 = (ArrayList<Task>) t[0].clone();
        ArrayList<Task> copy_list2 = (ArrayList<Task>) t[1].clone();
        ArrayList<Task> copy_list3 = (ArrayList<Task>) t[2].clone();
        ArrayList<Task> copy_list = new ArrayList<Task>();
        for (int i = 0; i < copy_list1.size(); i++) {
            copy_list.add(copy_list1.get(i));
        }
        for (int i = 0; i < copy_list2.size(); i++) {
            copy_list.add(copy_list2.get(i));
        }
        for (int i = 0; i < copy_list3.size(); i++) {
            copy_list.add(copy_list3.get(i));
        }
        copy_list.sort(new SortByLength());
        return copy_list;
    }
    public ArrayList<Machine> chargeQueue(ArrayList<Machine>[] machs)
    {

        ArrayList<Machine> copy_list1 = (ArrayList<Machine>) machs[0].clone();
        ArrayList<Machine> copy_list2 = (ArrayList<Machine>) machs[1].clone();
        ArrayList<Machine> copy_list3 = (ArrayList<Machine>) machs[2].clone();
        ArrayList<Machine> copy_list = new ArrayList<Machine>();
        for (int i = 0; i < copy_list1.size(); i++) {
            copy_list.add(copy_list1.get(i));
        }
        for (int i = 0; i < copy_list2.size(); i++) {
            copy_list.add(copy_list2.get(i));
        }
        for (int i = 0; i < copy_list3.size(); i++) {
            copy_list.add(copy_list3.get(i));
        }
        copy_list.sort(new SortByCharge());
        return copy_list;
    }
    public String timeString(int min)
    {
        int h = min/60;
        int mi = min%60;
        h+= data.start1_h;
        mi+= data.start1_m;
        if(mi>=60)
        {
            h+=1;
            mi = mi%60;
        }
        String hours;
        if (h<10)
            hours = "0" + Integer.toString(h);
        else
            hours = Integer.toString(h);
        String mins;
        if (mi<10)
            mins = "0" + Integer.toString(mi);
        else
            mins = Integer.toString(mi);
        return hours+":"+mins;
    }
    public void Simulate() throws IOException {
        //Statistics
        int[] finishedTasks = {0,0,0};
        int[] startedTasks = {0,0,0};
        File taskOutput = new File("./out_"+data.outname+"/TaskOutput.txt");
        FileWriter taskOutputWriter = new FileWriter(taskOutput);
        File machineOutput = new File("./out_"+data.outname+"/MachineOutput.txt");
        FileWriter machineOutputWriter = new FileWriter(machineOutput);
        int shift1end = (this.data.end1_h-this.data.start1_h)*60+this.data.end1_m-this.data.start1_m;
        int shift2start= (this.data.start2_h-this.data.start1_h)*60+this.data.start2_m-this.data.start1_m;
        int shift2end = (this.data.end2_h-this.data.start1_h)*60+this.data.end2_m-this.data.start1_m;
        for (int min = 0; min < shift2end; min++) {
            //Get current time in hours for printing purposes
            String timeStr = timeString(min);
            if(min<shift1end || min>shift2start)
            {
                ArrayList<Task> tasksToBeDone = taskQueue(tasks);
                for (int i = 0; i < tasksToBeDone.size(); i++) {
                    Task t = tasksToBeDone.get(i);
                    if(t.isInQueue())
                    {
                        for (int j = 0; j < machines[t.getType()].size(); j++) {    //check all the machines of this type
                            if(machines[t.getType()].get(j).isAvailable())
                            {
                                if(machines[t.getType()].get(j).isChargedEnough(t.getTaskLength()))
                                {
                                    tasks[t.getType()].get(t.getTaskID()).setInProgress(true);
                                    tasks[t.getType()].get(t.getTaskID()).setMachineID(j);
                                    machines[t.getType()].get(j).setOnJob(true);
                                    machines[t.getType()].get(j).setTaskID(t.getTaskID());
                                    if(machines[t.getType()].get(j).isBeingCharged())
                                    {
                                        stations[machines[t.getType()].get(j).getStationID()].setAvailable(true);
                                        stations[machines[t.getType()].get(j).getStationID()].setMachineID(-1);
                                        machines[t.getType()].get(j).setBeingCharged(false);
                                        machines[t.getType()].get(j).setStationID(-1);
                                    }
                                    taskOutputWriter.write(t.getTypeName()+" task "+t.getTaskID()+" is started on machine "+ machines[t.getType()].get(j).getMachineID()+" at "+timeStr+"\n");
                                    startedTasks[t.getType()]++;
                                    break;
                                }
                            }
                        }
                    }
                    else if(t.isInProgress())
                    {
                        //if task is in progress, then decrease the minutes by 1
                        tasks[t.getType()].get(t.getTaskID()).setRemainingTime(tasks[t.getType()].get(t.getTaskID()).getRemainingTime()-1);
                        if(tasks[t.getType()].get(t.getTaskID()).getRemainingTime()==0)
                        {
                            finishedTasks[t.getType()]++;
                            taskOutputWriter.write(t.getTypeName()+" task "+t.getTaskID()+" is completed by machine "+tasks[t.getType()].get(t.getTaskID()).getMachineID()+" at "+timeStr+"\n");
                            machines[t.getType()].get(t.getMachineID()).setOnJob(false);
                            machines[t.getType()].get(t.getMachineID()).setTaskID(-1);
                            tasks[t.getType()].get(t.getTaskID()).setDone(true);
                            tasks[t.getType()].get(t.getTaskID()).setInProgress(false);
                            tasks[t.getType()].get(t.getTaskID()).setMachineID(-1);
                        }
                    }
                }
                ArrayList<Machine> chargingQueue = chargeQueue(machines);
                for (int i = 0; i < chargingQueue.size(); i++) {
                    Machine m = chargingQueue.get(i);
                    if(!machines[m.getType()].get(m.getMachineID()).isOnJob() && !machines[m.getType()].get(m.getMachineID()).isBeingCharged() && machines[m.getType()].get(m.getMachineID()).getCurrentCharge()<=50.0)
                    {
                        for (int j = 0; j < stations.length; j++) {
                            if(stations[j].isAvailable())
                            {
                                stations[j].setMachineID(m.getMachineID());
                                stations[j].setAvailable(false);
                                machines[m.getType()].get(m.getMachineID()).setBeingCharged(true);
                                machines[m.getType()].get(m.getMachineID()).setStationID(j);
                                machineOutputWriter.write(m.getTypeName()+" machine "+m.getMachineID()+" is put into charge in station "+ j +" at "+timeStr+"\n");
                                break;

                            }
                        }
                    }
                    else if(machines[m.getType()].get(m.getMachineID()).isOnJob())
                    {
                        machines[m.getType()].get(m.getMachineID()).decreaseCharge();
                    }
                    else if (machines[m.getType()].get(m.getMachineID()).isBeingCharged()) {
                        if(machines[m.getType()].get(m.getMachineID()).getCurrentCharge()>=100.0)
                        {
                            machineOutputWriter.write(m.getTypeName()+" machine "+m.getMachineID()+" is fully charged and plugged out from station "+machines[m.getType()].get(m.getMachineID()).getStationID()+" at "+timeStr+"\n");
                            stations[machines[m.getType()].get(m.getMachineID()).getStationID()].setMachineID(-1);
                            stations[machines[m.getType()].get(m.getMachineID()).getStationID()].setAvailable(true);
                            machines[m.getType()].get(m.getMachineID()).setBeingCharged(false);
                            machines[m.getType()].get(m.getMachineID()).setStationID(-1);
                        }
                        else
                        {
                            machines[m.getType()].get(m.getMachineID()).increaseCharge();
                        }
                    }
                }
            }
            else{
                if(min==shift1end)
                {
                    taskOutputWriter.write("\n\nNoon break has started at "+ timeStr+"!\n\n");
                    machineOutputWriter.write("\n\nNoon break has started at "+ timeStr+"!\n\n");
                }
                if(min==shift2start)
                {
                    taskOutputWriter.write("\n\nNoon break has ended at "+timeStr+"!\n\n");
                    machineOutputWriter.write("\n\nNoon break has started at "+ timeStr+"!\n\n");
                }
                ArrayList<Machine> chargingQueue = chargeQueue(machines);
                for (int i = 0; i < chargingQueue.size(); i++) {
                    Machine m = chargingQueue.get(i);
                    if (machines[m.getType()].get(m.getMachineID()).isBeingCharged()) {
                        machines[m.getType()].get(m.getMachineID()).increaseCharge();
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < machines[i].size(); j++) {
                    machines[i].get(j).keepChargeData();
                }
            }

        }
        System.out.println("Number of started and finished tasks for each type are given below");
        for (int i = 0; i < 3; i++) {
            System.out.println("Out of "+data.taskCounts[i]+" "+ data.names[i] +" tasks; "+startedTasks[i] +" has been started and "+finishedTasks[i]+" has been finished!");
        }
        for (int i = 0; i < 3; i++) {
            File chargeOutput = new File("./out_"+data.outname+"/chargeOutput_"+machines[i].get(0).getTypeName()+".csv");
            FileWriter chargeOutputWriter = new FileWriter(chargeOutput);
            chargeOutputWriter.write("Each line represents charge values for a different machine of this type!\n");
            for (int j = 0; j < machines[i].size(); j++) {
                ArrayList<Double> chargeData = machines[i].get(j).getChargeData();
                for (int k = 0; k < chargeData.size(); k++) {
                    chargeOutputWriter.write(String.valueOf(chargeData.get(k)));
                    if(k!=chargeData.size()-1)
                        chargeOutputWriter.write(",");
                    else
                        chargeOutputWriter.write("\n");
                }
            }
            chargeOutputWriter.close();
        }

        taskOutputWriter.close();
        machineOutputWriter.close();
    }
}