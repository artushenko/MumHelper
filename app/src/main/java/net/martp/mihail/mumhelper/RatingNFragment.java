package net.martp.mihail.mumhelper;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

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
public class RatingNFragment extends Fragment {

    private static ArrayList<RatingStructure> arrayListRatingN = new ArrayList<>();

    public RatingNFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marks, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseDataRatingNAsyncTask parseDataRatingNAsyncTask = new ParseDataRatingNAsyncTask();
        parseDataRatingNAsyncTask.execute();
        // OutToActivity outToActivity =  new OutToActivity();
        //       new OutToActivity();
    }


    private class ParseDataRatingNAsyncTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog dialog;
        TableLayout queryTableLayout;
        Context context;
        private String retName;
        private String retSurame;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getView().getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        private ArrayList<RatingStructure> getArrayRatingN() {
            return arrayListRatingN;
        }

        private void setArrayRatingN(ArrayList<RatingStructure> arrayRating) {
            arrayListRatingN = arrayRating;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<RatingStructure> ratingGroupArrayListLocal = new ArrayList<>();
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
                    splitFullNamefromStr(cols.get(0).text());
                    System.out.println(retSurame + " " + retName + " " + srBall);
                    ratingGroupArrayListLocal.add(new RatingStructure("0", retName, retSurame, srBall));

                    //System.out.println(splitFullNamefromStr(cols.get(0).text()) + " " + srBall);
                    // ratingGroupArrayListLocal.add(splitFullNamefromStr(cols.get(0).text()) + " " + srBall);
                    continue;
                }
                System.out.print(cols.get(0).text());
                System.out.print(" ");
                splitFullNamefromStr(cols.get(2).text());
                //  System.out.print(splitFullNamefromStr(cols.get(2).text()));
                System.out.print(retName + retSurame);
                System.out.print(" ");
                srBall = cols.get(3).text(); ///не удалять
                //System.out.print(srBall = cols.get(3).text());
                System.out.print(srBall);
                System.out.println();
                //ratingGroupArrayListLocal.add(cols.get(0).text() + "\n" + splitFullNamefromStr(cols.get(2).text()) + " " + cols.get(3).text());

                ratingGroupArrayListLocal.add(new RatingStructure(cols.get(0).text(), retSurame, retName, srBall));
            }

            setArrayRatingN(ratingGroupArrayListLocal);
            return null;
        }

        private void splitFullNamefromStr(String str) {
            String[] strArray = str.split(" ");
            retName = strArray[0];
            retSurame = strArray[1] + " " + strArray[2];
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            ArrayList<RatingStructure> arrayListRatingLocal = getArrayRatingN();

            RatingStructure ratingStructure;

            queryTableLayout = (TableLayout) getView().findViewById(R.id.ratingTable);


            for (int i = 0; i < arrayListRatingLocal.size(); i++) {
                ratingStructure = arrayListRatingLocal.get(i);
                makeRatingsLine(ratingStructure.getNumber(),
                        ratingStructure.getSurname(),
                        ratingStructure.getName(),
                        ratingStructure.getAvarege(), i);
            }

        }

        private void makeRatingsLine(String number, String surname, String name, String avarege, int index) {
            context = getView().getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newTagView = inflater.inflate(R.layout.rating_list_item, null);

            TextView textNumberRating = (TextView) newTagView.findViewById(R.id.numberRating);
            textNumberRating.setText(number);

            TextView textSurname = (TextView) newTagView.findViewById(R.id.surname);
            textSurname.setText(surname);

            TextView textName = (TextView) newTagView.findViewById(R.id.name);
            textName.setText(name);

            TextView textAvarege = (TextView) newTagView.findViewById(R.id.avarege);
            textAvarege.setText(avarege);

            queryTableLayout.addView(newTagView, index);
        }
    }
}
