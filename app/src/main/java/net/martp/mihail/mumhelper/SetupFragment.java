package net.martp.mihail.mumhelper;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetupFragment extends Fragment {


    public SetupFragment() {
        // Required empty public constructor
    }

    SharedPreferences sPref;
    EditText editText2_studentID;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//Читаем studentID из preferences
        EditText editStudentID = (EditText) getView().findViewById(R.id.editStudentID);
        sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        editStudentID.setText(sPref.getString(MainActivity.SAVED_STUDENT_ID, ""));

//создаем обработчик начажият кнопки saveStudentID
        Button btnSaveID = (Button) getView().findViewById(R.id.buttonSaveID);
        editText2_studentID = (EditText) getView().findViewById(R.id.editStudentID);
        sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);

        View.OnClickListener oclBtnSaveID = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "press SaveID", Toast.LENGTH_SHORT).show();

//сохраняем параметр studentID в preferences
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(MainActivity.SAVED_STUDENT_ID, editText2_studentID.getText().toString());
                ed.commit();
            }
        };
        btnSaveID.setOnClickListener(oclBtnSaveID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viev = inflater.inflate(R.layout.fragment_setup, container, false);
        return viev;
    }
}
