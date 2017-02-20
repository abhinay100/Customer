package customer.apnacare.in.customer.model;

/**
 * Created by root on 17/2/17.
 */

public class Routine {
    private String session;
    private String routineID;
    private String routineName;
    private String routineCompletedTime;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getRoutineID() {
        return routineID;
    }

    public void setRoutineID(String routineID) {
        this.routineID = routineID;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public String getRoutineCompletedTime() {
        return routineCompletedTime;
    }

    public void setRoutineCompletedTime(String routineCompletedTime) {
        this.routineCompletedTime = routineCompletedTime;
    }
}