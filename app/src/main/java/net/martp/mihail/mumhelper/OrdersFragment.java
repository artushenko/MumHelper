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
    private static ArrayList<OrderStructure> arrayListOrders = new ArrayList<>();

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viev = inflater.inflate(R.layout.fragment_orders, container, false);
        return viev;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseDataOrdersAsyncTask parseDataOrdersAsyncTask = new ParseDataOrdersAsyncTask();
        parseDataOrdersAsyncTask.execute();
    }

    private class ParseDataOrdersAsyncTask extends AsyncTask<Void, Integer, Void> {
        public ProgressDialog dialog;
        TableLayout queryOrderTableLayout;
        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getView().getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        private ArrayList<OrderStructure> getArrayOrders() {
            return arrayListOrders;
        }

        private void setArrayOrders(ArrayList<OrderStructure> arrayOrdersf) {
            arrayListOrders = arrayOrdersf;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<OrderStructure> arrayListOrdersLocal = new ArrayList<>();

            Document doc = null;
            Connection.Response res = null;

            try {
                res = Jsoup.connect("http://student.miu.by/learning-card.html")
                        .data("act", "regnum", "id", "id", "regnum", "20090312012423")
                        .method(Connection.Method.POST)
                        .execute();
            } catch (IOException e) {
                //  e.printStackTrace();
                System.out.println("Ошибка подключени к сети " + getClass().getSimpleName());
//                Toast.makeText(getActivity(), "Ошибка подключени к сети", Toast.LENGTH_SHORT).show();
                return null;
            }

            String sessionId = res.cookie("PHPSESSID");

            try {
                doc = Jsoup.connect("http://student.miu.by/learning-card/~/my.orders.html")
                        .cookie("PHPSESSID", sessionId).get();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Ошибка подключени к сети", Toast.LENGTH_SHORT).show();
                return null;
            }

            Element table = doc.select("table").get(1);
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");
                arrayListOrdersLocal.add(new OrderStructure(cols.get(0).text(), cols.get(1).text(), cols.get(2).text()));
            }
            setArrayOrders(arrayListOrdersLocal);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            ArrayList<OrderStructure> arrayListOrderLocal = getArrayOrders();

            OrderStructure orderStructure;

            queryOrderTableLayout = (TableLayout) getView().findViewById(R.id.orderTable);

            for (int i = 0; i < arrayListOrderLocal.size(); i++) {
                orderStructure = arrayListOrderLocal.get(i);
                makeOrdersLine(orderStructure.getCourese(),
                        orderStructure.getNumberOrder(),
                        orderStructure.getTextOrder(), i);
            }
        }

        private void makeOrdersLine(String getCourse, String getNumberOrder, String getTextOrder, int index) {
            context = getView().getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newTagView = inflater.inflate(R.layout.order_list_item, null);

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
