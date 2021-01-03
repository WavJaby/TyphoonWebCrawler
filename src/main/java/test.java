public class test {
    public static void main(String[] args) {
        String html = "''+    '<div class=\"panel panel-default\">'+        '<div class=\"panel-heading\">'+            '<h4 class=\"panel-title\">'+                '<a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion-1\" href=\"#collapse-A1\" title=\"點此展開\" aria-expanded=\"true\">'+                    '<span class=\"fa-red\">熱帶性低氣壓</span><span class=\"fa-blue\">TD26</span><br>(原科羅旺颱風)'+                '</a>'+            '</h4>'+        '</div>'+        '<div id=\"collapse-A1\" class=\"panel-collapse collapse in\" aria-expanded=\"true\" role=\"document\">'+            '<div class=\"panel-body\">'+                '<p>21日14時的中心位置在北緯 9.0 度，東經 112.5 度，以每小時18公里速度，向西南西進行。<span class=\"fa-blue\">中心氣壓</span><span class=\"fa-red\">1004</span><span class=\"fa-blue\">百帕</span>，<span class=\"fa-blue\">近中心最大風速每秒</span><span class=\"fa-red\">15</span><span class=\"fa-blue\">公尺</span>，瞬間最大陣風每秒 23 公尺。</p>'+            '</div>'+        '</div>'+    '</div>'";
        html = html.replace("  ", "").replace("'", "").replace("+", "");
        System.out.println(html);
    }

//    private static String findText(){
//
//    }
}
