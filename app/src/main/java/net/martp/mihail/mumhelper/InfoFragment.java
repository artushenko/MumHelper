package net.martp.mihail.mumhelper;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {


    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView showStudentID = (TextView) getView().findViewById(R.id.studentID);
        TextView showNumberGroup = (TextView) getView().findViewById(R.id.groupNumber);
        TextView showSurnameStudent = (TextView) getView().findViewById(R.id.surnameStudent);
        TextView showNameStudent = (TextView) getView().findViewById(R.id.nameStudent);
        TextView showMidameStudent = (TextView) getView().findViewById(R.id.midnameStudent);
        TextView showFacultyStudent = (TextView) getView().findViewById(R.id.faculty);
        TextView showSpecialtyStudent = (TextView) getView().findViewById(R.id.specailty);
        TextView showAvaregeScStudent = (TextView) getView().findViewById(R.id.avarageScore);

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

        ImageView bmImage = (ImageView) getView().findViewById(R.id.studentPhoto);
        if (SetupFragment.bm!=null) {
            bmImage.setImageBitmap(SetupFragment.bm);
        }



    }
}
