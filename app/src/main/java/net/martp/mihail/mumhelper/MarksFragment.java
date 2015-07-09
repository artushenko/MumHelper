package net.martp.mihail.mumhelper;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.martp.mihail.mumhelper.Structure.MarkStructure;

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

    View getViewMarksFragment;
    private static ArrayList<MarkStructure> arrayListMarks = new ArrayList<>();
    private static String marksUrl = "http://student.miu.by/learning-card.html";
    private String marksGetDataError = "";

    public MarksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getViewMarksFragment = inflater.inflate(R.layout.fragment_marks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParseDataMarksAsyncTask parseDataMarksAsyncTask = new ParseDataMarksAsyncTask();
        parseDataMarksAsyncTask.execute();
    }

    private class ParseDataMarksAsyncTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog dialog;
        TableLayout queryTableLayout;
        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getViewMarksFragment.getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Read studentID from preferences
            arrayListMarks.clear();
            SharedPreferences sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String studentID = sPref.getString(MainActivity.SAVED_STUDENT_ID, "");

            Document doc;//= null;
            try {
                //   doc = Jsoup.connect("http://student.miu.by/learning-card/~/sem.all.html")
                //    doc = Jsoup.connect("http://student.miu.by/learning-card.html")
                doc = Jsoup.connect(marksUrl)
                        .data("act", "regnum").data("id", "id").data("regnum", studentID).post();
            } catch (IOException e) {
                marksGetDataError = "network";
                return null;
            }

            try {
                Elements countSemestrsHTML = doc.select("a.but");
                countSemestrs = Integer.parseInt(countSemestrsHTML.get(0).text());
            } catch (Exception e) {
                marksGetDataError = "network";
                return null;
            }

            Element table = doc.select("table").get(1);
            Elements rows = table.select("tr");

            String status;// = "";

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                String body = cols.get(2).getElementsByTag("img").attr("alt");
                if (body.equals("зачтено") || body.equals("незачтено"))
                    status = body;
                else
                    status = cols.get(2).text();
                arrayListMarks.add(new MarkStructure(getStatusDiscipline(status), cols.get(0).text(), cols.get(1).text(), status, cols.get(3).text()));
            }
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

            if (!marksGetDataError.equals("")) {
                if (marksGetDataError.equals("network")) {
                    Toast.makeText(getActivity(), "Ошибка подключения к сети", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            MarkStructure mark;
            queryTableLayout = (TableLayout) getViewMarksFragment.findViewById(R.id.markTable);
            for (int i = 0; i < arrayListMarks.size(); i++) {
                mark = arrayListMarks.get(i);
                makeMarksLine(mark.getNameOfDiscipline(),
                        mark.getFormOfControl(),
                        mark.getMark(),
                        mark.getDate(),
                        mark.getStatud(), i);
            }

            int countSemestrsLine = arrayListMarks.size();
            //      Log.v("LOGSEMSTR", "Всего оценок " + countSemestrsLine);
            if (countSemestrs > 4) {
                for (int i = 0; i < normalizeCountSemestrs(); i++) {
                    makeSemestrButtonLine(i, countSemestrsLine++);
                }
            } else {
                makeSemestrButtonLine(0, countSemestrsLine);
            }
        }

        private int normalizeCountSemestrs() {
            int a = (countSemestrs / 4) + 1;
            if (countSemestrs == 8 || countSemestrs == 12) a--;
            return a;
        }

        private int countSemestrs = 8;
        private boolean interlaceLine = true;

        private void makeMarksLine(String nameOfDiscipline, String formOfControl, String mark, String date, boolean status, int index) {
            context = getViewMarksFragment.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newTagView = inflater.inflate(R.layout.marks_list_item3, (ViewGroup) getView(), false);
            //      View newTagView = inflater.inflate(R.layout.marks_list_item3, null);

            if (interlaceLine) {
                RelativeLayout subjectMarkLayout = (RelativeLayout) newTagView.findViewById(R.id.subjectMarkLayout);
                subjectMarkLayout.setBackgroundResource(R.color.background_interlace_line);
            }
            interlaceLine = !interlaceLine;

            ImageView statusPredmet = (ImageView) newTagView.findViewById(R.id.statusPredmetImageView);
            Bitmap srcBitmapLocal = BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.drawable.tick);
            if (!status) {
                srcBitmapLocal = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),
                        R.drawable.cross);
            }
            statusPredmet.setImageBitmap(srcBitmapLocal);

            TextView textNameOfDiscipline = (TextView) newTagView.findViewById(R.id.nameOfDiscipline);
            textNameOfDiscipline.setText(nameOfDiscipline);

            TextView textFormOfControl = (TextView) newTagView.findViewById(R.id.formOfControl);
            textFormOfControl.setText(formOfControl);

            TextView textMark = (TextView) newTagView.findViewById(R.id.mark);
            textMark.setText(mark);

            TextView textDate = (TextView) newTagView.findViewById(R.id.dayDate);
            textDate.setText(date);

            queryTableLayout.addView(newTagView, index);
        }

        private void makeSemestrButtonLine(int countSemestrsN, int index) {
            context = getViewMarksFragment.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           //   View newTagView = inflater.inflate(R.layout.marks_list_term, (ViewGroup) getView(), false);
            View newTagView = inflater.inflate(R.layout.marks_list_term, null);

            Button buttonAll = (Button) newTagView.findViewById(R.id.buttonAllSemestrs);
            if (countSemestrs > 4 && countSemestrsN > 0) {
                TableRow tableRow = (TableRow) newTagView.findViewById(R.id.tableRowEmpty1);
                tableRow.setVisibility(View.GONE);
                buttonAll.setVisibility(View.GONE);
            }
            buttonAll.setTag("0");
            buttonAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButtonSemestr(v);
                }
            });

            Button button1 = (Button) newTagView.findViewById(R.id.button1);
            button1.setText((countSemestrsN * 4 + 1) + "");
            button1.setTag((countSemestrsN * 4 + 1) + "");
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButtonSemestr(v);
                }
            });

            Button button2 = (Button) newTagView.findViewById(R.id.button2);
            if ((countSemestrsN * 4 + 2) > countSemestrs) {
                button2.setVisibility(View.GONE);
                TextView textView31 = (TextView) newTagView.findViewById(R.id.textView31Empty);
                textView31.setVisibility(View.GONE);
            } else {
                button2.setText((countSemestrsN * 4 + 2) + "");
                button2.setTag((countSemestrsN * 4 + 2) + "");
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickButtonSemestr(v);
                    }
                });
            }

            Button button3 = (Button) newTagView.findViewById(R.id.button3);
            if ((countSemestrsN * 4 + 3) > countSemestrs) {
                button3.setVisibility(View.GONE);
                TextView textView33 = (TextView) newTagView.findViewById(R.id.textView33Empty);
                textView33.setVisibility(View.GONE);
            } else {
                button3.setText((countSemestrsN * 4 + 3) + "");
                button3.setTag((countSemestrsN * 4 + 3) + "");
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickButtonSemestr(v);
                    }
                });
            }

            Button button4 = (Button) newTagView.findViewById(R.id.button4);
            if ((countSemestrsN * 4 + 4) > countSemestrs) {
                button4.setVisibility(View.GONE);
                TextView textView32 = (TextView) newTagView.findViewById(R.id.textView32Empty);
                textView32.setVisibility(View.GONE);
            } else {
                button4.setText((countSemestrsN * 4 + 4) + "");
                button4.setTag((countSemestrsN * 4 + 4) + "");
                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickButtonSemestr(v);
                    }
                });
            }
            queryTableLayout.addView(newTagView, index);
        }

        public void onClickButtonSemestr(View v) {
            if (v.getTag().toString().equals("0"))
                marksUrl = "http://student.miu.by/learning-card/~/sem.all.html";
            else
                marksUrl = "http://student.miu.by/learning-card/~/sem." + v.getTag().toString() + ".html";
            FragmentTransaction fTrans = getFragmentManager().beginTransaction();
            MarksFragment marksFragment = new MarksFragment();
            fTrans.replace(R.id.frgmCont, marksFragment);
            fTrans.commit();
        }

    }
}
