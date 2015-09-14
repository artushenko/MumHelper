package net.martp.mihail.mumhelper;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
    View firstStartFragmentView;
    private String image_URL = "";
    private SharedPreferences sPref;
    private EditText editStudentID;

    public FirstStartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideKeyboard();

//get studentID from preferences
        editStudentID = (EditText) firstStartFragmentView.findViewById(R.id.editStudentID);
        // sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editStudentID.setText(sPref.getString(MainActivity.SAVED_STUDENT_ID, ""));
        editStudentID.setFocusable(true);

        //if press "Enter"
        editStudentID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (editStudentID.getText().length() == 14) {
                        //   hideKeyboard();

                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putString(MainActivity.SAVED_STUDENT_ID, editStudentID.getText().toString());
                        ed.apply();

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
        Button btnSaveID = (Button) firstStartFragmentView.findViewById(R.id.buttonSaveID);
        View.OnClickListener oclBtnSaveID = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//save studentID to preferences
                if (editStudentID.getText().length() == 14) {

                    //   hideKeyboard();

                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(MainActivity.SAVED_STUDENT_ID, editStudentID.getText().toString());
                    //ed.commit();
                    ed.apply();

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
        return firstStartFragmentView = inflater.inflate(R.layout.fragment_first_screen, container, false);
    }

    private class ParseDataInfoAsyncTask extends AsyncTask<Void, Integer, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(firstStartFragmentView.getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            EditText editStudentID = (EditText) firstStartFragmentView.findViewById(R.id.editStudentID);
            studentID = editStudentID.getText().toString();
        }

        private String studentID;

        @Override
        protected Void doInBackground(Void... params) {

            Document doc;
            //      Connection.Response res;


            try {
                //               res = Jsoup.connect("http://student.miu.by/learning-card.html")
                doc = Jsoup.connect("http://student.miu.by/learning-card.html")
                        .data("act", "regnum", "id", "id", "regnum", studentID)
                        .method(Connection.Method.POST)
                        .post();//!
//                        .execute();
            } catch (IOException e) {
                error1 = "IO Error";
                return null;
            }

            /*
            String sessionId = res.cookie("PHPSESSID");
            try {
                doc = Jsoup.connect("http://student.miu.by/learning-card.html")
                        .cookie("PHPSESSID", sessionId)
                        .get();
            } catch (IOException e) {
                error1 = "IO Error";
                return null;
            }
*/
            Element table = doc.select("table").first();

            try {
                //search image photo url

                //   String link = table.select("tr").get(0).select("td").get(0).select("img").first().attr("src");
                //           System.out.println("http://student.miu.by" + link);
                //    imageFileName = link;
                //  imageFileName = table.select("tr").get(0).select("td").get(0).select("img").first().attr("src");

                image_URL = "http://student.miu.by" + table.select("tr").get(0).select("td").get(0).select("img").first().attr("src");
            } catch (NullPointerException e) {
                error1 = "No photo in doc";
            }

            try {
                Elements rows = doc.select("table").first().select("tr");
                SharedPreferences.Editor ed = sPref.edit();

                if (error1.equals("No photo in doc")) {
                    ed.putString(MainActivity.SAVED_NUMBER_GROUP, rows.get(0).select("td").get(1).text());
                } else {
                    ed.putString(MainActivity.SAVED_NUMBER_GROUP, rows.get(0).select("td").get(2).text());
                }

                ed.putString(MainActivity.SAVED_SURNAME_STUDENT, rows.get(1).select("td").get(1).text());
                ed.putString(MainActivity.SAVED_NAME_STUDENT, rows.get(2).select("td").get(1).text());
                ed.putString(MainActivity.SAVED_MIDNAME_STUDENT, rows.get(3).select("td").get(1).text());
                ed.putString(MainActivity.SAVED_FACULTY, rows.get(4).select("td").get(1).text());
                ed.putString(MainActivity.SAVED_SPECIALTY, rows.get(5).select("td").get(1).text());
                ed.putString(MainActivity.SAVED_AVARAGE_SCORE, rows.get(6).select("td").get(1).text());
                //     ed.putString(MainActivity.SAVED_STUDENT_ID, editStudentID.getText().toString());
                ed.putString(MainActivity.SAVED_STUDENT_ID, studentID.toString());
                //  ed.commit();
                ed.apply();

                if (!error1.equals("No photo in doc")) {
                    try {
                        getPhotoFromURL();
                        return null;
                    } catch (IOException e) {
                        error1 = "Photo not load";
                    }
                } else {
                    try {
                        InputStream bitmap = getActivity().getAssets().open("no_foto2.png");
                        SetupFragment.bm = BitmapFactory.decodeStream(bitmap);
                    } catch (IOException e1) {
                        error1 = "Photo not load";
                    }
                }
            } catch (NullPointerException e) {
                error1 = "ID not found";
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
                ed.putString(MainActivity.SAVED_STUDENT_ID, "");
                ed.apply();
                return;
            }
            if (error1.equals("IO Error")) {
                Toast.makeText(getActivity(), "Ошибка подключения к сети!", Toast.LENGTH_SHORT).show();
                ed.putString(MainActivity.SAVED_STUDENT_ID, "");
                ed.apply();
                return;
            }

            ImageView bmImage = (ImageView) firstStartFragmentView.findViewById(R.id.imageView4);
            bmImage.setImageBitmap(SetupFragment.bm);

            //save photo to sdcard
            try {
                //SetupFragment.getStudentPhoto(R.id.imageView4);
                //    new SetupFragment().getStudentPhoto((ImageView) firstStartFragmentView.findViewById(R.id.imageView4));
                getStudentPhoto((ImageView) firstStartFragmentView.findViewById(R.id.imageView4));

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Произошла какая-то ошибка #5", Toast.LENGTH_SHORT).show();
                Log.v("ERRR", "ERRR " + e);
            }
            hideKeyboard();

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

    public void getPhotoFromURL() throws IOException {
        BitmapFactory.Options bmOptions;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        SetupFragment.bm = LoadImage(image_URL, bmOptions);
    }

    public Bitmap LoadImage(String URL, BitmapFactory.Options options) throws IOException {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Произошла какая-то ошибка #6", Toast.LENGTH_SHORT).show();
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
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Произошла какая-то ошибка #7", Toast.LENGTH_SHORT).show();
        }
        return inputStream;
    }
/*
    private String getStudentPhoto() {
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/student.miu.by");

        iv = (ImageView) firstStartFragmentView.findViewById(R.id.imageView4);
        OutputStream fOut;
        try {
            File file = new File(sdPath, "photoStudent.jpg");
            fOut = new FileOutputStream(file);
            Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(MainActivity.SAVED_PHOTO, "yes");
  //          Log.e("Show photo yes", sPref.getString(MainActivity.SAVED_PHOTO, ""));
            ed.apply();
        } catch (IOException e) {
          //  Log.e("Show photo", e.toString());
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(MainActivity.SAVED_PHOTO, "no");
            ed.apply();
            return e.getMessage();
        }
        return "";
    }
    */

    public void getStudentPhoto(ImageView getImageViewFrom) {
        //    String folderToSave = Environment.getExternalStorageDirectory().toString();

        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/student.miu.by");
        if (sdPath.mkdirs()) {
            Toast.makeText(getActivity(), "Приложение создало каталог\n" + sdPath.toString(), Toast.LENGTH_SHORT).show();
        }

        //   Toast.makeText(getActivity(), "sdPath.mkdirs()", Toast.LENGTH_SHORT).show();
        //   iv = (ImageView) getVievSetupFragment.findViewById(R.id.imageView3);
        // iv = getImageView;
        OutputStream fOut;
        SharedPreferences.Editor ed = sPref.edit();
        try {
            // File file= new File(folderToSave, "photoStudent.jpg"); // создать уникальное имя для файла основываясь на дате сохранения
            File file = new File(sdPath, "photoStudent.jpg"); // создать уникальное имя для файла основываясь на дате сохранения
            fOut = new FileOutputStream(file);

            Bitmap bitmap = ((BitmapDrawable) getImageViewFrom.getDrawable()).getBitmap();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();


            ed.putString(MainActivity.SAVED_PHOTO, "yes").apply();
            //ed.apply();

        } catch (IOException e) {
            //      SharedPreferences.Editor ed = sPref.edit();
            ed.putString(MainActivity.SAVED_PHOTO, "no").apply();
            //ed.apply();
            Toast.makeText(getActivity(), "Произошла какая-то ошибка #8", Toast.LENGTH_SHORT).show();
            Log.v("ERRR", "ERRR2 " + e);
            //   return;
        }
    }
    // return;
}
