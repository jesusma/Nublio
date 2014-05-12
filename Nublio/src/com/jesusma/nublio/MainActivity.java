package com.jesusma.nublio;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;

/*
 * Basado en el código de ejemplo helloDropbox 
 * de la SyncAPI de Dropbox
 * 
 */

public class MainActivity extends Activity {

	//Inicialización variables de autenticación específicas para Nublio
    private static final String appKey = "2s0d8d9owizdne4"; 
    private static final String appSecret = "kzhnaa85s2clrk8";

    private static final int REQUEST_LINK_TO_DBX = 0;

    private DbxAccountManager mDbxAcctMgr;
    private Button mLinkButton;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Autentificación con las credenciales de la app
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
        
        mLinkButton = (Button) findViewById(R.id.button1);
        mLinkButton.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	
            	 if(!mDbxAcctMgr.hasLinkedAccount()){
            		//Muestra la pantalla de login de dropbox
            		 onClickLinkToDropbox(); 
                 }else{
                	//si ya se ha establecido la conexión muestra directamente la lista de libros 
                	 showBooks();
                	 
                 } 
                
            }
            
        });
        
    }

    //Mostrar pantalla login Dropbox
    
    private void onClickLinkToDropbox() {
    	
        mDbxAcctMgr.startLink((Activity)this, REQUEST_LINK_TO_DBX);
     
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
        if (requestCode == REQUEST_LINK_TO_DBX) {
        	
            if (resultCode == Activity.RESULT_OK) {
            	
            	Toast.makeText(this, "Link to your Dropbox account OK", Toast.LENGTH_LONG).show();
            	Toast.makeText(this, "Reading you ebook library...", Toast.LENGTH_LONG).show();
            	//Si el login ha sido correcto mostramos lista de libros
            	showBooks();
             	
            } else {
            	
            	Toast.makeText(this, "Link to Dropbox failed or was cancelled.", Toast.LENGTH_LONG).show();
                
            }
            
        } else {
        	
            super.onActivityResult(requestCode, resultCode, data);
            
        }
    }

    // Inicia la actividad que muestra la lista de libros
    private void showBooks(){
    	
        Intent intent = new Intent(this, DxLibrary.class);
        startActivity(intent);
        
    } 
}