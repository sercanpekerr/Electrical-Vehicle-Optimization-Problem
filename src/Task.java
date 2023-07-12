import java.util.Comparator;

public class Task {
    int taskID;
    int taskLength;
    int remainingTime;
    boolean isDone;
    boolean isInProgress;
    int machineID;
    int workerID;
    int type;
    String typeName;

    public Task(int taskID, int taskLength,int type) {
        this.taskID = taskID;
        this.taskLength = taskLength;
        this.remainingTime = taskLength;
        this.isDone = false;
        this.isInProgress = false;
        this.machineID = -1;
        this.workerID = -1;
        this.type = type;
        this.typeName = "Forklift";
        if(type==1)
            this.typeName = "RT";
        if(type==2)
            this.typeName = "Jet";
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    public int getWorkerID() {
        return workerID;
    }

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }

    public int getTaskLength() {
        return taskLength;
    }

    public boolean isInQueue()
    {
        return !this.isInProgress && !this.isDone;
    }

    public int getTaskID() {
        return taskID;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
class SortByLength implements Comparator<Task>
{
    @Override
    public int compare(Task o1, Task o2) {
        return o1.taskLength-o2.taskLength;
    }
}

