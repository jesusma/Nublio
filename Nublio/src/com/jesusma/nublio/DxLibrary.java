package com.jesusma.nublio;

import java.util.ArrayList;
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
import android.widget.ListView;

import com.jesusma.nublio.EbookAdapter;

/*
 * 
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
        
        EbookAdapter adapter = new EbookAdapter(this, new ArrayList<Ebook>(eBooks.keySet())); // Creamos cada línea de la vista
        listView.setAdapter(adapter); //Mostramos la lista compuesta por cada línea creada antes
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
            for(DbxFileInfo f: fileList){
                if(f.path.toString().toUpperCase().endsWith(EXTENSION)){
                    String title = f.path.toString().substring(f.path.toString().lastIndexOf("/") + 1, f.path.toString().length()-EXTENSION.length());
                    //Con cada epub creamos un objeto ebook y le asignamos su título y fecha creación
                    Ebook ebook = new Ebook(); 
                    ebook.setTitle(title);
                    Date date = f.modifiedTime;
                    ebook.setDate(date);
                    eBooks.put(ebook, f.path); //Guardamos cada ebook en el HashMap eBooks 
                if(f.isFolder){
                    ebookList(f.path);
                }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
  
	
	
    }
}