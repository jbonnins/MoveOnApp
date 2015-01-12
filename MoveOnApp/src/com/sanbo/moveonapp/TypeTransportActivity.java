package com.sanbo.moveonapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sanbo.adapter.ListViewAdapterTransport;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.datamodel.Transport;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;

import com.sanbo.enumerated.TypeTransport;

import de.keyboardsurfer.android.widget.crouton.Crouton;


public class TypeTransportActivity extends SherlockFragmentActivity {
	public static final String TAG = "TypeTransportActivity";
    private PublicTransport myPublic = PublicTransport.getInstance();
	
    private ListViewAdapterTransport adapter = null;
	private ProgressBar myProgressBar;
	private TextView myShowMessage;
    private ListView myList = null;
	private TextView isEmpty;
    private Context context;
    private long initialSelection;
    private long mSelection;
    private ArrayList<Transport> myTransport;
	private SavingState mySave = SavingState.getInstance();
    private boolean init = false;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TypeTransportActivity.onCreate(Bundle savedInstanceState)");
		if (savedInstanceState != null){
			synchronized(this){
				initialSelection = savedInstanceState.getLong("initialSelection");
				init = true;
			}
		}
		context = this;

		// Show the Up button in the action bar:
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.list_generic);
		myList = (ListView) findViewById(R.id.list);
		myProgressBar = (ProgressBar) findViewById(R.id.progress);
		myShowMessage = (TextView) findViewById(R.id.showmessage);
		TextView myTitle = (TextView) findViewById (R.id.name_list);
		TextView isEmpty = (TextView) findViewById (R.id.empty);
		
		//Execute asyntask (we can send string array)
		new BackgroundTask().execute("SOMETHING");
        //adapter = new ListViewAdapterTransport(context, myPublic.getListAllTypeOfTransport());
        //myList.setAdapter(adapter);
        
        //If empty list show message EMPTY LIST
        if (adapter != null && adapter.getCount()==0) isEmpty.setVisibility(View.VISIBLE);
        //Title of the list
        myTitle.setText(getResources().getString(R.string.type_of_transport));
        
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TypeTransportActivity.onSaveInstanceState(Bundle outState)");
        //Our state is saved on PublicTransport and in our DDBB
		//we control if there are changes to retrieve new data from public transport
    	// we control only for some public transport
		outState.putLong("initialSelection", initialSelection);
		mSelection = adapter.getMySelection();
		outState.putLong("actualSelection", mSelection);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	if (savedInstanceState != null){
    		initialSelection = savedInstanceState.getLong("initialSelection");
    		mSelection = savedInstanceState.getLong("mySelection");
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TypeTransportActivity.onCreateOptionsMenu(Menu menu)");
		// we don't have an specific menu 
        return true;
    }
 
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TypeTransportActivity. onMenuItemSelected(int featureId, MenuItem item)");
        int itemId = item.getItemId();
        switch (itemId) {
        case android.R.id.home:
            if (Config.DEBUG) Toast.makeText(this, getResources().getString(R.string.action_home_show), 
            		Toast.LENGTH_LONG).show();
            MoveOnCroutonStyle.croutonConfirm(TypeTransportActivity.this, R.string.action_home_show);
            mSelection = adapter.getMySelection(); //selection();
            if (initialSelection != mSelection){
            	initialSelection = mSelection;
            	myPublic.ResetAllLists();
            	myPublic.getListAllMessages();
            }
            finish();
            return true;

        default:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT DEF " + String.valueOf(item.getItemId()));
            return super.onOptionsItemSelected(item);
        }
        //return true;
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // here we leave the activity and control for changes
        mSelection = adapter.getMySelection(); //selection();
        if (initialSelection != mSelection){
        	initialSelection = mSelection;
        	myPublic.ResetAllLists();
        }
        // Clean queue of messages from this activity
        Crouton.clearCroutonsForActivity(TypeTransportActivity.this);
    }
    
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        //when we suspend the view, check for changes
        mSelection = adapter.getMySelection(); //selection();
        if (initialSelection != mSelection){
        	initialSelection = mSelection;
        	myPublic.ResetAllLists();
        }
    }
    
/*    protected Long selection(){
    	int[] actualSelection;
		actualSelection = new int[TypeTransport.LastPublic().getValue()];
    	for (int i = 0; i < TypeTransport.LastPublic().getValue(); i++){
    		actualSelection[i] = myTransport.get(i).isFavouriteTransport() ? 1 : 0;	    		
    	}	    		
    	return Config.hash(actualSelection);
    }
*/    
    
	private class BackgroundTask extends AsyncTask<String, Void, String> {
		// What we need in temp loadar
		int[] initSelection;
		SavingState mSave = SavingState.getInstance();
	  
	@Override
	protected void onPreExecute()
	{
		  //showing progressbar
		  myProgressBar.setVisibility(View.VISIBLE);
		  myShowMessage.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected String doInBackground(String... urls) 
	{			
	    try
		{
	    	//our task
	    	myTransport = myPublic.getListAllTypeOfTransport(mSave.isFavoriteOn());
	    	initSelection = new int[TypeTransport.LastPublic().getValue()];
	    	// we control only for some public transport
	    	for (int i = 0; i < myTransport.size(); i++){
	    		if (myTransport.get(i).getFK_id_TypeTransport() < TypeTransport.LastPublic().getValue()){
	    			initSelection[i] = myTransport.get(i).isFavouriteTransport() ? 1 : 0;
	    		}
	    	}
	    	initialSelection = Config.hash(initSelection);
	    	adapter = new ListViewAdapterTransport(context, myTransport);
			
		} catch (Exception e)
		{
			Log.e(MainActivity.class.toString(), e.getMessage());
		}
			//on post execute we can send an String	
			return "moveOn";
	 }

	 @Override
	 protected void onPostExecute(String result) 
	 {
	    //Asyntask is finished, we hide progressbar
		 myProgressBar.setVisibility(View.GONE);
		 myShowMessage.setVisibility(View.GONE);
		 //now we can show our list or simply working
		 if (adapter != null && adapter.getCount() > 0)
		 {
			 //THISACTIVITY.this.imageView.setImageBitmap(bmp);
			 //THISACTIVITY.this.imageView.setVisibility(View.VISIBLE);
		     myList.setAdapter(adapter);
		     if (!init) initialSelection = adapter.getMySelection();

		 } else if (adapter != null){
			 	//show empty message
			 	isEmpty.setVisibility(View.VISIBLE);
			 	if (mySave.isFavoriteOn()){
			 		isEmpty.setText(R.string.emptyFavoriteList);
			 	}			 
		 }
		 //if not, we show an error
		 else
		 {
			 	Builder builder = new Builder(TypeTransportActivity.this);
				builder.setTitle(R.string.title_error);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setMessage(R.string.error_database);					
				builder.setNeutralButton(getString(R.string.button_ok), null);			

				AlertDialog alertDialog = builder.create();
				alertDialog.show();
				alertDialog.setCancelable(false);
		 }
	  }
	}
    
}



