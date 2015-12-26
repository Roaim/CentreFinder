package tk.roaimsapp.centrefinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Toast;
import android.database.Cursor;
import android.widget.ImageView;
import java.io.File;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher, TextView.OnEditorActionListener {

    EditText et;
    Button bt;
    TextView tvCentre,tvTime,tvUnit;
	ImageView iv;
    private android.content.Intent intent;
    SharedPreferences sharedPreferences;
		private DataHelper dHelper;
		private Cursor c;
	private AlertDialog.Builder resetDialog;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		dHelper = new DataHelper(this);
        et = (EditText) findViewById(R.id.editText);
		et.setOnEditorActionListener(this);
        bt = (Button) findViewById(R.id.buttonCheck);
        tvCentre = (TextView) findViewById(R.id.textViewCentreName);
        tvTime = (TextView) findViewById(R.id.textViewTime);
				tvUnit = (TextView) findViewById(R.id.textViewUnit);
				iv = (ImageView) findViewById(R.id.imageView);
        bt.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("fr",true);
        if (isFirstRun){
            try {
                dHelper.copyDatabase();
                sharedPreferences.edit().putBoolean("fr",false).apply();
                Log.i("rah","db copied");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("rah",e.toString());
            }
        }
				et.addTextChangedListener(this);
		iv.setOnClickListener(this);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        intent = new Intent(this,Settings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		resetDialog = new AlertDialog.Builder(this);
		resetDialog.setTitle("Warning!");
		resetDialog.setIcon(R.mipmap.ic_warning);
		resetDialog.setMessage("Any modification within the database created by you will be reset to default condition. If you are agree then tap AGREE.");
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
            startActivity(intent);
            return true;
        } else if (id==R.id.action_share){
			Intent share = new Intent(Intent.ACTION_SEND);
			File apk = new File(getApplicationInfo().sourceDir);
			Uri uri = Uri.fromFile(apk);
			String type = URLConnection.guessContentTypeFromName(apk.toString());
			share.setType(type);
			share.putExtra(Intent.EXTRA_STREAM,uri);
			startActivity(Intent.createChooser(share,"Send CentreFinder.apk"));
			return true;
		} else if (id==R.id.action_reset){
			resetDialog.setNegativeButton("CANCEL",null);
			resetDialog.setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						dHelper.copyDatabase();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			resetDialog.show();
			return true;
		} else if (id==R.id.action_about_developer){
			About.developer(this);
		}

        return super.onOptionsItemSelected(item);
    }
		
		boolean isRollOK = false;

    @Override
    public void onClick(View v) {
		if (v==iv){
			et.setText("");
			return;
		}
				if(isRollOK && !et.getText().toString().equals("")){
						String centre= c.getString(0),
						address = c.getString(1),
						unit = c.getString(2),
						time = c.getString(3);
						Log.i("rah",centre+"\n"+address+"\n"+unit+"\n"+time);
						tvCentre.setText(centre + "\n" + address);
						tvTime.setText(time);
						tvUnit.setText(unit);
					hideKeyboard();
				} else {
						Toast.makeText(this,"Please input correct roll.",Toast.LENGTH_SHORT).show();
				}
    }

	public void hideKeyboard(){
		View view = getCurrentFocus();
		if (view!=null){
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
		}
	}

		@Override
		public void beforeTextChanged ( CharSequence p1, int p2, int p3, int p4 ) {
				// TODO: Implement this method
		}

		@Override
		public void onTextChanged ( CharSequence p1, int p2, int p3, int p4 ) {
				// TODO: Implement this method
		}

		@Override
		public void afterTextChanged ( Editable p1 ) {
				// TODO: Implement this method
				if(!(iv.getVisibility()==View.VISIBLE))
				iv.setVisibility(View.VISIBLE);
				tvCentre.setText("");
				tvUnit.setText("");
				tvTime.setText("");
				String roll = p1.toString();
				if (roll.equals("")){
					iv.setVisibility(View.GONE);
					isRollOK = false;
					return;
				}
				Log.i("rah","roll: "+roll);
				c = dHelper.mainCursor(roll);
				if(c!=null && c.getCount()==1){
						iv.setImageResource(R.mipmap.right);
						isRollOK = true;
				} else {
						iv.setImageResource(R.mipmap.wrong);
						isRollOK = false;
				}

		}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId== EditorInfo.IME_ACTION_DONE){
			onClick(et);
			return true;
		}
		return false;
	}
}
