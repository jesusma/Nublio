package com.jesusma.nublio;

import java.util.Date;

/*
 * Permite definir cada uno de los 
 * elementos la lista lista de libros.
 */

public class Ebook {
	
	
    private String title;
    private Date date;
	
	public Ebook (){
		super();
		
	}
	
	public Ebook(String title, Date date) {
        super();
        
        this.title = title;
        this.date = date;
    }
 
    public Date getDate() {
        return date;
    }
 
    public void setDate(Date date2) {
        this.date = date2;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    

}
