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
package com.sanbo.moveonapp.widgets;


import com.sanbo.moveonapp.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CreditsDialog extends Dialog {

	Button web_button, code_button, close_button, agpl_button, apache_button;
	Context context;
	Dialog self;
	Bundle myInstanceState;


	public CreditsDialog(Context context) {
		super(context);
		this.context = context;
		self = this;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.myInstanceState = savedInstanceState;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_credits);
		
		web_button    = (Button)findViewById(R.id.credits_button_visit_web);
		code_button   = (Button)findViewById(R.id.credits_button_source_code);
        agpl_button   = (Button)findViewById(R.id.credits_button_agpl_license);
		apache_button = (Button)findViewById(R.id.credits_button_apache_license);
		close_button  = (Button)findViewById(R.id.credits_button_close);

		web_button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        open_url("https://github.com/jbonnins/MoveOnApp/wiki");
		        self.dismiss();
		    }
		});
		
		code_button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        open_url("https://github.com/jbonnins/MoveOnApp");
		        self.dismiss();
		    }
		});

		agpl_button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        open_url("https://www.gnu.org/licenses/agpl-3.0.en.html");
		        self.dismiss();
		    }
		});
		
		apache_button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        open_url("https://www.apache.org/licenses/LICENSE-2.0");
		        self.dismiss();
		    }
		});
		
		close_button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        self.dismiss();
		    }
		});

		Linkify.addLinks((TextView)findViewById(R.id.credits_author), Linkify.EMAIL_ADDRESSES);
		Linkify.addLinks((TextView)findViewById(R.id.credits_EMT), Linkify.WEB_URLS);
		Linkify.addLinks((TextView)findViewById(R.id.credits_CityBik), Linkify.WEB_URLS);

	}
	
	
	private void open_url(String url) {
		Intent open_url_activity = new Intent(Intent.ACTION_VIEW);
		open_url_activity.setData(Uri.parse(url));
		context.startActivity(open_url_activity);
	}

}

