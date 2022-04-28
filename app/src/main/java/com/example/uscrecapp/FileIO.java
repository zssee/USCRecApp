package com.example.uscrecapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner ;

public class FileIO{
    private static final String TAG = "FileIO";
    public void write(String name, AppCompatActivity app){
//        String [] lines =  {  "one" ,  "two" ,  "three" ,  "four" ,  "five" ,  "six" ,  "Seven" ,  "..."  };

//		/ ** ** WRITING Form 1 /
		FileWriter file;
        try  {

//            file =  new FileWriter( "name.txt" );
            Log.d(TAG, app.getFilesDir() + "/name.txt");
            file = new FileWriter(new File(app.getFilesDir().getPath(), "name.txt"));

            file.write(name + "\n" );
            file.close();
            Log.d(TAG, " wrote to name.txt");

        }  catch  ( Exception ex )  {
            System.out.println ( "Message of exception:"  + ex.getMessage());
        }
    }
    public static String read(AppCompatActivity app){

        // File you want to read
        File file = new File (app.getFilesDir().getPath(), "name.txt");
        Scanner s = null ;

        try  {
            // read the file contents
            System.out.println("... read the contents of the file ...");
            s =  new  Scanner(file);

            // Read the file line by line
            if  ( s.hasNextLine())  {
                String line = s.nextLine();  	// We keep the line on a String
                System.out.println(line);       // We print the line
                return line;
            }

        }  catch (Exception ex)  {
            System.out.println("Message:" + ex.getMessage());
        }  finally  {
            // Close the file whether the reading was successful or not
            try  {
                if ( s !=  null )
                    s .close ();
            }  catch  (Exception ex2)  {
                System.out.println( "Message 2:"  + ex2.getMessage());
            }
        }
        return "";
    }
}