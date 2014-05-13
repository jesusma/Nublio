package com.jesusma.nublio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jesusma.nublio.Ebook;
 
/*
 * Adapter que formará cada línea de la lista de libros.
 */

public class EbookAdapter extends BaseAdapter {
 
	private ArrayList data;
	private Activity activity;
	private LayoutInflater inflater = null;
 
    public EbookAdapter(Activity activity, ArrayList data) {
    	
        this.activity = activity;
        this.data = data;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }
 
    @Override
    public int getCount() {
    	
        return this.data.size();
        
    }
 
    @Override
    public Object getItem(int position) {
    	
        return this.data.get(position);
        
    }
 
    @Override
    public long getItemId(int position) {
    	
        return position;
        
    }
 
    // Forma cada línea para mostrar en la lista
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
    	
        if(view == null){
        	
           view = inflater.inflate(R.layout.list_ebook, viewGroup, false);
           
        }

        TextView title = (TextView)view.findViewById(R.id.Title);
        TextView date = (TextView)view.findViewById(R.id.Date);
        ImageView image = (ImageView)view.findViewById(R.id.imageView1);
       
        Ebook ebook = (Ebook)data.get(position);
        title.setText(ebook.getTitle()); //Coge título del ebook y lo pone en la línea
        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        date.setText(d.format(ebook.getDate())); //Coge la fecha y la pone en la línea
        image.findViewById(R.id.imageView1);
        
        return view; //Devuelve la línea
        
    }
 
}