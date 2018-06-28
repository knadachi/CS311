import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class represents a dynamic finite state automaton (FSA) that simulates  
 * a FSA that needs to be able to modify its language dynamically.
 */
public class DynamicFSA
{
   private int[] switchArr;
   private char[] symbolArr;
   private int[] nextArr;
   private int flag;
   private String output;
   private int size;
   private int threshold;

   /* constructor for DynamicFSA */
   public DynamicFSA()
   {
      switchArr = initialize( 54 );
      symbolArr = new char[ 100 ];
      nextArr = initialize( 100 );
      flag = 0;
      output = "";
      size = 0;
      threshold = (int)(100 * 0.75);
   }

   /* initializes array of given size with -1's */
   public int[] initialize( int capacity )
   {
      int[] arr = new int[capacity];
      for( int i = 0; i < capacity; i++ ){
         arr[i] = -1;
      }
      return arr;
   }

   /* reads Input1.txt and records the Java reserved words */
   public void readInput1( String fileName ) throws IOException
   { 
      BufferedReader br = new BufferedReader( new FileReader( fileName ) );
      String curLine = br.readLine();

      while( curLine != null )
      {
         String[] reservedWords = curLine.split( " " );
         for( int i = 0; i < reservedWords.length; i++ ){
            checkReserved( reservedWords[i] );
         }
         curLine = br.readLine();
      }
   }

   /* reads Input2.txt and records the indentifiers in the file */
   public void readInput2( String fileName ) throws IOException
   {
      BufferedReader br = new BufferedReader( new FileReader( fileName ) );
      String curLine = br.readLine();

      while( curLine != null )
      {
         curLine = curLine.replaceAll( "[^_$A-Za-z0-9]", " " );
         curLine = curLine.trim().replaceAll( " +", " " );
         String[] identifiers = curLine.split( " " );
         for( int i = 0; i < identifiers.length; i++ )
         {
            String temp = identifiers[i];
            if( !temp.equals( "" ) && !Character.isDigit(temp.charAt(0)) )
               output += check( temp ) + " ";
         }
         if( !identifiers[0].equals( "" ) )
            output += "\n";
         curLine = br.readLine();
      }
   }

   /* helper method used for reading the reserved words of Input1 */
   public boolean checkReserved( String str )
   {
      str += "*";
      char[] identifier = str.toCharArray();
      int nextSymbol = 0;
      int first = getIndex( identifier[nextSymbol++] );
      int ptr = switchArr[first];
      int count = 0;
      if( ptr < 0 )
      {
         size += (identifier.length - 1);
         if( size >= threshold )
            resize( 2 * symbolArr.length );
         return create( identifier, first, ptr, nextSymbol );
      }
      else
      {
         while( true )
         {
            if( symbolArr[ptr] == identifier[nextSymbol] )
            {
               if( identifier[nextSymbol] != '*' )
               {
                  ptr++;
                  nextSymbol++;
                  count++;
               }
               else
                  return true;
            }
            else if( nextArr[ptr] >= 0 )
            {
               ptr = nextArr[ptr];
               count++;
            }
            else
            {
               size += (identifier.length - count - 1);
               if( size >= threshold )
                  resize( 2 * symbolArr.length );
               return create( identifier, first, ptr, nextSymbol );
            }
         }
      }
   }

   /* helper method used for reading the identifiers of Input2 */
   public String check( String str )
   {
      String temp = str + "@";
      char[] identifier = temp.toCharArray();
      int nextSymbol = 0;
      int first = getIndex( identifier[nextSymbol++] );
      int ptr = switchArr[first];
      int count = 0;
      if( ptr < 0 )
      {
         size += (identifier.length - 1);
         if( size >= threshold )
            resize( 2 * symbolArr.length );
         str += "?";
         create( identifier, first, ptr, nextSymbol );
         return str;
      }
      else
      {
         while( true )
         {
            if( symbolArr[ptr] == identifier[nextSymbol] || (symbolArr[ptr] == '*' && identifier[nextSymbol] == '@') )
            {
               if( identifier[nextSymbol] != '@' )
               {
                  ptr++;
                  nextSymbol++;
                  count++;
               }
               else
               {
                  str += symbolArr[ptr];
                  return str;
               }
            }
            else if( nextArr[ptr] >= 0 )
            {
               ptr = nextArr[ptr];
               count++;
            }
            else
            {
               size += (identifier.length - count - 1);
               if( size >= threshold )
                  resize( 2 * symbolArr.length );
               str += "?";
               create( identifier, first, ptr, nextSymbol );
               return str;
            }
         }
      }
   }

