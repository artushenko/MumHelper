package net.martp.mihail.mumhelper;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    static String weekSpinnerText = "1";
    static String dataSearch = "";
    ArrayList<String> arrayWeekSpinner = new ArrayList<>();
    private String marksGetDataError = "";

    private View getViewScheduleFragment;


    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        weekSpinnerText = "1";
        dataSearch = "";
        return getViewScheduleFragment = inflater.inflate(R.layout.fragment_schedule4, container, false);
    }

    public static String[] teachers_array = new String[]{
            "Агеенкова Е.К.", "Амбрусевич Т.Э.", "Аносов В.М.", "Антонова А.В.", "Ахременко И.Н.", "Байко О.М.", "Бардиловская С.А.", "Бацукова Н.Л.", "Бездетко И.П.", "Безлюдов О.А.", "Бельзецкий А.И.", "Беляев С.А.", "Беляева С.В.", "Боброва Е.С.", "Богатырева Е.А.", "Богданович Н.А.", "Бохан Т.И.", "Бруй М.Г.", "Буйкевич О.С.", "Бусыгин Ю.Н.", "Буцаев В.В.", "Валицкий С.В.", "Варонько Е.В.", "Верхотуров И.Г.", "Вирковская Н.Р.", "Вишняков В.А.", "Войтикова М.В.", "Волков О.П.", "Воробьев М.К.", "Воронкова Е.В.", "Воюш Н.В.", "Галета И.А.", "Гедранович А.Б.", "Гедранович В.В.", "Герасенко В.П.", "Глыбовская Н.А.", "Головачев А.С.", "Гончаров В.И.", "Горбач Л.В.", "Гребень В.А.", "Гребнев А.А.", "Гуслев В.Г.", "Демидович Е.М.", "Денисова К.Г.", "Департамент контроля", "Доморад М.А.", "Донцова В.И.", "Жданович А.Ф.", "Желиба Б.Н.", "Жмуровский С.А.", "Жуковский В.С.", "Жуковский И.В.", "Залуцкий А.И.", "Змеева Ю.В.", "Зубец А.В.", "Игнатьева Г.К.", "Кавецкий И.Т.", "Кажуро Т.И.", "Казляк Г.М.", "Калита Л.В.", "Капанец Ю.В.", "Карпенко В.М.", "Карпенко Е.М.", "Кашуба В.М.", "Кипнис Е.П.", "Клименко А.А.", "Ковалев А.П.", "Ковалевская Т.Ф.", "Коврик О.А.", "Кожарская Н.В.", "Козленко А.В.", "Колтун О.А.", "Комкова Е.И.", "Комличенко В.Н.", "Кондакова О.В.", "Кондратенко О.Б.", "Константинович А.М.", "Конышев Ф.В.", "Конышева А.В.", "Краско Е.В.", "Кругликова А.И.", "Кудашов В.И.", "Кулинка Т.В.", "Куницкая Н.А.", "Купцова Г.А.", "Курмашев В.И.", "Кустенко А.А.", "Лаврёнов А.Н.", "Лазаревич И.М.", "Лапанович И.В.", "Левданская Н.Е.", "Лейко О.И.", "Лемешова Т.В.", "Ленсу Я.Ю.", "Ленцевич О.М.", "Липская О.И.", "Литягина О.Н.", "Лобан Н.А.", "Лобачева Н.С.", "Ловкис Л.К.", "Лубчинская И.П.", "Лучина С.И.", "Майтак-Аннаоразова Л.В.", "Макавчик Ю.Ю.", "Макаревич Р.А.", "Макаренко В.А.", "Малашенко Е.А.", "Малько Н.П.", "Мамонова З.А.", "Маркитантов А.А.", "Мартышевская Е.А.", "Матузяник Н.П.", "Мацкевич И.П.", "Машковская Т.Г.", "Мащенко И.В.", "Медведев С.А.", "Мельниченко С.Г.", "Меньшойкина И.А.", "Микульчик О.Г.", "Минайлова О.И.", "Мисюк М.Н.", "Михайлюк Ю.В.", "Мицкевич М.М.", "Мичулис Э.Ф.", "Мищенко М.С.", "Молчан М.Я.", "Муравьева З.А.", "Низовцев М.С.", "Николаева О.И.", "Новиков В.Н.", "Орлова Е.И.", "Осипов И.А.", "Панова Л.И.", "Пашук Н.С.", "Петров В.А.", "Петрунникова Р.В.", "Пикуль М.И.", "Пилецкий В.А.", "Подгурский Н.И.", "Подобед Н.А.", "Полищук С.А.", "Потоцкий А.А.", "Пунчик Н.Н.", "Пуховский В.И.", "Рачковская Е.В.", "Рокшина И.Г.", "Романович Г.Г.", "Русецкая А.М.", "Рыжковская Т.Л.", "Рысаков В.В.", "Рысюкевич Н.С.", "Рябоконь Н.В.", "Саевич Е.А.", "Сакович Ж.Г.", "Селянинов Д.М.", "Сергеева Ю.И.", "Серебренников В.П.", "Сибирская А.В.", "Сидоренко О.В.", "Сильникова Е.П.", "Скуратович Е.И.", "Соковикова Е.Е.", "Соловьян Н.А.", "Спирков С.Н.", "Старовойтова Э.М.", "Степанович О.П.", "Супрун М.Н.", "Сухоруков И.С.", "Суша Г.З.", "Сыктым Г.А.", "Сынкова И.А.", "Сычев П.В.", "Таборовец В.В.", "Телятицкая Т.В.", "Терентьев В.И.", "Тетерина Л.М.", "Тиковенко А.Г.", "Тонкович И.Н.", "Тропец Ю.А.", "Турченко Н.С.", "Усова Е.Б.", "Ходенков А.Л.", "Хорольская Н.И.", "Цибулько В.А.", "Черкасова Е.В.", "Чернышев О.В.", "Чернявская Г.А.", "Шакиров А.А.", "Шевляков В.В.", "Шелег Н.П.", "Шемякин А.А.", "Шешко Я.В.", "Шидлович П.П.", "Шинкевич Н.В.", "Шлойда О.О.", "Шпак Д.Н.", "Шпаковская Л.И.", "Шульдова С.Г.", "Щербакова С.Г.", "Юшкевич Н.А."
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Update teachers list
        Boolean updateTeacherListError = false;
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/student.miu.by");
 /*           if (sdPath.mkdirs()) {
                Toast.makeText(getActivity(), "Приложение создало каталог\n" + sdPath.toString(), Toast.LENGTH_SHORT).show();
            }
*/
        String fileName = sdPath + "/teacherslist.txt";

        ArrayList<String> arrayListTeacher = new ArrayList<>();

        String st;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            updateTeacherListError = true;
        }
        try {
            assert bufferedReader != null;
            while ((st = bufferedReader.readLine()) != null) {
                //arrayTeacherListReader[i++] = st;
                arrayListTeacher.add(st);
            }
        } catch (IOException e) {
            updateTeacherListError = true;
            //e.printStackTrace();
        }

        String[] arrayTeacherListReader = new String[arrayListTeacher.size()];
        //      System.out.println(arrayListTeacher.size());
        for (int q = 0; q < arrayListTeacher.size(); q++) {
            arrayTeacherListReader[q] = arrayListTeacher.get(q);
        }

        //   arrayTeacherListReader[0] = "Пупкин А.Щ.";
        //  System.out.println("LOG8" + Arrays.toString(arrayTeacherListReader));

        //SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        SharedPreferences sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedNumberGroup = sPref.getString(MainActivity.SAVED_NUMBER_GROUP, "");

        AutoCompleteTextView textView = (AutoCompleteTextView)
                getViewScheduleFragment.findViewById(R.id.groupNumberSearch);
        textView.setText(savedNumberGroup);

        ArrayAdapter<String> adapter;
        if (!updateTeacherListError) {
            //   System.out.println("LOG8 - arrayTeacherListReader");
            adapter = new ArrayAdapter<>(getViewScheduleFragment.getContext(),
                    android.R.layout.simple_dropdown_item_1line, arrayTeacherListReader);
        } else {
            adapter = new ArrayAdapter<>(getViewScheduleFragment.getContext(),
                    android.R.layout.simple_dropdown_item_1line, teachers_array);

        }
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                sendRequestToGetSchedule();
            }
        });

        Spinner spinnerWeek = (Spinner) getViewScheduleFragment.findViewById(R.id.spinnerWeekNumberSearch);

        if (spinnerWeek.getSelectedItem() != null) {
            weekSpinnerText = spinnerWeek.getSelectedItem().toString(); //16
        } else {
            weekSpinnerText = "1";
        }

        ParseDataScheduleAsyncTask parseDataScheduleAsyncTask = new ParseDataScheduleAsyncTask();
        parseDataScheduleAsyncTask.execute();
    }

    private class ParseDataScheduleAsyncTask extends AsyncTask<Void, Integer, Void> {
        public ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getViewScheduleFragment.getContext());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Document doc;

            try {
                doc = Jsoup.connect("http://martp.net/miuby.info/parse_miuby_schedule.php?weekn=1").get();
            } catch (IOException e) {
                marksGetDataError = "network";
                return null;
            }
            Elements numberWeek = doc.select("body");

            int numberWeekInt;
            try {
                numberWeekInt = Integer.parseInt(numberWeek.text());
            } catch (NumberFormatException e) {
                numberWeekInt = 44;
            }
            for (int i = 1; i < numberWeekInt + 1; i++) {
                arrayWeekSpinner.add(String.valueOf(i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            if (!marksGetDataError.equals("")) {
                if (marksGetDataError.equals("network")) {
                    Toast.makeText(getActivity(), "Ошибка подключения к сети", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            Spinner spinnerWeek = (Spinner) getViewScheduleFragment.findViewById(R.id.spinnerWeekNumberSearch);
            ArrayAdapter<String> snprAdapter = new ArrayAdapter(getViewScheduleFragment.getContext(),
                    R.layout.schedule_spinner_layout, arrayWeekSpinner);
            snprAdapter.setDropDownViewResource(R.layout.schedule_spinner_layout);
            spinnerWeek.setAdapter(snprAdapter);
            spinnerWeek.setSelection(arrayWeekSpinner.size() - 1);
            weekSpinnerText = spinnerWeek.getSelectedItem().toString();

          //  Log.v("LOG4", "number = " + weekSpinnerText);

            spinnerWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {
                    sendRequestToGetSchedule();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    sendRequestToGetSchedule();//??????
                }
            });
        }
    }

    private void sendRequestToGetSchedule() {
        //hide keyboard when navigation menu is open
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        //--------------------------------------------

        Spinner spinnerWeek = (Spinner) getViewScheduleFragment.findViewById(R.id.spinnerWeekNumberSearch);

        AutoCompleteTextView textView = (AutoCompleteTextView)
                getViewScheduleFragment.findViewById(R.id.groupNumberSearch);
        dataSearch = textView.getText().toString();
        weekSpinnerText = spinnerWeek.getSelectedItem().toString();

        FragmentTransaction fTrans = getFragmentManager().beginTransaction();
        ScheduleListFragment scheduleListFragment = new ScheduleListFragment();
        fTrans.replace(R.id.scheduleFrameContainer, scheduleListFragment);
        fTrans.commit();
    }
}
