/*
 * Copyright 2013 Juan José Bonnín Sansó (jbonnins@uoc.edu)
 *
 * This file is part of MoveOnApp.
 *
 *    MoveOnApp is free software: you can redistribute it and/or modify
 *    it under the terms of the Affero GNU General Public License version 3
 *    as published by the Free Software Foundation.
 *
 *    MoveOnApp is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Affero GNU General Public License for more details
 *    (https://www.gnu.org/licenses/agpl-3.0.html).
 *    
 *    All the creations are under sanbo package, which is my short surname. 
 */

package com.sanbo.fragment.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.sanbo.moveonapp.R;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.fragment.tabA.AppTabAFirstFragment;
import com.sanbo.fragment.tabA.AppTabASecondFragment;
import com.sanbo.fragment.tabA.AppTabAThirdFragment;
import com.sanbo.fragment.tabB.AppTabBFirstFragment;
import com.sanbo.fragment.tabB.AppTabBSecondFragment;
import com.sanbo.fragment.tabC.AppTabCFirstFragment;
import com.sanbo.fragment.tabC.AppTabCSecondFragment;
import com.sanbo.fragment.tabC.AppTabCThirdFragment;
import com.sanbo.fragment.tabD.AppTabDFirstFragment;
import com.sanbo.fragment.tabD.AppTabDFithFragment;
import com.sanbo.fragment.tabD.AppTabDFourthFragment;
import com.sanbo.fragment.tabD.AppTabDSecondFragment;
import com.sanbo.fragment.tabD.AppTabDThirdFragment;
import com.sanbo.fragment.tabE.AppTabEFirstFragment;
import com.sanbo.fragment.tabE.AppTabESecondFragment;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

@SuppressLint("InflateParams")
public class AppMainTabActivity extends SherlockFragmentActivity implements SynchronizableActivity {
	/* This activity */
    // SINGLETON DEFINITION
	private AppMainTabActivity myActivity = null;
	// Saving Data
	private SavingState savingState;
	
    /* Your Tab host */
    private TabHost mTabHost;
    
    /* If Bike are selected */
    private boolean isBikeSelected;
    
    /* A HashMap of stacks, where we use tab identifier as keys..*/
    private HashMap<String, Stack<SherlockFragment>> mStacks;

    /*Save current tabs identifier in this..*/
    private String mCurrentTab;
    // data to pass to next fragment f.e. Id_Line -> List Stations...
    private HashMap<String, Stack<Integer>> mCurSel;
    
    // Data we need
    private PublicTransport myPublic = PublicTransport.getInstance();
    private SavingState mySave = SavingState.getInstance();
      
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // My activity containing tabs, each tab with its own stack of fragments
        if (myActivity == null)
        	myActivity = this;
        // pointer to my activity
        savingState = SavingState.getInstance();
        savingState.setMyActivity(myActivity);

        setContentView(R.layout.app_main_tab_fragment_layout);

        /*  
         *  Navigation stacks for each tab gets created.. 
         *  tab identifier is used as key to get respective stack for each tab
         */
        mStacks             =   new HashMap<String, Stack<SherlockFragment>>();
        mStacks.put(AppConstants.TAB_A, new Stack<SherlockFragment>());
        mStacks.put(AppConstants.TAB_B, new Stack<SherlockFragment>());
        mStacks.put(AppConstants.TAB_C, new Stack<SherlockFragment>());
        mStacks.put(AppConstants.TAB_D, new Stack<SherlockFragment>());
        mStacks.put(AppConstants.TAB_E, new Stack<SherlockFragment>());

        /*  
         *  Selection stacks for each tab gets created.. 
         *  tab identifier is used as key to get respective stack for each tab
         */
        mCurSel             =   new HashMap<String, Stack<Integer>>();
        mCurSel.put(AppConstants.TAB_A, new Stack<Integer>());
        mCurSel.put(AppConstants.TAB_B, new Stack<Integer>());
        mCurSel.put(AppConstants.TAB_C, new Stack<Integer>());
        mCurSel.put(AppConstants.TAB_D, new Stack<Integer>());
        mCurSel.put(AppConstants.TAB_E, new Stack<Integer>());
        
