package net.martp.mihail.mumhelper;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.martp.mihail.mumhelper.Structure.RatingStructure;

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

    //  private static ArrayList<RatingStructure> arrayListRatingN = new ArrayList<>();
    private String ratingGetDataError = "";
    View getViewRatingFragment;
    static private ArrayList<RatingStructure> ratingGroupArrayList = new ArrayList<>();

    public RatingNFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getViewRatingFragment = inflater.inflate(R.layout.fragment_rating_n, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseDataRatingNAsyncTask parseDataRatingNAsyncTask = new ParseDataRatingNAsyncTask();
        parseDataRatingNAsyncTask.execute();
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

            dialog = new ProgressDialog(getViewRatingFragment.getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
           // SharedPreferences sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String studentID = getActivity().getPreferences(Context.MODE_PRIVATE).getString(MainActivity.SAVED_STUDENT_ID, "");
            Document doc;
            Connection.Response res;
            ratingGetDataError = "";
            ratingGroupArrayList.clear();
            try {
                res = Jsoup.connect("http://student.miu.by/learning-card.html")
                        .data("act", "regnum", "id", "id", "regnum", studentID)
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                ratingGetDataError = "network";
                return null;
            }

            String sessionId = res.cookie("PHPSESSID");

            try {
                doc = Jsoup.connect("http://student.miu.by/rating/~/my.group.html")
                        .cookie("PHPSESSID", sessionId)
                        .get();
            } catch (IOException e) {
                ratingGetDataError = "network";
                return null;
            }


            Elements rows;
            try {
                Element table = doc.select("table").first();
                rows = table.select("tr");
            } catch (Exception e) {
                ratingGetDataError = "other_error";
                return null;
            }

            String srBall = "";
            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() == 1) {
                    splitFullNamefromStr(cols.get(0).text());
                    ratingGroupArrayList.add(new RatingStructure("0", retSurname, retName, srBall));
                    continue;
                }

                if (cols.get(1).text().equals("")) {
                    splitFullNamefromStr(cols.get(2).text());
                    srBall = cols.get(3).text(); ///не удалять
                } else {
                    splitFullNamefromStr(cols.get(1).text());
                    srBall = cols.get(2).text(); ///не удалять
                }
                ratingGroupArrayList.add(new RatingStructure(cols.get(0).text(), retSurname, retName, srBall));
            }

            if (ratingGroupArrayList.size() == 0) ratingGetDataError = "other_error";
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

            if (!ratingGetDataError.equals("")) {
                if (ratingGetDataError.equals("network")) {
                    Toast.makeText(getActivity(), "Ошибка подключения к сети", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Произошла какая-то ошибка #13", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            RatingStructure ratingStructure;

            queryRatingTableLayout = (TableLayout) getViewRatingFragment.findViewById(R.id.ratingTable);

            for (int i = 0; i < ratingGroupArrayList.size(); i++) {
                ratingStructure = ratingGroupArrayList.get(i);
                makeRatingsLine(ratingStructure.getNumber(),
                        ratingStructure.getSurname(),
                        ratingStructure.getName(),
                        ratingStructure.getAvarege(), i);
            }
        }

        private void makeRatingsLine(String number, String surname, String name, String avarege, int index) {
            context = getViewRatingFragment.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //    View newTagView = inflater.inflate(R.layout.rating_list_item2, null);
            View newTagView = inflater.inflate(R.layout.rating_list_item2, (ViewGroup)getView(), false);

            ImageView rtImage = (ImageView) newTagView.findViewById(R.id.imageViewRaiting);

            switch (number) {
                case "1":
                    rtImage.setImageResource(R.drawable.rait1);
                    break;
                case "2":
                    rtImage.setImageResource(R.drawable.rait2);
                    break;
                case "3":
                    rtImage.setImageResource(R.drawable.rait3);
                    break;
                case "4":
                    rtImage.setImageResource(R.drawable.rait4);
                    break;
                case "5":
                    rtImage.setImageResource(R.drawable.rait5);
                    break;
                case "6":
                    rtImage.setImageResource(R.drawable.rait6);
                    break;
                case "7":
                    rtImage.setImageResource(R.drawable.rait7);
                    break;
                case "8":
                    rtImage.setImageResource(R.drawable.rait8);
                    break;
                case "9":
                    rtImage.setImageResource(R.drawable.rait9);
                    break;
                case "10":
                    rtImage.setImageResource(R.drawable.rait10);
                    break;
                default:
                    rtImage.setImageResource(R.drawable.rait10);
                    break;
            }

            TextView textNumberRating = (TextView) newTagView.findViewById(R.id.numberRating);
            textNumberRating.setText(number);

            TextView textSurname = (TextView) newTagView.findViewById(R.id.surname);
            textSurname.setText(surname);

            TextView textName = (TextView) newTagView.findViewById(R.id.name);
            textName.setText(name);

            TextView textAvarege = (TextView) newTagView.findViewById(R.id.avarege);
            textAvarege.setText(avarege);


            if (number.equals("0")) {
                textNumberRating.setVisibility(View.GONE);
                rtImage.setVisibility(View.GONE);

                TableRow tableRow = (TableRow) newTagView.findViewById(R.id.tableRowLine);
                tableRow.setVisibility(View.GONE);
            }

          //  SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
            SharedPreferences sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String fullNameStudent = sPref.getString(MainActivity.SAVED_SURNAME_STUDENT, "") +
                    sPref.getString(MainActivity.SAVED_NAME_STUDENT, "") + " " +
                    sPref.getString(MainActivity.SAVED_MIDNAME_STUDENT, "");
            String currentNameStudent = surname + name;

            if (currentNameStudent.equals(fullNameStudent)) {
                TableRow tableRowSurname = (TableRow) newTagView.findViewById(R.id.tableRowSurname);
                TableRow tableRowName = (TableRow) newTagView.findViewById(R.id.tableRowName);
                Resources resource = context.getResources();
                tableRowSurname.setBackgroundColor(resource.getColor(R.color.light_blue44));
                tableRowName.setBackgroundColor(resource.getColor(R.color.light_blue44));
            }

            queryRatingTableLayout.addView(newTagView, index);
        }
    }
}