   /**
    * adds the identifier to the FSA (considers if the identifier is partially in 
    * the table already or if it needs to add a completely new identifier
    */
   public boolean create( char[] identifier, int first, int ptr, int nextSymbol )
   {
      if( switchArr[first] < 0 )
      {
         switchArr[first] = flag;
         for( int i = 1; i < identifier.length; i++ ){
            symbolArr[flag++] = identifier[i];
         }
      }
      else
      {
         nextArr[ptr] = flag;
         for( int i = nextSymbol; i < identifier.length; i++ ){
            symbolArr[flag++] = identifier[i];
         }
      }
      return true;
   }

   /* returns the index of the symbol in switchArr */
   public int getIndex( char ch )
   {
      if( ch >= 65 && ch <= 90 )
         return ch - 65;
      else if( ch >= 97 && ch <= 122 )
         return ch - 71;
      else if( ch == 95 )
         return 52;
      else if( ch == 36 )
         return 53;
      return -1;
   }

   /**
    * resizes the symbolArr and nextArr if the current size reaches the 
    * threshold
    */
   public void resize( int newSize )
   {
      symbolArr = Arrays.copyOf( symbolArr, newSize );
      nextArr = Arrays.copyOf( nextArr, newSize );
      Arrays.fill( nextArr, flag, nextArr.length, -1 );
      threshold = (int)(newSize * 0.75);
   }

   /* prints the output where each identifier has its corresponding symbol */
   public void printOutput()
   {
      System.out.println( "------------" );
      System.out.println( "   Output" );
      System.out.println( "------------\n" );
      System.out.println( output );
   }

   /* prints the switchArr, symbolArr, and nextArr */
   public void print()
   {
      System.out.println( "-------------------------------" );
      System.out.println( "Switch, Symbol, and Next Arrays" );
      System.out.println( "-------------------------------\n" );

      //prints indices 0-19 of switchArr
      System.out.print( "       " );
      for( int i = 0; i < 20; i++ ){
         System.out.printf( "%6s", (char)(i+65) );
      }
      System.out.print( "\nswitch:" );
      for( int i = 0; i < 20; i++ ){
         System.out.printf( "%6d", switchArr[i] );
      }
      System.out.println( "\n" );

      //prints indices 20-39 of switchArr
      System.out.print( "       " );
      for( int i = 20; i < 40; i++ )
      {
         if( i < 26 )
            System.out.printf( "%6s", (char)(i+65) );
         else
            System.out.printf( "%6s", (char)(i+71) );
      }
      System.out.print( "\nswitch:" );
      for( int i = 20; i < 40; i++ ){
         System.out.printf( "%6d", switchArr[i] );
      }
      System.out.println( "\n" );

      //prints indices 40-51 of switchArr
      System.out.print( "       " );
      for( int i = 40; i < 54; i++ )
      {
         if( i < 52 )
            System.out.printf( "%6s", (char)(i+71) );
         else if( i == 52 )
            System.out.printf( "%6s", '_' );
         else
            System.out.printf( "%6s", '$' );
      }
      System.out.print( "\nswitch:" );
      for( int i = 40; i < 54; i++ ){
         System.out.printf( "%6d", switchArr[i] );
      }
      System.out.println( "\n" );

      //prints symbolArr and nextArr
      for( int i = 0; i < symbolArr.length/20; i++ )
      {
         System.out.print( "       " );
         for( int j = 0; j < 20; j++ ){
            System.out.printf( "%6d", (j + (i*20)) );
         }

         System.out.print( "\nsymbol:" );
         for( int j = 0; j < 20; j++ ){
            System.out.printf( "%6s", symbolArr[(j + (i*20))] );
         }
			
         System.out.print( "\nnext:  " );
         for( int j = 0; j < 20; j++ )
         {
            if( nextArr[(j + (i*20))] == -1 )
               System.out.printf( "%6s", " " );
            else
               System.out.printf( "%6d", nextArr[(j + (i*20))] );
         }
         System.out.println( "\n" );
      }
   }
}

