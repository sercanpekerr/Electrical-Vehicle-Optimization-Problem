public class Station {
    int stationID;
    int machineID;
    boolean isAvailable;

    public Station(int stationID) {
        this.stationID = stationID;
        this.machineID = -1;
        this.isAvailable = true;
    }

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getStationID() {
        return stationID;
    }
}