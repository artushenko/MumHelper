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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.martp.mihail.mumhelper.Structure.ScheduleStructure;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleListFragment extends Fragment {

    private static ArrayList<ScheduleStructure> arrayListSchedule = new ArrayList<>();
    private View viewScheduleFragment;

    public ScheduleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return viewScheduleFragment = inflater.inflate(R.layout.fragment_schedule_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseDataScheduleWeekAsyncTask parseDataScheduleWeekAsyncTask = new ParseDataScheduleWeekAsyncTask();
        parseDataScheduleWeekAsyncTask.execute();
    }

    private class ParseDataScheduleWeekAsyncTask extends AsyncTask<Void, Integer, Void> {
        public ProgressDialog dialog;
        TableLayout querySchedultTableLayout;
        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(viewScheduleFragment.getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        private String scheduleListGetDataError="";

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<ScheduleStructure> arrayListScheduetLocal = new ArrayList<>();

            String week = ScheduleFragment.weekSpinnerText;
            Log.e("LOG3", "week= - " + week);

            String dataSearch = ScheduleFragment.dataSearch;
            Log.e("LOG3", "dataSearch= - " + dataSearch);

            boolean letterStatus = true;
            try {
                Integer.parseInt(dataSearch.substring(0, 1));
            } catch (Exception e) {
                letterStatus = false;
            }

            Document doc;
            try {
                if (letterStatus) {
                    Log.e("LOG3", "2week= - " + week);
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
             //   System.out.println("Ошибка подключени к сети main");
                Log.v("LOG3", "Error in network!!!");
                scheduleListGetDataError="network";
                return null;
            }

            if (doc.text().equals("")) {
              //  System.out.println("Error empty html");
                Log.v("LOG3", "Error empty html");

            } else {

                Element table = doc.select("table").get(0);
                Elements rows = table.select("tr");

                System.out.println(rows.size());

                String date = "";
                for (int i = 0; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");
                    if (cols.size() == 1) {
                        date = cols.get(0).text();
                        continue;
                    }

                    /// Log.v("LOG3", "+cols.get(1) = "+cols.get(1).text());
                    String[] arraySubjectTypeLesson = cols.get(1).text().split(" ");
                    //  Log.v("LOG3", "+arraySubjectTypeLesson.lenght = "+arraySubjectTypeLesson.length);
                    String typeLesson = arraySubjectTypeLesson[arraySubjectTypeLesson.length - 1];
                    //   Log.v("LOG3", "typeLesson = "+typeLesson);

                    String subject = "";
                    for (int j = 0; j < arraySubjectTypeLesson.length - 1; j++) {
                        subject = subject + arraySubjectTypeLesson[j] + " ";
                    }
                    //    Log.v("LOG3", "Subject = "+subject);

                    arrayListScheduetLocal.add(new ScheduleStructure(date, cols.get(0).text(), subject, cols.get(2).text(), cols.get(3).text(), typeLesson));
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

        private ArrayList<ScheduleStructure> getArraySchedule() {
            return arrayListSchedule;
        }

        private void setArraySchedule(ArrayList<ScheduleStructure> arraySchedultf) {
            arrayListSchedule = arraySchedultf;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            if (!scheduleListGetDataError.equals("")) {
                if (scheduleListGetDataError.equals("network")) {
                    Toast.makeText(getActivity(), "Ошибка подключения к сети", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            ArrayList<ScheduleStructure> arrayListScheduleLocal = getArraySchedule();

            ScheduleStructure scheduleStructure;

            querySchedultTableLayout = (TableLayout) viewScheduleFragment.findViewById(R.id.scheduleTableLayout);

            if (arrayListScheduleLocal.size() > 0) {
                for (int i = 0; i < arrayListScheduleLocal.size(); i++) {
                    scheduleStructure = arrayListScheduleLocal.get(i);
                    makeScheduleLine(scheduleStructure.getDate(),
                            scheduleStructure.getTime(),
                            scheduleStructure.getSubject(),
                            scheduleStructure.getTeacher(),
                            scheduleStructure.getClassroom(),
                            scheduleStructure.getTypelesson(), i);
                }
            } else {
                Toast toast = Toast.makeText(getActivity(), "Расписание не найдено!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        String oldDate = "";

        private boolean interlaceLine =true;

        private void makeScheduleLine(String getDate, String getTime, String getSubject, String getTeacher, String getClassroom, String getTypelesson, int index) {
            context = viewScheduleFragment.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

          // View newTagView = inflater.inflate(R.layout.schedule_list_item, null);

           View newTagView = inflater.inflate(R.layout.schedule_list_item2, null);

            if (interlaceLine) {
                RelativeLayout scgeduleLayout = (RelativeLayout) newTagView.findViewById(R.id.subjectLayout);
             //   scgeduleLayout.setBackgroundColor(R.color.light_blue100);
              //  scgeduleLayout.setBackgroundColor(R.color.background_interlace_line);
                scgeduleLayout.setBackgroundResource(R.color.background_interlace_line);
            }
            interlaceLine = !interlaceLine;

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
            textTypeLesson.setText(getTypelesson); //!!!!!

            querySchedultTableLayout.addView(newTagView, index);
        }
    }

}
