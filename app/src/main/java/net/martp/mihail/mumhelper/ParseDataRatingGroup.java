package net.martp.mihail.mumhelper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Mihail on 29.12.2014.
 */
public class ParseDataRatingGroup {
    public ParseDataRatingGroup() {
        Document doc = null;
        Connection.Response res = null;


        try {
            res = Jsoup.connect("http://student.miu.by/learning-card.html")
                    /*
                    .data("act", "regnum")
                    .data("id", "id")
                    .data("regnum", "20090312012423")
                    */
                    .data("act", "regnum", "id", "id", "regnum", "20090312012423")
                    .method(Connection.Method.POST)
                    .execute();
        } catch (IOException e) {
            //   e.printStackTrace();
            System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
            return;
        }

        String sessionId = res.cookie("PHPSESSID");

        try {
            doc = Jsoup.connect("http://student.miu.by/rating/~/my.group.html")
                    .cookie("PHPSESSID", sessionId)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element table = doc.select("table").first();
        Elements rows = table.select("tr");
        String srBall = "";
        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");

            if (cols.size() == 1) {
                System.out.println(splitFullNamefromStr(cols.get(0).text()) + " " + srBall);
                continue;
            }
            System.out.print(cols.get(0).text());
            System.out.print(" ");
            //   System.out.print(cols.get(1).text());
            //    System.out.print(" ");
            System.out.print(splitFullNamefromStr(cols.get(2).text()));
            // System.out.print(cols.get(2).text());
            System.out.print(" ");
            System.out.print(srBall = cols.get(3).text());
            System.out.println();
        }
    }

    private String splitFullNamefromStr(String str) {
        String[] strArray = str.split(" ");
        return strArray[0] + " " + strArray[1] + " " + strArray[2] + " " + strArray[3];
    }
}
