Nublio
======

#Aplicación Android que trata la cuenta de usuario de dropbox como una biblioteca de libros remota#


- Al iniciar la aplicación aparece una pantalla con un botón que al ser pulsado lleva a la página de login de Dropbox. Si el login es correcto la aplicación pedirá permiso para acceder a los archivos de la cuenta. Si ya estuviéramos logueados, al pulsar el botón se mostrará directamente la pantalla "Biblioteca"

- Después aparecerá la pantalla bilioteca con la lista de archivos .epub hay en la cuenta de Dropbox en la que estamos logueados. Cada linea posee un icono genérico por cada archivo acompañado del nombre del archivo y de la fecha de creación. 

- En la parte superior derecha se encuentra un botón de menú que al pulsarlo despliega dos opciones: ordenar por nombre o ordenar por fecha. Al pulsar cualquiera de ellos ordena los archivos de la biblioteca por nombre (si se pulsa ordnar por nombre) o por fecha (si se pulsa ordenar por fecha).

- Si se raliza una pulsación larga sobre alguno de los elementos de la biblioteca se mostrará la portada asociada a dicho elemento. En caso de que no haya portada asociada aparecerá un mensaje por pantalla informando de ello.


#Notas#

- Se han usado las siguientes librerias externas :

       - Sync Api de Dropbox --> https://www.dropbox.com/developers/sync
       - Epublib para el manejo de archivos .epub --> http://www.siegmann.nl/epublib/android
