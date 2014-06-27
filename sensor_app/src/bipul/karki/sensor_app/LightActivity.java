package bipul.karki.sensor_app;

import java.io.IOException;
import java.util.Timer;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LightActivity extends Activity implements SurfaceHolder.Callback {
	Camera mCamera = null;
	EditText input_box;
	Timer timer;
	public static SurfaceView preview;
	public static SurfaceHolder mHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		input_box = (EditText) findViewById(R.id.editTextInput);

		Context context = this;
		PackageManager pm = context.getPackageManager();

		// Does device support camera flash?
		if (!(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))) {
			Log.e("a", "Device has no camera flash!");
			Toast.makeText(this, "Device has no camera flash",
					Toast.LENGTH_LONG).show();
			return;
		}

		preview = (SurfaceView) findViewById(R.id.PREVIEW);
		mHolder = preview.getHolder();
		mHolder.addCallback(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		mCamera = Camera.open();
		Parameters params = mCamera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_TORCH);
		mCamera.setParameters(params);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mCamera != null) {
			mCamera.release();
		}

	}

	public void enableLight(View v) {
		Log.i("a", "enable sensor button");
		timer = new Timer();
		int input_value = 0;
		try {
			input_value = 1000 * Integer.parseInt(input_box.getText().toString()); // convert to msec
		} catch (Exception e) {
			Log.i("a", "Not an Integer: " + input_value);
		}
		Log.i("a", "number of Seconds: " + input_value/1000);

		new CountDownTimer(input_value, 1000) {

			public void onTick(long millisUntilFinished) {
				Log.i("a", "Seconds remaining: " + millisUntilFinished / 1000);
			}

			public void onFinish() {
				mCamera.startPreview();
				Log.i("a", "Light is on ");
			}
		}.start();

	}

	public void closeLightActivity(View v) {
		mCamera.stopPreview();
		LightActivity.this.finish();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mHolder = holder;
		try {
			mCamera.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mHolder = null;

	}

}
