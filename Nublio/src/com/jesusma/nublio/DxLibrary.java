package com.jesusma.nublio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.jesusma.nublio.EbookAdapter;

/*
 * Activity que muestra la lista de libros por pantalla
 * y que permite ordenarlos por nombre o fecha
 */

public class DxLibrary extends Activity {

	
	private static final String appKey = "2s0d8d9owizdne4"; 
    private static final String appSecret = "kzhnaa85s2clrk8";
    private static final String EXTENSION = ".EPUB";
    
    private DbxAccountManager mDbxAcctMgr;
    private DbxFileSystem dbxFs;
    private Map<Ebook, DbxPath> eBooks = new HashMap<Ebook, DbxPath>();
  	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dxlibrary);
		ListView listView = (ListView) findViewById(R.id.listView1);
	    
		eBooks.clear(); //Borramos lista
        ebookList(DbxPath.ROOT); //Invocamos metodo que lista los libros
     
        EbookAdapter adapter = new EbookAdapter(this, new ArrayList<Ebook>(eBooks.keySet())); //Guardamos las lineas de cada libro
        listView.setAdapter(adapter); //Mostramos la lista compuesta por cada línea creada antes
		
	}
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		
		getMenuInflater().inflate(R.menu.men, menu);
		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	// Elegimos entre las dos opciones del menú
	case R.id.action_name:
		
        ListView listView = (ListView) findViewById(R.id.listView1);
	    eBooks.clear(); 
        ebookList(DbxPath.ROOT); 
        List<Ebook> ebooks = new ArrayList<Ebook>(eBooks.keySet());
		Collections.sort(ebooks); //Ordenamos por nombre 
        EbookAdapter adapter = new EbookAdapter(this, new ArrayList<Ebook> (ebooks));
        listView.setAdapter(adapter); 

	    break;
	
	case R.id.action_date:
		
		ListView listView1 = (ListView) findViewById(R.id.listView1);
	    eBooks.clear(); 
        ebookList(DbxPath.ROOT); 
        List<Ebook> ebooks1 = new ArrayList<Ebook>(eBooks.keySet());
        Collections.sort(ebooks1, Ebook.OrderByDate);//Ordenamos por fecha
       
        EbookAdapter adapter1 = new EbookAdapter(this, new ArrayList<Ebook> (ebooks1));
        listView1.setAdapter(adapter1);
     
        break;
     
     default:
        
        break;
        
	}
	
	return true;
	
	}
 
	// Busca en la cuenta de DX y lista todos los libros por su título y fecha de creación
	private void ebookList(DbxPath path){
		
		try{
			
			mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
            dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount()); // Guardamos el sistema de archivos de la cuenta de Dropbox
            
        }catch (Exception e){
        	
            e.printStackTrace();
        }
		
		try{
			
            List<DbxFileInfo> fileList = dbxFs.listFolder(path);

            //Leemos cada elemento de la lista y comprobamos si es un .epub y si es así sacamos su título y fecha de creación
            for(DbxFileInfo fInfo: fileList){
                if(fInfo.path.toString().toUpperCase().endsWith(EXTENSION)){
                    String title = fInfo.path.toString().substring(fInfo.path.toString().lastIndexOf("/") + 1, fInfo.path.toString().length()-EXTENSION.length());
                    //Con cada epub creamos un objeto ebook y le asignamos su título y fecha creación
                    Ebook ebook = new Ebook(); 
                    ebook.setTitle(title);
                    Date date = fInfo.modifiedTime;
                    ebook.setDate(date);
                    eBooks.put(ebook, fInfo.path); //Guardamos cada ebook en el HashMap eBooks 
                if(fInfo.isFolder){
                    ebookList(fInfo.path);
                }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
  
	}
	
}