        mTabHost                =   (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(listener);
        mTabHost.setup();

        initializeTabs();
        
		// Show the Up button in the action bar:
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
        
	    if (savedInstanceState != null) {
            if (Config.DEBUG)Toast.makeText(this, getResources().getString(R.string.action_home_show), Toast.LENGTH_LONG).show();
            
            String tabId = savedInstanceState.getString("myFrag", AppConstants.TAB_D);
            int num = savedInstanceState.getInt("numInList");
            int myTab = savedInstanceState.getInt("tabSelected");
            setCurrentTab(myTab);

            if (num == 2){
                if(tabId.equals(AppConstants.TAB_A)){
                	  pushFragments(tabId, new AppTabASecondFragment(), false,true);
                  }else if(tabId.equals(AppConstants.TAB_B)){
                	  pushFragments(tabId, new AppTabBSecondFragment(), false,true);
                  }else if(tabId.equals(AppConstants.TAB_C)){
                      pushFragments(tabId, new AppTabCSecondFragment(), false,true);
                  }else if(tabId.equals(AppConstants.TAB_D)){
                      pushFragments(tabId, new AppTabDSecondFragment(), false,true);
                  }else if(tabId.equals(AppConstants.TAB_E)){
                      pushFragments(tabId, new AppTabESecondFragment(), false,true);
                  }
            }
            if (num == 3){
                if(tabId.equals(AppConstants.TAB_A)){
					pushFragments(tabId, new AppTabAThirdFragment(), false,true);               	
                }else if(tabId.equals(AppConstants.TAB_C)){
                    pushFragments(tabId, new AppTabCThirdFragment(), false,true);
                }else if(tabId.equals(AppConstants.TAB_D)){
                    pushFragments(tabId, new AppTabDThirdFragment(), false,true);
                }
            }	    
            if (num == 4){
                if(tabId.equals(AppConstants.TAB_D)){
					pushFragments(tabId, new AppTabDFourthFragment(), false,true);               	
                }
            }	    
            if (num > 4){
                if(tabId.equals(AppConstants.TAB_D)){
 					pushFragments(tabId, new AppTabDFithFragment(), false,true);               	
                 }
            }	    
        } else if (savingState.getTabInit() != null){
        	String tabId = savingState.getTabInit();
        	//by default the first screen is Favorite
        	int myTab = 3;
        	if(tabId.equals(AppConstants.TAB_A)){
        		myTab = 0;
        	}else if(tabId.equals(AppConstants.TAB_B)){
            	myTab = 1;
            }else if(tabId.equals(AppConstants.TAB_C)){
            	myTab = 2;
            }else if(tabId.equals(AppConstants.TAB_D)){
            	myTab = 3;
            }else if(tabId.equals(AppConstants.TAB_E)){
            	myTab = 4;
            }
            // activate the current tab
            setCurrentTab(myTab);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	if (Config.DEBUG) Toast.makeText(this, getResources().getString(R.string.action_home_show), Toast.LENGTH_LONG).show();
    	outState.putString("myFrag", mCurrentTab);
    	outState.putInt("numInList", mStacks.get(mCurrentTab).size());
    	outState.putInt("tabSelected", mTabHost.getCurrentTab());
    	Iterator<Integer> iter = mCurSel.get(mCurrentTab).iterator();
    	int i = 0;
    	int[] val = new int[mCurSel.get(mCurrentTab).size()];
    	// saving selections to arrive to the fragment
    	while (iter.hasNext()){
    		val[i] = iter.next();
    	}
    	outState.putIntArray("valPass", val);
    	
    }
    
    private View createTabView(final int id) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_icon, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        return view;
    }
	    
