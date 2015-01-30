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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
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
        View viev = inflater.inflate(R.layout.fragment_schedule, container, false);
        return viev;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseDataScheduleAsyncTask parseDataScheduleAsyncTask = new ParseDataScheduleAsyncTask();
        parseDataScheduleAsyncTask.execute();
    }

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

        private void setArraySchedult(ArrayList<ScheduleStructure> arraySchedultf) {
            arrayListSchedule = arraySchedultf;
        }

        ArrayList<String> arrayWeekSpinner =new ArrayList<>();

        String weekSpinnerText="";
        private String getWeekSpinnerText(){
            return weekSpinnerText;
        }
        private void setWeekSpinnerText(String weekSpinnerText){
             this.weekSpinnerText=weekSpinnerText;
        }


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



            setArraySchedult(arrayListSchedultLocal);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();


            Spinner spinnerWeek = (Spinner) getView().findViewById(R.id.spinnerWeek);
            //           spinnerSpeciality.setPrompt("Выберите специальность");

            ArrayAdapter<String> snprAdapter =new ArrayAdapter (getView().getContext(),
                    android.R.layout.simple_spinner_item, arrayWeekSpinner);
            spinnerWeek.setAdapter(snprAdapter);

            spinnerWeek.setSelection(arrayWeekSpinner.size()-1);
            setWeekSpinnerText(spinnerWeek.getSelectedItem().toString());

            spinnerWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {

                    Spinner spinnerWeek = (Spinner) getView().findViewById(R.id.spinnerWeek);

            //        String[] choose = getResources().getStringArray(R.array.animals);
                    //Toast toast = Toast.makeText(getActivity(),"Ваш выбор: " + spinnerWeek[selectedItemPosition], Toast.LENGTH_SHORT);
                    Toast toast = Toast.makeText(getActivity(),"Ваш выбор: "+spinnerWeek.getSelectedItem().toString() , Toast.LENGTH_SHORT);
                    toast.show();
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }


            });



            //          Log.v("TestLog", "snprAdapter =" + snprAdapter);
            //          Log.v("TestLog", "spinnerSpeciality =" + spinnerSpeciality);


            ArrayList<ScheduleStructure> arrayListSchedultLocal = getArraySchedule();

            ScheduleStructure scheduleStructure;

            querySchedultTableLayout = (TableLayout) getView().findViewById(R.id.scheduleTableLayout);

            for (int i = 0; i < arrayListSchedultLocal.size(); i++) {
                scheduleStructure = arrayListSchedultLocal.get(i);
                makeScheduleLine(scheduleStructure.getDate(),
                        scheduleStructure.getTime(),
                        scheduleStructure.getSubject(),
                        scheduleStructure.getTeacher(),
                        scheduleStructure.getClassroom(), i);
            }
        }

        private void makeScheduleLine(String getDate, String getTime, String getSubject,String getTeacher,String getClassroom, int index) {
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

            querySchedultTableLayout.addView(newTagView, index);


            */
        }
    }




}
