package course.examples.Services.KeyService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import course.examples.Services.KeyCommon.KeyGenerator;

public class KeyGeneratorImpl extends Service {

	protected static final String TAG = "KeyGeneratorImpl";

	private MediaPlayer mPlayer;

	private DatabaseHelperNew mydbHelper;

	String currentsng = "empty";

	int count = 1;

	// Set of already assigned IDs
	// Note: These keys are not guaranteed to be unique if the Service is killed 
	// and restarted.

	public void onCreate() {
		//mPlayer = MediaPlayer.create(this, R.raw.clip1);
		//mPlayer = new MediaPlayer();
		mydbHelper = new DatabaseHelperNew(this);
		clearAll();
	}

	// Implement the Stub for this Object
	private final KeyGenerator.Stub mBinder = new KeyGenerator.Stub() {

		// Implement the play remote method
		public void getPlay(String str) {

			currentsng = str;

			//System.out.println(str);

			String filename = "android.resource://" + getApplicationContext().getPackageName() + "/raw/"+str;

			if(mPlayer!=null)
			{
				if(mPlayer.isPlaying())
					mPlayer.stop();
			}

			mPlayer = new MediaPlayer();

			try
			{ mPlayer.setDataSource(getApplicationContext(), Uri.parse(filename)); }
			catch (Exception e) {}
			try
			{ mPlayer.prepare(); }
			catch (Exception e) {}

			if (null != mPlayer) {

				if (mPlayer.isPlaying()) {

					// Rewind to beginning of song
					mPlayer.seekTo(0);

				} else {

					mPlayer.seekTo(0);
					// Start playing song
					mPlayer.start();

				}

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();

				Date now = cal.getTime(); // set the current datetime in a Date-object

				// SimpleDateFormat.format( Date date ) returns a formatted string
				// with the predefined format
				String mTimeString = sdf.format( now ); // contains yyyy-MM-dd

				if(count > 2){
					//System.out.println(mydbHelper.getLastEnteredRecord());
					int end = mydbHelper.getLastEnteredRecord().indexOf(",");

					mydbHelper.insertData(str + " is " + "Played" + " , After " + mydbHelper.getLastEnteredRecord().substring(0,end), mTimeString);
				}

				if(count == 2){
					//System.out.println(mydbHelper.getLastEnteredRecord());
					int end = mydbHelper.getLastEnteredRecord().indexOf(",");
					mydbHelper.insertData(str + " is " + "Played" + " , After " + mydbHelper.getLastEnteredRecord().substring(0,end), mTimeString);
					count++;
				}

				if(count == 1) {
					//System.out.println("First Song Played");
					mydbHelper.insertData(str + " is " + "Played" + " at First, ", mTimeString);
					count++;
				}

				//Toast.makeText(getApplicationContext(), "Play Button Clicked. Data Inserted.", Toast.LENGTH_LONG).show();

				Log.i(TAG, "The music service was bound in the server!");

			} else {

				Log.i(TAG, "The music service was not bound in the server!");

			}
		}

		// Implement the pause remote method
		public void getPause() {

			if (null != mPlayer) {

				if (mPlayer.isPlaying()) {

					// Start playing song
					mPlayer.pause();

					if(!currentsng.equals("empty")){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar cal = Calendar.getInstance();

						Date now = cal.getTime(); // set the current datetime in a Date-object

						// SimpleDateFormat.format( Date date ) returns a formatted string
						// with the predefined format
						String mTimeString = sdf.format( now ); // contains yyyy-MM-dd
						int end = mydbHelper.getLastEnteredRecord().indexOf(",");

						mydbHelper.insertData(currentsng + " is " + "Paused" + " , After " + mydbHelper.getLastEnteredRecord().substring(0,end), mTimeString);
						//mydbHelper.insertData(currentsng + " is " + "Paused" + " , After " + mydbHelper.getLastEnteredRecord(), mTimeString);
						//mydbHelper.insertData(currentsng + " is " + "Paused", mTimeString);
					}


				}
				Log.i(TAG, "The music service was bound in the server!");

			} else {

				Log.i(TAG, "The music service was not bound in the server!");

			}
		}

		// Implement the resume remote method
		public void getResume() {

			if (null != mPlayer) {

				if (mPlayer.isPlaying()) {

				} else {

					// Start playing song
					mPlayer.start();

					if(!currentsng.equals("empty")){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar cal = Calendar.getInstance();

						Date now = cal.getTime(); // set the current datetime in a Date-object

						// SimpleDateFormat.format( Date date ) returns a formatted string
						// with the predefined format
						String mTimeString = sdf.format( now ); // contains yyyy-MM-dd
						//mydbHelper.insertData(currentsng + " is " + "Resumed", mTimeString);
						//mydbHelper.insertData(currentsng + " is " + "Resumed" + " , After " + mydbHelper.getLastEnteredRecord(), mTimeString);

						int end = mydbHelper.getLastEnteredRecord().indexOf(",");

						mydbHelper.insertData(currentsng + " is " + "Resumed" + " , After " + mydbHelper.getLastEnteredRecord().substring(0,end), mTimeString);

					}

				}
				Log.i(TAG, "The music service was bound in the server!");
			} else {

				Log.i(TAG, "The music service was not bound in the server!");

			}
		}

		// Implement the stop remote method
		public void getStop() {

			if (null != mPlayer) {

				if (mPlayer.isPlaying()) {

					// Rewind to beginning of song
					mPlayer.pause();
					mPlayer.seekTo(0);
					//mPlayer.stop();

					if(!currentsng.equals("empty")){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar cal = Calendar.getInstance();

						Date now = cal.getTime(); // set the current datetime in a Date-object

						// SimpleDateFormat.format( Date date ) returns a formatted string
						// with the predefined format
						String mTimeString = sdf.format( now ); // contains yyyy-MM-dd
						//mydbHelper.insertData(currentsng + " is " + "Stopped", mTimeString);
						int end = mydbHelper.getLastEnteredRecord().indexOf(",");

						mydbHelper.insertData(currentsng + " is " + "Stopped" + " , After " + mydbHelper.getLastEnteredRecord().substring(0,end), mTimeString);
					}


				} else {

					//System.out.println(mPlayer.getCurrentPosition());
					if(mPlayer.getCurrentPosition()!=0){

						mPlayer.seekTo(0);

						if(!currentsng.equals("empty")){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Calendar cal = Calendar.getInstance();

							Date now = cal.getTime(); // set the current datetime in a Date-object

							// SimpleDateFormat.format( Date date ) returns a formatted string
							// with the predefined format
							String mTimeString = sdf.format( now ); // contains yyyy-MM-dd
							//mydbHelper.insertData(currentsng + " is " + "Stopped", mTimeString);
							int end = mydbHelper.getLastEnteredRecord().indexOf(",");

							mydbHelper.insertData(currentsng + " is " + "Stopped" + " , After " + mydbHelper.getLastEnteredRecord().substring(0,end), mTimeString);
						}

					}

				}
				Log.i(TAG, "The music service was stopped in the server!");
			} else {
				Log.i(TAG, "The music service was not sucessfully stopped in the server!");
			}
		}

		//Method to retrieve all the records from the SQL Lite Database
		public String[] getAllData(){
			String arr[] = mydbHelper.getAllData();
			return arr;
		}
	};

	//Method to delete the SQL Lite Database everytime the app is started.
	public void clearAll(){
		mydbHelper.getWritableDatabase().delete(DatabaseHelperNew.TABLE_NAME,null,null);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}
