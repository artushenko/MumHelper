package net.martp.mihail.mumhelper;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

    View getViewInfoFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return getViewInfoFragment = inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)  {
        super.onViewCreated(view, savedInstanceState);

        TextView showStudentID = (TextView) getViewInfoFragment.findViewById(R.id.studentID);
        TextView showNumberGroup = (TextView) getViewInfoFragment.findViewById(R.id.groupNumber);
        TextView showSurnameStudent = (TextView) getViewInfoFragment.findViewById(R.id.surnameStudent);
        TextView showNameStudent = (TextView) getViewInfoFragment.findViewById(R.id.nameStudent);
        TextView showMidameStudent = (TextView) getViewInfoFragment.findViewById(R.id.midnameStudent);
        TextView showFacultyStudent = (TextView) getViewInfoFragment.findViewById(R.id.faculty);
        TextView showSpecialtyStudent = (TextView) getViewInfoFragment.findViewById(R.id.specailty);
        TextView showAvaregeScStudent = (TextView) getViewInfoFragment.findViewById(R.id.avarageScore);

        //get data from preferences
      //  SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        SharedPreferences sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedStudentID = sPref.getString(MainActivity.SAVED_STUDENT_ID, "");
        String savedSurnameStudent = sPref.getString(MainActivity.SAVED_SURNAME_STUDENT, "");
        String savedNameStudent = sPref.getString(MainActivity.SAVED_NAME_STUDENT, "");
        String savedMidnameStudent = sPref.getString(MainActivity.SAVED_MIDNAME_STUDENT, "");
        String savedFaculty = sPref.getString(MainActivity.SAVED_FACULTY, "");
        String savedSpecialty = sPref.getString(MainActivity.SAVED_SPECIALTY, "");
        String savedAvaregeScore = sPref.getString(MainActivity.SAVED_AVARAGE_SCORE, "");
        String savedNumberGroup = sPref.getString(MainActivity.SAVED_NUMBER_GROUP, "");

        showStudentID.setText(savedStudentID);
        showNumberGroup.setText(savedNumberGroup);
        showSurnameStudent.setText(savedSurnameStudent);
        showNameStudent.setText(savedNameStudent);
        showMidameStudent.setText(savedMidnameStudent);
        showFacultyStudent.setText(savedFaculty);
        showSpecialtyStudent.setText(savedSpecialty);
        showAvaregeScStudent.setText(savedAvaregeScore);

        ImageView bmImage = (ImageView) getViewInfoFragment.findViewById(R.id.studentPhoto);

        String statusSavedPhoto = "";
        String statusSavedPhotoValue = sPref.getString(MainActivity.SAVED_PHOTO, "");
        if (statusSavedPhotoValue != null) statusSavedPhoto = statusSavedPhotoValue;

        if (statusSavedPhoto.equals("yes") && (SetupFragment.bm != null)) {
            bmImage.setImageBitmap(SetupFragment.bm);
        } else {
            //get url path app folder
            File sdPath = Environment.getExternalStorageDirectory();
            sdPath = new File(sdPath.getAbsolutePath() + "/student.miu.by");

            FileInputStream in = null;
            BufferedInputStream buf = null;
            try {
                in = new FileInputStream(sdPath + "/photoStudent.jpg");
                buf = new BufferedInputStream(in);
                Bitmap bMap = BitmapFactory.decodeStream(buf);
                bmImage.setImageBitmap(bMap);
            } catch (Exception e) {
                // if file photoStudent.jpg not found get file no_foto2.png from Assets
                Toast.makeText(getActivity(), "Неизвестная ошибка #3", Toast.LENGTH_SHORT).show();
                try {
                    InputStream bitmap = getActivity().getAssets().open("no_foto2.png");
                    Bitmap bm = BitmapFactory.decodeStream(bitmap);
                    bmImage.setImageBitmap(bm);
                } catch (IOException e1) {
                    Toast.makeText(getActivity(), "Неизвестная ошибка #4", Toast.LENGTH_SHORT).show();
                }
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Неизвестная ошибка #1", Toast.LENGTH_SHORT).show();
                    }
                }
                if (buf != null) {
                    try {
                        buf.close();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Неизвестная ошибка #2", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
