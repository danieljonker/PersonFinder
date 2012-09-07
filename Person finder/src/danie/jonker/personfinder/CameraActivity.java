package danie.jonker.personfinder;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.widget.FrameLayout;

public class CameraActivity extends Activity {

	private static final String TAG = "CameraActivity";
	private Camera mCamera;
    private CameraPreview mPreview;
    OrientationEventListener myOrientationEventListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        
        // Create an instance of Camera
        mCamera = getCameraInstance();
        
        mCamera.setFaceDetectionListener(new MyFaceDetectionListener());

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        /*
         * Check if orientation changes so that we can pause and restart camera
         */
        myOrientationEventListener
        = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL){

         @Override
         public void onOrientationChanged(int arg0) {
          // TODO Auto-generated method stub
        	 Log.i(TAG, "Orientation Changed");
        	 mCamera.stopPreview();
        	 if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
        		 mCamera.setDisplayOrientation(90);
        	 } else {
        		 mCamera.setDisplayOrientation(0);
        	 }
        	 mCamera.startPreview();

        	 
        	 
        	 
         }};
         
           
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_camera, menu);
        return true;
    }
    
    
    
    
    /** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	

    
}
