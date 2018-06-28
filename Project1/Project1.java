import java.io.IOException;

/**
 * Name: Kristin Adachi
 * Course: CS311
 * Project: Universal Finite State Automaton
 * Due: 1/26/17
 *
 * Instructions to Compile:
 * (1) Open command line or terminal
 * (2) Navigate to the directory that contains the project files
 * (3) Enter "javac Project1.java" to compile
 * (4) Enter "java Project1" to run
 */
public class Project1
{
   public static void main( String[] args )
   {
      try
      {
         FSA fsa = new FSA();
         fsa.readFile( "input.txt" );
      }
      catch( IOException e )
      {
         System.out.println( "IO Exception occurred." );
      }
   }
}

