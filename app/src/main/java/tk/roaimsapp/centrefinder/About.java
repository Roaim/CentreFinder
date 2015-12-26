package tk.roaimsapp.centrefinder;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

public class About
{
		public static void developer(Context context){
				AlertDialog.Builder adb = new AlertDialog.Builder(context);
				View v = View.inflate(context,R.layout.developer,null);
				adb.setCancelable(true);
				adb.setView(v);
				adb.show();
		}
}
