/**
 * Name: Kristin Adachi
 * Course: CS311
 * Project: Dynamic Finite State Automaton
 * Due: 2/21/17
 * 
 * Instructions to Compile:
 * (1) Open command line or terminal
 * (2) Navigate to the directory that contains the project files
 * (3) Replace Input1 and Input2 in this file if necessary
 * (4) Enter "javac Project2.java" to compile
 * (5) Enter "java Project2" to run
 */

import java.io.IOException;

public class Project2
{
   public static void main( String[] args )
   {
      try
      {
         DynamicFSA fsa = new DynamicFSA();
         fsa.readInput1( "Input1.txt" );
         fsa.readInput2( "Input2.txt" );
         fsa.printOutput();
         fsa.print();
      }
      catch( IOException e )
      {
         System.out.println( "IO Exception occurred." );
      }
   }
}