    public void initializeTabs(){
    	TabHost.TabSpec spec;
    	
    	isBikeSelected = myPublic.isBikeFavorite();

        /* Setup your tab icons and content views.. Nothing special in this..*/
        spec = mTabHost.newTabSpec(AppConstants.TAB_A);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
		//if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        spec.setIndicator(createTabView(R.drawable.tab_a_state_btn));
        mTabHost.addTab(spec);
        
        spec = mTabHost.newTabSpec(AppConstants.TAB_B);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.tab_b_state_btn));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(AppConstants.TAB_C);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.tab_c_state_btn));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(AppConstants.TAB_D);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.tab_d_state_btn));
        mTabHost.addTab(spec);
        
        // only if we have Bike selected as Type Transport
        if (isBikeSelected){    
	        spec = mTabHost.newTabSpec(AppConstants.TAB_E);
	        spec.setContent(new TabHost.TabContentFactory() {
	            public View createTabContent(String tag) {
	                return findViewById(R.id.realtabcontent);
	            }
	        });
	        spec.setIndicator(createTabView(R.drawable.tab_e_state_btn));
	        mTabHost.addTab(spec);
        }
        
        setCurrentTab(0);
    
    }
    /*Comes here when user switch tab, or we do programmatically*/
    TabHost.OnTabChangeListener listener    =   new TabHost.OnTabChangeListener() {
      public void onTabChanged(String tabId) {
        /*Set current tab..*/
        mCurrentTab = tabId;

        if(mStacks.get(tabId).size() == 0){
          /*
           *    First time this tab is selected. So add first fragment of that tab.
           *    Dont need animation, so that argument is false.
           *    We are adding a new fragment which is not present in stack. So add to stack is true.
           */
          if(tabId.equals(AppConstants.TAB_A)){
        	  mySave.setFavoriteOn(false);
        	  pushFragments(tabId, new AppTabAFirstFragment(), false,true);
          }else if(tabId.equals(AppConstants.TAB_B)){
        	  mySave.setFavoriteOn(false);        	  
        	  pushFragments(tabId, new AppTabBFirstFragment(), false,true);
          }else if(tabId.equals(AppConstants.TAB_C)){
        	  mySave.setFavoriteOn(false);        	  
              pushFragments(tabId, new AppTabCFirstFragment(), false,true);
          }else if(tabId.equals(AppConstants.TAB_D)){
        	  mySave.setFavoriteOn(mySave.isLastFavorite());
              pushFragments(tabId, new AppTabDFirstFragment(), false,true);
          }else if(tabId.equals(AppConstants.TAB_E)){
        	  mySave.setFavoriteOn(false);
              pushFragments(tabId, new AppTabEFirstFragment(), false,true);
          }
        }else {
          /*
           *    We are switching tabs, and target tab is already has atleast one fragment. 
           *    No need of animation, no need of stack pushing. Just show the target fragment
           */
           if (!tabId.equals(AppConstants.TAB_D)){
        	   mySave.setFavoriteOn(false);
           } else {
        	   mySave.setFavoriteOn(mySave.isLastFavorite());
           }
           pushFragments(tabId, mStacks.get(tabId).lastElement(), false, false);
        }
      }
    };

    /* Might be useful if we want to switch tab programmatically, from inside any of the fragment.*/
    public void setCurrentTab(int val){
        mTabHost.setCurrentTab(val);
    }

    /* 
     *      To add fragment to a tab. 
     *  tag             ->  Tab identifier
     *  fragment        ->  Fragment to show, in tab identified by tag
     *  shouldAnimate   ->  should animate transaction. false when we switch tabs, or adding first fragment to a tab
     *                      true when when we are pushing more fragment into navigation stack. 
     *  shouldAdd       ->  Should add to fragment navigation stack (mStacks.get(tag)). false when we are switching tabs (except for the first time)
     *                      true in all other cases.
     */
    public synchronized void pushFragments(String tag, SherlockFragment fragment, boolean shouldAnimate, boolean shouldAdd){
      if(shouldAdd)
          mStacks.get(tag).push(fragment);
      FragmentManager   manager         =   getSupportFragmentManager();
      FragmentTransaction ft            =   manager.beginTransaction();
      if(shouldAnimate)
          ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
      ft.replace(R.id.realtabcontent, fragment);
      ft.commit();
    }
        
    public synchronized void popFragments(){
      /*    
       *    Select the second last fragment in current tab's stack.. 
       *    which will be shown after the fragment transaction given below 
       */
      SherlockFragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

      /*pop current fragment from stack.. */
      mStacks.get(mCurrentTab).pop();

      /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
      FragmentManager   manager         =   getSupportFragmentManager();
      FragmentTransaction ft            =   manager.beginTransaction();
      ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
      ft.replace(R.id.realtabcontent, fragment);
      ft.commit();
    }   

    @Override
    public synchronized void onBackPressed() {
       	if(((BaseFragment)mStacks.get(mCurrentTab).lastElement()).onBackPressed() == false){
       		/*
       		 * top fragment in current tab doesn't handles back press, we can do our thing, which is
       		 * 
       		 * if current tab has only one fragment in stack, ie first fragment is showing for this tab.
       		 *        finish the activity
       		 * else
       		 *        pop to previous fragment in stack for the same tab
       		 * 
       		 */
       		if(mStacks.get(mCurrentTab).size() == 1){
       			super.onBackPressed();  // or call finish..
       		}else{
       			//savingState.setBackTabA(true);
   				popFragments();
       			//savingState.setBackTabA(false);
       		}
       	}else{
       		//do nothing.. fragment already handled back button press.
       	}
    }

    /*
     *   Imagine if you wanted to get an image selected using ImagePicker intent to the fragment. Of course I could have created a public function
     *  in that fragment, and called it from the activity. But couldn't resist myself.
     */
    @Override
    protected synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mStacks.get(mCurrentTab).size() == 0){
            return;
        }

        mStacks.get(mCurrentTab).lastElement().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
        case android.R.id.home:
            if (Config.DEBUG) Toast.makeText(this, getResources().getString(R.string.action_home_show), Toast.LENGTH_LONG).show();
            toggle();
            return true;
            
        default:
		    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT DEF " +String.valueOf(item.getItemId()));
	        return super.onOptionsItemSelected(item);
        }
        
        //return true;
    }
    
	private boolean toggle(){
    	//here we managed were we return
       	if(((BaseFragment)mStacks.get(mCurrentTab).lastElement()).onBackPressed() == false){
       		/*
       		 * top fragment in current tab doesn't handles back press, we can do our thing, which is
       		 * 
       		 * if current tab has only one fragment in stack, ie first fragment is showing for this tab.
       		 *        finish the activity
       		 * else
       		 *        pop to previous fragment in stack for the same tab
       		 * 
       		 */
       		if(mStacks.get(mCurrentTab).size() == 1){
       			super.onBackPressed();  // or call finish..
       		}else{
       			popFragments();
       		}
       	}else{
       		//do nothing.. fragment already handled back button press.
       	}
    	return true;
    }

	@Override
	public void onSuccessfulNetworkSynchronization() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnsuccessfulNetworkSynchronization() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationSynchronization() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SherlockFragmentActivity getSynchronizableActivity() {
		// TODO Auto-generated method stub
		return this;
	}

  
}
