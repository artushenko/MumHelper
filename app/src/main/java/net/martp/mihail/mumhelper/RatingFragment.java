package net.martp.mihail.mumhelper;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {


    public RatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_rating, container, false);
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseDataRatingGroup parseDataRatingGroup = new ParseDataRatingGroup();
        parseDataRatingGroup.execute();
        OutToActivity outToActivity = new OutToActivity(); //бесполезная какая-то фигня


    }

    private class OutToActivity extends Activity {
        private OutToActivity() {
            // можно удалить
            Toast.makeText(getActivity(), " OutToActivity", Toast.LENGTH_SHORT).show();
        }
    }

    private class ParseDataRatingGroup extends AsyncTask<Void, Integer, Void> {

        public ProgressDialog dialog;
        private ArrayList<String> ratingGroupArrayList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getView().getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        private ArrayList<String> getRatingGroupArrayList() {
            return ratingGroupArrayList;
        }

        private void setRatingGroupArrayList(ArrayList<String> ratingGroupArrayListLocal) {
            ratingGroupArrayList = ratingGroupArrayListLocal;
        }


        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<String> ratingGroupArrayListLocal = new ArrayList<>();
            Document doc = null;
            Connection.Response res = null;
            try {
                res = Jsoup.connect("http://student.miu.by/learning-card.html")
                        .data("act", "regnum", "id", "id", "regnum", "20090312012423")
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                //   e.printStackTrace();
                System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
                //  return;
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
                    ratingGroupArrayListLocal.add(splitFullNamefromStr(cols.get(0).text()) + " " + srBall);
                    continue;
                }
                System.out.print(cols.get(0).text());
                System.out.print(" ");
                System.out.print(splitFullNamefromStr(cols.get(2).text()));
                System.out.print(" ");
                srBall = cols.get(3).text(); ///не удалять
                //System.out.print(srBall = cols.get(3).text());
                System.out.print(srBall);
                System.out.println();
                ratingGroupArrayListLocal.add(cols.get(0).text() + "\n" + splitFullNamefromStr(cols.get(2).text()) + " " + cols.get(3).text());
            }

            setRatingGroupArrayList(ratingGroupArrayListLocal);
            return null;
        }

        private String splitFullNamefromStr(String str) {
            String[] strArray = str.split(" ");
            // return strArray[0] + " " + strArray[1] + " " + strArray[2] + " " + strArray[3];
            return strArray[0] + "\n" + strArray[1] + " " + strArray[2];
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            Toast.makeText(getActivity(), "Reading news is comlpete", Toast.LENGTH_SHORT).show();


            ArrayList<String> ratingGroupArrayListLocal = getRatingGroupArrayList();
            if (ratingGroupArrayListLocal.get(0) == null) {
                Toast.makeText(getActivity(), "Array is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            // String[] array=new String[ratingGroupArrayListLocal.size()+1];
            String[] array = new String[ratingGroupArrayListLocal.size()];
            for (int i = 0; i < ratingGroupArrayListLocal.size(); i++) {
                array[i] = ratingGroupArrayListLocal.get(i);
            }


            ListView list = (ListView) getView().findViewById(R.id.listView);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getView().getContext(), R.layout.custom_list_item, array);

            // устанавливаем адаптер списку
            list.setAdapter(adapter);
        }
    }
}
