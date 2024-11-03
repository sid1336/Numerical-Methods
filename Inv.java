import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;


public class Inv
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  

  private JButton startButton, space;  // button objects
  private JTextField nDegree, matAField, matXField, tolField, iteratn;
  private JTextArea solution;
  private int N, I, J, K, NN, LP, NROW[]; 
  private boolean OK, inputCancel;
  private NumberFormat formatter;
  private double A[][], X[], Y[], B[];
  private double T, YMU, ERR, TOL, Q, S, AMAX;
  

  public void init() {
    //super("Inverse Power Method Algorithm");
 
    setVisualComponent();    
   
    }

  public void actionPerformed(ActionEvent e) {


    if (e.getSource() == startButton) {

      try {
                A = new double[10][10]; B = new double[10];
                X = new double[10]; Y = new double[10];
                NROW = new int[10];
                N = Integer.parseInt(nDegree.getText());
                NN = Integer.parseInt(iteratn.getText());
                TOL = Double.parseDouble(tolField.getText());

                OK = true; inputCancel = false;
                arrayInput(A, matAField.getText());
                toArray(matXField.getText(), X);
                solution.setText("");       

           if(inputCheck())
             {
            solution.setText("");       
            solution.append("The Inverse Method\n\n");

      /* STEP 1 */
      /* Q could be input instead of computed by deleting
         the next 7 steps */
      Q = 0.0;
      S = 0.0;
      for (I=1; I<=N; I++) {
         S = S + X[I-1] * X[I-1];
         for (J=1; J<=N; J++) Q = Q + A[I-1][J-1] * X[I-1] * X[J-1];
      }  
      Q = Q / S;
/*      solution.append("Q is "+ Q+"\n");
      solution.append("Input new Q? Enter Y or N.\n");
      scanf("\n%c", &AA);
      if ((AA == 'Y') || (AA == 'y')) {
         solution.append("input new Q\n");
         scanf("%lf", &Q);
      } 
*/

      solution.append( "Iteration\tEigenvalue\tEigenvector\n");
      /* STEP 2 */
      K = 1;
      for (I=1; I<=N; I++) A[I-1][I-1] = A[I-1][I-1] - Q;
      /* Call subroutine to compute multipliers M(I,J) and
         upper triangular matrix for matrix A using Gauss
         elimination with partial pivoting.
         NROW holds the ordering of rows for interchanges */
      MULTIP(N, OK, NROW, Q, A); 
      if (OK) {
         /* STEP 3 */
         LP = 1;
         for (I=2; I<=N; I++) 
            if (Math.abs(X[I-1]) > Math.abs(X[LP-1])) LP = I;
         /* STEP 4 */
         AMAX = X[LP-1];
         for (I=1; I<=N; I++) X[I-1] = X[I-1] / (AMAX);
         /* STEP 5 */
         while ((K <= NN) && OK) {
            /* STEPS 6 AND 7 */
            for (I=1; I<=N; I++) B[I-1] = X[I-1];
            /* Subroutine solve returns the solution of   
               ( A - Q * I )Y = B in Y */
            SOLVE(N, B, A, Y, NROW);
            /* STEP 8 */
            YMU = Y[LP-1];
            /* STEP 9 AND 10 */
            LP = 1;
            for (I=2; I<=N; I++) 
               if (Math.abs(Y[I-1]) > Math.abs(Y[LP-1])) LP = I;
            AMAX = Y[LP-1];
            ERR = 0.0;
            for (I=1; I<=N; I++) {
               T = Y[I-1] / AMAX;
               if (Math.abs(X[I-1] - T) > ERR) 
                  ERR = Math.abs(X[I-1] - T);
               X[I-1] = T;
            }  
            YMU = 1 / YMU + Q;
            /* STEP 11 */ 
            solution.append(""+K+"\t"+formatter.format(YMU)+"\t");
            for (I=1; I<=N; I++) solution.append( ""+formatter.format(X[I-1])+"\t");
            solution.append( "\n");
            if (ERR < TOL) {
               OK = false;
               solution.append( "\nEigenvalue = "+formatter.format(YMU));
               solution.append( " to tolerance = "+ TOL+"\n");
               solution.append( "obtained on iteration number "+ K+"\n\n");
               solution.append( "Unit eigenvector is :\n");
               for (I=1; I<=N; I++) solution.append( ""+formatter.format(X[I-1])+"    ");
               solution.append( "\n");
            }  
            else 
               /* STEP 12 */
               K++;
         }     
         if (K > NN) 
         solution.append("Method did not converge within "+NN+" iterations\n");
      
      }
    }
}


      catch (NumberFormatException ex)
        {
        solution.append("Error");
        }
      }
    }

