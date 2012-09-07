package danie.jonker.personfinder;

import android.hardware.Camera;
import android.util.Log;

class MyFaceDetectionListener implements Camera.FaceDetectionListener {


	@Override
	public void onFaceDetection(android.hardware.Camera.Face[] faces, Camera arg1) {
		// TODO Auto-generated method stub
		if (faces.length > 0){
            Log.i("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );
            
            
        }
	}
}