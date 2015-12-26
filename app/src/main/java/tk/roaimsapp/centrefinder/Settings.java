package tk.roaimsapp.centrefinder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.view.View;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;


public class Settings extends AppCompatActivity implements OnItemSelectedListener {

    Spinner spinner;
    //String[] mList;
    private android.support.v4.app.Fragment fragment;
		
		// need different transaction for different fragment otherwise
		// it will throw illegal state exeption that transaction already commited
		//private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spinner = (Spinner) findViewById(R.id.spinnerSettings);
				spinner.setOnItemSelectedListener(this);
        //mList = getResources().getStringArray(R.array.setting_items);
        //transaction=getSupportFragmentManager().beginTransaction();
    }

		@Override
		public void onItemSelected ( AdapterView<?> p1, View p2, int p3, long p4 ) {
				// TODO: Implement this method
				fragment = new SettingFragment();
				Bundle bundle = new Bundle();
        bundle.putInt("position",p3);
        fragment.setArguments(bundle);
				Log.i("rah","set argument"+p3);
				try{
						getSupportFragmentManager().beginTransaction().replace(R.id.settingFrameLayout,fragment).commit();
				} catch(Exception e){
						Log.i("rah",e.toString());
				}
				//transaction.addToBackStack(null);
		}

		@Override
		public void onNothingSelected ( AdapterView<?> p1 ) {
				// TODO: Implement this method
		}

		@Override
		protected void onDestroy ( ) {
				// TODO: Implement this method
				super.onDestroy ( );
		}

}