//****************************************************************
   public String[] StringtoArray( String s, String sep ) {
       // convert a String s to an Array, the elements
       // are delimited by sep
       StringBuffer buf = new StringBuffer(s);
       int arraysize = 1;
       for ( int i = 0; i < buf.length(); i++ ) {
         if ( sep.indexOf(buf.charAt(i) ) != -1 )
         arraysize++;
        }
    String [] elements  = new String [arraysize];
    int y,z = 0;
    if ( buf.toString().indexOf(sep) != -1 ) {
      while (  buf.length() > 0 ) {
        if ( buf.toString().indexOf(sep) != -1 ) {
          y =  buf.toString().indexOf(sep);
          if ( y != buf.toString().lastIndexOf(sep) ) {
            elements[z] = buf.toString().substring(0, y ); z++;
            buf.delete(0, y + 1);
          }
          else if ( buf.toString().lastIndexOf(sep) == y ) {
            elements[z] = buf.toString().substring(0, buf.toString().indexOf(sep) );z++;
            buf.delete(0, buf.toString().indexOf(sep) + 1);
            elements[z] = buf.toString();z++;
            buf.delete(0, buf.length() );
          }
        }
      }
    }
    else {elements[0] = buf.toString(); }
    buf = null;
    return elements;
  }

//****************************************************************

  public void MULTIP(int N, boolean OK, int NROW[], double Q, double A[][])
/*  Procedure MULTIP determines the row ordering and multipliers
    for the matrix A - Q*I for use in Gaussian elimination with
    Partial Pivoting.    */
{
   int K,I,M,IMAX,J,IP,L1,L2,JJ,I1,J1,N1;

   for (I=1; I<=N; I++) NROW[I-1] = I;
   OK = true;
   I = 1;
   M = N - 1;
   while ((I <= M) && OK) {
      IMAX = I;
      J = I + 1;
      for (IP=J; IP<=N; IP++) {
         L1 = NROW[IMAX-1];
         L2 = NROW[IP-1];
         if (Math.abs(A[L2-1][I-1]) > Math.abs(A[L1-1][I-1])) IMAX = IP;
      }  
      if (Math.abs(A[NROW[IMAX-1]-1][I-1]) <= 0) {
         OK = false;
         solution.append("A - Q * I is singular, Q = "+Q+" is an eigenvalue\n");
      }
      else {
         JJ = NROW[I-1];
         NROW[I-1] = NROW[IMAX-1];
         NROW[IMAX-1] = JJ;
         I1 = NROW[I-1];
         for (JJ=J; JJ<=N; JJ++) {
            J1 = NROW[JJ-1];
            A[J1-1][I-1] = A[J1-1][I-1] / A[I1-1][I-1];
            for (K=J; K<=N; K++)
               A[J1-1][K-1] = A[J1-1][K-1] - A[J1-1][I-1] * A[I1-1][K-1];
         }  
      }  
      I++;
   }
   if (Math.abs(A[NROW[N-1]-1][N-1]) <= 0) {
      OK = false;
      solution.append("A - Q * I is singular, Q = "+Q+" is an eigenvalue\n");
   }  
}

  public void SOLVE(int N, double B[], double A[][], double Y[], int NROW[])
