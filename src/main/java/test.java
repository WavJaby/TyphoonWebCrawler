import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        String html = "''+    '<div class=\"panel panel-default\">'+        '<div class=\"panel-heading\">'+            '<h4 class=\"panel-title\">'+                '<a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion-1\" href=\"#collapse-A1\" title=\"點此展開\" aria-expanded=\"true\">'+                    '<span class=\"fa-red\">熱帶性低氣壓</span><span class=\"fa-blue\">TD26</span><br>(原科羅旺颱風)'+                '</a>'+            '</h4>'+        '</div>'+        '<div id=\"collapse-A1\" class=\"panel-collapse collapse in\" aria-expanded=\"true\" role=\"document\">'+            '<div class=\"panel-body\">'+                '<p>21日14時的中心位置在北緯 9.0 度，東經 112.5 度，以每小時18公里速度，向西南西進行。<span class=\"fa-blue\">中心氣壓</span><span class=\"fa-red\">1004</span><span class=\"fa-blue\">百帕</span>，<span class=\"fa-blue\">近中心最大風速每秒</span><span class=\"fa-red\">15</span><span class=\"fa-blue\">公尺</span>，瞬間最大陣風每秒 23 公尺。</p>'+            '</div>'+        '</div>'+    '</div>'";
        html = html.replace("  ", "").replace("'", "").replace("+", "");

//        String testHTML = "<div class=\"abc\" id=\"root\"><span>hello</span><span id='window' class=\"def\">ssss</span></div>";
        String testHTML = "<span>Hello </span><span id='world' class=\"black\">World</span>";
//        String testHTML = "<h4 class=\"panel-title\"><a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion-1\" href=\"#collapse-A1\" title=\"點此展開\" aria-expanded=\"true\"><span class=\"fa-red\">熱帶性低氣壓</span><span class=\"fa-blue\">TD26</span></a></h4>";
//        String testHTML = html.replace("<br>", "");
        System.out.println(testHTML + "\n");
        findTag(testHTML, null);


