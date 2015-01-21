package net.martp.mihail.mumhelper;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarksFragment extends Fragment {

    private static ArrayList<String> arrayListMarks = new ArrayList<>();

    public MarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseDataMarksAsyncTask parseDadaNewsAsyncTask = new ParseDataMarksAsyncTask();
        parseDadaNewsAsyncTask.execute();
        OutToActivity outToActivity = new OutToActivity();

    }

    private class OutToActivity extends Activity {
        private OutToActivity() {


        }
    }


    private class ParseDataMarksAsyncTask extends AsyncTask<Void, Integer, Void> {

        public ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dialog = new ProgressDialog(getView().getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();

        }


        private ArrayList<String> getArrayMarks() {
            return arrayListMarks;
        }

        private void setArrayMarks(ArrayList<String> arrayMarksf) {
            arrayListMarks = arrayMarksf;
        }
        @Override
        protected Void doInBackground(Void... params) {

            Document doc = null;
            try {
                doc = Jsoup.connect("http://martp.net/muu/learning-card.html")
                        .data("act", "regnum").data("id", "id").data("regnum", "20090312012423").post();
            } catch (IOException e) {
                System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
               // return;
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
            return null;
        }

    }
}
