package danie.jonker.personfinder;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
	private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
        	
        	
        	
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            startFaceDetection();
            
            
            
            
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        } catch (NullPointerException e){
        	Log.d(TAG, "Error in surface created, could be orientation change: " + e.getMessage());
        }
 	
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }
    

	//Method to startFaceDetection, it also checks to see if facedetection supported
	public void startFaceDetection(){
	    // Try starting Face Detection
	    Camera.Parameters params = mCamera.getParameters();

	    Log.i(TAG, "num Faces: " + String.valueOf(params.getMaxNumDetectedFaces()));
	    // start face detection only *after* preview has started
	    if (params.getMaxNumDetectedFaces() > 0){
	        // camera supports face detection, so can start it:
	        mCamera.startFaceDetection();
	    }
	}

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }
        
         Log.i(TAG, "Orientation Changed");
      	 mCamera.stopPreview();
      	 if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
      		 mCamera.setDisplayOrientation(90);
      	 } else {
      		 mCamera.setDisplayOrientation(0);
      	 }
      	 mCamera.startPreview();
      	 startFaceDetection();

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}