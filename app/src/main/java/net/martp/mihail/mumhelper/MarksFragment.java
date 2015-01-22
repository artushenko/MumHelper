package net.martp.mihail.mumhelper;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private static ArrayList<MarkStructure> arrayListMarks = new ArrayList<>();

    public MarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marks, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseDataMarksAsyncTask parseDataMarksAsyncTask = new ParseDataMarksAsyncTask();
        parseDataMarksAsyncTask.execute();
        // OutToActivity outToActivity =  new OutToActivity();
        new OutToActivity();
    }

    private class OutToActivity extends Activity {
        private OutToActivity() {
            Toast.makeText(getActivity(), "Начало OutToActivity", Toast.LENGTH_SHORT).show();
        }
    }

    private class ParseDataMarksAsyncTask extends AsyncTask<Void, Integer, Void> {

        ProgressDialog dialog;
        TableLayout queryTableLayout;
        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dialog = new ProgressDialog(getView().getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();

        }

        private ArrayList<MarkStructure> getArrayMarks() {
            return arrayListMarks;
        }

        private void setArrayMarks(ArrayList<MarkStructure> arrayMarksf) {
            arrayListMarks = arrayMarksf;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<MarkStructure> arrayListMarksLocal = new ArrayList<>();

            Document doc = null;
            try {
                //  doc = Jsoup.connect("http://martp.net/muu/learning-card.html")
                doc = Jsoup.connect("http://student.miu.by/learning-card/~/sem.all.html")
                        .data("act", "regnum").data("id", "id").data("regnum", "20090312012423").post();
            } catch (IOException e) {
                System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
                // return;
            }

            Element table = doc.select("table").get(1);
            Elements rows = table.select("tr");

            String status = "";

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                System.out.print(cols.get(0).text());
                System.out.print(" ");
                System.out.print(cols.get(1).text());
                System.out.print(" ");


                String body = cols.get(2).getElementsByTag("img").attr("alt");
                if (body.equals("зачтено") || body.equals("незачтено")) {
                    status = body;
                    System.out.print(body);
                } else {
                    System.out.print(cols.get(2).text());
                    status = cols.get(2).text();
                }
                System.out.print(" ");
                System.out.println(cols.get(3).text());
                arrayListMarksLocal.add(new MarkStructure(getStatusDiscipline(status), cols.get(0).text(), cols.get(1).text(), cols.get(2).text(), cols.get(3).text()));
            }
            setArrayMarks(arrayListMarksLocal);

            //         System.out.println("1111111111111");

            return null;
        }

        private boolean getStatusDiscipline(String mark) {
            if (mark.equals("зачтено")) return true;
            if (mark.equals("")) return false;

            try {
                if (Integer.parseInt(mark) >= 4) return true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Toast.makeText(getActivity(), "Reading marks is comlpete", Toast.LENGTH_SHORT).show();
            ArrayList<MarkStructure> arrayListMarkLocal = new ArrayList<>();
            arrayListMarkLocal = getArrayMarks();

            MarkStructure mark;
            queryTableLayout = (TableLayout) getView().findViewById(R.id.markTable);
            for (int i = 0; i < arrayListMarkLocal.size(); i++) {
                mark = arrayListMarkLocal.get(i);
                makeMarksLine(mark.getNameOfDiscipline(), mark.getFormOfControl(),mark.getMark(),mark.getDate(), mark.getStatud(),i);
            }
        }

        private void makeMarksLine(String nameOfDiscipline, String formOfControl, String mark, String date, boolean status, int index) {
            context=getView().getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newTagView = inflater.inflate(R.layout.marks_list_item, null);


            ImageView statusPredmet = (ImageView) newTagView.findViewById(R.id.StatusPredmetImageView);
            Bitmap srcBitmapLocal = BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.drawable.tick);
            if (!status) {
                srcBitmapLocal = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),
                        R.drawable.cross);
            }
            statusPredmet.setImageBitmap(srcBitmapLocal);

            TextView textPredmet = (TextView) newTagView.findViewById(R.id.nameOfDiscipline);
            textPredmet.setText(nameOfDiscipline);

            TextView textVidPredmate = (TextView) newTagView.findViewById(R.id.formOfControl);
            textVidPredmate.setText(formOfControl);

            TextView textMarks = (TextView) newTagView.findViewById(R.id.mark);
            textMarks.setText(mark);

            TextView textDateEkz = (TextView) newTagView.findViewById(R.id.date);
            textDateEkz.setText(date);

            queryTableLayout.addView(newTagView, index);
        }


    }
}
