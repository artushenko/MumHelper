package net.martp.mihail.mumhelper;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstStartFragment extends Fragment {

    private String image_URL = "";
    private String imageFileName;// = "";
    ImageView iv;

    public FirstStartFragment() {
        // Required empty public constructor
    }

    SharedPreferences sPref;
    EditText editText2_studentID;

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideKeyboard();

//get studentID from preferences
        EditText editStudentID = (EditText) getView().findViewById(R.id.editStudentID);
        sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        editStudentID.setText(sPref.getString(MainActivity.SAVED_STUDENT_ID, ""));
        editStudentID.setFocusable(true);

        //if press "Enter"
        editStudentID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {

                  //  if (!editText2_studentID.getText().toString().equals("")||editText2_studentID.getText().length()==14) {
                    if (editText2_studentID.getText().length()==14) {
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putString(MainActivity.SAVED_STUDENT_ID, editText2_studentID.getText().toString());
                        ed.commit();

                        ParseDataInfoAsyncTask parseDataInfoAsyncTask = new ParseDataInfoAsyncTask();
                        parseDataInfoAsyncTask.execute();
                    } else {
                        Toast.makeText(getActivity(), "Ошибка!\nID должен состоять из 14 цифр.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

// saveStudentID
        Button btnSaveID = (Button) getView().findViewById(R.id.buttonSaveID);
        editText2_studentID = (EditText) getView().findViewById(R.id.editStudentID);
        //     sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);

        View.OnClickListener oclBtnSaveID = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//save studentID to preferences
           //     if (!editText2_studentID.getText().toString().equals("")) {
                if (editText2_studentID.getText().length()==14) {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(MainActivity.SAVED_STUDENT_ID, editText2_studentID.getText().toString());
                    ed.commit();

                    //      hideKeyboard();

                    ParseDataInfoAsyncTask parseDataInfoAsyncTask = new ParseDataInfoAsyncTask();
                    parseDataInfoAsyncTask.execute();
                } else {
                    Toast.makeText(getActivity(), "Ошибка!\nID должен состоять из 14 цифр.", Toast.LENGTH_SHORT).show();
                }
                /// tut nado by skachat dannye !!!!!!
            }
        };
        btnSaveID.setOnClickListener(oclBtnSaveID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_screen, container, false);
    }

    private class ParseDataInfoAsyncTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getView().getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Document doc = null;
            Connection.Response res = null;

            //          SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
            //           String studentID = sPref.getString(MainActivity.SAVED_STUDENT_ID, "");
            EditText editStudentID = (EditText) getView().findViewById(R.id.editStudentID);

            String studentID = editStudentID.getText().toString();

            try {
                res = Jsoup.connect("http://student.miu.by/learning-card.html")
                        .data("act", "regnum", "id", "id", "regnum", studentID)
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                //   e.printStackTrace();
                System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
                Log.e("MyError", e.toString());
                error1 = "IO Error";
                return null;
                //    return;
            }

            String sessionId = res.cookie("PHPSESSID");

            try {
                doc = Jsoup.connect("http://student.miu.by/learning-card.html")
                        .cookie("PHPSESSID", sessionId)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Element table = doc.select("table").first();
                Elements rows = table.select("tr");
                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");

                    System.out.print(cols.get(0).text());
                    System.out.print(" ");
                    System.out.print(cols.get(1).text());
                    System.out.println();
                }

                System.out.println("-------------------------------------------");

                String link = table.select("tr").get(0).select("td").get(0).select("img").first().attr("src");

                System.out.println("http://student.miu.by" + link);

                imageFileName = link;
                image_URL = "http://student.miu.by" + link;

                System.out.println("-------------------------------------------");

                rows = doc.select("table").first().select("tr");

                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(MainActivity.SAVED_NUMBER_GROUP, rows.get(0).select("td").get(2).text());
                ed.commit();

                ed = sPref.edit();
                ed.putString(MainActivity.SAVED_SURNAME_STUDENT, rows.get(1).select("td").get(1).text());
                ed.commit();

                ed = sPref.edit();
                ed.putString(MainActivity.SAVED_NAME_STUDENT, rows.get(2).select("td").get(1).text());
                ed.commit();

                ed = sPref.edit();
                ed.putString(MainActivity.SAVED_MIDNAME_STUDENT, rows.get(3).select("td").get(1).text());
                ed.commit();

                ed = sPref.edit();
                ed.putString(MainActivity.SAVED_FACULTY, rows.get(4).select("td").get(1).text());
                ed.commit();

                ed = sPref.edit();
                ed.putString(MainActivity.SAVED_SPECIALTY, rows.get(5).select("td").get(1).text());
                ed.commit();

                ed = sPref.edit();
                ed.putString(MainActivity.SAVED_AVARAGE_SCORE, rows.get(6).select("td").get(1).text());
                ed.commit();

                //save studentID in preferences
                ed = sPref.edit();
                ed.putString(MainActivity.SAVED_STUDENT_ID, editText2_studentID.getText().toString());
                ed.commit();

                System.out.print(rows.get(1).select("td").get(1).text()); //surName
                System.out.println();

                System.out.print(rows.get(2).select("td").get(1).text()); //surName
                System.out.println();

                System.out.print(rows.get(3).select("td").get(1).text()); //surName
                System.out.println();

                System.out.print(rows.get(4).select("td").get(1).text()); //surName
                System.out.println();

                System.out.print(rows.get(5).select("td").get(1).text()); //surName
                System.out.println();

                System.out.print(rows.get(6).select("td").get(1).text()); //surName
                System.out.println();

                try {
                    getPhotoFromURL();
                } catch (IOException e) {
                    e.printStackTrace();
                    error1 = "Photo not load";
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e("MyError", "ID not found");
                Log.e("MyError", e.toString());
                error1 = "ID not found";
                //Toast.makeText(getActivity(), "ID not found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }

        private String error1 = "";

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            SharedPreferences.Editor ed = sPref.edit();
            if (error1.equals("ID not found")) {
                Toast.makeText(getActivity(), "Ошибка!\nСтудент с таким ID не найден.", Toast.LENGTH_SHORT).show();
                ed = sPref.edit();
                ed.putString(MainActivity.SAVED_STUDENT_ID, "");
                ed.commit();
                return;
            }
            if (error1.equals("IO Error")) {
                Toast.makeText(getActivity(), "Ошибка подключения к сети!", Toast.LENGTH_SHORT).show();
                ed = sPref.edit();
                ed.putString(MainActivity.SAVED_STUDENT_ID, "");
                ed.commit();
                return;
            }

            ImageView bmImage = (ImageView) getView().findViewById(R.id.imageView4);
            bmImage.setImageBitmap(bm);

            //save photo to sdcard
            getStudentPhoto();
            //     Toast.makeText(getActivity(), "Все прошло успешно!!!!", Toast.LENGTH_SHORT).show();
            FragmentTransaction fTrans = getFragmentManager().beginTransaction();
            InfoFragment infoFragment = new InfoFragment();

            fTrans.replace(R.id.frgmCont, infoFragment);
            fTrans.commit();
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    static Bitmap bm;

    public void getPhotoFromURL() throws IOException {
        BitmapFactory.Options bmOptions;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        //Bitmap bm = LoadImage(image_URL, bmOptions);
        bm = LoadImage(image_URL, bmOptions);
    }

    public Bitmap LoadImage(String URL, BitmapFactory.Options options) throws IOException {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
        } catch (Exception ex) {
            //Toast.makeText(getApplicationContext(), "Problems: " + ex.getMessage(), 1).show();
            //   Toast.makeText(getApplicationContext(), "Problems: access to network is closed", 1).show();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return bitmap;
    }

    private InputStream OpenHttpConnection(String strURL) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        //  url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            //Toast.makeText(getApplicationContext(), "Problems: " + ex.getMessage(), 1).show();
            //  Toast.makeText(getApplicationContext(), "Problems: access to network is closed", 1).show();
        }
        return inputStream;
    }

    private String getStudentPhoto() {
        String folderToSave = Environment.getExternalStorageDirectory().toString();
        iv = (ImageView) getView().findViewById(R.id.imageView4);
        OutputStream fOut = null;
        try {
            File file = new File(folderToSave, "photoStudent.jpg"); // создать уникальное имя для файла основываясь на дате сохранения
            fOut = new FileOutputStream(file);

            Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(MainActivity.SAVED_PHOTO, "yes");
            Log.e("Show photo yes", sPref.getString(MainActivity.SAVED_PHOTO, ""));
            ed.commit();

            //   MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(),  file.getName());
        } catch (IOException e) {
            Log.e("Show photo", e.toString());
            SharedPreferences.Editor ed = sPref.edit();
            ed = sPref.edit();
            ed.putString(MainActivity.SAVED_PHOTO, "no");
            ed.commit();
            return e.getMessage();
        }
        return "";
    }
}
