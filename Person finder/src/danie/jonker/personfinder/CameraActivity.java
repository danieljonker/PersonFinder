package danie.jonker.personfinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity extends Activity {

	private static final String TAG = "CameraActivity";
	private Camera mCamera;
    private CameraPreview mPreview;
    OrientationEventListener myOrientationEventListener;
	

	@SuppressLint("ParserError")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        
        // Create an instance of Camera
        mCamera = getCameraInstance();
        
        mCamera.setFaceDetectionListener(new MyFaceDetectionListener());
        
        View onTop = (View) findViewById(R.id.rectangle_overlay);
        Canvas canvas = new Canvas();
        
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(152, 48, -282, 22, paint);
        
        onTop.draw(canvas);
        
     // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        
        preview.addView(mPreview);
        
        
        
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_camera, menu);
        return true;
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	mCamera.stopPreview();
    }
    
    
    @Override
    public void onResume(){
    	super.onResume();
    	mCamera.startPreview();
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
