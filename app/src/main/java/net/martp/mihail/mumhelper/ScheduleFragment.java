package net.martp.mihail.mumhelper;


import android.app.FragmentTransaction;
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
    static String weekSpinnerText = "1";
    static String dataSearch="";

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viev = inflater.inflate(R.layout.fragment_schedule3, container, false);
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
                Log.v("TestLog", "Error in network!!!");
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

    //                Toast toast = Toast.makeText(getActivity(), "Ваш выбор: " + spinnerWeek.getSelectedItem().toString(), Toast.LENGTH_SHORT);
    //                toast.show();
                    Log.e("LOG1", "log vnutri - " + spinnerWeek.getSelectedItem().toString());


                    EditText textEdit = (EditText) getView().findViewById(R.id.groupNumberSearch);
                    dataSearch = textEdit.getText().toString();
                    weekSpinnerText=spinnerWeek.getSelectedItem().toString();

                    FragmentTransaction fTrans = getFragmentManager().beginTransaction();
                    ScheduleListFragment scheduleListFragment=new ScheduleListFragment();
                    fTrans.replace(R.id.scheduleFrameContainer, scheduleListFragment);
                    fTrans.commit();

                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }


    }


}
