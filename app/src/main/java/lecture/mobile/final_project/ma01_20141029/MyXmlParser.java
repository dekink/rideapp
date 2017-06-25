package lecture.mobile.final_project.ma01_20141029;

/**
 * Created by DE on 2016-10-13.
 * 공원정보  API Parser
 * 작성자 :김다은
 * 학번: 20141029
 */
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class MyXmlParser {

    //P_PARK:공원이름
    //P_ADDR:공원주소
    //P_IMG:공원이미지주소
    //LONGITUDE:경도
    //LATITUDE:위도
    public enum TagType { NONE, P_PARK, P_ADDR, P_IMG, LONGITUDE, LATITUDE};

    public MyXmlParser() {
    }

    public ArrayList<Park> parse(String xml) {

        ArrayList<Park> resultList = new ArrayList();
        Park dbo = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("row")) {
                            dbo = new Park();
                        } else if (parser.getName().equals("P_PARK")) {
                            tagType = TagType.P_PARK;
                        } else if (parser.getName().equals("P_ADDR")) {
                            tagType = TagType.P_ADDR;
                        } else if (parser.getName().equals("P_IMG")) {
                            tagType = TagType.P_IMG;
                        } else if (parser.getName().equals("LONGITUDE")) {
                            tagType = TagType.LONGITUDE;
                        } else if (parser.getName().equals("LATITUDE")) {
                            tagType = TagType.LATITUDE;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("row")) {
                            resultList.add(dbo);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case P_PARK:
                                dbo.setP_PARK(parser.getText());
                                break;
                            case P_ADDR:
                                dbo.setP_ADDR(parser.getText());
                                break;
                            case P_IMG:
                                dbo.setP_IMG(parser.getText());
                                break;
                            case LONGITUDE:
                                dbo.setLONGITUDE(parser.getText());
                                break;
                            case LATITUDE:
                                dbo.setLATITUDE(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