//        System.out.println(getTextInQuotation(testHTML.substring(testHTML.indexOf("class"))));
//        System.out.println(getTextInQuotation(testHTML));
//        int nowIndex = 0;
//
//        //html tag開頭
//        int htmlTagStart = testHTML.indexOf("<", nowIndex);
//        String htmlTag = testHTML.substring(htmlTagStart + 1, testHTML.indexOf(" "));
//
//        int lastQuotationEnd = nowIndex;
//        while (true) {
//            int htmlTagEnd = testHTML.indexOf(">", lastQuotationEnd);
//
//            //尋找引號裡的值與key
//            int quotationPos = testHTML.indexOf("\"", nowIndex);
//            char firstQuotation = '\"';
//            if (quotationPos == -1 || htmlTagEnd < quotationPos) {//不是雙引號||超過html tag結束
//                quotationPos = testHTML.indexOf("'", nowIndex);
//                if (quotationPos != -1)//是單引號
//                    firstQuotation = '\'';
//                else //沒找到引號
//                    return;
//            }
//
//
//            int quotationEnd = testHTML.indexOf(firstQuotation, quotationPos + 1);
//            if (quotationEnd > htmlTagEnd)//超過tag了
//                break;
//
//            String value = testHTML.substring(quotationPos + 1, quotationEnd);
//            int equalsPosition = testHTML.lastIndexOf("=", quotationPos);
//            String key = testHTML.substring(testHTML.lastIndexOf(" ", equalsPosition) + 1, equalsPosition);
//
//            System.out.println(htmlTag);
//            System.out.println(key);
//            System.out.println(value);
//
//            lastQuotationEnd = quotationEnd;
//            nowIndex = quotationEnd + 1;
//        }
    }

    private static int findTag(String html, String findingTag) {
//        System.out.println(html);
        boolean inStr = false;
        boolean inTag = false;
        boolean hasAttribute = false;
//        //是不是html
//        boolean innerHTML = false;

        char firstQuotation = 0;
        int lastQuotation = -1;
        int lastSpace = -1;
        int tagStart = -1;

        String tagName = "";

        for (int i = 0; i < html.length(); i++) {
            //字串開頭
            if (!inStr && (html.charAt(i) == '\'' || html.charAt(i) == '\"' || html.charAt(i) == '`')) {//找到字串
                firstQuotation = html.charAt(i);
                //表示在字串裡
                inStr = true;
                continue;
            }

            //找到字串結束
            if (html.charAt(i) == firstQuotation && inStr) {
                //表示在字串外
                inStr = false;
                lastQuotation = i;
            }

            //html start
            if (html.charAt(i) == '<' && !inStr) {
                //是HTML
                if (i == 0) {
//                    innerHTML = true;
                    inTag = true;
                }
                tagStart = i;
            }

            //find tag name, and find attribute (在tag裡找到空白 或是 沒有找到空白但找到結束)
            if ((html.charAt(i) == ' ' || html.charAt(i) == '>') && inTag && !inStr) {
                if (lastQuotation + 1 == i && hasAttribute) {
                    System.out.println("Attribute: " + html.substring(lastSpace + 1, i));
                    lastSpace = i;
                }

                if (!hasAttribute) {
                    //是空白且還沒找到Attribute
                    if (html.charAt(i) == ' ') {
                        lastSpace = i;
                        hasAttribute = true;
                    }
                    tagName = html.substring(tagStart + 1, i);
                    System.out.println("tag name: " + tagName);
//                    if(tagName.equals("br")){
//                        System.out.println("find: " + findingTag);
//                        inTag = false;
//                    }
                }
            }


            //html close tag("<"在"/"的前一格，表示找到</)
            if (html.charAt(i) == '/' && tagStart + 1 == i && !inTag && !inStr) {
                //如果找到正確的結束
                if (findingTag == null) {
                    return i - 2;
                } else if (html.substring(i + 1, i + 1 + findingTag.length()).equals(findingTag)) {
//                    if (findingTag.equals("span"))
//                        System.out.println(html.substring(i + 2 + findingTag.length()));
                    return i + 2 + findingTag.length();
                } else {
                    System.out.println("failed");
                }
            }

            //找到html tag結束
            if (html.charAt(i) == '>' && inTag && !inStr) {
                inTag = false;
//                System.out.println(html.substring(tagStart, i + 1));

                int cache = i;
                i += 1 + findTag(html.substring(i + 1), tagName);
                System.out.println(tagName + " text: " + html.substring(cache + 1, i));
                System.out.println();


                //如果還有其他的
                if (i < html.length()) {
                    i += 1 + findTag(html.substring(i), null);
                    System.out.println(i + " " + html.length());
                }
            }
        }


        System.out.println("end");
        return 0;
    }

    private static void findHtmlTagEnd(String html) {
        boolean inStr = false;
        char firstQuotation = ' ';
        int htmlTagStart = -1;
        int htmlTagEnd = -1;
        int htmlAttributeStart = -1;
        int htmlAttributeEnd = -1;
        int lastAttributeEnd = -1;
        int equalPos = -1;
        String htmlTag = null;
        for (int i = 0; i < html.length(); i++) {
            //找到開頭
            if (html.charAt(i) == '<')
                htmlTagStart = i;

            //找到html tag
            if (html.charAt(i) == ' ' && htmlTagStart != -1 && !inStr && htmlTag == null) {
                htmlTag = html.substring(htmlTagStart + 1, i);
                htmlAttributeEnd = i;
            }

            //找到結束
            if (html.charAt(i) == '>' && htmlTagStart != -1 && !inStr) {
                htmlTagEnd = i;
                //那個html tag沒有Attribute
                if (htmlTag == null)
                    htmlTag = html.substring(htmlTagStart + 1, i);

                int htmlTagCloseTagPos = html.lastIndexOf("</" + htmlTag + ">");
                int thisInnerHtmlLength = (htmlTagCloseTagPos + htmlTag.length() + 3);
                System.out.println("tag= " + htmlTag);
                System.out.println("inner html= " + html.substring(htmlTagEnd + 1));
                System.out.println("");
//                System.out.println((htmlTagEnd + 1) + " " + htmlTagCloseTagPos);
//                System.out.println(thisInnerHtmlLength);
//                System.out.println("inner html= " + html.substring(htmlTagEnd + 1, htmlTagCloseTagPos));

                //如果有分岔
//                if (thisInnerHtmlLength < html.length()) {
////                    findHtmlTagEnd(html.substring(thisInnerHtmlLength));
//                    findHtmlTagEnd(html.substring(htmlTagEnd + 1));
//                }
                //找結束
                findHtmlTagEnd(html.substring(htmlTagEnd + 1, htmlTagCloseTagPos));

                break;
            }

            //找到key
            if (html.charAt(i) == '=' && !inStr) {
                equalPos = i;
                lastAttributeEnd = htmlAttributeEnd;
            }

            //有找到開頭了且還沒找到字串開頭
            if (htmlTagStart != -1 && !inStr)
                if (html.charAt(i) == '\'' || html.charAt(i) == '\"' || html.charAt(i) == '`') {//找到字串
                    firstQuotation = html.charAt(i);
                    htmlAttributeStart = i;
                    inStr = true;
                }

            //找到字串結束
            if (html.charAt(i) == firstQuotation && inStr && htmlAttributeStart != i) {
                htmlAttributeEnd = i;
                inStr = false;
                System.out.print(html.substring(lastAttributeEnd + 1, equalPos).replace(" ", "") + "= ");
                System.out.println(html.substring(htmlAttributeStart + 1, htmlAttributeEnd));
            }

        }
//        System.out.println(htmlTagStart + " " + htmlTagEnd);
    }

    private static int readHtml(String html) {
        boolean inStr = false;
        boolean inTag = false;
        boolean inInnerHTML = false;
        char firstQuotation = 0;
        for (int i = 0; i < html.length(); i++) {
            //不在string裡
            if (!inStr)
                if (html.charAt(i) == '\'' || html.charAt(i) == '\"' || html.charAt(i) == '`') {//找到字串
                    firstQuotation = html.charAt(i);
                    inStr = true;
                    continue;
                }

            //找到字串結束
            if (html.charAt(i) == firstQuotation && inStr) {
                inStr = false;
            }

            if (html.charAt(i) == '<' && !inStr) {
                System.out.println(i);
                inTag = true;
            }

            if (html.charAt(i) == '>' && inTag && !inStr) {
                inTag = false;
                System.out.println(i);
            }
        }
        return 0;
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
