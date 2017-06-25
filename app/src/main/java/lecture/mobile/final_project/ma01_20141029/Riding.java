package lecture.mobile.final_project.ma01_20141029;

/**
 * Created by DE on 2016-12-20.
 * Riding 정보저장 클래스
 * 작성자 :김다은
 * 학번: 20141029
 */

public class Riding {

    private int _id;
    private String ridingTime; //라이딩한 시간
    private String startTime; //라이딩을 시작한시간
    private String distance; //거리
    private String speed;  //속력

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getRidingTime() {
        return ridingTime;
    }

    public void setRidingTime(String ridingTime) {
        this.ridingTime = ridingTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
