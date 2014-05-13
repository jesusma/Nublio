package com.jesusma.nublio;

import java.util.Comparator;
import java.util.Date;

/*
 * Permite definir cada uno de los 
 * elementos la lista lista de libros.
 */

public class Ebook implements Comparable<Ebook> {
	
	
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
	
    // Métodos para comparar partes de un objeto Ebook
    
    //Compara los atributis título de dos objetos Ebook
    @Override
    public int compareTo(Ebook ebook) {
        return this.getTitle().compareTo(ebook.getTitle());
    }

   
    
    public static Comparator<Ebook> OrderByDate = new Comparator<Ebook>() {
        //Compara el atributo fecha de dos Ebook dados
        @Override
        public int compare(Ebook ebook1, Ebook ebook2) {
            return ebook1.getDate().compareTo(ebook2.getDate());
        }

    };
 
}
