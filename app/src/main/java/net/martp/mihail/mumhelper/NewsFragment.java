package net.martp.mihail.mumhelper;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsFragment extends Fragment {
    private static String[][] arrayNews = new String[5][4];
    private String newsGetDataError = "";
    private View viewNewsFragment;
    SharedPreferences sPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return viewNewsFragment = inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sPref.getString(MainActivity.CACHE_DATA, "").equals("no")) {
            ParseDadaNewsAsyncTask parseDadaNewsAsyncTask = new ParseDadaNewsAsyncTask();
            parseDadaNewsAsyncTask.execute();
        } else {
            ParseDataNewsCacheAsyncTask parseDataNewsCacheAsyncTask = new ParseDataNewsCacheAsyncTask();
            parseDataNewsCacheAsyncTask.execute();
        }
    }

    private class ParseDadaNewsAsyncTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(viewNewsFragment.getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Document doc;
            newsGetDataError = "";
            try {
                doc = Jsoup.connect("http://miu.by/").get();
            } catch (IOException e) {
                newsGetDataError = "network";
                return null;
            }

            Elements cols = doc.select("table").get(8).select("tr").get(1).select("td");
            doc = Jsoup.parse(cols.get(0).html());
            Element link;

            if (doc == null) return null;

            int n = 0;
            for (int i = 0; i < 10; i++) {
                link = doc.select("a").get(i);
                String newsHref = link.attr("href");
                String newsTextHref = link.text();
                System.out.println(newsTextHref + " " + newsHref);

                if (arrayNews[n][0] == null) {
                    arrayNews[n][0] = newsTextHref;
                    arrayNews[n][1] = newsHref;
                } else if (arrayNews[n][2] == null) {
                    arrayNews[n][2] = newsTextHref;
                    arrayNews[n][3] = newsHref;
                    n++;
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            if (!newsGetDataError.equals("")) {
                if (newsGetDataError.equals("network")) {
                    Toast.makeText(getActivity(), "Ошибка подключения к сети", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (arrayNews[0][0] != null) {
                outLineTextNews(arrayNews[0][1], arrayNews[0][0], arrayNews[0][2], R.id.textView4_date_1, R.id.textView);
                outLineTextNews(arrayNews[1][1], arrayNews[1][0], arrayNews[1][2], R.id.textView6_date_2, R.id.textView2);
                outLineTextNews(arrayNews[2][1], arrayNews[2][0], arrayNews[2][2], R.id.textView7_date_3, R.id.textView3);
                outLineTextNews(arrayNews[3][1], arrayNews[3][0], arrayNews[3][2], R.id.textView8_date_4, R.id.textView5);
                outLineTextNews(arrayNews[4][1], arrayNews[4][0], arrayNews[4][2], R.id.textView9_date_5, R.id.textView10);
            }
        }

        private void outLineTextNews(String urlNews, String dateNews, String textNews, int idDateNews, int idTextNews) {
            TextView newsDatetext = (TextView) viewNewsFragment.findViewById(idDateNews);
            newsDatetext.setText(Html.fromHtml("<a href=\"http://miu.by" + urlNews + "\">" + dateNews + "</a>"));
            newsDatetext.setMovementMethod(LinkMovementMethod.getInstance());
            TextView newsText = (TextView) viewNewsFragment.findViewById(idTextNews);
            newsText.setText(textNews);
        }

    }

    private class ParseDataNewsCacheAsyncTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(viewNewsFragment.getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            Document doc;

            try {
                doc = Jsoup.connect("http://martp.net/miuby.info/parse_miuby_schedule.php")
                        .data("get_news", "1")
                        .timeout(10000)
                        .method(Connection.Method.POST)
                        .get();
            } catch (IOException e) {
                newsGetDataError = "IOError";
                return null;
            }

            String jsonDoc;
            if (doc != null) {
                jsonDoc = doc.text();
            } else {
                newsGetDataError = "Empty";
                return null;
            }
            if ("error".equals(doc.text())) {
                newsGetDataError = "Empty";
                return null;
            }

            JsonParser parser = new JsonParser();
            JsonArray jsonArray = (JsonArray) parser.parse(jsonDoc);

            for (int i = 0; i < 5; i++) {
                JsonArray jsonArrayLineArray = (JsonArray) jsonArray.get(i);
                /*
                System.out.println("LOG8 "+jsonArrayLineArray.get(0).getAsString() + " " +
                        jsonArrayLineArray.get(1).getAsString().replaceAll("\\p{Cntrl}", "").trim() + " " +
                        jsonArrayLineArray.get(2).getAsString());
                        */
                arrayNews[i][0] = jsonArrayLineArray.get(0).getAsString();
                arrayNews[i][1] = jsonArrayLineArray.get(2).getAsString();
                arrayNews[i][2] = jsonArrayLineArray.get(1).getAsString().replaceAll("\\p{Cntrl}", "").trim();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            if (!newsGetDataError.equals("")) {
                switch (newsGetDataError) {
                    case "IOError":
                        Toast.makeText(getActivity(), "Ошибка подключения к сети", Toast.LENGTH_SHORT).show();
                        break;
                    case "Empty":
                        Toast.makeText(getActivity(), "Ошибка. Пустой документ", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getActivity(), "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                        break;
                }
                return;
            }

            if (arrayNews[0][0] != null) {
                outLineTextNews(arrayNews[0][1], arrayNews[0][0], arrayNews[0][2], R.id.textView4_date_1, R.id.textView);
                outLineTextNews(arrayNews[1][1], arrayNews[1][0], arrayNews[1][2], R.id.textView6_date_2, R.id.textView2);
                outLineTextNews(arrayNews[2][1], arrayNews[2][0], arrayNews[2][2], R.id.textView7_date_3, R.id.textView3);
                outLineTextNews(arrayNews[3][1], arrayNews[3][0], arrayNews[3][2], R.id.textView8_date_4, R.id.textView5);
                outLineTextNews(arrayNews[4][1], arrayNews[4][0], arrayNews[4][2], R.id.textView9_date_5, R.id.textView10);
            }
        }

        private void outLineTextNews(String urlNews, String dateNews, String textNews, int idDateNews, int idTextNews) {
            TextView newsDatetext = (TextView) viewNewsFragment.findViewById(idDateNews);
            newsDatetext.setText(Html.fromHtml("<a href=\"http://miu.by" + urlNews + "\">" + dateNews + "</a>"));
            newsDatetext.setMovementMethod(LinkMovementMethod.getInstance());
            TextView newsText = (TextView) viewNewsFragment.findViewById(idTextNews);
            newsText.setText(textNews);
        }
    }

}
