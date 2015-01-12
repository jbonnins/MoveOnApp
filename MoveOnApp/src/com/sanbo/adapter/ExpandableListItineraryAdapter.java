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

package com.sanbo.adapter;

import java.util.ArrayList;

import com.sanbo.datamodel.Itinerary;
import com.sanbo.datamodel.Line;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.fragment.base.AppConstants;
import com.sanbo.fragment.tabA.AppTabASecondFragment;
import com.sanbo.fragment.tabE.AppTabEFirstFragment;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;
import com.sanbo.enumerated.TypeTransport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



@SuppressLint("CutPasteId")
public class ExpandableListItineraryAdapter extends BaseExpandableListAdapter {
    protected static final String TAG = "ExpandableListItineraryAdapter";
	
	public static final int INVALID_ROW_ID = -1;
	private static final int MAX_TRANSFER = 45;

    // Declare Variables
	private Context mContext;
    private Activity activity;

	private ExpandableListView mExpandableListView;
	private ArrayList<Itinerary> mGroupCollection;
	private int[] groupStatus;
	private SavingState savingState = SavingState.getInstance();
	private PublicTransport myPublic;
	
	public ExpandableListItineraryAdapter(Context pContext,
			ExpandableListView pExpandableListView,
			ArrayList<Itinerary> pGroupCollection) {
		// TODO Auto-generated constructor stub
		mContext = pContext;
		activity = (Activity) mContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];
		myPublic = PublicTransport.getInstance();

