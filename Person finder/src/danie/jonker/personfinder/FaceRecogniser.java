package danie.jonker.personfinder;

import static com.googlecode.javacv.cpp.opencv_contrib.createEigenFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_contrib.createFisherFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.util.Log;

import com.googlecode.javacpp.IntPointer;
import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizerPtr;
import com.googlecode.javacv.cpp.opencv_core.CvArr;
import com.googlecode.javacv.cpp.opencv_core.MatVector;

public class FaceRecogniser {
	private static final String TAG = "Face Recogniser";
	

	public static void train() {
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
		
		ArrayList<Integer> labels = new ArrayList<Integer>();
		
		//Make an intPointer - this is the c++ array type, will be filled with labels.
		IntPointer iPoint = new IntPointer();
		
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

}
