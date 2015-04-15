package net.martp.mihail.mumhelper;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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
        View view = inflater.inflate(R.layout.fragment_rating_n, container, false);
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
        TableLayout queryRatingTableLayout;
        Context context;
        private String retName;
        private String retSurname;

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
            SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
            String studentID = sPref.getString(MainActivity.SAVED_STUDENT_ID, "");
            Document doc = null;
            Connection.Response res = null;
            try {
                res = Jsoup.connect("http://student.miu.by/learning-card.html")
                        .data("act", "regnum", "id", "id", "regnum", studentID)
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
                    System.out.println(retSurname + " " + retName + " " + srBall);
                    ratingGroupArrayListLocal.add(new RatingStructure("0", retSurname, retName, srBall));

                    //System.out.println(splitFullNamefromStr(cols.get(0).text()) + " " + srBall);
                    // ratingGroupArrayListLocal.add(splitFullNamefromStr(cols.get(0).text()) + " " + srBall);
                    continue;
                }
                System.out.print(cols.get(0).text());
                System.out.print(" ");

                if (cols.get(1).text().equals("")) {
                    splitFullNamefromStr(cols.get(2).text());
                    //  System.out.print(splitFullNamefromStr(cols.get(2).text()));
                    System.out.print(retName + retSurname);
                    System.out.print(" ");
                    srBall = cols.get(3).text(); ///не удалять
                    //System.out.print(srBall = cols.get(3).text());
                    System.out.print(srBall);
                    System.out.println();
                }
                else
                {
                    splitFullNamefromStr(cols.get(1).text());
                    //  System.out.print(splitFullNamefromStr(cols.get(2).text()));
                    System.out.print(retName + retSurname);
                    System.out.print(" ");
                    srBall = cols.get(2).text(); ///не удалять
                    //System.out.print(srBall = cols.get(3).text());
                    System.out.print(srBall);
                    System.out.println();
                }
                //ratingGroupArrayListLocal.add(cols.get(0).text() + "\n" + splitFullNamefromStr(cols.get(2).text()) + " " + cols.get(3).text());

                ratingGroupArrayListLocal.add(new RatingStructure(cols.get(0).text(), retSurname, retName, srBall));
            }

            setArrayRatingN(ratingGroupArrayListLocal);
            return null;
        }

        private void splitFullNamefromStr(String str) {
            String[] strArray = str.split(" ");
            retSurname = strArray[0];
            retName = strArray[1] + " " + strArray[2];
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            ArrayList<RatingStructure> arrayListRatingLocal = getArrayRatingN();

            RatingStructure ratingStructure;

            queryRatingTableLayout = (TableLayout) getView().findViewById(R.id.ratingTable);


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


            if (number.equals("0")) {
                TableRow tableRow = (TableRow) newTagView.findViewById(R.id.tableRowNumber);
                tableRow.setVisibility(View.GONE);

                tableRow = (TableRow) newTagView.findViewById(R.id.tableRowLine);
                tableRow.setVisibility(View.GONE);
            }

            SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
            String fullNameStudent = sPref.getString(MainActivity.SAVED_SURNAME_STUDENT, "")+
                    sPref.getString(MainActivity.SAVED_NAME_STUDENT, "")+" "+
                    sPref.getString(MainActivity.SAVED_MIDNAME_STUDENT, "");
            String currentNameStudent= surname+name;

            /*
            Log.v("LOG4", "fullNameStudent = " + fullNameStudent);
            Log.v("LOG4", "currentNameStudent = " + currentNameStudent);
            boolean b= currentNameStudent.equals(fullNameStudent);
            Log.v("LOG4", "currentNameStudent = " +b);
*/


            if  (currentNameStudent.equals(fullNameStudent)) {
                TableRow tableRowSurname = (TableRow) newTagView.findViewById(R.id.tableRowSurname);
             //   TableRow tableRowNumber = (TableRow) newTagView.findViewById(R.id.tableRowNumber);
                TableRow tableRowName = (TableRow) newTagView.findViewById(R.id.tableRowName);
                Resources resource = context.getResources();
                tableRowSurname.setBackgroundColor(resource.getColor(R.color.light_blue44));
                tableRowName.setBackgroundColor(resource.getColor(R.color.light_blue44));
            //    tableRowNumber.setBackgroundColor(resource.getColor(R.color.light_blue44));
            }

            queryRatingTableLayout.addView(newTagView, index);
        }
    }
}
