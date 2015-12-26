package tk.roaimsapp.centrefinder;
import android.content.Context;
import android.widget.TextView;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.util.Log;
import android.widget.Spinner;

public class SeatPlan
{
		private Context mContext;
		private View diagView;
		private Spinner spn1,spn2,spn3;
		private DataHelper datahelper;
		
		public SeatPlan(Context context,DataHelper dh){
				mContext=context;
				datahelper = dh;
		}
		public void insert(SettingAdapter adapter,int argument){
				update(adapter,argument,datahelper.getCursor(argument),null,null);
		}

		public void update (final SettingAdapter adapter,final int argument,final Cursor c, final String id, final String string1 ) {
				// TODO: Implement this
				String pButton,nButton;
				try{
				AlertDialog.Builder adb = new AlertDialog.Builder ( mContext );
				diagView = View.inflate ( mContext, R.layout.dialog_plan, null );
				spn1=(Spinner) diagView.findViewById(R.id.dialogplanSpinner1);
				spn2=(Spinner) diagView.findViewById(R.id.dialogplanSpinner2);
				spn3=(Spinner) diagView.findViewById(R.id.dialogplanSpinner3);
				spn1.setAdapter(new SpinnerAdapter(mContext,datahelper.getCursor(0)));
				spn2.setAdapter(new SpinnerAdapter(mContext,datahelper.getCursor(1)));
				spn3.setAdapter(new SpinnerAdapter(mContext,datahelper.getCursor(2)));
				if(string1==null){
						pButton = "Insert";
						nButton = "Cancel";
				} else {
						pButton="Update";
						nButton="Delete";
						datahelper.setSpinnerPosition(spn1,spn2,spn3,id);
				}
				adb.setView ( diagView );
				adb.setPositiveButton ( pButton, new DialogInterface.OnClickListener ( ){

						@Override
						public void onClick ( DialogInterface p1, int p2 ) {
								// TODO: Implement this method
								Log.i("rah",c.getColumnName(3)+"\nSelected item: "+(Integer)spn1.getSelectedItem());
								int pos1 = spn1.getSelectedItemPosition();
								int pos2 = spn2.getSelectedItemPosition();
								int pos3 = spn3.getSelectedItemPosition();
								ContentValues cvs = new ContentValues();
								cvs.put("spinner1",pos1);
								cvs.put("spinner2",pos2);
								cvs.put("spinner3",pos3);
								ContentValues cv = new ContentValues ( );
								cv.put ( c.getColumnName ( 1 ), (Integer)spn1.getSelectedItem() );
								cv.put ( c.getColumnName ( 2 ), (Integer)spn2.getSelectedItem() );
								cv.put("\""+c.getColumnName(3)+"\"",(Integer)spn3.getSelectedItem());
								
								if(string1==null){		
										long lastId = datahelper.insert(4,cvs);
										cv.put("spinner",lastId);
										datahelper.insert(argument,cv);
								} else {
										datahelper.update(4,cvs,c.getString(4));
										cv.put("spinner",c.getString(4));
										datahelper.update ( argument, cv, id );
								}
								adapter.changeCursor(datahelper.getCursor(argument));
								adapter.notifyDataSetChanged ( );
						}
				} );
				adb.setNegativeButton ( nButton, new DialogInterface.OnClickListener ( ){

						@Override
						public void onClick ( DialogInterface p1, int p2 ) {
								// TODO: Implement this method
								if(string1==null) return;
								datahelper.delete(4,c.getString(4));
								datahelper.delete ( argument, id );
								adapter.changeCursor(datahelper.getCursor(argument));
								adapter.notifyDataSetChanged ( );
						}
				} );
						adb.show ( );
				} catch (Exception e) {
						Log.i("rah", e.toString ( ) );
				}
		}
		
}
