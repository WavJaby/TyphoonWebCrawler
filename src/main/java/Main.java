import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws JSONException, BadLocationException {
        TyphoonData typhoonData = new TyphoonData();
        System.out.println(typhoonData.typhoonID);
        System.out.println(typhoonData.typhoonName);
        System.out.println(typhoonData.typhoonTime);
        System.out.println(typhoonData.typhoonInfo);
//        System.out.println(typhoonData.typhoonPicture);

    }
}
