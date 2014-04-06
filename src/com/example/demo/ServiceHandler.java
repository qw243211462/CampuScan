package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class ServiceHandler {
	private static final String TAG_NAME = "name";
	private static final String TAG_LON = "gps_lon";
	private static final String TAG_LAT = "gps_lat";
	
	ArrayList<String> PList;
	 
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    static InputStream is= null;
    static JSONObject jObj=null;
    ProgressDialog pDialog;
    Activity A;
    JSONArray json;
 
    public ServiceHandler(Activity A) {
    	this.A=A;
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method, List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
             
            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
 
                httpResponse = httpClient.execute(httpPost);
 
            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
 
                httpResponse = httpClient.execute(httpGet);
 
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return response;
 
    }
    
    public ArrayList<String> getList(String url) throws JSONException{
    	GetList AT = new GetList();

    	AT.execute(url);
    	
    	//PList = new ArrayList<String>();
    	//int i;
    	Log.d("",""+AT.getStatus());
    	/*if((AT.getStatus().equals(AsyncTask.Status.FINISHED))){
    		if (pDialog.isShowing()) pDialog.dismiss();
    		Log.d("Status: ",AT.getStatus().toString());
	    	/*for (i = 0; i < json.length(); i++) {
	        	JSONObject c = json.getJSONObject(i);
	        	Log.i("Name of Object: "+i,c.getString(TAG_NAME));
	            PList.add(c.getString(TAG_NAME));
	            //LatList.add(c.getDouble(TAG_LAT));
	            //LonList.add(c.getDouble(TAG_LON));
	        }
    	}*/
    	return PList;
    }
    
    public class GetList extends AsyncTask<String, Void, JSONArray>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("GetList: ","onPreExecute()");
			/*pDialog = new ProgressDialog(A);
			pDialog.setMessage("Retreiving points...");
			pDialog.setCancelable(false);
			pDialog.show();*/
		}
					
		@Override
		protected JSONArray doInBackground(String... arg){
			//ServiceHandler sh = new ServiceHandler();
						 
			//Log.i("arg[0]",arg[0]);
			// Making a request to url and getting response
			Log.d("GetList: ","doInBackground()");
			String ptStr = makeServiceCall(arg[0], GET);
			 
			Log.d("Response: ", "> " + ptStr);
			 
			if (ptStr != null) {
			    try {
			    	json = new JSONArray(ptStr); 
			    	//Log.d("New json: ", json.toString());
			    } catch (JSONException e) {
			            e.printStackTrace();
			    }
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url"); 
				//PList.add("THIS IS GAY AS SHIT");
			}
			return json;
		}
					
		@Override
		protected void onPostExecute(JSONArray result){
			super.onPostExecute(result);
			Log.d("GetList: ","onPostExecute()");
			PList = new ArrayList<String>();
			JSONObject c;
			int i;
			for (i = 0; i < result.length(); i++) {
				try {
					c = result.getJSONObject(i);
					Log.i("Name of Object: "+i, c.getString(TAG_NAME));
					PList.add(c.getString(TAG_NAME));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
			//Log.d("New json: ", json.toString());
			// Dismiss the progress dialog
			//if (pDialog.isShowing())
			//    pDialog.dismiss();
			//for(String entry: PList){
			//	Log.i("GETPOINTS", entry);	
			//	}
							
			/* Adapters are updated here, so change this to fit whatever we use in UI
			stAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, FList);
			festAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			inputFest.setAdapter(festAdapter);
							*/
		}
	}
    
}