import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/** 
 * This class represents a universal finite state automaton (fsa) that can simulate different 
 * machines and determine whether the machine accepts or rejects given strings.
 */
public class FSA
{
   final int DEAD_END = -1;
   String END_MARKER = "!";
   int curState;
   int numStates;
   String curSymbol;
   String tape;
   boolean[] finalStates;
   int[][] transitions;
   String[] alphabet;

   /* reads file, initializes data, and prints appropriate file information */
   public void readFile( String fileName ) throws IOException
   {
      BufferedReader br = new BufferedReader( new FileReader( fileName ) );
      String curLine = " ";
      int count = 1;

      while( curLine != null )
      {
         System.out.println( "--------------------------" );
         System.out.println( "Finite State Automaton #" + count );
         System.out.println( "--------------------------" );

         // sets the number of states
         numStates = Integer.parseInt( br.readLine() );
         System.out.println( "(1) number of states: " + numStates );

         // initializes finalStates array of size numStates and sets the indices of final states to true
         finalStates = new boolean[numStates];
         String[] tempFinalStates = br.readLine().split( " " );
         System.out.print( "(2) final states: " );
         for( int i = 0; i < tempFinalStates.length; i++ )
         {
            finalStates[ Integer.parseInt(tempFinalStates[i]) ] = true;
            System.out.print( tempFinalStates[i] + " " );
         }

         // initializes the alphabet array
         System.out.print( "\n(3) alphabet: " );
         alphabet = br.readLine().split( " " );
         String alph = "";
         for( int i = 0; i < alphabet.length; i++ ){
            alph += alphabet[i] + " ";
         }
         System.out.println( alph.trim().replace( " ", ", " ));

         // initializes the transitions table with appropriate transitions
         transitions = new int[numStates][alphabet.length];
         for( int i = 0; i < transitions.length; i++ )
         {
            for( int j = 0; j < transitions[0].length; j++ )
            {
               transitions[i][j] = DEAD_END;
            }
         }
         System.out.println( "(4) transitions:" );
         while(( curLine = br.readLine() ).contains( "(" ) )
         {
            String str = curLine.substring( 1, curLine.length()-1 );
            String[] temp = str.split( " " );
            System.out.println( "\t" + str );

            // first index = first in input line
            // second index = symbol's index in the alphabet array
            // value = next state
            transitions[ Integer.parseInt(temp[0]) ][ convertSymbol(temp[1]) ] = Integer.parseInt( temp[2] );
         }

         // prints the test cases and whether they were accepted or rejected
         System.out.println( "(5) strings:" );
         while( curLine != null && !curLine.equals( "..." ) )
         {
            System.out.println( "\t" + curLine + "\t" + test( curLine ) );
            curLine = br.readLine();
         }

         System.out.println();
         count++;
      }
   }

   /** 
    * implementation of the given fsa algorithm in instructions
    * determines if the input is accepted or rejected by the machine and returns the result
    */
   public String test( String input )
   {
      tape = input;
      curState = 0;
      while( true )
      {
         curSymbol = getNextSymbol();
         if( inAlphabet( curSymbol ) )
         {
            curState = getNextState( curState, curSymbol );
            if( curState == DEAD_END )
               return "Reject";
         }
         else
         {
            if( !curSymbol.equals( END_MARKER ) )
               return "Reject";
            else if( finalStates[curState] )
               return "Accept";
            else
               return "Reject";
         }
      }
   }

   /* returns the next symbol available in the input tape and returns END_MARKER if length is 0 */
   public String getNextSymbol()
   {
      if( tape.length() == 0 )
         return END_MARKER;
      int length = tape.length();
      String temp = tape.substring( 0, length - (length-1) );
      if( length > 0 )
          tape = tape.substring(1);
      return temp;
   }

   /* finds the next state in the machine with the provided current state and symbol */
   public int getNextState( int current, String symbol )
   {
      return transitions[current][convertSymbol( symbol )];
   }

   /* returns true if symbol is in the alphabet and false otherwise */
   public boolean inAlphabet( String symbol )
   {
      int index = convertSymbol( symbol );
      if( index == -1 )
         return false;
      else
         return true;
   }

   /* returns the symbol's index in the alphabet array and -1 if it doesn't exist in the alphabet */
   public int convertSymbol( String symbol )
   {
      for( int i = 0; i < alphabet.length; i++ )
      {
         if( alphabet[i].equals( symbol ) ){
            return i;
         }
      }
      return -1;
   }
}

