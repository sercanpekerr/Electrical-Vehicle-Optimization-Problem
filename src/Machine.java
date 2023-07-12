import java.util.ArrayList;
import java.util.Comparator;

public class Machine {
    int machineID;
    int taskID;
    int stationID;
    double currentCharge;
    int chargeTime;
    int lifeTime;
    boolean isBeingCharged;
    boolean isOnJob;
    int type;
    String typeName;
    ArrayList<Double> chargeData;
    public Machine(int machineID, double currentCharge, int chargeTime, int lifeTime,int type) {
        this.machineID = machineID;
        this.type = type;
        this.currentCharge = currentCharge;
        this.chargeTime = chargeTime;
        this.lifeTime = lifeTime;
        this.taskID = -1;
        this.stationID = -1;
        this.isBeingCharged = false;
        this.isOnJob = false;
        this.typeName = "Forklift";
        if(type==1)
            this.typeName = "RT";
        if(type==2)
            this.typeName = "Jet";
        this.chargeData = new ArrayList<Double>();
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public boolean isBeingCharged() {
        return isBeingCharged;
    }

    public void setBeingCharged(boolean beingCharged) {
        isBeingCharged = beingCharged;
    }

    public boolean isOnJob() {
        return isOnJob;
    }

    public void setOnJob(boolean onJob) {
        isOnJob = onJob;
    }

    public int getMachineID() {
        return machineID;
    }

    public double getCurrentCharge() {
        return currentCharge;
    }

    public void increaseCharge()
    {
        currentCharge += 100.0/chargeTime;
    }

    public void decreaseCharge()
    {
        currentCharge -= 100.0/lifeTime;
    }

    public boolean isChargedEnough(int taskLength)
    {
        if(currentCharge*lifeTime/100.0>=taskLength && currentCharge>=50.0)
        {
            return true;
        }
        return false;
    }

    public boolean isAvailable()
    {
        return !this.isBeingCharged && !this.isOnJob;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
    public void keepChargeData()
    {
        if(currentCharge>=100.0)
            chargeData.add(100.0);
        else
            chargeData.add(currentCharge);
    }

    public ArrayList<Double> getChargeData() {
        return chargeData;
    }
}

class SortByCharge implements Comparator<Machine>
{


    @Override
    public int compare(Machine o1, Machine o2) {
        return (int) (o1.currentCharge-o2.currentCharge);
    }

    @Override
    public Comparator<Machine> reversed() {
        return Comparator.super.reversed();
    }
}
