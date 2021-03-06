package bdcash.dcash.bdcash;


public class Time {

    private long last_time;
    private long end_time;
    private int total_balance;
    private int isTimerRunning;
    private int clickBalance;
    private int eachDayEarning;

    public Time() {

    }

    public Time(long last_time, long end_time, int total_balance, int isTimerRunning, int clickBalance, int eachDayEarning) {
        this.last_time = last_time;
        this.end_time = end_time;
        this.total_balance = total_balance;
        this.isTimerRunning = isTimerRunning;
        this.clickBalance = clickBalance;
        this.eachDayEarning = eachDayEarning;
    }

    public long getLast_time() {
        return last_time;
    }

    public void setLast_time(long last_time) {
        this.last_time = last_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public int getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(int total_balance) {
        this.total_balance = total_balance;
    }

    public int getIsTimerRunning() {
        return isTimerRunning;
    }

    public void setIsTimerRunning(int isTimerRunning) {
        this.isTimerRunning = isTimerRunning;
    }

    public int getClickBalance() {
        return clickBalance;
    }

    public void setClickBalance(int clickBalance) {
        this.clickBalance = clickBalance;
    }

    public int getEachDayEarning() {
        return eachDayEarning;
    }

    public void setEachDayEarning(int eachDayEarning) {
        this.eachDayEarning = eachDayEarning;
    }
}
