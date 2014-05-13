package com.jesusma.nublio;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.jesusma.nublio.EbookAdapter;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

/*
 * Muestra la lista de libros por pantalla
 * Permite ordenarlos por nombre o fecha
 * Mediante un click prolongado muestra la portada del libro seleccionado
 */

public class DxLibrary extends Activity {

	
	private static final String appKey = "2s0d8d9owizdne4"; 
    private static final String appSecret = "kzhnaa85s2clrk8";
    private static final String EXTENSION = ".EPUB";
    
    private DbxAccountManager mDbxAcctMgr;
    private DbxFileSystem dbxFs;
    private Map<Ebook, DbxPath> eBooks = new HashMap<Ebook, DbxPath>();
    private String path;
    private ImageView bookImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dxlibrary);
		ListView listView = (ListView) findViewById(R.id.listView1);
	    
		eBooks.clear(); //Borramos lista
        ebookList(DbxPath.ROOT); //Invocamos metodo que lista los libros
     
        EbookAdapter adapter = new EbookAdapter(this, new ArrayList<Ebook>(eBooks.keySet())); //Guardamos las lineas de cada libro
        listView.setAdapter(adapter); //Mostramos la lista compuesta por cada línea creada antes
      
        //Recogemos la pulsación prolongada un elemento del la lista
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long arg) {
            	try{
        			
        			mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
                    dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount()); 
                    
                }catch (Exception e){
                	
                    e.printStackTrace();
                    
                }
            	
			    
			    bookImage = (ImageView) view.findViewById(R.id.bookImage);
			    ListView listView1 = (ListView) findViewById(R.id.listView1);
		        // Guardamos la ruta del archivo para buscar el archivo del que quemos mostrar la portada
		        path = eBooks.get(listView1.getAdapter().getItem(position)).toString();
		        showCover(path); // Invocamos el método que muestra la portada
		        setContentView(R.layout.cover_ebook); //Lanzamos la portada
		        
		        return true;
			}  
           
        });
	}
	
	// Menú despegable
	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		
		getMenuInflater().inflate(R.menu.men, menu);
		return true;
		
	}
	
	//Captura el elemnto del menu despegable seleccionado
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	// Elige la opcíón dependiendo del item del menú seleccionado
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
	
	// Usa la libreria externa epublib para manejar archivos .epub
	public void showCover (String path) {
		  
		DbxFile eBookFile = null;
      
        WeakReference<ImageView> imageViewReference = new WeakReference<ImageView>(bookImage);
        try{
        	
            eBookFile = dbxFs.open(new DbxPath(path)); // guardamos el archivo correspondiente al path dado
            Book book = new EpubReader().readEpub(eBookFile.getReadStream()); 
            
            if (book.getCoverImage() != null){ //Comprobamos que el ebook tenga portada
                try {
                    // Si hay portada, la pintamos
                	Toast.makeText(this, "Portada del ebook seleccionado", Toast.LENGTH_LONG).show();
                    Bitmap result = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());//"Pintamos" la portada
                    ImageView imageView = imageViewReference.get();
                    imageView.setImageBitmap(result);//Lanza la imagen
                    
                    
                } catch (IOException e) {
                	
                    e.printStackTrace();
                    
                }
                
            }else{
            	// Si no hay portada mostramos mensaje por pantalla 
            	Toast.makeText(this, "El ebook seleccionado no tiene portada", Toast.LENGTH_LONG).show();
                // Y volvemos a la biblioteca
                Intent intent = new Intent(this, DxLibrary.class);
                startActivity(intent);
                    
               }
            
            }catch(Exception e){
            	
                 e.printStackTrace();
                 
            }
        
            finally {
            	
                eBookFile.close();
                
            }
        
      }
	
	}
	
