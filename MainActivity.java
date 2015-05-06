package com.example.kurtiscc.gasmileage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
    private static String addressString = "No address found";
    public static Double lat;
    public static Double lng;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {}
    };


    private void updateWithNewLocation(Location location) {
        //TextView myLocationText;
        //myLocationText = (TextView)findViewById(R.id.LocationTV);

        //String latLongString = "No location found";


        if (location != null) {
             lat = location.getLatitude();
             lng = location.getLongitude();
            //latLongString = "Lat:" + lat + "\nLong:" + lng;

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder gc = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);

                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        sb.append(address.getAddressLine(i)).append("\n");

                    //sb.append(address.getLocality()).append("\n");
                    //sb.append(address.getPostalCode()).append("\n");
                    //sb.append(address.getCountryName());
                }
                addressString = sb.toString();
            } catch (IOException e) {}
        }

        //myLocationText.setText("Your Current Position is:\n" +
                //latLongString + "\n\n" + addressString);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager;
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);

        String provider = locationManager.getBestProvider(criteria, true);

        Location l = locationManager.getLastKnownLocation(provider);

        updateWithNewLocation(l);

        locationManager.requestLocationUpdates(provider, 2000, 10,
                locationListener);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            ((TextView) getActivity().findViewById(R.id.DateTV)).setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(month + 1).append("/").append(day).append("/")
                    .append(year).append(" "));
        }
    }

    public void DateBtn(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return InsertFragment.newInstance(position +1, MainActivity.this);
                case 1:
                    return HistoryFragment.newInstance(position +1, MainActivity.this);
                case 2:
                    return GraphFragment.newInstance(position +1, MainActivity.this);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class InsertFragment extends Fragment {
        //private Button DateButton, GoButton;
       // private TextView DisplayDateTV;
        private Gas gas;
        private DBHelper mydb;
        TextView myLocation;
        TextView date;
        TextView odometer;
        TextView gallons;
        TextView price;
        TextView location;
        String dat, odom, gal, pric, locat, la, ln;
        Context context;


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static InsertFragment newInstance(int sectionNumber, Context context) {
            InsertFragment fragment = new InsertFragment(context);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public InsertFragment() {}

        public InsertFragment(Context context) {
            this.context = context;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_insert, container, false);
            date = (TextView) rootView.findViewById(R.id.DateTV);
            odometer = (EditText) rootView.findViewById(R.id.OdometerET);
            gallons = (EditText) rootView.findViewById(R.id.GallonET);
            price = (EditText) rootView.findViewById(R.id.MoneyET);
            //location = (EditText) rootView.findViewById(R.id.LocationET);
            myLocation = (TextView) rootView.findViewById(R.id.LocationTV);
            gas = new Gas();
            mydb = new DBHelper(context);

            Button b = (Button) rootView.findViewById(R.id.go_button);
            b.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   save();
                }
            });
            Button B = (Button) rootView.findViewById(R.id.get_location);
            B.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    myLocation.setText(addressString);
                }
            });

            //DateButton = (Button) rootView.findViewById(R.id.get_date);
            //DisplayDateTV = (TextView) rootView.findViewById(R.id.DateTV);


            return rootView;

        }

        public void save() {
            Log.v("Test", "test");
            dat=date.getText().toString();
            odom=odometer.getText().toString();
            gal=gallons.getText().toString();
            pric=price.getText().toString();
            locat=myLocation.getText().toString();
            la = String.valueOf(lat);
            ln = String.valueOf(lng);
            mydb.insertGas(dat, odom, gal, pric, locat, la, ln);

            odometer.setText("");
            gallons.setText("");
            price.setText("");
            myLocation.setText("");


            Log.v("Test", mydb.getAllGas().toString());
        }


    }

    public static class HistoryFragment extends Fragment {
        private ListView historyList;
        private ArrayList<Gas> arrayList = new ArrayList<Gas>();
        private List<Gas> GasList;
        private HistoryRowAdapter adapter;
        private Button mapsBtn;

        private DBHelper mydb;
        Context context;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static HistoryFragment newInstance(int sectionNumber, Context context) {
            HistoryFragment fragment = new HistoryFragment(context);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public HistoryFragment() {

        }

        public HistoryFragment(Context context) {
            this.context = context;        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
            historyList = (ListView) rootView.findViewById(R.id.history_list);
            mapsBtn = (Button) rootView.findViewById(R.id.maps_button);
            mapsBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    startActivity(intent);
                }
            });
            GasList = new ArrayList<Gas>();
            mydb = new DBHelper(context);
            return rootView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if(isVisibleToUser)
            {
                GasList.clear();
                arrayList.clear();

                GasList = mydb.getAllGas();

                for(int i = 0; i < GasList.size(); i++) {
                    arrayList.add(GasList.get(i));
                }

                adapter = new HistoryRowAdapter(context, R.layout.rows_history_frag, arrayList);
                historyList.setAdapter(adapter);
            }
        }
    }

    public static class GraphFragment extends Fragment {
        Context context;
        private DBHelper mydb;

        private GraphView graph;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GraphFragment newInstance(int sectionNumber, Context context) {
            GraphFragment fragment = new GraphFragment(context);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GraphFragment(Context context) {
            this.context = context;
            mydb = new DBHelper(context);
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if(isVisibleToUser)
            {
                fillData();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
            graph = (GraphView) rootView.findViewById(R.id.graph);
            return rootView;
        }

        public void fillData(){
            List<Gas> gasgraph = new ArrayList<Gas>();
            gasgraph = mydb.getAllGas();

            DataPoint[] values = new DataPoint[gasgraph.size()];
            String[] strings = new String[gasgraph.size()];
            graph.removeAllSeries();

            for (int i = 0; i < gasgraph.size(); i++)
            {
                double x = i;
                double y = gasgraph.get(i).getGallons();
                values[i] = new DataPoint(x, y);
                strings[i] = String.valueOf(i);
            }

            graph.addSeries(new LineGraphSeries(values));
            graph.setTitle("How Many Gallons You've Put in Your Car");

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(strings);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        }


    }

}


