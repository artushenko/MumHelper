package net.martp.mihail.mumhelper;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        TextView showStudentID= (TextView) getView().findViewById(R.id.studentID);

        //Читаем studentID из preferences
        SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        String savedText = sPref.getString(MainActivity.SAVED_STUDENT_ID, "");
        showStudentID.setText(savedText);
    }
}
