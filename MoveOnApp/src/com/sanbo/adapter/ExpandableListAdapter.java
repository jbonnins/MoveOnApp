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
import com.sanbo.fragment.tabC.AppTabCSecondFragment;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.R;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;
import com.sanbo.enumerated.TypeTransport;

import android.annotation.SuppressLint;
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



@SuppressLint("InflateParams")
public class ExpandableListAdapter extends BaseExpandableListAdapter {
	public static final int INVALID_ROW_ID = -1;
	private static final int MAX_TRANSFER = 45;

	private Context mContext;
	private ExpandableListView mExpandableListView;
	private ArrayList<Itinerary> mGroupCollection;
	private int[] groupStatus;
	private SavingState savingState = SavingState.getInstance();
	private PublicTransport myPublic;
	
	public ExpandableListAdapter(Context pContext,
			ExpandableListView pExpandableListView,
			ArrayList<Itinerary> pGroupCollection) {
		// TODO Auto-generated constructor stub
		mContext = pContext;
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
                	if (groupPosition != savingState.getGroupItineary()) {
                        parent.collapseGroup(savingState.getGroupItineary());
                        parent.expandGroup(groupPosition);                		
                	} else {
                        parent.collapseGroup(groupPosition);
                	}
                } else {
                    parent.expandGroup(groupPosition);
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
				savingState.setGroupItineary(groupPosition);
				savingState.setChildLine(childPosition);
				savingState.setMyItinerary(mGroupCollection.get(groupPosition));
				savingState.setMyLine(mGroupCollection.get(groupPosition).getLines().get(childPosition));
	            savingState.getMyActivity().pushFragments(AppConstants.TAB_C, new AppTabCSecondFragment(),true,true);
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
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub

		ChildHolder childHolder;
		if (arg3 == null) {
			//arg3 = LayoutInflater.from(mContext).inflate(R.layout.list_group_item, null);
			arg3 = LayoutInflater.from(mContext).inflate(R.layout.group_itinerary_item, null);

			childHolder = new ChildHolder();
			
			//Get childrow.xml file elements and set values
			childHolder.lineimage = (ImageView) arg3.findViewById(R.id.lineimage);
			childHolder.linename1 = (TextView) arg3.findViewById(R.id.linename1);	
			childHolder.linename2 = (TextView) arg3.findViewById(R.id.linename2);	
			childHolder.groupItem = (RelativeLayout) arg3.findViewById(R.id.groupItem);
			childHolder.groupItem.setTag(getChild(arg0, arg1));
			childHolder.check_favorite_line = (CheckBox) arg3.findViewById(R.id.check_favorite_line);
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
			childHolder.imgtransferline = new ArrayList<ImageView>();
			for (int i=0; i < MAX_TRANSFER; i++){
				String _image = "imgtransferline" + (i+1);
				ImageView image = new ImageView(mContext);
				try{
					image = (ImageView) arg3.findViewById(Config.getResId(_image));
					childHolder.imgtransferline.add(image);
				}
				catch (Exception localException){
					// not controlled
					if (Config.DEBUG) Log.e(Config.LOGTAG, "ExpandableListAdapterItinerary.getChildView.gettingImageTransfer()");    		 	 	 		
				}
			}
			arg3.setTag(childHolder);
		}else {
			childHolder = (ChildHolder) arg3.getTag();
			childHolder.check_favorite_line.setTag(mGroupCollection.get(arg0).getLines().get(arg1));
		}

		childHolder.lineimage.setImageResource(Image.getImageItinerary(mContext, mGroupCollection.get(arg0)));
		childHolder.linename1.setText(mGroupCollection.get(arg0).getLines().get(arg1).getUNameOneWay());
		childHolder.linename2.setText(mGroupCollection.get(arg0).getLines().get(arg1).getUNameOneWay2());
		childHolder.check_favorite_line.setChecked(mGroupCollection.get(arg0).getLines().get(arg1).isFavorite());
		int min = (MAX_TRANSFER < mGroupCollection.get(arg0).getListTransfer().size())? 
				MAX_TRANSFER : mGroupCollection.get(arg0).getListTransfer().size();
		for (int i = 0; i < min; i++){
			childHolder.imgtransferline.get(i).setImageResource(Image.getImageLine(mContext, 
					mGroupCollection.get(arg0).getListTransfer().get(i)));
					childHolder.imgtransferline.get(i).setVisibility(View.VISIBLE);
		}
		for (int i = min; i < MAX_TRANSFER; i++){
			childHolder.imgtransferline.get(i).setVisibility(View.GONE);
		}
		return arg3;
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
		if (arg2 == null) {
			arg2 = LayoutInflater.from(mContext).inflate(R.layout.group_itinerary, null);
			groupHolder = new GroupHolder();
			//group indicator
			groupHolder.tag_img = (ImageView) arg2.findViewById(R.id.tag_img);
			//image itinerary
			groupHolder.itineraryimage = (ImageView) arg2.findViewById(R.id.itineraryimage);
			groupHolder.itinerarycode = (TextView) arg2.findViewById(R.id.itinerarycode);
			groupHolder.itineraryname = (TextView) arg2.findViewById(R.id.itineraryname);
			groupHolder.typeimage = (ImageView) arg2.findViewById(R.id.typeimage);
			groupHolder.pmrimage = (ImageView) arg2.findViewById(R.id.pmrimage);
			groupHolder.imgtransfer = new ArrayList<ImageView>();
			String _image = "imgtransfer";
			ImageView _myImg;
			for (int i = 0; i < MAX_TRANSFER; i++){
				_image = "imgtransfer" + (i+1);
				try {
					_myImg = (ImageView) arg2.findViewById(Config.getResId(_image));
					_myImg.setVisibility(View.VISIBLE);
					groupHolder.imgtransfer.add( _myImg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			groupHolder.check_favorite = (CheckBox) arg2.findViewById(R.id.check_favorite);
			groupHolder.check_favorite.setTag(getGroup(arg0));
			groupHolder.check_favorite.setOnClickListener(new OnClickListener() {

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
			arg2.setTag(groupHolder);					
		}else {
			groupHolder = (GroupHolder) arg2.getTag();		
			groupHolder.check_favorite.setTag(mGroupCollection.get(arg0));
		}
		//Filling data...
		groupHolder.itineraryimage.setImageResource(Image.getImageItinerary(mContext, mGroupCollection.get(arg0)));
		groupHolder.itinerarycode.setText(mGroupCollection.get(arg0).getCode());
		groupHolder.itineraryname.setText(mGroupCollection.get(arg0).getName());
		groupHolder.check_favorite.setChecked(mGroupCollection.get(arg0).isFavourite());
		groupHolder.typeimage.setImageResource(Image.getImageTransport(mContext, 
				TypeTransport.getTypeTransport(mGroupCollection.get(arg0).getTypeOfTransport())));
		if (mGroupCollection.get(arg0).isDisabledFacilities()){
			groupHolder.pmrimage.setVisibility(View.VISIBLE);			
		} else {
			groupHolder.pmrimage.setVisibility(View.GONE);						
		}
		//Transfer Itineraries from others type of transport
		int min = (MAX_TRANSFER < mGroupCollection.get(arg0).getListTransfer().size())? 
				MAX_TRANSFER : mGroupCollection.get(arg0).getListTransfer().size();
		for (int i = 0; i < min; i++){
			groupHolder.imgtransfer.get(i).setImageResource(Image.getImageLine(mContext, 
					mGroupCollection.get(arg0).getListTransfer().get(i)));
			groupHolder.imgtransfer.get(i).setVisibility(View.VISIBLE);
		}
		for (int i = min; i < MAX_TRANSFER; i++){
			groupHolder.imgtransfer.get(i).setVisibility(View.GONE);
		}
		return arg2;
	}

	class GroupHolder {
		// container item group
		ImageView itineraryimage;
		TextView itinerarycode;
		ImageView typeimage;
		ImageView pmrimage;
		ImageView tag_img;
		ArrayList<ImageView> imgtransfer;
		TextView itineraryname;
		CheckBox check_favorite;
	}

	class ChildHolder {
		// container item child
		RelativeLayout groupItem;
		ImageView lineimage;
		TextView linename1;
		CheckBox check_favorite_line;
		TextView linename2;
		ArrayList<ImageView> imgtransferline;
	}

	private void savingDataLineToPublicTransport(Line line, boolean isFavorite) {
		// TODO Auto-generated method stub
    	//nothing to save, all info is in PublicTransport
    	myPublic.setIsFavoriteLine(line, isFavorite);
	}
	
    private void savingDataItineraryToPublicTransport(Itinerary itinerary, boolean isFavorite){
    	//nothing to save, all info is in PublicTransport
    	myPublic.setIsFavoriteItinerary(itinerary, isFavorite);
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

