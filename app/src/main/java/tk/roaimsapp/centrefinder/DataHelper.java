package tk.roaimsapp.centrefinder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.ContentValues;
import android.widget.Toast;
import android.util.Log;
import android.widget.Spinner;

/**
 * Created by Roaim on 14-Dec-15.
 */
public class DataHelper extends SQLiteOpenHelper {

    private final Context mContext;
    private SQLiteDatabase db;
		public static final String[] mTables = {"centre","roll","\"group\"","plan","spinner"};

    public DataHelper(Context context) {
        super(context, "data", null, 1);
        mContext = context;
    }
		
		public Cursor mainCursor(String roll){
				if(roll.equals("")) return null;
				String sql = "select centre.name as centre,centre.address,\"group\".name as unit,\"group\".time from centre,\"group\",plan,roll where plan.centre=centre._id and plan.\"group\"=\"group\"._id and plan.roll=roll._id and roll.start<="+roll+" and roll.end>="+roll;
				Cursor c = null;
				try{
						c = getDB().rawQuery(sql,null);
						c.moveToFirst();
				} catch(Exception e){
						Log.i("rah",e.toString());
				}
				return c;
		}

		public void setSpinnerPosition ( Spinner spn1, Spinner spn2, Spinner spn3, String id ) {
				// TODO: Implement this method
				String sql = "select spinner.spinner1,spinner.spinner2,spinner.spinner3 from spinner,plan where spinner._id=plan.spinner and plan._id="+id;
				Cursor c = getDB ( ).rawQuery ( sql, null );
				c.moveToFirst();
				spn1.setSelection(c.getInt(0));
				spn2.setSelection(c.getInt(1));
				spn3.setSelection(c.getInt(2));
		}

    public void copyDatabase() throws Exception {
        InputStream inputStream = mContext.getAssets().open("data");
        File file = mContext.getDatabasePath("data");
        if (!file.exists())
            file.getParentFile().mkdir();
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[inputStream.available()];
        int length;
        while ((length=inputStream.read(buffer))>=0){
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    public Cursor getCursor(int position){
        Cursor c = getDB().query(getTable(position),null,null,null,null,null,null);
				c.moveToFirst();
        return c;
    }
		
		public Cursor getPlanCursor(String id){
				String sql = "select centre.name as centre,centre.address,roll.start,roll.end,\"group\".name,\"group\".time from centre,roll,plan,\"group\" where centre._id=plan.centre and roll._id=plan.roll and \"group\"._id=plan.\"group\" and plan._id="+id;
				Log.i("rah",sql);
				Cursor rc = null;
				try{
						rc= getDB ( ).rawQuery ( sql, null );
				} catch (Exception e){
						Log.i("rah",this.getClass().getName()+":"+e.toString());
				}
				return rc;
		}
		
		public long insert(int tablePosition,ContentValues cv){
				long inserted = 0;
				try{
						inserted=getDB().insert(getTable(tablePosition),null,cv);
				} catch (Exception e){
						Log.i("rah",e.toString());
				}
				if(inserted>0)
						mkt("Inserted!");
					else mkt("Failed!");
				return inserted;
		}
		
		public void update(int position,ContentValues cv,String arg){
				String where = "_id=?";
				String[] args ={arg};
				int updated=getDB().update(getTable(position),cv,where,args);
				if(updated>0)
						mkt("Updated!");
						else mkt("Failed!");
		}
		
		public void delete(int position,String arg){
				String where = "_id=?";
				String[] args ={arg};
				int deleted=getDB().delete(getTable(position),where,args);
				if(deleted>0){
						mkt("Deleted!");
				} else mkt("Failed!");
		}

		private void mkt ( String p0 ) {
				Toast.makeText(mContext,p0,0).show();
		}

		public String getTable ( int position ) {
				return mTables[position];
		}

    public SQLiteDatabase getDB(){
        if (db==null)
        db = this.getWritableDatabase();
        return db;
    }

    public void closeDB(){
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
