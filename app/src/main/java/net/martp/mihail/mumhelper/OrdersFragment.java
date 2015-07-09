package net.martp.mihail.mumhelper;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.martp.mihail.mumhelper.Structure.OrderStructure;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    public View getViewOrdersFregment;


    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getViewOrdersFregment = inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseDataOrdersAsyncTask parseDataOrdersAsyncTask = new ParseDataOrdersAsyncTask();
        parseDataOrdersAsyncTask.execute();
    }

    private class ParseDataOrdersAsyncTask extends AsyncTask<Void, Integer, Void> {
        public ProgressDialog dialog;
        private TableLayout queryOrderTableLayout;
        private Context context;
        private String ordersGetDataError = "";
        private ArrayList<OrderStructure> arrayListOrders = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getViewOrdersFregment.getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String studentID = getActivity().getPreferences(Context.MODE_PRIVATE).getString(MainActivity.SAVED_STUDENT_ID, "");
            ordersGetDataError = "";
            arrayListOrders.clear();
            Document doc;
            Connection.Response res;

            try {
                res = Jsoup.connect("http://student.miu.by/learning-card.html")
                        .data("act", "regnum", "id", "id", "regnum", studentID)
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                ordersGetDataError = "network";
                return null;
            }

            String sessionId = res.cookie("PHPSESSID");

            try {
                doc = Jsoup.connect("http://student.miu.by/learning-card/~/my.orders.html")
                        .cookie("PHPSESSID", sessionId).get();
            } catch (IOException e) {
                ordersGetDataError = "network";
                return null;
            }

            try {
                Element table = doc.select("table").get(1);
                Elements rows = table.select("tr");

                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");
                    arrayListOrders.add(new OrderStructure(cols.get(0).text(), cols.get(1).text(), cols.get(2).text()));
                }
            } catch (Exception e) {
                ordersGetDataError = "other error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            if (!ordersGetDataError.equals("")) {
                if (ordersGetDataError.equals("network")) {
                    Toast.makeText(getActivity(), "Ошибка подключения к сети", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Произошла какая-то ошибка #11", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            queryOrderTableLayout = (TableLayout) getViewOrdersFregment.findViewById(R.id.orderTable);

            for (int i = 0; i < arrayListOrders.size(); i++) {
                OrderStructure orderStructure = arrayListOrders.get(i);
                makeOrdersLine(orderStructure.getCourese(),
                        orderStructure.getNumberOrder(),
                        orderStructure.getTextOrder(), i);
            }
        }

        private void makeOrdersLine(String getCourse, String getNumberOrder, String getTextOrder, int index) {
            context = getViewOrdersFregment.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //   View newTagView = inflater.inflate(R.layout.order_list_item, null);
            View newTagView = inflater.inflate(R.layout.order_list_item, (ViewGroup) getView(), false);

            TextView textNumberRating = (TextView) newTagView.findViewById(R.id.course);
            textNumberRating.setText(getCourse);

            TextView textSurname = (TextView) newTagView.findViewById(R.id.numberOrder);
            textSurname.setText(getNumberOrder);

            TextView textName = (TextView) newTagView.findViewById(R.id.textOrder);
            textName.setText(getTextOrder);

            queryOrderTableLayout.addView(newTagView, index);
        }
    }
}
