package net.martp.mihail.mumhelper;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstStartFragment extends Fragment {


    public FirstStartFragment() {
        // Required empty public constructor

    }

    SharedPreferences sPref;
    EditText editText2_studentID;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//get studentID from preferences
        EditText editStudentID = (EditText) getView().findViewById(R.id.editStudentID);
        sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        editStudentID.setText(sPref.getString(MainActivity.SAVED_STUDENT_ID, ""));

// saveStudentID
        Button btnSaveID = (Button) getView().findViewById(R.id.buttonSaveID);
        editText2_studentID = (EditText) getView().findViewById(R.id.editStudentID);
        //     sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);

        View.OnClickListener oclBtnSaveID = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Save ID", Toast.LENGTH_SHORT).show();

//seved studentID to preferences
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(MainActivity.SAVED_STUDENT_ID, editText2_studentID.getText().toString());
                ed.commit();
                

                /// tut nado skachat dannye !!!!!!

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


}
