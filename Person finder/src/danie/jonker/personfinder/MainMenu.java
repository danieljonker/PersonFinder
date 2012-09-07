package danie.jonker.personfinder;


import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/*
 * This is the class for the main menu of the person finder app
 * 
 * @author: Daniel Jonker
 */


public class MainMenu extends FragmentActivity implements ActionBar.TabListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static final String TAG = "Main MEnu";
    //TextView btnCamera = (TextView) findViewById(R.id.btnCamera); 
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //setContentView(R.layout.empty_fragment);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // For each of the sections in the app, add a tab to the action bar.
        actionBar.addTab(actionBar.newTab().setText(R.string.title_section1).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.title_section2).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.title_section3).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.title_section4).setTabListener(this));
        
        /**
         * Button listener class, this is temporary because fragments are taking waaaaaaaaaaaaaaaaaaaay too long
         */
        final Button button = (Button) findViewById(R.id.btnCamera);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Switch to camera activity
    			if (checkCameraHardware(getApplicationContext())){
    				Log.i(TAG, "Camera exists, starting camera activity");
    				Intent i = new Intent(getApplicationContext(), CameraActivity.class);
    				startActivity(i);				
    			} else {
    				//write toast to say no camera hardware
    				Log.i(TAG, "No camera hardware");
    		}
            }
        });
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    	//tab.getText().toString() gets name of tab, use for switch/ if else
    	Log.i (TAG , tab.getText().toString());
    	//Context context = getApplicationContext();
    	//int duration = Toast.LENGTH_SHORT;
    	FragmentManager fm = getFragmentManager();
    	Bundle args = new Bundle();
    	//Toast toast = null;
    	//toast.setDuration(duration);
    	
    	
        //Switch between different tabs, each tab has its own activity/fragment
        switch (tab.getPosition()){
        	case 0:
        		//main menu activity
        		Log.i (TAG, "in main menu case");
        		//toast.setText("Main menu toast");
        		//toast.show();
        		break;
        	case 1: // camera
        		Log.i (TAG, "in camera case");
        		CameraFragment camFragment = new CameraFragment();
        		
        		fm.beginTransaction().replace(R.id.container, camFragment).commit();
        		//Intent i = new Intent(getApplicationContext(), CameraActivity.class);
				//startActivity(i);	
        		//toast.setText("Camera toast");
        		//toast.show();
        		break;
        	case 2: //database
        		
        		break;
        	case 3: //settings
        		
        		break;
        	default:
        		//Main menu activity
        		
        }
        // When the given tab is selected, show the tab contents in the container
        Fragment fragment = new DummySectionFragment();
        //Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, tab.getPosition() + 1);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    
    

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */

	public static class DummySectionFragment extends Fragment {
		
        public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            Bundle args = getArguments();
            textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
            
            Integer a = args.getInt(ARG_SECTION_NUMBER);
            
            switch (a){
            	case 0:
            		System.out.println("000000 blah");
            		break;
            	case 1:
            		System.out.println("11111111111111");
            		break;
            	default:
            		
            }

            return textView;
        }
    }
}
