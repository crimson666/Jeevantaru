package com.example.stealth.log_ui;



import android.content.Context;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class green_req_doc extends  RecyclerView.Adapter<green_req_doc.NumberViewHolder>{

    private static final String TAG = green_req_doc.class.getSimpleName();
    ListView listView;
    ArrayAdapter<String> adapter;
    int ArraySize;
    int dist;
    String address ="http://asuratest1996.000webhostapp.com/jeevan/recycle2.php";
    InputStream inputStream=null;
    String line = null;
    String result=null;
    private double lati=22.647051;
    private double longi=88.431683;
    String[] data;
    String name;
    ArrayList<Double> distance=new ArrayList<Double>();
    ArrayList<String> Aname=new ArrayList<String>();
    ArrayList<String> Aphone=new ArrayList<String>();
    ArrayList<String> latitude=new ArrayList<String>();
    ArrayList<String> longitude=new ArrayList<String>();
    ArrayList<String> ASpecialist=new ArrayList<String>();
    ArrayList<String> AVisitinghours=new ArrayList<String>();
    ArrayList<String> AAddress=new ArrayList<String>();
    ArrayList<String> Auserid=new ArrayList<String>();
    final private ListItemClickListener mOnClickListener;

    private static int viewHolderCount;

    private int mNumberItems;
    public String Type;

    public interface ListItemClickListener
    {
        void onListItemClick(int clickedItemIndex);
    }


    public green_req_doc(int numberOfItems, ListItemClickListener listener)
    {

        mNumberItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_req_doc;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        dist=distance.get(viewHolderCount).intValue();
        viewHolder.name.setText("Name: " + Aname.get(viewHolderCount));
        viewHolder.visitinghours.setText("Specialist: " + ASpecialist.get(viewHolderCount) + "");
        viewHolder.Speciality.setText("Visiting Hours: " + AVisitinghours.get(viewHolderCount) + "");
        viewHolder.disleft.setText(dist+"Km");
        //viewHolder.Aphone.setText("Visiting Hours: " + Aphone.get(viewHolderCount) + "");


        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return viewHolder;


    }

    public int getdata(){

        try {
            doc_req_rec d1=new doc_req_rec();

            URL url = new URL(address+"?Type="+d1.an());
            HttpURLConnection httpURLConnection= (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder=new StringBuilder();
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line+"\n");


            }
            inputStream.close();
            result=stringBuilder.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }


        //parse jason

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = null;
            data = new String[jsonArray.length()];
            ArraySize = jsonArray.length();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Aname.add(jsonObject.getString("dname"));
                ASpecialist.add(jsonObject.getString("spec"));
                Auserid.add(jsonObject.getString("dlicense"));
                Aphone.add(jsonObject.getString("phone"));
                AAddress.add(jsonObject.getString("address"));
                latitude.add(jsonObject.getString("lat"));
                longitude.add(jsonObject.getString("lng"));
                AVisitinghours.add(jsonObject.getString("vhour"));




                //distance from source house

                int r = 6371; // average radius of the earth in km
                double dLat = Math.toRadians(lati - Double.parseDouble(latitude.get(i)));
                double dLon = Math.toRadians(longi - Double.parseDouble(longitude.get(i)));
                double aa = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lati)) * Math.cos(Math.toRadians( Double.parseDouble(latitude.get(i))))
                                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
                double cc = 2 * Math.atan2(Math.sqrt(aa), Math.sqrt(1 - aa));
                double d = r * cc;
                distance.add(d);



            }



            //sorting Algorithm
            int n = jsonArray.length();
            double temp;
            String alter="";
            for (int i = 0; i < n; i++) {
                for (int j = 1; j < (n - i); j++) {
                    if (distance.get(j - 1) > distance.get(j)) {
                        //swap elements
                        temp = distance.get(j - 1);
                        distance.set((j-1),distance.get(j)) ;
                        distance.set((j),temp) ;

                        alter = Aname.get(j-1 );
                        Aname.set((j-1),Aname.get(j)) ;
                        Aname.set((j),alter) ;

                        alter = ASpecialist.get(j-1);
                        ASpecialist.set((j-1),ASpecialist.get(j)) ;
                        ASpecialist.set((j),alter) ;

                        alter = Auserid.get(j-1 );
                        Auserid.set((j-1),Auserid.get(j)) ;
                        Auserid.set((j),alter) ;

                        alter = Aphone.get(j-1 );
                        Aphone.set((j-1),Aphone.get(j)) ;
                        Aphone.set((j),alter) ;

                        alter = AAddress.get(j-1 );
                        AAddress.set((j-1),AAddress.get(j)) ;
                        AAddress.set((j),alter) ;

                        alter = latitude.get(j-1 );
                        latitude.set((j-1),latitude.get(j)) ;
                        latitude.set((j),alter) ;

                        alter = longitude.get(j-1 );
                        longitude.set((j-1),Aname.get(j)) ;
                        longitude.set((j),alter) ;

                        alter = AVisitinghours.get(j-1 );
                        AVisitinghours.set((j-1),AVisitinghours.get(j)) ;
                        AVisitinghours.set((j),alter) ;
                    }

                }

            }



        }

        catch (Exception e){
            e.printStackTrace();
        }
        return ArraySize;
    }


    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
    }
    @Override
    public int getItemCount() {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        return getdata();
    }






    class NumberViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        TextView name,Speciality,visitinghours,disleft;

        public NumberViewHolder(View itemView) {
            super(itemView);
            //  asder();

            name = (TextView) itemView.findViewById(R.id.name);
            Speciality = (TextView) itemView.findViewById(R.id.Speciality);
            visitinghours=(TextView) itemView.findViewById(R.id.visitinghours);
            disleft=(TextView) itemView.findViewById(R.id.dis);

            // COMPLETED (7) Call setOnClickListener on the View passed into the constructor (use 'this' as the OnClickListener)
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }
}
