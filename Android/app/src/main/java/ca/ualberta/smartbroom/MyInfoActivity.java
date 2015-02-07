package ca.ualberta.smartbroom;

import ca.ualberta.smartbroom.conn.BLEDeviceConnection;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import java.util.ArrayList;

public class MyInfoActivity extends Activity {

    private Thread worker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new MyOptionsFragment()).commit();
		}

        /*if (worker == null) {
            worker = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        BLEDeviceConnection.getInstance().cmdAck();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            break;
                        }
                        BLEDeviceConnection.getInstance().cmdAccel();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        BLEDeviceConnection.getInstance().cmdPressure();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            });
            worker.start();
        }*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        if (id == R.id.action_connect) {
            FragmentManager fm = getFragmentManager();
            DialogFragment newFragment = new BLEDeviceDialog();
            newFragment.show(fm, "BLE_DEVICE");

            return true;
        } else if (id == R.id.action_disconnect) {
            BLEDeviceConnection.getInstance().disconnect();
        }
		return super.onOptionsItemSelected(item);
	}


    /**
     * The main fragment. Allows users to view previous data and to view certain calculations
     * of previous data.
     */
	public static class MyOptionsFragment extends Fragment implements GraphListener{

        BarGraph graphs;
        BarGraph frequencyGraph;
        ArrayList<Bar> barList;

		public MyOptionsFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my_options,
					container, false);



            DataManager dm = DataManager.getInstance();
            dm.subscribe(this);

            LinearLayout activityLayout = (LinearLayout) rootView.findViewById(R.id.activityLayout);
            activityLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout ll = (LinearLayout) view;
                }
            });

            barList = new ArrayList<Bar>();

            Bar pressureBar = new Bar();
            Bar frequencyBar = new Bar();
            pressureBar.setName("Pressure");
            frequencyBar.setName("Frequency");
            pressureBar.setValue(10);
            frequencyBar.setValue(20);
            pressureBar.setColor(Color.parseColor("#99CC00"));
            frequencyBar.setColor(Color.parseColor("#FFBB33"));

            barList.add(pressureBar);
            barList.add(frequencyBar);

            graphs = (BarGraph) rootView.findViewById(R.id.graphView);
            graphs.setBars(barList);
            graphs.setUnit(" ");

			return rootView;
		}

        @Override
        public void callback(Fragment f, final double value, String graph) {
            final View view = f.getView().getRootView();
            if (graph.equals(GraphListener.frequencyGraph)) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        barList.get(0).setValue((float) value);
                    }
                });
            }
            else if (graph.equals(GraphListener.pressureGraph)) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        barList.get(1).setValue((float) value);
                    }
                });
            }
        }
    }

    public static class MyGamesFragment extends Fragment {

        public MyGamesFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_options,
                    container, false);

            MyGamesListAdapter adapter = new MyGamesListAdapter(getActivity(), R.layout.my_game_list_item, new ArrayList<GameDataModel>());

            ListView lv = (ListView) rootView.findViewById(R.id.myGamesListView);
            lv.setAdapter(adapter);

            return rootView;
        }
    }
}
