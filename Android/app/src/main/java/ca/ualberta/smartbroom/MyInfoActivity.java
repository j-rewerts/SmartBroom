package ca.ualberta.smartbroom;

import ca.ualberta.smartbroom.R;
import ca.ualberta.smartbroom.conn.BLEDeviceConnection;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bluecreation.melodysmart.DataService;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class MyInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new MyOptionsFragment()).commit();
		}
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
		if (id == R.id.action_settings) {
			return true;
		}
        else if (id == R.id.action_connect) {
            FragmentManager fm = getFragmentManager();
            DialogFragment newFragment = new BLEDeviceDialog();
            newFragment.show(fm, "BLE_DEVICE");

            return true;
        } else if (id == R.id.action_disconnect) {
            BLEDeviceConnection.getInstance().disconnect();
        } else if (id == R.id.action_command) {
            BLEDeviceConnection.getInstance().cmdAck();
            BLEDeviceConnection.getInstance().cmdEcho("DATA!");
            BLEDeviceConnection.getInstance().cmdAccel();
            BLEDeviceConnection.getInstance().cmdPressure();
        }
		return super.onOptionsItemSelected(item);
	}


    /**
     * The main fragment. Allows users to view previous data and to view certain calculations
     * of previous data.
     */
	public static class MyOptionsFragment extends Fragment implements GraphListener{

        private Thread worker;

		public MyOptionsFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my_options,
					container, false);

            if (getView() == null) {
                return rootView;
            }

            DataManager dm = DataManager.getInstance();
            dm.subscribe(this);

            LinearLayout activityLayout = (LinearLayout) rootView.findViewById(R.id.activityLayout);
            activityLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout ll = (LinearLayout) view;
                }
            });

            if (worker == null) {
                worker = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            BLEDeviceConnection.getInstance().cmdAck();
                            BLEDeviceConnection.getInstance().cmdAccel();
                            BLEDeviceConnection.getInstance().cmdPressure();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                });
                worker.run();
            }

			return rootView;
		}

        @Override
        public void callback(Fragment f, double value, String graph) {
            View view = f.getView().getRootView();
            if (graph.equals(GraphListener.frequencyGraph)) {
                GraphView graphView = (GraphView) view.findViewById(R.id.frequencyGraph);
                BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(0, value)
                });

                graphView.removeAllSeries();
                graphView.addSeries(series);
            }
            else if (graph.equals(GraphListener.pressureGraph)) {
                GraphView graphView = (GraphView) view.findViewById(R.id.pressureGraph);
                BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, value)
                });

                graphView.removeAllSeries();
                graphView.addSeries(series);
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
