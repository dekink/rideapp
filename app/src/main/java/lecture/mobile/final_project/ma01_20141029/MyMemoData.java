package lecture.mobile.final_project.ma01_20141029;

/**
 * Created by DE on 2016-12-19.
 * Memo dto
 * 작성자 :김다은
 * 학번: 20141029
 */

public class MyMemoData {
    private int _id;
    private String parkname;
    private String title;
    private String content;
    private String currentdate;

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getParkname() {
        return parkname;
    }

    public void setParkname(String parkname) {
        this.parkname = parkname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
