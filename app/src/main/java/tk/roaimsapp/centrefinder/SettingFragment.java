package tk.roaimsapp.centrefinder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.util.Log;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.widget.Toast;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.support.v4.widget.CursorAdapter;
import android.view.View.OnClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements OnItemClickListener,OnItemLongClickListener,OnClickListener {


    private View view,diagView;
    private ListView lv;
    private SettingAdapter adapter;
    private DataHelper datahelper;
		private View header,footer;
		private int argument;
		private SeatPlan sp;

		private static final CharSequence MSG_LONG_PRESS ="Long press to edit or delete" ;

    public SettingFragment ( ) {
        // Required empty public constructor
    }


    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState ) {
				lg ( "creating fragment" );
        view = inflater.inflate ( R.layout.fragment_setting, container, false );
        lv = (ListView) view.findViewById ( R.id.listViewFragmentSetting );
        datahelper = new DataHelper ( getActivity ( ) );
				lg ( "data helper init" );
				argument = getArguments ( ).getInt ( "position" );
				try {
        		adapter = new SettingAdapter ( getActivity ( ), datahelper, argument );
						lg ( "adapter init" );
				} catch (Exception e) {
						lg ( e.toString ( ) );
						lg ( datahelper.getDB ( ).toString ( ) );
				}
				header = getHeader ( );
				changeStyle ( header );
				footer = View.inflate(getActivity(),R.layout.button_addnew,null);
				footer.setOnClickListener(this);
				lv.addHeaderView ( header, null, false );
				lv.addFooterView(footer,null,true);
        lv.setAdapter ( adapter );
				lg ( "adapter set/ frag created" );
				lv.setOnItemClickListener ( this );
				lv.setOnItemLongClickListener ( this );
				if(argument==3) sp = new SeatPlan(getActivity(),datahelper);
        return view;
		}

		@Override
		public void onDestroyView ( ) {
				// TODO: Implement this method
				super.onDestroyView ( );
				datahelper.closeDB ( );
		}

		@Override
		public void onClick ( View p1 ) {
				// TODO: Implement this method
				if(argument==3){
						sp.insert(adapter,argument);
						return;
				}
				insert();
		}

		@Override
		public void onItemClick ( AdapterView<?> p1, View p2, int p3, long p4 ) {
				// TODO: Implement this method
				if(argument==3){
						String itemId=String.valueOf(p1.getItemIdAtPosition(p3));
						lg(itemId);
						Cursor rc = datahelper.getPlanCursor(itemId);
						rc.moveToFirst();
						lg("row count: "+rc.getCount()+" column count: "+rc.getColumnCount());
						String msg = null;
						try{
								msg= "#Centre: "+rc.getString(0)+"\n"+
												"#Address: "+rc.getString(1)+"\n"+
												"#Roll range: "+rc.getString(2)+" - "+rc.getString(3)+"\n"+
												"#Group: "+rc.getString(4)+"\n"+
												"#Time: "+rc.getString(5);
												lg(msg);
						} catch (Exception e){
								lg(e.toString());
						}
						new AlertDialog.Builder ( getActivity ( ) ).
						setMessage ( msg ).
						setCancelable(true).
						show();
				} else Toast.makeText ( getActivity ( ),MSG_LONG_PRESS , 0 ).show ( );
		}

		@Override
		public boolean onItemLongClick ( AdapterView<?> p1, View p2, int p3, long p4 ) {
				// TODO: Implement this method
				lg ( "long click \nArgument: "+argument );
				if(argument==3){
						sp.update(adapter,argument,adapter.getCursor(),String.valueOf(p1.getItemIdAtPosition(p3)),"update");
						return true;
				}
				//CursorAdapter ca = (CursorAdapter) p1.getAdapter();
				final Cursor c = adapter.getCursor ( );
				lg ( "get cursor" );
				final String id = c.getString ( 0 ),
				string1 = c.getString ( 1 ),
				string2 = c.getString ( 2 );
				lg ( "index: " + id );
				lg ( "String1: " + string1 );
				lg ( "String2: " + string2 );
				//final int position = adapter.mPosition;
			  //lg ( "indexTable: " + position );
				update(c,id,string1,string2);
				return true;
		}
		
		private void insert(){
				update(datahelper.getCursor(argument),null,null,null);
		}

		private void update (final Cursor c, final String id, final String string1, String string2 ) {
				// TODO: Implement this
				String pButton,nButton;
				AlertDialog.Builder adb = new AlertDialog.Builder ( getActivity ( ) );
				diagView = View.inflate ( getActivity ( ), R.layout.view_dialog, null );
				final EditText et1 = (EditText) diagView.findViewById ( R.id.viewdialogEditText1 ),
				et2=(EditText) diagView.findViewById ( R.id.viewdialogEditText2 );
				if(string1==null){
						pButton = "Insert";
						nButton = "Cancel";
						et1.setHint(getTvText(0));
						et2.setHint(getTvText(1));
				} else {
						pButton="Update";
						nButton="Delete";
						et1.setText ( string1 );
						et2.setText ( string2 );
				}
				adb.setView ( diagView );
				adb.setPositiveButton ( pButton, new DialogInterface.OnClickListener ( ){

						@Override
						public void onClick ( DialogInterface p1, int p2 ) {
								// TODO: Implement this method
								String newString1 = et1.getText ( ).toString ( ),
								newString2 = et2.getText ( ).toString ( );
								ContentValues cv = new ContentValues ( );
								cv.put ( c.getColumnName ( 1 ), newString1 );
								cv.put ( c.getColumnName ( 2 ), newString2 );
								if(string1==null){
										datahelper.insert(argument,cv);
								} else {
										datahelper.update ( argument, cv, id );
								}
								adapter.changeCursor(datahelper.getCursor(argument));
								adapter.notifyDataSetChanged ( );
								//if(string1==null)
								//		lv.setSelection(adapter.getCount());
						}
				} );
				adb.setNegativeButton ( nButton, new DialogInterface.OnClickListener ( ){

						@Override
						public void onClick ( DialogInterface p1, int p2 ) {
								// TODO: Implement this method
								if(string1==null) return;
								datahelper.delete ( argument, id );
								adapter.changeCursor(datahelper.getCursor(argument));
								adapter.notifyDataSetChanged ( );
						}
				} );
				try {
						adb.show ( );
						lg ( "diag shown" );
				} catch (Exception e) {
						lg ( e.toString ( ) );
				}
		}

		private View getHeader ( ) {
				// TODO: Implement this method
				View v = null;
				switch ( argument ) {
						case 0:{
								v = View.inflate ( getActivity ( ), R.layout.viewlist_centre, null );
						} break;
						case 1:{
								v = View.inflate ( getActivity ( ), R.layout.viewlist_roll, null );
						} break;
						case 2:{
								v = View.inflate ( getActivity ( ), R.layout.viewlist_group, null );
						} break;
						case 3:{
								//seat plan
								v = View.inflate(getActivity(),R.layout.viewlist_plan,null);
						}
				}
				return v;
		}
		
		public String getTvText(int position){
				if(position<3){
						return changeStyle(header)[position].getText().toString();
				}
				return null;
		}

		private TextView[] changeStyle ( View v ) {
				// TODO: Implement this method
				TextView tv1,tv2,tv3;
				int id1 = 0,id2 = 0;
				switch ( argument ) {
						case 0:{
								id1 = R.id.textViewCentre;
								id2 = R.id.textViewAddress;
						} break;
						case 1:{
								id1 = R.id.viewlistrollTextViewStart;
								id2 = R.id.viewlistrollTextViewEnd;
						} break;
						case 2:{
								id1 = R.id.viewlistgroupTextViewGroup;
								id2 = R.id.viewlistgroupTextViewTime;
						} break;
						case 3:{
								tv1 = getTv ( v, R.id.viewlistplanTextView1 );
								tv2 = getTv ( v, R.id.viewlistplanTextView2 );
								tv3 = getTv(v, R.id.viewlistplanTextView3);
								changeTvStyle ( tv1 );
								changeTvStyle ( tv2 );
								changeTvStyle( tv3);
								return new TextView[]{tv1,tv2,tv3};
						}
				}
				tv1 = getTv ( v, id1 );
				tv2 = getTv ( v, id2 );
				changeTvStyle ( tv1 );
				changeTvStyle ( tv2 );
				return new TextView[]{tv1,tv2};
		}

		private TextView getTv ( View v, int id ) {
				return (TextView)v.findViewById ( id );
		}

		private void changeTvStyle ( TextView tv ) {
				// TODO: Implement this method
				tv.setTextColor ( Color.parseColor ( "#00ad80" ) );
		}

		private void lg ( String p0 ) {
				Log.i ( "rah", p0 );
		}


}
