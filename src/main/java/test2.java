public class test2 {
    public static void main(String[] args) {
        String html = "''+    '<div class=\"panel panel-default\">'+        '<div class=\"panel-heading\">'+            '<h4 class=\"panel-title\">'+                '<a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion-1\" href=\"#collapse-A1\" title=\"點此展開\" aria-expanded=\"true\">'+                    '<span class=\"fa-red\">熱帶性低氣壓</span><span class=\"fa-blue\">TD26</span><br>(原科羅旺颱風)'+                '</a>'+            '</h4>'+        '</div>'+        '<div id=\"collapse-A1\" class=\"panel-collapse collapse in\" aria-expanded=\"true\" role=\"document\">'+            '<div class=\"panel-body\">'+                '<p>21日14時的中心位置在北緯 9.0 度，東經 112.5 度，以每小時18公里速度，向西南西進行。<span class=\"fa-blue\">中心氣壓</span><span class=\"fa-red\">1004</span><span class=\"fa-blue\">百帕</span>，<span class=\"fa-blue\">近中心最大風速每秒</span><span class=\"fa-red\">15</span><span class=\"fa-blue\">公尺</span>，瞬間最大陣風每秒 23 公尺。</p>'+            '</div>'+        '</div>'+    '</div>'";
        html = html.replace("  ", "").replace("'", "").replace("+", "");

//        String testHTML = "<div class=\"abc\" id=\"root\"><span>hello</span><span id='window' class=\"def\">ssss</span></div>";
        String testHTML = "<span id='hello'>Hey<span>Hello</span></span><span id='world' class=\"black\">World</span>";
//        String testHTML = "<h4 class=\"panel-title\"><a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion-1\" href=\"#collapse-A1\" title=\"點此展開\" aria-expanded=\"true\"><span class=\"fa-red\">熱帶性低氣壓</span><span class=\"fa-blue\">TD26</span></a></h4>";
//        String testHTML = html.replace("<br>", "");
        System.out.println(testHTML + "\n");
        findTag(testHTML, null);

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
            if ((html.charAt(i) == '\'' || html.charAt(i) == '\"' || html.charAt(i) == '`') && !inStr && inTag) {//找到字串
                firstQuotation = html.charAt(i);
                //表示在字串裡
                inStr = true;
                continue;
            }

            //找到字串結束
            if (html.charAt(i) == firstQuotation && inStr && inTag) {
                //表示在字串外
                inStr = false;
                lastQuotation = i;
            }

            //html start
            if (html.charAt(i) == '<' && !inStr) {
                inTag = true;
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
                    return i + 2 + tagName.length();
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
                }
            }
        }


        System.out.println("end");
        return 0;
    }

}
