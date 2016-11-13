package course.examples.Services.KeyClient;

import android.app.Activity ;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import course.examples.Services.KeyCommon.KeyGenerator;

public class KeyServiceUser extends Activity {

	protected static final String TAG = "KeyServiceUser";
	public static KeyGenerator mKeyGeneratorService;
	private boolean mIsBound = false;

	String input;
	String isPlayed = "";

	protected EditText textField ;		// the edit text in the GUI

	private GoogleApiClient client;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.main);

		final Button records = (Button) findViewById(R.id.records);

		textField = (EditText) findViewById(R.id.editText) ;

		final Button play = (Button) findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {

					if (mIsBound) {
						input = "clip" + textField.getText().toString();

						if(textField.getText().toString().equals("")){
							Toast.makeText(getApplicationContext(), "Please enter a clip number starting from 1 to 5 to play the clip.", Toast.LENGTH_SHORT).show();
						}
					else{
							mKeyGeneratorService.getPlay(input);
							textField.setEnabled(false);

							isPlayed = "yes";
						}
					}
					else {
						Log.i(TAG, "The music service was not bound in client!");
					}

				} catch (RemoteException e) {

					Log.e(TAG, e.toString());

				}
			}
		});

		final Button pause = (Button) findViewById(R.id.pause);
		pause.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {

					// Call KeyGenerator and get a new ID
					if (mIsBound) {
						mKeyGeneratorService.getPause();
						textField.setEnabled(true);
					}
					else {
						Log.i(TAG, "The music service was not bound in client!");
					}

				} catch (RemoteException e) {

					Log.e(TAG, e.toString());

				}
			}
		});

		final Button resume = (Button) findViewById(R.id.resume);
		resume.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {

					// Call KeyGenerator and get a new ID
					if (mIsBound) {
						mKeyGeneratorService.getResume();

						if(isPlayed.equals("yes")){
							textField.setEnabled(false);
						}
						else{
							textField.setEnabled(true);
						}

					}
					else {
						Log.i(TAG, "The music service was not bound in client!");
					}

				} catch (RemoteException e) {

					Log.e(TAG, e.toString());

				}
			}
		});

		final Button stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {

					// Call KeyGenerator and get a new ID
					if (mIsBound) {
						mKeyGeneratorService.getStop();
						textField.setEnabled(true);
					}
					else {
						Log.i(TAG, "The music service was not bound in client!");
					}

				} catch (RemoteException e) {

					Log.e(TAG, e.toString());

				}
			}
		});


		//client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


		records.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), Transactions.class) ;
				startActivity(i) ;
			}

		});


	}

	// Bind to KeyGenerator Service
	@Override
	protected void onResume() {
		super.onResume();

		if (!mIsBound) {

			boolean b = false;
			Intent i = new Intent(KeyGenerator.class.getName());

			ResolveInfo info = getPackageManager().resolveService(i, Context.BIND_AUTO_CREATE);
			i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

			b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
			if (b) {
				Log.i(TAG, "bindService() succeeded!");
			} else {
				Log.i(TAG, "bindService() failed!");
			}

		}
	}

	@Override
	protected void onPause() {

		/*if (mIsBound) {

			unbindService(this.mConnection);

		}*/
		super.onPause();
	}

	private final ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder iservice) {

			mKeyGeneratorService = KeyGenerator.Stub.asInterface(iservice);

			mIsBound = true;

		}

		public void onServiceDisconnected(ComponentName className) {

			mKeyGeneratorService = null;

			mIsBound = false;

		}
	};

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	// Unbind from Service
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mIsBound) {
			unbindService(this.mConnection);
		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
			//moveTaskToBack(true);

			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
	}
}
