package tk.roaimsapp.centrefinder;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.util.Log;

/**
 * Created by Roaim on 14-Dec-15.
 */
public class SettingAdapter extends CursorAdapter {
    public final int mPosition;

    public SettingAdapter(Context context, DataHelper dbhelper, int position) {
        super(context, dbhelper.getCursor(position), false);
        mPosition = position;
				lg("adaptr position"+position);
    }
		/*
		public int[] getPlanIds(int position){
				int[] ids = new int[3];
				Cursor c= getCursor();
				c.moveToPosition(position);
				ids[0]=c.getInt(1);
				ids[1]=c.getInt(2);
				ids[2]=c.getInt(3);
				return ids;
		}
*/
		@Override
		public long getItemId ( int position ) {
				// TODO: Implement this method
				Cursor c = getCursor();
				c.moveToPosition(position);
				return c.getLong(0);
		}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = null;
        switch (mPosition){
            case 0:{
								v=View.inflate ( context, R.layout.viewlist_centre, null );
						} break;
						case 1:{
								v=View.inflate ( context, R.layout.viewlist_roll, null );
						} break;
						case 2:{
								v=View.inflate ( context, R.layout.viewlist_group, null );
						} break;
						case 3:{
								//seat plan
								v = View.inflate(context,R.layout.viewlist_plan,null);
						}
        }
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        setTvText(view, mPosition,cursor);
    }
		
		private void setTvText ( View v,int argument,Cursor c ) {
				// TODO: Implement this method
				TextView tv1,tv2,tv3;
				int id1 = 0,id2 = 0;
				switch(argument){
						case 0:{
								id1=R.id.textViewCentre;
								id2=R.id.textViewAddress;
						} break;
						case 1:{
								id1=R.id.viewlistrollTextViewStart;
								id2=R.id.viewlistrollTextViewEnd;
						} break;
						case 2:{
								id1=R.id.viewlistgroupTextViewGroup;
								id2=R.id.viewlistgroupTextViewTime;
						} break;
						case 3 :{
								tv1 = getTv ( v, R.id.viewlistplanTextView1 );
								tv2 = getTv ( v, R.id.viewlistplanTextView2 );
								tv3 = getTv(v,R.id.viewlistplanTextView3);
								tv1.setText(c.getString(1));
								tv2.setText(c.getString(2));
								tv3.setText(c.getString(3));
								return;
						}
				}
				tv1 = getTv ( v, id1 );
				tv2 = getTv ( v, id2 );
				tv1.setText(c.getString(1));
				tv2.setText(c.getString(2));
		}

		private TextView getTv(View v,int id){
				return (TextView)v.findViewById(id);
		}
		private void lg ( String p0 ) {
				Log.i("rah",p0);
		}
}