		setListEvent();
	}

	private void setListEvent() {
		
		mExpandableListView.setOnGroupClickListener(new OnGroupClickListener(){

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				
                if (savingState.getGroupItineary() != INVALID_ROW_ID){
                	// if we have other group expanded first
                	if (groupPosition != savingState.getGroupItineary()) {
                        parent.collapseGroup(savingState.getGroupItineary());
                        parent.expandGroup(groupPosition); 
        				ImageView myItineraryImg = (ImageView)(ImageView) v.findViewById(R.id.itineraryimage);
        				Itinerary it = (Itinerary) myItineraryImg.getTag();
        				savingState.setMyItinerary(it);
                	} else {
                        parent.expandGroup(groupPosition);
                        // force to refresh - ugly solution
                        parent.collapseGroup(groupPosition);
                	}
                } else {
                    parent.expandGroup(groupPosition);
    				ImageView myItineraryImg = (ImageView)(ImageView) v.findViewById(R.id.itineraryimage);
    				Itinerary it = (Itinerary) myItineraryImg.getTag();
    				savingState.setMyItinerary(it);
            	}
                return true;
            }
			
		});
		
		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int arg0) {
						// TODO Auto-generated method stub
						groupStatus[arg0] = 1;
						savingState.setGroupItineary(arg0);
						mExpandableListView.setSelectionFromTop(arg0, 0);
					}
				});

		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						if (savingState.getGroupItineary() == arg0){
							savingState.setGroupItineary(INVALID_ROW_ID);
						}	
						groupStatus[arg0] = 0;
						mExpandableListView.setSelectionFromTop(arg0, 0);											
					}
				});
		
		mExpandableListView.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
		        /* Go to next fragment in navigation stack*/
				ImageView myLineImg = (ImageView) v.findViewById(R.id.lineimage);
				Line ln = (Line) myLineImg.getTag();
				savingState.setMyLine(ln);
				savingState.setGroupItineary(groupPosition);
				savingState.setChildLine(childPosition);
				savingState.setClosestStation(false);
				savingState.setOneWay(true);
				//savingState.getMyLine().setStationsOW(myPublic.getAllStationByLine(ln, true));
				if (myPublic.IsBikeLine(ln.get_id())) {
					savingState.getMyActivity().pushFragments(AppConstants.TAB_A, new AppTabEFirstFragment(),true,true);
				} else if (myPublic.IsBusLine(ln.get_id())){
					savingState.getMyActivity().pushFragments(AppConstants.TAB_A, new AppTabASecondFragment(),true,true);
				} else {
 					savingState.getMyActivity().pushFragments(AppConstants.TAB_A, new AppTabASecondFragment(),true,true);
				}
				return true;
			}
			
		});
	}

	@Override
	public Line getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).getLines().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).getLines().get(arg1).get_id();
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub

	    final View item;

		ChildHolder childHolder;
		if (arg3 == null) {
			item = (View) LayoutInflater.from(mContext).inflate(R.layout.group_itinerary_item, mExpandableListView, false);

			childHolder = new ChildHolder();
			
			//Get childrow.xml file elements and set values
			childHolder.lineimage = (ImageView) item.findViewById(R.id.lineimage);
			childHolder.lineimage.setTag(getChild(arg0, arg1));
			childHolder.linename1 = (TextView) item.findViewById(R.id.linename1);	
			childHolder.linename2 = (TextView) item.findViewById(R.id.linename2);
			childHolder.pmrimage_Line = (ImageView) item.findViewById(R.id.pmrimage);
			childHolder.check_favorite_line = (CheckBox) item.findViewById(R.id.check_favorite_line);
			childHolder.check_favorite_line.setTag(mGroupCollection.get(arg0).getLines().get(arg1));
			childHolder.check_favorite_line.setOnClickListener(new OnClickListener() {
				  @Override
				  public void onClick(View v) {
			                //is chkIos checked?
					  CheckBox cb = (CheckBox) v;
					  Line ln = (Line) v.getTag();
					  if (cb.isChecked()){
						  savingDataLineToPublicTransport(ln, true);
					  }else{ 
						  savingDataLineToPublicTransport(ln, false);
					  }
				  }

			});
			// Catching image transfer in View Item
			childHolder.imgtransferline = new ArrayList<ImageView>();
			for (int i=0; i < MAX_TRANSFER; i++){
				String _image = "imgtransferline" + (i+1);
				ImageView image = new ImageView(mContext);
				try{
					image = (ImageView) item.findViewById(Config.getResId(_image));
					childHolder.imgtransferline.add(image);
				}
				catch (Exception localException){
					// not controlled
					Log.e(Config.LOGTAG, "ExpandableListAdapterItinerary.getChildView.gettingImageTransfer()");    		 	 	 		
				}
			}
			item.setTag(childHolder);
		}else {
	    	item = (View) arg3;	    	
			childHolder = (ChildHolder) item.getTag();
			childHolder.check_favorite_line.setTag(mGroupCollection.get(arg0).getLines().get(arg1));
			childHolder.lineimage.setTag(mGroupCollection.get(arg0).getLines().get(arg1));
		}

		childHolder.lineimage.setImageResource(Image.getImageItinerary(mContext, mGroupCollection.get(arg0)));
		childHolder.linename1.setText(mGroupCollection.get(arg0).getLines().get(arg1).getUNameOneWay());
		childHolder.linename2.setText(mGroupCollection.get(arg0).getLines().get(arg1).getUNameOneWay2());
		// Disable facilities
		if (myPublic.HasDisabledFacilities(mGroupCollection.get(arg0).getLines().get(arg1))){
			childHolder.pmrimage_Line.setVisibility(View.VISIBLE);			
		} else {
			childHolder.pmrimage_Line.setVisibility(View.GONE);						
		}		
		childHolder.check_favorite_line.setChecked(mGroupCollection.get(arg0).getLines().get(arg1).isFavorite());
		// Load image of transfer
		if (myPublic.getmPreferences().isOptionShowTransfer()){
			int min = (MAX_TRANSFER < mGroupCollection.get(arg0).getLines().get(arg1).getListTransfer().size())? 
					MAX_TRANSFER : mGroupCollection.get(arg0).getLines().get(arg1).getListTransfer().size();
			for (int i = 0; i < min; i++){
				childHolder.imgtransferline.get(i).setImageResource(Image.getImageLine(mContext, 
						mGroupCollection.get(arg0).getLines().get(arg1).getListTransfer().get(i)));
			}
			for (int j = min; j < MAX_TRANSFER; j++){
				childHolder.imgtransferline.get(j).setImageResource(R.drawable.emtblackpqt);
			}
			// show only what you need
			RelativeLayout c1;
			if (min > MAX_TRANSFER*2/3){
				c1 = (RelativeLayout) item.findViewById(R.id.imgtransferchild3);
				c1.setVisibility(View.VISIBLE);
				c1 = (RelativeLayout) item.findViewById(R.id.imgtransferchild2);
				c1.setVisibility(View.VISIBLE);
				c1 = (RelativeLayout) item.findViewById(R.id.imgtransferchild);
				c1.setVisibility(View.VISIBLE);																									
			} else if (min <= MAX_TRANSFER*2/3){
				c1 = (RelativeLayout) item.findViewById(R.id.imgtransferchild3);
				c1.setVisibility(View.GONE);
				if (min <= MAX_TRANSFER*1/3){
					c1 = (RelativeLayout) item.findViewById(R.id.imgtransferchild2);
					c1.setVisibility(View.GONE);
					if (min == 0){
						c1 = (RelativeLayout) item.findViewById(R.id.imgtransferchild);
						c1.setVisibility(View.GONE);															
					} else {
						c1 = (RelativeLayout) item.findViewById(R.id.imgtransferchild);
						c1.setVisibility(View.VISIBLE);																					
					}
				} else {
					c1 = (RelativeLayout) item.findViewById(R.id.imgtransferchild2);
					c1.setVisibility(View.VISIBLE);
					c1 = (RelativeLayout) item.findViewById(R.id.imgtransferchild);
					c1.setVisibility(View.VISIBLE);																														
				}
			}			
		} else {
	        // Hide Transfer of Itinerary to make more clear
			RelativeLayout c = (RelativeLayout) item.findViewById(R.id.imgtransferchild);
			c.setVisibility(View.GONE);
			c = (RelativeLayout) item.findViewById(R.id.imgtransferchild2);
			c.setVisibility(View.GONE);
			c = (RelativeLayout) item.findViewById(R.id.imgtransferchild3);
			c.setVisibility(View.GONE);			
		}
		return item;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).getLines().size();
	}

	@Override
	public Itinerary getGroup(int arg0) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroupCollection.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).get_id();
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		GroupHolder groupHolder;
		RelativeLayout c;
		
	    final View itemGroup;
		
		if (arg2 == null) {
			itemGroup = (View) LayoutInflater.from(mContext).inflate(R.layout.group_itinerary, arg3, false);
			
			groupHolder = new GroupHolder();
			//group indicator
			groupHolder.tag_img = (ImageView) itemGroup.findViewById(R.id.tag_img);
			//image itinerary
			groupHolder.itineraryimage = (ImageView) itemGroup.findViewById(R.id.itineraryimage);
			groupHolder.itineraryimage.setTag(mGroupCollection.get(arg0));
			groupHolder.itinerarycode = (TextView) itemGroup.findViewById(R.id.itinerarycode);
			groupHolder.itineraryname = (TextView) itemGroup.findViewById(R.id.itineraryname);
			groupHolder.typeimage = (ImageView) itemGroup.findViewById(R.id.typeimage);
			groupHolder.pmrimage = (ImageView) itemGroup.findViewById(R.id.pmrimage);
			// Show transfer is selected
			groupHolder.imgtransfer = new ArrayList<ImageView>();
			String _image = "imgtransfer";
			ImageView _myImg;
			for (int i = 0; i < MAX_TRANSFER; i++){
				_image = "imgtransfer" + (i+1);
				try {
					_myImg = (ImageView) itemGroup.findViewById(Config.getResId(_image));
					_myImg.setVisibility(View.VISIBLE);
					groupHolder.imgtransfer.add( _myImg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e(Config.LOGTAG, "ExpandableListAdapterItinerary.getChildView.gettingImageTransfer() ");
				}
			}

			groupHolder.check_favorite_itinerary = (CheckBox) itemGroup.findViewById(R.id.check_favorite);
			groupHolder.check_favorite_itinerary.setTag(mGroupCollection.get(arg0));
			groupHolder.check_favorite_itinerary.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Itinerary it = (Itinerary) v.getTag();
					
					if (((CheckBox) v).isChecked()) { 
						  //checked
						  savingDataItineraryToPublicTransport(it, true);
						  if (Config.DEBUG) Toast.makeText( mContext ,
								  mContext.getResources().getString(R.string.checked), Toast.LENGTH_LONG).show();
					} else {
						  //not checked
						  savingDataItineraryToPublicTransport(it, false);
						  if (Config.DEBUG) Toast.makeText( mContext ,
								  mContext.getResources().getString(R.string.checked), Toast.LENGTH_LONG).show();						
					} 
				}
			});
			itemGroup.setTag(groupHolder);	
		}else {
	    	itemGroup = (View) arg2;	    	
			groupHolder = (GroupHolder) itemGroup.getTag();		
			groupHolder.check_favorite_itinerary.setTag(mGroupCollection.get(arg0));
			groupHolder.itineraryimage.setTag(mGroupCollection.get(arg0));
		}
		//Filling data...
		groupHolder.itineraryimage.setImageResource(Image.getImageItinerary(mContext, mGroupCollection.get(arg0)));
		groupHolder.itinerarycode.setText(mGroupCollection.get(arg0).getCode());
		groupHolder.itineraryname.setText(mGroupCollection.get(arg0).getName());
		groupHolder.check_favorite_itinerary.setChecked(mGroupCollection.get(arg0).isFavourite());
		groupHolder.typeimage.setImageResource(Image.getImageTransport(mContext, 
				TypeTransport.getTypeTransport(mGroupCollection.get(arg0).getTypeOfTransport())));
		// Disable facilities
		if (mGroupCollection.get(arg0).isDisabledFacilities()){
			groupHolder.pmrimage.setVisibility(View.VISIBLE);			
		} else {
			groupHolder.pmrimage.setVisibility(View.GONE);						
		}
		// Load image of transfer
		if (myPublic.getmPreferences().isOptionShowTransfer()){
			int min = (MAX_TRANSFER < mGroupCollection.get(arg0).getListTransfer().size())? 
					MAX_TRANSFER : mGroupCollection.get(arg0).getListTransfer().size();
			for (int i = 0; i < min; i++){
				groupHolder.imgtransfer.get(i).setImageResource(Image.getImageLine(mContext, 
						mGroupCollection.get(arg0).getListTransfer().get(i)));
			}
			for (int j = min; j < MAX_TRANSFER; j++){
				groupHolder.imgtransfer.get(j).setImageResource(R.drawable.emtblackpqt);
			}
			// show only what you need
			RelativeLayout c1;
			if (min > MAX_TRANSFER*2/3){
				c1 = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup3);
				c1.setVisibility(View.VISIBLE);
				c1 = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup2);
				c1.setVisibility(View.VISIBLE);
				c1 = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup);
				c1.setVisibility(View.VISIBLE);																									
			} else if (min <= MAX_TRANSFER*2/3){
				c1 = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup3);
				c1.setVisibility(View.GONE);
				if (min <= MAX_TRANSFER*1/3){
					c1 = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup2);
					c1.setVisibility(View.GONE);
					if (min == 0){
						c1 = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup);
						c1.setVisibility(View.GONE);															
					} else {
						c1 = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup);
						c1.setVisibility(View.VISIBLE);																					
					}
				} else {
					c1 = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup2);
					c1.setVisibility(View.VISIBLE);
					c1 = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup);
					c1.setVisibility(View.VISIBLE);																														
				}
			}			
		} else {
	        // Hide Transfer of Itinerary to make more clear
			c = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup);
			c.setVisibility(View.GONE);
			c = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup2);
			c.setVisibility(View.GONE);
			c = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup3);
			c.setVisibility(View.GONE);			
		}
		// Allways if group colapsed
		if (myPublic.getmPreferences().isOptionShowTransfer() && groupStatus[arg0] == 1){
	        // Show Transfer of Itinerary to make more clear
			c = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup);
			c.setVisibility(View.GONE);
			c = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup2);
			c.setVisibility(View.GONE);
			c = (RelativeLayout) itemGroup.findViewById(R.id.imgtransfergroup3);
			c.setVisibility(View.GONE);
		}
		return itemGroup;
	}

	class GroupHolder {
		// container item group
		ImageView itineraryimage; //
		TextView itinerarycode;	//
		ImageView typeimage;	//
		ImageView pmrimage;	//
		ImageView tag_img;	//
		ArrayList<ImageView> imgtransfer;	//
		TextView itineraryname;	//
		CheckBox check_favorite_itinerary;	//
	}

	class ChildHolder {
		// container item child
		// RelativeLayout childItem;	// childitem
		ImageView lineimage;	// lineimage
		TextView linename1;	// linename1
		ImageView pmrimage_Line; // pmrimage
		CheckBox check_favorite_line;	// check_favorite_line
		TextView linename2;	// linename2
		ArrayList<ImageView> imgtransferline; // imgtransferline1 to imgtransferline45
		// RelativeLayout imgtransferchild imgtransferchild2 imgtransferchild3
	}

	private void savingDataLineToPublicTransport(Line line, boolean isFavorite) {
		// TODO Auto-generated method stub
    	//nothing to save, all info is in PublicTransport
     	if (myPublic.setIsFavoriteLine(line, isFavorite)){
     		if (isFavorite){
			  MoveOnCroutonStyle.croutonConfirm(activity, mContext.getResources().getString(R.string.checked) +
					  " " + mContext.getResources().getString(R.string.line) + " " + myPublic.getItineraryOfLine(line).getCode());
     		} else {
  			  MoveOnCroutonStyle.croutonInfo(activity, mContext.getResources().getString(R.string.unchecked) +
					  " " + mContext.getResources().getString(R.string.line) + " " + myPublic.getItineraryOfLine(line).getCode());    		     			
     		}
    	} else {
			  MoveOnCroutonStyle.croutonWarn(activity, mContext.getResources().getString(R.string.error_database));    		
    	}
    }
	
    private void savingDataItineraryToPublicTransport(Itinerary itinerary, boolean isFavorite){
    	//nothing to save, all info is in PublicTransport
     	if (myPublic.setIsFavoriteItinerary(itinerary, isFavorite)){
     		if (isFavorite){
			  MoveOnCroutonStyle.croutonConfirm(activity, mContext.getResources().getString(R.string.selected) +
					  mContext.getResources().getString(R.string.itinerary) + " " + itinerary.getCode());
     		} else {
  			  MoveOnCroutonStyle.croutonInfo(activity, mContext.getResources().getString(R.string.unselected) +
  					mContext.getResources().getString(R.string.itinerary) + " " + itinerary.getCode());    		    			
     		}
    	} else {
			  MoveOnCroutonStyle.croutonWarn(activity, mContext.getResources().getString(R.string.error_database));    		
    	}
    }

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean areAllItemsEnabled(){
		return true;
	}
	
    public void updateResults(ArrayList<Itinerary> newItineraries) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.updateResults(ArrayList<Itinerary> newItineraries)");    		 	 	 		
		mGroupCollection = newItineraries;        
        //Triggers the list update
        notifyDataSetChanged();
    }
	
}

