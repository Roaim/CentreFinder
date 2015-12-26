package tk.roaimsapp.centrefinder;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.view.View;
import android.database.Cursor;
import android.view.ViewGroup;
import android.widget.TextView;

public class SpinnerAdapter extends CursorAdapter
{

		private View v;
		public SpinnerAdapter(Context context,Cursor cursor){
				super(context,cursor,false);
		}
		@Override
		public View newView ( Context p1, Cursor p2, ViewGroup p3 ) {
						v=View.inflate(p1,R.layout.view_spinner,null);
				return v;
		}

		@Override
		public void bindView ( View p1, Context p2, Cursor p3 ) {
				// TODO: Implement this method
				TextView tv = (TextView) p1.findViewById(R.id.viewspinnerTextView1);
				String text = p3.getString(1)+"\n"+p3.getString(2);
				tv.setText ( text );
		}

		@Override
		public Integer getItem ( int position ) {
				// TODO: Implement this method
				Cursor c = getCursor();
				c.moveToPosition(position);
				return c.getInt(0);
		}
		
}
