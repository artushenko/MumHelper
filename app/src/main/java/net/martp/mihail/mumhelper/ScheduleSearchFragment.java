package net.martp.mihail.mumhelper;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleSearchFragment extends Fragment {


    private static ArrayList<ScheduleStructure> arrayListSchedule = new ArrayList<>();

    public ScheduleSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viev = inflater.inflate(R.layout.fragment_schedule_search, container, false);
        return viev;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseDataScheduleSearchAsyncTask parseDataScheduleSearchAsyncTask = new ParseDataScheduleSearchAsyncTask();
        parseDataScheduleSearchAsyncTask.execute();
    }

    private class ParseDataScheduleSearchAsyncTask extends AsyncTask<Void, Integer, Void> {
        public ProgressDialog dialog;
        TableLayout querySchedultSearchTableLayout;
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

        private ArrayList<ScheduleStructure> getArraySchedult() {
            return arrayListSchedule;
        }

        private void setArraySchedult(ArrayList<ScheduleStructure> arraySchedultf) {
            arrayListSchedule = arraySchedultf;
        }

        ArrayList<String> arraySpecialitySpinner =new ArrayList<>();
        ArrayList<String> arrayChairSpinner=new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<ScheduleStructure> arrayListSchedultLocal = new ArrayList<>();

            Document doc = null;
            Connection.Response res = null;

            try {
                doc = Jsoup.connect("http://miu.by/rus/schedule/schedule.php").get();
            } catch (IOException e) {
                //  e.printStackTrace();
                System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
//                Toast.makeText(getActivity(), "Ошибка подключени к сети", Toast.LENGTH_SHORT).show();
                return null;
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
                arraySpecialitySpinner.add(s);
            }

            System.out.println();
            System.out.println("Кафедра:");
            for (String s : arrayChair) {
                arrayChairSpinner.add(s);
                System.out.print(s + ", ");
            }

            Log.v("TestLog", "arrayChair =" + arrayChair);

            setArraySchedult(arrayListSchedultLocal);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();


            Spinner spinnerSpeciality = (Spinner) getView().findViewById(R.id.spinnerSpeciality);
 //           spinnerSpeciality.setPrompt("Выберите специальность");

            ArrayAdapter<String> snprAdapter =new ArrayAdapter (getView().getContext(),
                    android.R.layout.simple_spinner_item, arraySpecialitySpinner);
            spinnerSpeciality.setAdapter(snprAdapter);


            Spinner spinnerChair = (Spinner) getView().findViewById(R.id.spinnerChair);
            ArrayAdapter<String> spnrChairAdapter= new ArrayAdapter(getView().getContext(), android.R.layout.simple_spinner_item, arrayChairSpinner);
            spinnerChair.setAdapter(spnrChairAdapter);


  //          Log.v("TestLog", "snprAdapter =" + snprAdapter);
  //          Log.v("TestLog", "spinnerSpeciality =" + spinnerSpeciality);


            ArrayList<ScheduleStructure> arrayListSchedultLocal = getArraySchedult();

            ScheduleStructure scheduleStructure;

            querySchedultSearchTableLayout = (TableLayout) getView().findViewById(R.id.schedultSearchTableLayout);

            for (int i = 0; i < arrayListSchedultLocal.size(); i++) {
                scheduleStructure = arrayListSchedultLocal.get(i);
                makeSchedultSearchLine(scheduleStructure.getDate(),
                        scheduleStructure.getTime(),
                        scheduleStructure.getSubject(),
                        scheduleStructure.getTeacher(),
                        scheduleStructure.getClassroom(), i);
            }
        }

        private void makeSchedultSearchLine(String getDate, String getTime, String getSubject,String getTeacher,String getClassroom, int index) {
            context = getView().getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            /*
            View newTagView = inflater.inflate(R.layout.order_list_item, null);

            TextView textNumberRating = (TextView) newTagView.findViewById(R.id.course);
            textNumberRating.setText(getCourse);

            TextView textSurname = (TextView) newTagView.findViewById(R.id.numberOrder);
            textSurname.setText(getNumberOrder);

            TextView textName = (TextView) newTagView.findViewById(R.id.textOrder);
            textName.setText(getTextOrder);

            TextView textName = (TextView) newTagView.findViewById(R.id.textOrder);
            textName.setText(getTextOrder);

            TextView textName = (TextView) newTagView.findViewById(R.id.textOrder);
            textName.setText(getTextOrder);

            querySchedultSearchTableLayout.addView(newTagView, index);


            */
        }
    }

}
