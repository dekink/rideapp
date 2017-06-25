package lecture.mobile.final_project.ma01_20141029;

/**
 * Created by DE on 2016-10-13.
 * Park 정보 저장 클래스
 * 작성자 :김다은
 * 학번: 20141029
 */

public class Park {

    private int _id;
    private String P_PARK; //공원명
    private String P_ADDR; //공원주소
    private String P_IMG;  //공원이미지
    private String LONGITUDE;
    private String LATITUDE;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getP_PARK() {
        return P_PARK;
    }

    public void setP_PARK(String p_PARK) {
        P_PARK = p_PARK;
    }

    public String getP_ADDR() {
        return P_ADDR;
    }

    public void setP_ADDR(String p_ADDR) {
        P_ADDR = p_ADDR;
    }

    public String getP_IMG() {
        return P_IMG;
    }

    public void setP_IMG(String p_IMG) {
        P_IMG = p_IMG;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }
}
