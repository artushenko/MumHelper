package net.martp.mihail.mumhelper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Mihail on 05.01.2015.
 */
public class ParseDataOrder {
    public ParseDataOrder() {

        Document doc = null;
        Connection.Response res = null;


        try {
            res = Jsoup.connect("http://student.miu.by/learning-card.html")
                    .data("act", "regnum", "id", "id", "regnum", "20090312012423")
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException e) {
            //  e.printStackTrace();
            System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
            return;
        }

        String sessionId = res.cookie("PHPSESSID");

        try {
            doc = Jsoup.connect("http://student.miu.by/learning-card/~/my.orders.html")
//            doc = Jsoup.connect("http://martp.net/muu/my.orders.html")
                    .cookie("PHPSESSID", sessionId)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
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
            System.out.print(cols.get(2).text());
            System.out.println();


        }

    }
}
