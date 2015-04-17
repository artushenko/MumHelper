package net.martp.mihail.mumhelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String[][] arrayNews = new String[5][4];
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_news, container, false);
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ParseDadaNewsAsyncTask parseDadaNewsAsyncTask = new ParseDadaNewsAsyncTask();
        parseDadaNewsAsyncTask.execute();
        OutToActivity outToActivity = new OutToActivity();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class OutToActivity extends Activity {
        private OutToActivity() {

            Toast.makeText(getActivity(), " OutToActivity", Toast.LENGTH_SHORT).show();
      /*
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View nowView = inflater.inflate(R.layout.fragment_news, null);




            //        setContentView(R.layout.fragment_news);

            TextView newsDate1text = (TextView) nowView.findViewById(R.id.textView4); //to here
            newsDate1text.setText("01.01.2015");


            //          TextView newsText1text=(TextView) nowView.findViewById(R.id.textView); //to here
            //         newsText1text.setText("Bla-bla-bla-bla");

            /*
            TextView newsDate2text=(TextView) findViewById(R.id.textView6); //to here
            TextView newsText2text=(TextView) findViewById(R.id.textView2); //to here
            TextView newsDate3text=(TextView) findViewById(R.id.textView7); //to here
            TextView newsText3text=(TextView) findViewById(R.id.textView3); //to here
            TextView newsDate4text=(TextView) findViewById(R.id.textView8); //to here
            TextView newsText4text=(TextView) findViewById(R.id.textView5); //to here
            */
        }


    }

    private class ParseDadaNewsAsyncTask extends AsyncTask<Void, Integer, Void> {
        //   private ProgressDialog spinner;

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


        private String[][] getArrayNews() {
            return arrayNews;
        }

        private void setArrayNews(String[][] arrayNewsf) {
            arrayNews = arrayNewsf;
        }

        private String newsGetDataError = "";

        @Override
        protected Void doInBackground(Void... params) {
            String[][] arrayNewslocal = new String[5][4];
            Document doc = null;
            newsGetDataError = "";
            try {
                doc = Jsoup.connect("http://miu.by/").get();
            } catch (IOException e) {
                // e.printStackTrace();
                System.out.println("Ошибка подключени к сети" + getClass().getSimpleName());
             //   Toast.makeText(getActivity(), "Ошибка подключени к сети", Toast.LENGTH_SHORT).show();

                 newsGetDataError = "network";
                 return null; //ERROR!!!!
            }
            Elements cols = doc.select("table").get(5).select("tr").get(21).select("td");


            doc = Jsoup.parse(cols.get(0).html());
            Element link = doc.select("a").first();


            int n = 0;
            for (int i = 0; i < 10; i++) {
                link = doc.select("a").get(i);
                String newsAbsHref = link.attr("href"); // "http://jsoup.org/"
                String newsAbsTextHref = link.text();
                System.out.println(newsAbsTextHref + " " + newsAbsHref);

                if (arrayNewslocal[n][0] == null) {
                    arrayNewslocal[n][0] = newsAbsTextHref;
                    arrayNewslocal[n][1] = newsAbsHref;
                } else if (arrayNewslocal[n][2] == null) {
                    arrayNewslocal[n][2] = newsAbsTextHref;
                    arrayNewslocal[n][3] = newsAbsHref;
                    n++;
                }

                setArrayNews(arrayNewslocal);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            if (!newsGetDataError.equals("")) {
                if (newsGetDataError.equals("network")) {
                    Toast.makeText(getActivity(), "Ошибка подключения к сети", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            //Toast.makeText(getActivity(), "Reading news is comlpete", Toast.LENGTH_SHORT).show();

            String[][] arrayNewslocal = new String[5][4];
            arrayNewslocal = getArrayNews();

            if (arrayNewslocal[0][0] != null) {
                TextView newsDate1text = (TextView) getView().findViewById(R.id.textView4_date_1); //to here
                newsDate1text.setText(Html.fromHtml("<a href=\"http://miu.by" + arrayNewslocal[0][1].toString() + "\">" + arrayNewslocal[0][0].toString() + "</a>"));
                newsDate1text.setMovementMethod(LinkMovementMethod.getInstance());
                TextView newsText1text = (TextView) getView().findViewById(R.id.textView); //to here
                newsText1text.setText(arrayNewslocal[0][2]);

                TextView newsDate2text = (TextView) getView().findViewById(R.id.textView6_date_2); //to here
                newsDate2text.setMovementMethod(LinkMovementMethod.getInstance());
                newsDate2text.setText(Html.fromHtml("<a href=\"http://miu.by" + arrayNewslocal[1][1].toString() + "\">" + arrayNewslocal[1][0].toString() + "</a>"));
                TextView newsText2text = (TextView) getView().findViewById(R.id.textView2); //to here
                newsText2text.setText(arrayNewslocal[1][2]);

                TextView newsDate3text = (TextView) getView().findViewById(R.id.textView7_date_3); //to here
                newsDate3text.setMovementMethod(LinkMovementMethod.getInstance());
                newsDate3text.setText(Html.fromHtml("<a href=\"http://miu.by" + arrayNewslocal[2][1].toString() + "\">" + arrayNewslocal[2][0].toString() + "</a>"));
                TextView newsText3text = (TextView) getView().findViewById(R.id.textView3); //to here
                newsText3text.setText(arrayNewslocal[2][2]);

                TextView newsDate4text = (TextView) getView().findViewById(R.id.textView8_date_4); //to here
                newsDate4text.setMovementMethod(LinkMovementMethod.getInstance());
                newsDate4text.setText(Html.fromHtml("<a href=\"http://miu.by" + arrayNewslocal[3][1].toString() + "\">" + arrayNewslocal[3][0].toString() + "</a>"));
                TextView newsText4text = (TextView) getView().findViewById(R.id.textView5); //to here
                newsText4text.setText(arrayNewslocal[3][2]);

                TextView newsDate5text = (TextView) getView().findViewById(R.id.textView9_date_5); //to here
                newsDate5text.setMovementMethod(LinkMovementMethod.getInstance());
                newsDate5text.setText(Html.fromHtml("<a href=\"http://miu.by" + arrayNewslocal[4][1].toString() + "\">" + arrayNewslocal[4][0].toString() + "</a>"));
                TextView newsText5text = (TextView) getView().findViewById(R.id.textView10); //to here
                newsText5text.setText(arrayNewslocal[4][2]);
            }
        }

    }


}
