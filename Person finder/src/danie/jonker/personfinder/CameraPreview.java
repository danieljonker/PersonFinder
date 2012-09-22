package danie.jonker.personfinder;

import static com.googlecode.javacv.cpp.opencv_contrib.createFisherFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizerPtr;
import com.googlecode.javacv.cpp.opencv_core.CvArr;
import com.googlecode.javacv.cpp.opencv_core.MatVector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.FaceDetector;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */



@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError", "ParserError" })
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = "CameraPreview";
	private static final int NUM_FACES = 5;
	private SurfaceHolder mHolder;
    private Camera mCamera;
    private byte[] mBuffer;
    private FaceRecognition faceRec = new FaceRecognition();
    
    
    @SuppressWarnings("deprecation")
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
        	 	
            
            
            
            Parameters parameters = mCamera.getParameters();
            
            //Supported sizes: 720x480, 640x480, 352x288, 176x144
            /*for (Camera.Size prevSizes : parameters.getSupportedPreviewSizes()){
            	Log.i("Supported Sizes", String.valueOf(prevSizes.width) + "x" + String.valueOf(prevSizes.height));
            }*/
            //Camera.Size prevSizes = parameters.getPreviewSize();
            //Log.i("Size", String.valueOf(prevSizes.width) + "x" + String.valueOf(prevSizes.height));
            for (Camera.Size prevSizes : parameters.getSupportedPictureSizes()){
            	Log.i("Supported Sizes", String.valueOf(prevSizes.width) + "x" + String.valueOf(prevSizes.height));
            }
            for (Integer prevForms : parameters.getSupportedPreviewFormats()){
            	Log.i("Supported Formats", String.valueOf(prevForms));
            }
            
            
            
            Log.i("Current Picture Size", String.valueOf(parameters.getPictureSize().height) + "x" + String.valueOf(parameters.getPictureSize().width));
           
            int iFormat = parameters.getPreviewFormat();
            Log.i("Image Format", String.valueOf(iFormat));
            
            //parameters.setPreviewFormat(842094169);
            parameters.setPictureSize(1600,900);
            parameters.setPreviewSize(352, 288);
            //parameters.setPreviewFormat(ImageFormat.RGB_565) ; This is not supported
            mCamera.setParameters(parameters);
            
            
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            //startFaceDetection();
            
            mCamera.setPreviewCallback(this);
            
            //Available picture sizes for samsung nexus: 480x640, 960x1280, 1200x1600, 1536x2048, 1920x2560
            //parameters.setPreviewSize(pictureLength, pictureHeight);
            
            
            
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        } catch (NullPointerException e){
        	Log.d(TAG, "Error in surface created, could be orientation change: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    	mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
    
    @Override
    public void onPreviewFrame(byte[] data, Camera cam) 
    {
    	Collection<Rect> faces = new ArrayList<Rect>(); 
        
    	
    	Parameters parameters = cam.getParameters(); 
    	
    	Integer width = parameters.getPreviewSize().width;
        Integer height = parameters.getPreviewSize().height;
        
        Log.i("preview size: ", String.valueOf(width) + "x" + String.valueOf(height));
        int[] mIntArray = new int[width*height];
        
        
        // Decode Yuv data to integer array
        decodeYUV420SP(mIntArray, data, width, height);

        
        //Initialize the bitmap, with the replaced color  
        //Bitmap bmp = Bitmap.createBitmap(mIntArray, width, height, Bitmap.Config.ARGB_8888); 
        
        Bitmap bmp = Bitmap.createBitmap(mIntArray, width, height, Bitmap.Config.RGB_565);  
        //saveImage(bmp);
        
        Collection<Rect> faceRects = findFaces(bmp);
        
        if (faceRects.size() > 0){
        	for (Rect r : faceRects){
        		
        		saveFace(bmp, r);
        	}
        }
        Log.i(TAG, faceRects.toString());
        //Collection<Rect> faceRects = FaceDetection.findfaces(bmp);
        //Rect r = FaceDetection.findFace(bmp);
        
    }
    
    

	@Override
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
      	 mCamera.setPreviewCallback(this);
      	 //startFaceDetection();

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

            mCamera.setPreviewCallback(this);
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
	
	
	/**
	 * This method is to change the image format yuv, to the supported rgb format for bitmaps
	 * @param rgba
	 * @param yuv420sp
	 * @param width
	 * @param height
	 */
	
	
	/**
	 * This method is to find the faces
	 */
	@SuppressLint("ParserError")
	public ArrayList<Rect> findFaces(Bitmap bmp){
		ArrayList<Rect> rects = new ArrayList<Rect>();
		
		FaceDetector.Face faces[] = new FaceDetector.Face[NUM_FACES];
		FaceDetector arrayFaces = new FaceDetector(bmp.getWidth(), bmp.getHeight(), NUM_FACES);
		//Log.i(TAG, String.valueOf(bmp.getWidth() + "x" + String.valueOf(bmp.getHeight())));
		
		Integer facesFound = arrayFaces.findFaces(bmp, faces);
		Log.i("Faces Found", String.valueOf(facesFound));
		
		if (facesFound > 0) {
			//saveImage(bmp);
		
			for(int i=0; i<facesFound; i++){
				FaceDetector.Face face = faces[i];
				
				PointF midEyes = new PointF();
		        face.getMidPoint( midEyes );
		        
		        
		        float eyedist = face.eyesDistance();
		        PointF lt = new PointF( midEyes.x - eyedist * 2.0f, midEyes.y - eyedist * 2.5f );
		        // Create rectangle around face.  Create a box based on the eyes and add some padding.
		        // The ratio of head height to width is generally 9/5 but that makes the rect a bit to tall.
		        Log.i("left", "lt.x: " + String.valueOf((int) ( lt.x)));
		        Log.i("top", "lt.y: " + String.valueOf((int) ( lt.y)));
		        Log.i("right", "lt.x + eyedist: " + String.valueOf((int) ( lt.x + eyedist * 3.0f )) + "bmp width" + String.valueOf(bmp.getWidth()));
		        
		        //These values determine the bounding box and the face image that is taken and given to the recogniser
		        Rect r = new Rect(
		            Math.max( (int) ( lt.x) + 40, 0 ),
		            Math.max( (int) ( lt.y) + 50, 0 ),
		            Math.min( (int) ( lt.x + eyedist * 3.0f ), bmp.getWidth() ),
		            Math.min( (int) ( lt.y + eyedist * 4.2f ), bmp.getHeight() )
		        );
		        //adding rectangle to list 
		        rects.add(r);
		        
		        
		        
		        Log.i( TAG,
		                "Found face. Confisdence: " + face.confidence() + ". Eye Distance: " + face.eyesDistance() + " Pose: ("
		                        + face.pose( FaceDetector.Face.EULER_X ) + "," + face.pose( FaceDetector.Face.EULER_Y ) + ","
		                        + face.pose( FaceDetector.Face.EULER_Z ) + "). Eye Midpoint: (" + midEyes.x + "," + midEyes.y + ")" );
		        
			}
		}
		return rects;
	}
	
	private void saveFace(Bitmap bmp, Rect r){
		File myDir=new File("/sdcard/saved_images");
		  myDir.mkdirs();
		  Random generator = new Random();
		  int n = 10000;
		  n = generator.nextInt(n);
		  String fname = "FaceImage-"+ n +".jpg";
		  File file = new File (myDir, fname);
		  if (file.exists ()) file.delete (); 
		  try {
			  Bitmap  bitmap = Bitmap.createBitmap(r.width(),r.height(), Bitmap.Config.RGB_565);
			  Canvas canvas = new Canvas(bitmap);
			  Bitmap toDisk = bmp.copy(bmp.getConfig(), true);
			  
			  //destination rectangle, please work
			  Rect dest = new Rect(0,0, 0 + (r.right - r.left), 0 + (r.bottom - r.top));
			  canvas.drawBitmap(toDisk, r, dest, null);
			  
			   
			  FileOutputStream out = new FileOutputStream(file);
			  //Bitmap bitmap = this.getDrawingCache();
			  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		      //faceBmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
		      out.flush();
		      out.close();

		   } catch (Exception e) {
		       e.printStackTrace();
		  }
	}

	
	private void saveImage(Bitmap bmp) {

		  File myDir=new File("/sdcard/FaceDB");
		  myDir.mkdirs();
		  Random generator = new Random();
		  int n = 10000;
		  n = generator.nextInt(n);
		  String fname = "Image-"+ n +".jpg";
		  File file = new File (myDir, fname);
		  if (file.exists ()) file.delete (); 
		  try {
			
		       FileOutputStream   out = new FileOutputStream(file);
		       bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
		       out.flush();
		       out.close();

		   } catch (Exception e) {
		       e.printStackTrace();
		  }
		}
	
	
	/**
	 * DecodeYUV
	 * @param rgb
	 * @param yuv420sp
	 * @param width
	 * @param height
	 */
	static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
    	final int frameSize = width * height;
    	
    	for (int j = 0, yp = 0; j < height; j++) {
    		int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
    		for (int i = 0; i < width; i++, yp++) {
    			int y = (0xff & ((int) yuv420sp[yp])) - 16;
    			if (y < 0) y = 0;
    			if ((i & 1) == 0) {
    				v = (0xff & yuv420sp[uvp++]) - 128;
    				u = (0xff & yuv420sp[uvp++]) - 128;
    			}
    			
    			int y1192 = 1192 * y;
    			int r = (y1192 + 1634 * v);
    			int g = (y1192 - 833 * v - 400 * u);
    			int b = (y1192 + 2066 * u);
    			
    			if (r < 0) r = 0; else if (r > 262143) r = 262143;
    			if (g < 0) g = 0; else if (g > 262143) g = 262143;
    			if (b < 0) b = 0; else if (b > 262143) b = 262143;
    			
    			rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
    		}
    	}
    } //decodeYUV
	
	/**
	 * train facerecogniser
	 */
	public void train() {
		Log.i(TAG, "Starting train method");
		long startTime = System.nanoTime();
		
		
		MatVector images;
		CvArr labelsCV;

		//Total number of faces in database
		final int numberOfImages = 20;

		//top level path of face database directory
		File faceDir = new File("/sdcard/FaceDB");
		
		// Allocate some memory:
		images = new MatVector(numberOfImages);
		
		//CvArr arr = new CvArr(new CvMatArray(images));
		//array of integer labels
		//Integer[] labels = new Integer[numberOfImages];
		ArrayList<Integer> labels = new ArrayList<Integer>();
		
		//Make an intPointer - this is the c++ array type, will be filled with labels.
		IntPointer iPoint = new IntPointer(0,0);
		
		int[] labelIntArray = new int[2];
		labelIntArray[0] = 0;
		labelIntArray[1] = 0;
		
		CvArr image;
		// Load an image:
		image = cvLoadImage(faceDir + "/s1/1.pgm");
		
		// And put it into the MatVector:
		images.put(0, image); 
		// and put the label in
		labels.add(0);
		
		// and repeat
		image = cvLoadImage(faceDir + "/s1/2.pgm");
		images.put(1, image);
		labels.add(0);
		
		iPoint.put(labelIntArray);
		for (Integer i: labels){
			//iPoint.put(i);
		}
		//turn the intPointer into a CvArr  
		//labelsCV = new CvArr(iPoint);
		labelsCV = new CvArr(iPoint);
		// Then get a Pointer to a FaceRecognizer (FaceRecognizerPtr).
		// Java doesn't have default parameters, so you have to add some yourself,
		// if you pass 0 as num_components to the EigenFaceRecognizer, the number of
		// components is determined by the data, for the threshold use the maximum possible
		// value if you don't want one. I don't know the constant in Java:
		FaceRecognizerPtr model = createFisherFaceRecognizer(0,1000);
		// Then train it. See how I call get(), to get the FaceRecognizer inside the FaceRecognizerPtr:
	    model.get().train(images, labelsCV);
		
	    //get endtime and calculate time training process takes
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		double seconds = (double)duration / 1000000000.0;
		Log.i(TAG, "Training took: " + String.valueOf(seconds));
	}
	
	
	public void train(String filepath) {
		faceRec.learn(filepath);
	}


}