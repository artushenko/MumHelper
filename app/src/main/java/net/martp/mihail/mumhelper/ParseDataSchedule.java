package net.martp.mihail.mumhelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Mihail on 24.12.2014.
 */
public class ParseDataSchedule {

    public ParseDataSchedule() {
        Document doc = null;

        try {
            doc = Jsoup.connect("http://miu.by/rus/schedule/schedule.php").get();
        } catch (IOException e) {
            //  e.printStackTrace();
            System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
            return;
        }

        ArrayList<String> arrayN = new ArrayList<String>();
        Elements elems = doc.select("option");
        for (Element elem : elems) {
            arrayN.add(elem.attr("value"));
        }

        Iterator<String> iterator = arrayN.iterator();
        ArrayList<String> arrayWeek = new ArrayList<String>();
        while (iterator.hasNext()) {
            String arrayEl = iterator.next();
            if (arrayEl.equals("")) break;
            arrayWeek.add(arrayEl);
        }

        ArrayList<String> arraySpeciality = new ArrayList<String>();
        while (iterator.hasNext()) {
            String arrayEl = iterator.next();
            if (arrayEl.equals("")) break;
            arraySpeciality.add(arrayEl);
        }

        ArrayList<String> arrayChair = new ArrayList<String>();
        while (iterator.hasNext()) {
            String arrayEl = iterator.next();
            if (arrayEl.equals("")) break;
            arrayChair.add(arrayEl);
        }

        System.out.println("Неделя:");
        for (String s : arrayWeek) {
            System.out.print(s + ", ");
        }
        System.out.println();
        System.out.println("Специальность:");
        for (String s : arraySpeciality) {
            System.out.print(s + ", ");
        }

        System.out.println();
        System.out.println("Кафедра:");
        for (String s : arrayChair) {
            System.out.print(s + ", ");
        }
    }


}
