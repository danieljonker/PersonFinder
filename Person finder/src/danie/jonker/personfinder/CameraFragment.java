package danie.jonker.personfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CameraFragment extends Fragment{
	
	public CameraFragment(){
		
	}
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  
	    Log.e("Test", "hello");
	  }

	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	  }

	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
		  Log.e("Test", "oncreateview");
		View view = inflater.inflate(R.layout.activity_camera, container, false);
	    return view;
	  }
	

}