/*  Procedure SOLVE solves the linear system (A - Q*I) Y = X
    given a new vector X and the row ordering and multipliers form
    procedure MULTIP   */
{
   int M,I,J,I1,JJ,J1,N1,N2,L,K,KK;
   
   M = N - 1;
   for (I=1; I<=M; I++) {
      J = I + 1;
      I1 = NROW[I-1];
      for (JJ=J; JJ<=N; JJ++) {
         J1 = NROW[JJ-1];
         B[J1-1] = B[J1-1] - A[J1-1][I-1] * B[I1-1];
      }  
   }
   N1 = NROW[N-1];
   Y[N-1] = B[N1-1] / A[N1-1][N-1];
   L = N - 1;
   for (K=1; K<=L; K++) {
      J = L - K + 1;
      JJ = J + 1;
      N2 = NROW[J-1];
      Y[J-1] = B[N2-1];
      for (KK=JJ; KK<=N; KK++) Y[J-1] = Y[J-1] - A[N2-1][KK-1] * Y[KK-1];
      Y[J-1] = Y[J-1] / A[N2-1][J-1];
   }   
}
  

  public void arrayInput(double arr[][], String str){

     try{
 
      String[] charArray = StringtoArray(str,","); 
      if(N>0){
      if(N>(charArray.length/N)) {
//         solution.append("Dimension is greater than matrix entered\n");
         inputCancel = true; 
         }
       else{
      for(int i = 0; i< N; i++){
  
        for(int j=0;j < N; ++j)
             arr[i][j] = Double.parseDouble(charArray[N*i +j]);
       }
      }
     }
    }
   catch(NumberFormatException ex){ inputCancel= true; }       
  }

  public void toArray(String s, double arr[])
   {
    
    String[] charArray = StringtoArray(s,","); //.split(",\\s*"); 
    for(int i= 0; i< charArray.length; i++)
       {
         arr[i] = Double.parseDouble(charArray[i]);
       }
      
     }


  public boolean inputCheck()
   {
   
          if (N <= 0 ){
            solution.append("The Number of Equations N must be a positive integer. \n");
          OK = false;}
          if (NN <= 0 ){
            solution.append("Number of Iterations must be a positive integer. \n");
          OK = false;}

          if (TOL <= 0 ){
            solution.append("Tolerance must be a positive number. \n");
          OK = false;}


        if (inputCancel){
            solution.append("Dimension is greater than matrix entered\n");
            solution.append("Values must be entered for matrix. \n");
            inputCancel = false;
          OK = false;}
          return OK;

   }   
 


   public void setVisualComponent()
    {
    startButton = new JButton("Start");
    nDegree = new JTextField(2);
    tolField = new JTextField(2);
    iteratn = new JTextField(2);
    matAField = new JTextField(40);
    matXField = new JTextField(40);
    solution = new JTextArea(17,40);
    JScrollPane scrollPane = new JScrollPane(solution);   
    startButton.addActionListener(this);

    JPanel inputPanel = new JPanel(new GridLayout(3,6));

    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));

    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("Dimension n =:", JLabel.RIGHT));
    inputPanel.add(nDegree);
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));


    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("Iterations  =:", JLabel.RIGHT));
    inputPanel.add(iteratn);
    inputPanel.add(new JLabel("Tolerance  =:", JLabel.RIGHT));
    inputPanel.add(tolField);


    JPanel inputPanel2 = new JPanel(new GridLayout(3,2));

    inputPanel2.add(new JLabel("A(i,j) in row form. Separate elements with commas \",\" A =:", JLabel.RIGHT));
    inputPanel2.add(matAField); matAField.setText("-4,14,0,-5,13,0,-1,0,2");

    inputPanel2.add(new JLabel("X(i), initial approximation. Separate elements with commas \",\" X =:", JLabel.RIGHT));
    inputPanel2.add(matXField); matXField.setText("1,1,1");

    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));


    JPanel buttonPanel = new JPanel(new GridLayout(1,6));
    buttonPanel.add(startButton);
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    

    JPanel inputArea = new JPanel(new BorderLayout());
    inputArea.add(inputPanel, "North"); 
    inputArea.add(inputPanel2, "South");
     
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(inputArea, "North");
    mainPanel.add(scrollPane, "Center");
    mainPanel.add(buttonPanel, "South");

    getContentPane().add(mainPanel);
    
    formatter = NumberFormat.getNumberInstance();
    formatter.setMaximumFractionDigits(9);
    formatter.setMinimumFractionDigits(9);
  }


  }
    
   
