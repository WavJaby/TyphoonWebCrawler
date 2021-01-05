public class test {
    public static void main(String[] args) {
        String html = "''+    '<div class=\"panel panel-default\">'+        '<div class=\"panel-heading\">'+            '<h4 class=\"panel-title\">'+                '<a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion-1\" href=\"#collapse-A1\" title=\"點此展開\" aria-expanded=\"true\">'+                    '<span class=\"fa-red\">熱帶性低氣壓</span><span class=\"fa-blue\">TD26</span><br>(原科羅旺颱風)'+                '</a>'+            '</h4>'+        '</div>'+        '<div id=\"collapse-A1\" class=\"panel-collapse collapse in\" aria-expanded=\"true\" role=\"document\">'+            '<div class=\"panel-body\">'+                '<p>21日14時的中心位置在北緯 9.0 度，東經 112.5 度，以每小時18公里速度，向西南西進行。<span class=\"fa-blue\">中心氣壓</span><span class=\"fa-red\">1004</span><span class=\"fa-blue\">百帕</span>，<span class=\"fa-blue\">近中心最大風速每秒</span><span class=\"fa-red\">15</span><span class=\"fa-blue\">公尺</span>，瞬間最大陣風每秒 23 公尺。</p>'+            '</div>'+        '</div>'+    '</div>'";
        html = html.replace("  ", "").replace("'", "").replace("+", "");
        System.out.println(html);

        String testHTML = "<div class=\"abc\" id='root'><div class=\"def\"></div></div>";
//        System.out.println(getTextInQuotation(testHTML.substring(testHTML.indexOf("class"))));
//        System.out.println(getTextInQuotation(testHTML));

        int nowIndex = 0;

        //html tag開頭
        int htmlTagStart = testHTML.indexOf("<", nowIndex);
        String htmlTag = testHTML.substring(htmlTagStart + 1, testHTML.indexOf(" "));

        int lastQuotationEnd = nowIndex;
        while (true) {
            //尋找引號裡的值與key
            int quotationPos = testHTML.indexOf("\"", nowIndex);
            char firstQuotation = '\"';
            if (quotationPos == -1 || testHTML.indexOf(">", lastQuotationEnd) < quotationPos) {//不是雙引號||超過html tag結束
                quotationPos = testHTML.indexOf("'", nowIndex);
                if (quotationPos != -1)//是單引號
                    firstQuotation = '\'';
                else //沒找到引號
                    return;
            }


            int quotationEnd = testHTML.indexOf(firstQuotation, quotationPos + 1);
            if (testHTML.indexOf(">", lastQuotationEnd) < quotationEnd)//超過tag了
                break;

            String value = testHTML.substring(quotationPos + 1, quotationEnd);
            int equalsPosition = testHTML.lastIndexOf("=", quotationPos);
            String key = testHTML.substring(testHTML.lastIndexOf(" ", equalsPosition) + 1, equalsPosition);

            System.out.println(htmlTag);
            System.out.println(key);
            System.out.println(value);

            lastQuotationEnd = quotationEnd;
            nowIndex = quotationEnd + 1;
        }


    }

    private static String getInnerHTML(String in) {
        return "";
    }

    private static String getTextInQuotation(String in) {
        int classIndex = in.indexOf("class") + 5;
        int idIndex = in.indexOf("id") + 2;
        if (classIndex > -1)
            in = in.substring(classIndex);


        return in;
    }

    private static String findText(String in) {
        String out = "";
        int index = 0;
        int count = 0;

        while (index != -1) {
            index = in.indexOf(">", count);
            if (index > in.length() - 2)
                break;

            if (in.charAt(index + 1) == '<') {
                count = index + 1;
            } else {
                count = in.indexOf("<", count + 1);
                System.out.println(in.substring(index + 1, count));
            }
        }

        return out;
    }
}
