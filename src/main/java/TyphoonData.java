import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TyphoonData {
    public JSONObject typhoonName;
    public JSONObject typhoonTime;
    public JSONObject typhoonPicture;
    public String typhoonID;

    //TODO:指測試過單個颱風，一次多個需要修改
    TyphoonData() throws JSONException, BadLocationException {
        Map<String, String> head = new HashMap<>();
        head.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");

        //取得最新的颱風
        String mainData = getWebHtml("https://www.cwb.gov.tw/Data/js/typhoon/TY_NEWS-Data.js", head);
        //取得時間
        String dataTime = getJsValue(mainData, "TY_DataTime").replace("'", "");
        //取得info
        JSONObject tyInfo = new JSONObject(getJsValue(mainData, "TYPHOON"));
        //颱風編號
        typhoonID = tyInfo.names().get(0).toString();
        //颱風名字
        typhoonName = (JSONObject) ((JSONObject) tyInfo.get(typhoonID)).get("Name");
        //時間
        typhoonTime = new JSONObject(getJsValue(mainData, "TYPHOON"));

        //暴風圈侵襲機率圖片資料
        String wspMap = getWebHtml("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/WSP-MAP_IMGS_" + dataTime + "_zhtw.json", head);
        //路徑潛勢預報圖片資料
        String ptaMap = getWebHtml("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/PTA_IMGS_" + dataTime + "_zhtw.json", head);

        //路徑潛勢預報圖片所有
        JSONObject ptaMapJSON = new JSONObject(ptaMap);

        //暴風圈侵襲機率圖片(區域)
        byte[] wspMapBig = getWebByte("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/WSP-MAP_" + dataTime + "_" + typhoonID + "_zhtw.png", head);
        //暴風圈侵襲機率圖片(全域)
        JSONArray wspMapWhole = new JSONArray(wspMap);
        //路徑潛勢預報圖片(區域)
        //TODO:可能多颱風時會有錯誤
        JSONArray ptaMapEach = (JSONArray) ((JSONObject) ((JSONArray) ptaMapJSON.get("EACH")).get(0)).get("list");
        //路徑潛勢預報圖片(全域)
        JSONArray ptaMapWhole = (JSONArray) ptaMapJSON.get("WHOLE");

        JSONObject allPicture = new JSONObject();
        JSONObject cache = new JSONObject();
        //路徑潛勢預報圖片(全域)
        for (int i = 0; i < ptaMapWhole.length(); i++) {
            byte[] picture = getWebByte("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/" + ptaMapWhole.get(i), head);
            cache.put(wspMapWhole.get(i).toString().split("_")[1], byteToBase64(picture));
        }
        allPicture.put("ptaWhole", cache);
        cache = new JSONObject();

        //路徑潛勢預報圖片(區域)
        for (int i = 0; i < ptaMapEach.length(); i++) {
            byte[] picture = getWebByte("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/" + ptaMapEach.get(i), head);
            cache.put(wspMapWhole.get(i).toString().split("_")[1], byteToBase64(picture));
        }
        allPicture.put("ptaEach", cache);
        cache = new JSONObject();

        //暴風圈侵襲機率(全域)
        for (int i = 0; i < wspMapWhole.length(); i++) {
            byte[] picture = getWebByte("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/" + wspMapWhole.get(i), head);
            cache.put(wspMapWhole.get(i).toString().split("_")[1], byteToBase64(picture));
        }
        allPicture.put("wspWhole", cache);

        //暴風圈侵襲機率(區域)
        allPicture.put("wspBig", byteToBase64(wspMapBig));

        //寫入資料
        typhoonPicture = new JSONObject();
        typhoonPicture.put(typhoonID, allPicture);

        System.out.println(typhoonPicture.toString());

        //颱風資料
        //簡介
        String tyData1 = getJsValue(mainData, "TY_LIST_1['C']");
        //詳細資料
        String tyData2 = getJsValue(mainData, "TY_LIST_2['C']");

        //解析HTML
        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setContentType("text/html; charset=utf-8");
        jEditorPane.setText(tyData1.replace("+", "").replace("'", "").replace("  ", "") + tyData2.replace("+", "").replace("'", "").replace("  ", ""));
        HTMLDocument document = (HTMLDocument) jEditorPane.getDocument();
//        ElementIterator iterator = new ElementIterator(document);
        ElementIterator iterator = new ElementIterator(document);
        Element element = iterator.current();


//        System.out.println(tyData.replace("+    ", "\n").replace("'", ""));
        System.out.println(mainData.replace(";", "\n"));

        System.out.println(document.getText(element.getStartOffset(), element.getEndOffset()));

//        System.out.println(element.get);

//        System.out.println(tyData.replace("+", "+\n"));
//        System.out.println(mainData.replace("+", "+\n").replace(";", ";\n"));
//        System.out.println();
//        System.out.println(htmlGetText(tyData));

        System.out.println(wspMapWhole);
        System.out.println(ptaMapEach);
        System.out.println(ptaMapWhole);

    }


    public static String htmlGetText(String input) {
        int lastIndex = -1;
        int index = 0;
        int lastOne = input.lastIndexOf(">");
        StringBuilder stringBuilder = new StringBuilder();
        while (index != lastOne) {
            index = input.indexOf(">", lastIndex);
            char ch = input.charAt(index + 1);
//            System.out.println(lastIndex);
//            System.out.println(index);
            if (ch != '\'' && ch != '\"' && ch != '+' && ch != '>') {
                stringBuilder.append(input.substring(index + 1, input.indexOf("<", index)));
//                System.out.println(stringBuilder.toString());
            }

            lastIndex = index + 1;
        }

        return stringBuilder.toString();
    }

    public static String byteToBase64(byte[] input) {
        return new String(Base64.getEncoder().encode(input));
    }

    public static String getWebHtml(String urlString, Map<String, String> requestHead) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            for (Map.Entry<String, String> i : requestHead.entrySet())
                connection.setRequestProperty(i.getKey(), i.getValue());

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getWebByte(String urlString, Map<String, String> requestHead) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            for (Map.Entry<String, String> i : requestHead.entrySet())
                connection.setRequestProperty(i.getKey(), i.getValue());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();
            byte[] byteArr = new byte[4096];
            int length;
            while ((length = inputStream.read(byteArr)) > 0) {
                outputStream.write(byteArr, 0, length);
            }
            inputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getJsValue(String data, String key) {
        int result = data.indexOf(key);
        int resValStart = data.indexOf("=", result) + 2;
        int resValEnd = data.indexOf(";", result);
        return data.substring(resValStart, resValEnd);
    }
}
