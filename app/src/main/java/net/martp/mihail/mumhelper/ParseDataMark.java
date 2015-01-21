package net.martp.mihail.mumhelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Mihail on 29.12.2014.
 */
public class ParseDataMark {
    public ParseDataMark() {

        Document doc = null;
        //  doc = Jsoup.connect("http://student.miu.by/learning-card.html")
        //  doc = Jsoup.connect("http://student.miu.by/learning-card/~/sem.all.html")

        try {
            doc = Jsoup.connect("http://martp.net/muu/learning-card.html")
                    .data("act", "regnum")
                    .data("id", "id")
                    .data("regnum", "20090312012423")
                    .post();
        } catch (IOException e) {
            //  e.printStackTrace();
            System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
            return;
        }

        Element table = doc.select("table").get(1);
        Elements rows = table.select("tr");

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");
            System.out.print(cols.get(0).text());
            System.out.print(" ");
            System.out.print(cols.get(1).text());
            System.out.print(" ");

            String body = cols.get(2).getElementsByTag("img").attr("alt");
            if (body.equals("зачтено") || body.equals("незачтено"))
                System.out.print(body);
            else System.out.print(cols.get(2).text());
            System.out.print(" ");
            System.out.println(cols.get(3).text());
        }

    }
}
