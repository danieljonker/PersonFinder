package danie.jonker.personfinder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


@SuppressLint({ "ParserError", "ParserError" })
public class FaceDetection {
	
	private static int imageWidth;
	private static int imageHeight;
	public static final String TAG = "Face Detection";
    private static int numberOfFace = 5;
    private static FaceDetector myFaceDetect; 
    private static FaceDetector.Face[] myFace;
    static float myEyesDistance;
    static int numberOfFaceDetected;
    

	public static Rect findFace(Bitmap bmp) {
	    // Ask for 1 face
		Log.d(TAG, "Finding Faces");
		Face faces[] = new FaceDetector.Face[1];
	    if (bmp != null){
		FaceDetector detector = new FaceDetector(bmp.getWidth(), bmp.getHeight(), 1 );
	    
	    int count = detector.findFaces( bmp, faces );

	    Face face = null;
	    //saveBitmap(bmp);
	    if( count > 0 ) {
	    	saveBitmap(bmp);
	    	Log.i(TAG, "FOUND FACE");
	    	
	        face = faces[0];

	        PointF midEyes = new PointF();
	        face.getMidPoint( midEyes );
	        Log.i( TAG,
	                "Found face. Confisdence: " + face.confidence() + ". Eye Distance: " + face.eyesDistance() + " Pose: ("
	                        + face.pose( FaceDetector.Face.EULER_X ) + "," + face.pose( FaceDetector.Face.EULER_Y ) + ","
	                        + face.pose( FaceDetector.Face.EULER_Z ) + "). Eye Midpoint: (" + midEyes.x + "," + midEyes.y + ")" );

	        float eyedist = face.eyesDistance();
	        PointF lt = new PointF( midEyes.x - eyedist * 2.0f, midEyes.y - eyedist * 2.5f );
	        // Create rectangle around face.  Create a box based on the eyes and add some padding.
	        // The ratio of head height to width is generally 9/5 but that makes the rect a bit to tall.
	        return new Rect(
	            Math.max( (int) ( lt.x ), 0 ),
	            Math.max( (int) ( lt.y ), 0 ),
	            Math.min( (int) ( lt.x + eyedist * 4.0f ), bmp.getWidth() ),
	            Math.min( (int) ( lt.y + eyedist * 5.5f ), bmp.getHeight() )
	        );
	        
	    }
	    } else {
	    	Log.d(TAG, "bmp = null");
	    }
	    return null;
	}
	


	public static void saveBitmap(Bitmap bmp){
		String root = Environment.getExternalStorageDirectory().toString();
		Log.i(TAG, root);
	    File myDir = new File(root + "/saved_images");    
	    myDir.mkdirs();
	    Random generator = new Random();
	    int n = 10000;
	    n = generator.nextInt(n);
	    String fname = "Image-"+ n +".jpg";
	    File file = new File (myDir, fname);
	    if (file.exists ()) file.delete (); 
	    try {
	    	
	        FileOutputStream out = new FileOutputStream(file);
	        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
	        out.flush();
	        out.close();

	    } catch (Exception e) {
	           e.printStackTrace();
	    }
	}
	
	
	
	
	
}

	
	
	




