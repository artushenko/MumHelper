package net.martp.mihail.mumhelper;

import android.app.Fragment;
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
import java.io.FileInputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

    View infoFragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return infoFragmentView = inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView showStudentID = (TextView) infoFragmentView.findViewById(R.id.studentID);
        TextView showNumberGroup = (TextView) infoFragmentView.findViewById(R.id.groupNumber);
        TextView showSurnameStudent = (TextView) infoFragmentView.findViewById(R.id.surnameStudent);
        TextView showNameStudent = (TextView) infoFragmentView.findViewById(R.id.nameStudent);
        TextView showMidameStudent = (TextView) infoFragmentView.findViewById(R.id.midnameStudent);
        TextView showFacultyStudent = (TextView) infoFragmentView.findViewById(R.id.faculty);
        TextView showSpecialtyStudent = (TextView) infoFragmentView.findViewById(R.id.specailty);
        TextView showAvaregeScStudent = (TextView) infoFragmentView.findViewById(R.id.avarageScore);

        //get data from preferences
        SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
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

        ImageView bmImage = (ImageView) infoFragmentView.findViewById(R.id.studentPhoto);

        String statusSavedPhoto = "";
        String statusSavedPhotoValue = sPref.getString(MainActivity.SAVED_PHOTO, "");
        if (statusSavedPhotoValue != null) statusSavedPhoto = statusSavedPhotoValue;

        if (statusSavedPhoto.equals("yes") && SetupFragment.bm != null) {
            bmImage.setImageBitmap(SetupFragment.bm);
        } else {
            String folderToSave = Environment.getExternalStorageDirectory().toString();
            FileInputStream in;// = null;
            BufferedInputStream buf;// = null;
            try {
                in = new FileInputStream(folderToSave + "/photoStudent.jpg");
                buf = new BufferedInputStream(in);
                Bitmap bMap = BitmapFactory.decodeStream(buf);
                bmImage.setImageBitmap(bMap);

                if (in != null) {
                    in.close();
                }
                if (buf != null) {
                    buf.close();
                }
//               Toast.makeText(getActivity(), " Read from sdcard", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //               Log.e("Error reading file", e.toString());
                Toast.makeText(getActivity(), "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
