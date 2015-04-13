package net.martp.mihail.mumhelper;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
public class ScheduleFragment extends Fragment {

    private static ArrayList<ScheduleStructure> arrayListSchedule = new ArrayList<>();

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viev = inflater.inflate(R.layout.fragment_schedule2, container, false);
        return viev;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        String savedNumberGroup = sPref.getString(MainActivity.SAVED_NUMBER_GROUP, "");
        EditText textEdit = (EditText) getView().findViewById(R.id.groupNumberSearch);
        textEdit.setText(savedNumberGroup);

        Spinner spinnerWeek = (Spinner) getView().findViewById(R.id.spinnerWeekNumberSearch);
        Log.e("LOG2", "Spinner= - " + spinnerWeek.getSelectedItem());

        if (spinnerWeek.getSelectedItem() != null) {
            weekSpinnerText = spinnerWeek.getSelectedItem().toString(); //16
        } else {
            weekSpinnerText = "16";
        }
        Log.e("LOG2", "SpinnerTXT= - " + weekSpinnerText.toString());

        ParseDataScheduleAsyncTask parseDataScheduleAsyncTask = new ParseDataScheduleAsyncTask();
        parseDataScheduleAsyncTask.execute();
    }

    String weekSpinnerText = "1";

    private class ParseDataScheduleAsyncTask extends AsyncTask<Void, Integer, Void> {
        public ProgressDialog dialog;
        TableLayout querySchedultTableLayout;
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

        private ArrayList<ScheduleStructure> getArraySchedule() {
            return arrayListSchedule;
        }

        private void setArraySchedule(ArrayList<ScheduleStructure> arraySchedultf) {
            arrayListSchedule = arraySchedultf;
        }

        ArrayList<String> arrayWeekSpinner = new ArrayList<>();


        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<ScheduleStructure> arrayListScheduetLocal = new ArrayList<>();


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

            /*
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
*/

            System.out.println("Неделя:");
            for (String s : arrayWeek) {
                System.out.print(s + ", ");
                arrayWeekSpinner.add(s);
            }


            Log.v("TestLog", "arrayWeekSpinner =" + arrayWeekSpinner);


            ////++++++++++++++++++++++++++++

            //   String week = "19"; //16
            String week = weekSpinnerText;
            Log.e("LOG2", "week= - " + weekSpinnerText);

            //   String group = "91201з";
            //   String prep = "Жуковский В.С.";

            // tut nado schitat iz editbox
            //String dataSearch = group;
            EditText textEdit = (EditText) getView().findViewById(R.id.groupNumberSearch);
            String dataSearch = textEdit.getText().toString();

            boolean letterStatus = true;
            try {
                Integer.parseInt(dataSearch.substring(0, 1));
            } catch (Exception e) {
                letterStatus = false;
            }

            // Document doc = null;
            doc = null;
            try {
                if (letterStatus) {
                    doc = Jsoup.connect("http://miu.by/rus/schedule/shedule_load.php")
                            .data("week", week)
                            .data("group", dataSearch)
                            .userAgent("Mozilla")
                            .post();
                } else {
                    doc = Jsoup.connect("http://miu.by/rus/schedule/shedule_load.php")
                            .data("week", week)
                            .data("prep", dataSearch)
                            .userAgent("Mozilla")
                            .post();
                }
            } catch (IOException e) {
                // e.printStackTrace();
                System.out.println("Ошибка подключени к сети main");
            }

            //  System.out.println(doc);
            //     ArrayList<ScheduleStructure> schedultStructureArrayList = new ArrayList<ScheduleStructure>();

            if (doc.text().equals("")) System.out.println("Error empty html");
            else {

                Element table = doc.select("table").get(0);
                Elements rows = table.select("tr");

                System.out.println(rows.size());

                String date = "";
                for (int i = 0; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");
                    //             System.out.print(cols.get(0).text());
                    if (cols.size() == 1) {
                        //                 System.out.println();
                        date = cols.get(0).text();
                        continue;
                    }

                    arrayListScheduetLocal.add(new ScheduleStructure(date, cols.get(0).text(), cols.get(1).text(), cols.get(2).text(), cols.get(3).text()));
                }
            }

            for (int i = 0; i < arrayListScheduetLocal.size(); i++) {
                ScheduleStructure schedultStructure = arrayListScheduetLocal.get(i);
                String getLastDay = "";
                if (i > 0) getLastDay = arrayListScheduetLocal.get(i - 1).getDate();
                if (schedultStructure.getDate().equals(getLastDay)) {
                    System.out.println(schedultStructure.getTime() + " " + schedultStructure.getSubject() + " " + schedultStructure.getClassroom() + schedultStructure.getTypelesson() + schedultStructure.getTeacher());
                } else {
                    System.out.println(schedultStructure.getDate() + "\n" + schedultStructure.getTime() + " " + schedultStructure.getSubject() + " " + schedultStructure.getClassroom() + schedultStructure.getTypelesson() + schedultStructure.getTeacher());
                }
            }

            setArraySchedule(arrayListScheduetLocal);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            Spinner spinnerWeek = (Spinner) getView().findViewById(R.id.spinnerWeekNumberSearch);

            ArrayAdapter<String> snprAdapter = new ArrayAdapter(getView().getContext(),
                    android.R.layout.simple_spinner_item, arrayWeekSpinner);
            spinnerWeek.setAdapter(snprAdapter);

            spinnerWeek.setSelection(arrayWeekSpinner.size() - 1);
            //setWeekSpinnerText(spinnerWeek.getSelectedItem().toString());
            weekSpinnerText = spinnerWeek.getSelectedItem().toString();

            spinnerWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {

                    Spinner spinnerWeek = (Spinner) getView().findViewById(R.id.spinnerWeekNumberSearch);

                    Toast toast = Toast.makeText(getActivity(), "Ваш выбор: " + spinnerWeek.getSelectedItem().toString(), Toast.LENGTH_SHORT);
                    toast.show();
                    Log.e("LOG1", "log vnutri - " + spinnerWeek.getSelectedItem().toString());
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            ArrayList<ScheduleStructure> arrayListScheduleLocal = getArraySchedule();

            ScheduleStructure scheduleStructure;

            querySchedultTableLayout = (TableLayout) getView().findViewById(R.id.scheduleTableLayout);

            if (arrayListScheduleLocal.size() > 0) {
                for (int i = 0; i < arrayListScheduleLocal.size(); i++) {
                    scheduleStructure = arrayListScheduleLocal.get(i);
                    makeScheduleLine(scheduleStructure.getDate(),
                            scheduleStructure.getTime(),
                            scheduleStructure.getSubject(),
                            scheduleStructure.getTeacher(),
                            scheduleStructure.getClassroom(), i);
                }
            } else {
                Toast toast = Toast.makeText(getActivity(), "Расписание не найдено!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        String oldDate = "";

        private void makeScheduleLine(String getDate, String getTime, String getSubject, String getTeacher, String getClassroom, int index) {
            context = getView().getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            View newTagView = inflater.inflate(R.layout.schedule_list_item, null);

            TextView textDate = (TextView) newTagView.findViewById(R.id.dayDate);
            if (oldDate.equals(getDate)) {
                textDate.setVisibility(View.GONE);

                TableRow tableRow = (TableRow) newTagView.findViewById(R.id.scheduleLine);
                tableRow.setVisibility(View.GONE);
            } else {
                textDate.setText(getDate);
                oldDate = getDate;
            }

            TextView textTime = (TextView) newTagView.findViewById(R.id.lessonTime);
            textTime.setText(getTime);

            TextView textSubject = (TextView) newTagView.findViewById(R.id.subject);
            textSubject.setText(getSubject);

            TextView textTeacher = (TextView) newTagView.findViewById(R.id.teacherName);
            textTeacher.setText(getTeacher);

            TextView textClassroom = (TextView) newTagView.findViewById(R.id.classroomNumber);
            textClassroom.setText(getClassroom);

            TextView textTypeLesson = (TextView) newTagView.findViewById(R.id.typeLesson);
            textTypeLesson.setText(""); //!!!!!


            querySchedultTableLayout.addView(newTagView, index);

        }
    }


}
