import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws JSONException {
        Map<String, String> head = new HashMap<>();
        head.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");

        //取得最新的颱風
        String mainData = getWebHtml("https://www.cwb.gov.tw/Data/js/typhoon/TY_NEWS-Data.js", head);
        //取得時間
        String dataTime = getJsValue(mainData, "TY_DataTime").replace("'", "");
        //取得info
        JSONObject tyInfo = new JSONObject(getJsValue(mainData, "TYPHOON"));
        //颱風編號
        String tyID = tyInfo.names().get(0).toString();


        //暴風圈侵襲機率圖片資料
        String wspMap = getWebHtml("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/WSP-MAP_IMGS_" + dataTime + "_zhtw.json", head);
        //路徑潛勢預報圖片資料
        String ptaMap = getWebHtml("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/PTA_IMGS_" + dataTime + "_zhtw.json", head);

        //暴風圈侵襲機率圖片
        byte[] wspMapPicture = getWebByte("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/WSP-MAP_" + dataTime + "_" + tyID + "_zhtw.png", head);


        JSONArray wspMapArray = new JSONArray(wspMap);
        JSONObject ptaMapJSON = new JSONObject(ptaMap);


//        System.out.println(mainData.replace(";", ";\n").replace("+", "+\n"));
        String tyData = getJsValue(mainData, "TY_LIST_1['C']");

        System.out.println(tyData.replace("+", "+\n"));
        System.out.println(htmlGetText(tyData));
        System.out.println(wspMapArray);
        System.out.println(((JSONObject) ((JSONArray) ptaMapJSON.get("EACH")).get(0)).get("list"));
        System.out.println(ptaMapJSON.get("WHOLE"));

        for (int i = 0; i < wspMapArray.length(); i++) {
            byte[] picture = getWebByte("https://www.cwb.gov.tw/Data/typhoon/TY_NEWS/" + wspMapArray.get(i), head);
            System.out.println(byteToBase64(picture));
        }
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
                System.out.println(stringBuilder.toString());
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
