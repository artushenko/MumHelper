package net.martp.mihail.mumhelper;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarksFragment extends Fragment {


    public MarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ParseDataMarksAsyncTask parseDadaNewsAsyncTask = new ParseDataMarksAsyncTask();
        parseDadaNewsAsyncTask.execute();
        OutToActivity outToActivity = new OutToActivity();

    }

    private class OutToActivity extends Activity {
        private OutToActivity() {


        }
    }


    private class ParseDataMarksAsyncTask extends AsyncTask<Void, Integer, Void> {

        public ProgressDialog dialog;

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

            return null;
        }

    }
}
