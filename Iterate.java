import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;


public class Iterate
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  

  private JButton startButton, space;  // button objects
  private JTextField nDegree, matAField, rndField, digitField, tolField, iteratn;
  private JTextArea solution;
  private int IS, FLAG, N, M, I, J, K, KK, NN, L, LL, D, I1, J1, RND, NROW[]; 
  private boolean OK, inputCancel;
  private NumberFormat formatter;
  private double A[][], B[][],X[], XX[], R[];
  private double S, C, COND, XXMAX, YMAX, ERR1, TOL, TEMP;
  

  public void init() {
 
    setVisualComponent();    
   
    }

  public void actionPerformed(ActionEvent e) {


    if (e.getSource() == startButton) {

      try {
                A = new double[10][11]; B = new double[10][11]; X = new double[10];
                XX = new double[10]; R = new double[10]; NROW = new int[10];
                N = Integer.parseInt(nDegree.getText());
                NN = Integer.parseInt(iteratn.getText());
                TOL = Double.parseDouble(tolField.getText());
                RND = Integer.parseInt(rndField.getText());
                D = Integer.parseInt(digitField.getText());

                OK = true;  inputCancel = false;
                arrayInput(A, matAField.getText());
                solution.setText("");       

           if(inputCheck())
             {
            solution.setText("");       
            solution.append("Iterative Refinement Method\n");
      M = N + 1;
       solution.append(  "Original system\n");
      for (I=1; I<=N; I++) {
	 for (J=1; J<=M; J++) 
	    solution.append( formatter.format(A[I-1][J-1])+"\t");
	    solution.append( "\n");
      }
      if (RND == 1)  solution.append( "Rounding to "+D+" Digits.\n");
      else  solution.append( "Chopping to "+D+" Digits.\n");
       solution.append( "\n Modified System \n");
      for (I=1; I<=N; I++) {
	 NROW[I-1] = I;
	 for (J=1; J<=M; J++) {
	    A[I-1][J-1] = CHIP(RND,D,A[I-1][J-1]);
	    B[I-1][J-1] = A[I-1][J-1];
	     solution.append( formatter.format(A[I-1][J-1])+"\t");
	 }
	  solution.append(  "\n");
      }  
      /* NROW and B have been initialized,
	 Gauss elimination will begin */
      /* STEP 0 */
      I = 1;
      while ((I <= N-1) && OK) {
	 KK = I;
	 while ((Math.abs(A[KK-1][I-1]) < 0) && (KK <= N))  
	    KK++;
	 if (KK > N) {
	    OK = false;
	     solution.append(  "System does not have a unique solution.\n");
	 }  
	 else { 
	    if (KK != I) {
	       /* Row interchange necessary */
	       IS = NROW[I-1];
	       NROW[I-1] = NROW[KK-1];
	       NROW[KK-1] = IS;
	       for (J=1; J<=M; J++) {
		  C = A[I-1][J-1];
		  A[I-1][J-1] = A[KK-1][J-1];
		  A[KK-1][J-1] = C;
	       }  
	    }  
	    for (J=I+1; J<=N; J++) {
	       A[J-1][I-1] = CHIP(RND,D,A[J-1][I-1]/A[I-1][I-1]);
	       for (L=I+1; L<=M; L++)
		  A[J-1][L-1] = CHIP(RND,D,A[J-1][L-1]-
			    CHIP(RND,D,A[J-1][I-1]*A[I-1][L-1]));
	    }
	 }
	 I++;
      }
      if ((Math.abs(A[N-1][N-1]) < 0) && OK) {
	  OK = false;
	   solution.append(  "System has singular matrix\n");
      }
      if (OK) {
	  solution.append(  "Reduced system\n");
	 for (I=1; I<=N; I++) {
	    for (J=1; J<=M; J++)  solution.append(formatter.format(A[I-1][J-1])+"\t"); 
	     solution.append(  "\n");
	 }
	 X[N-1] = CHIP(RND,D,A[N-1][M-1]/A[N-1][N-1]); 
	 for (I=1; I<=N-1; I++) {
	    J = N-I;
	    S = 0.0;
	    for (L=J+1; L<=N; L++) 
		S = CHIP(RND,D,S-CHIP(RND,D,A[J-1][L-1]*X[L-1]));
	    S = CHIP(RND,D,A[J-1][M-1]+S);
	    X[J-1] = CHIP(RND,D,S/A[J-1][J-1]);
	 }  
      }
       solution.append(  "Initial solution\n"); 
      for (I=1; I<=N; I++)  solution.append(formatter.format(X[I-1])+"\t");
       solution.append(  "\n"); 
      /* Refinement begins */
      /* STEP 1 */
      if (OK) {
	 K = 1;
	 for (I=1; I<=N; I++) XX[I-1] = X[I-1];
      }
      /* STEP 2 */
      while (OK && (K <= NN)) {
	 /* LL is set to 1 if the desired accuracy in any component
	    is not achieved.  Thus, LL is initially 0 for each
	    iteration. */
	 LL = 0;
	 /* STEP 3 */
	 for (I=1; I<=N; I++) {
	    R[I-1] = 0.0;
	    for (J=1; J<=N; J++) 
	       R[I-1] = CHIP(RND,2*D,R[I-1]-
			   CHIP(RND,2*D,B[I-1][J-1]*XX[J-1]));
	    R[I-1] = CHIP(RND,2*D,B[I-1][M-1]+R[I-1]);
	 }  
	  solution.append(  "Residual number "+ K+"  ");
	 for (I=1; I<=N; I++) { 
	    R[I-1] = CHIP(RND,D,R[I-1]);
	     solution.append(formatter.format(R[I-1])+"\t");
	 }
	  solution.append(  "\n");
	 /* STEP 4 */ 
	 /* Solve the linear system in the same order as in
	    step 0.  The solution will be placed in X instead
	    of in Y */ 
	 for (I=1; I<=N-1; I++) {
	    I1 = NROW[I-1];
	    for (J=I+1; J<=N; J++) {
	       J1 = NROW[J-1];
	       R[J1-1] = CHIP(RND,D,R[J1-1]-
			    CHIP(RND,D,A[J-1][I-1]*R[I1-1]));
	    }  
	 }  
	 X[N-1] = CHIP(RND,D,R[NROW[N-1]-1]/A[N-1][N-1]);
	 for (I=1; I<=N-1 ; I++) {
	    J = N-I;
	    S = 0.0;
	    for (L=J+1; L<=N; L++)
	       S = CHIP(RND,D,S-CHIP(RND,D,A[J-1][L-1]*X[L-1]));
	    S = CHIP(RND,D,S+R[NROW[J-1]-1]);
	    X[J-1] = CHIP(RND,D,S/A[J-1][J-1]);
	 }  
	  solution.append(  "Vector Y\n");
	 for (I=1; I<=N; I++)  solution.append(formatter.format(X[I-1])+"\t");
	  solution.append(  "\n");
	 /* Steps 5 and 6 */
	 XXMAX = 0.0;
	 YMAX = 0.0;
	 ERR1 = 0.0;
	 for (I=1; I<=N; I++) {
	    /* If not accurate set LL to 1 */
	    if (Math.abs(X[I-1]) > TOL) LL = 1; 
	    if (K == 1) {
	       if (Math.abs(X[I-1]) > YMAX) 
		  YMAX = Math.abs(X[I-1]);
	       if (Math.abs(XX[I-1]) > XXMAX)
		  XXMAX = Math.abs(XX[I-1]);
	    }
	    TEMP = XX[I-1];
	    XX[I-1] = CHIP(RND,D,XX[I-1]+X[I-1]);
	    TEMP = Math.abs(TEMP-XX[I-1]);
	    if (TEMP > ERR1) ERR1 = TEMP;
	 }  
	 if (ERR1 <= TOL) LL = 2;
	 if (K == 1) COND = YMAX/XXMAX*Math.exp(D*Math.log(10.0));
	  solution.append(  "New approximation\n");
	 for (I=1; I<=N; I++)  solution.append(formatter.format(XX[I-1])+"\t");
	  solution.append(  "\n");
	 /* STEP 7 */
	 if (LL == 0) {
	     solution.append(  "The above vector is the solution.\n");
	    OK = false;
	 }
	 else if (LL == 2) {
	     solution.append( "The above vector is the best possible\n");
	     solution.append( "with TOL = "+TOL+" \n");
	    OK = false;
	 }
	 else K++;
	 /* Step 8 is not used in this implementation */
      }  
      if (K > NN) 
	  solution.append(  "Maximum Number of Iterations Exceeded.\n");
       solution.append(  "Condition number is "+formatter.format(COND)+" \n");
      
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


  public void arrayInput(double arr[][], String str){

     try{
         int m = N+1;
      String[] charArray = StringtoArray(str,","); 
     if(N>0){
      if(N>(charArray.length/N)) {
//       solution.append("Dimension is greater than matrix entered\n");
         inputCancel = true; 
         }
       else{
      for(int i = 0; i< N; i++){
  
        for(int j=0;j<=N; ++j)
             arr[i][j] = Double.parseDouble(charArray[m*i +j]);
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

          if (D <= 0 ){
            solution.append("Number of Digits must be a positive integer. \n");
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

public double CHIP(int RND, int R, double X)
{
   /* The function chip rounds or chops the number x to r digits. */
   double TEMP1[], TN[];
   double ZZ[];
   TEMP1 = new double[5]; TN= new double[5];
   double SL, Z, Z1, Z2, Y, TEMP, TT;
   ZZ = new double[5];
   int NN, MM, J, I;
   boolean OK;

   if (Math.abs(X) < 0) return 0.0;
   else {
      I = 0;
      Y = X;
      OK = true;
      Z1 = Math.exp(R*Math.log(10.0));
      Z2 = Math.exp((R-1.0)*Math.log(10.0));
      if (Math.abs(X) >= Z1) {
	 while (OK) {
	    Y = Y/10.0;
	    I++;
	    if (Math.abs(Y) < Z1) OK = false;
	 }  
      }  
      else 
	 if (Math.abs(X) < Z2) {
	    while (OK) { 
	       Y = Y*10.0;
	       I--;
	       if (Math.abs(Y) >= Z2) OK = false;
	    }
	 }  
      SL = Math.exp(-R*Math.log(10.0));
      if (RND == 1) {
	 if (Y >= 0.0) {
	    TEMP = Y+0.5+0.1*SL;
	  }  
	  else {
	     TEMP = Y-0.5-0.1*SL;
	   }  
      }
      else {     
	 if (Y >= 0.0) {
	    TEMP = Y+0.1*SL;
	 }
	 else {
	    TEMP = Y-0.1*SL;
	 }
      }
      NN = R/4;
      MM = 4*NN;
      TT = TEMP;
      for (J=1; J<=NN; J++) {
	 TN[J] = Math.exp(MM*Math.log(10.0));
	 TEMP1[J] = TT/TN[J];
         if(TEMP1[J]>0)
	 ZZ[J] = Math.floor(TEMP1[J]);
         else ZZ[J] = Math.ceil(TEMP1[J]);
	 TT = TT - ZZ[J]*TN[J];
	 MM = MM - 4;
      }
      if(TT>0)
      ZZ[NN+1] = Math.floor(TT);
      else
      ZZ[NN+1] = Math.ceil(TT);
      TN[NN+1] = 1;
      Z = ZZ[1]*TN[1];
      for (J=2; J<=NN+1; J++)
	 Z = Z + ZZ[J]*TN[J];
      Z = Z*Math.exp(I*Math.log(10.0));
      return Z;
   }
}


  public void setVisualComponent()
    {
    startButton = new JButton("Start");
    nDegree = new JTextField(2);
    tolField = new JTextField(2);
    rndField = new JTextField(2);
    iteratn = new JTextField(2);
    digitField = new JTextField(2);
    matAField = new JTextField(40);
    solution = new JTextArea(17,40);
    JScrollPane scrollPane = new JScrollPane(solution);   
    startButton.addActionListener(this);

    JPanel inputPanel = new JPanel(new GridLayout(4,6));

    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));

    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("Equations n =:", JLabel.RIGHT));
    inputPanel.add(nDegree);
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));


    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("Iterations  =:", JLabel.RIGHT));
    inputPanel.add(iteratn);
    inputPanel.add(new JLabel("Tolerance  =:", JLabel.RIGHT));
    inputPanel.add(tolField);

    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("Round=1, Chop= 2:", JLabel.RIGHT));
    inputPanel.add(rndField);
    inputPanel.add(new JLabel("Digits <=8 :", JLabel.RIGHT));
    inputPanel.add(digitField);

    JPanel inputPanel2 = new JPanel(new GridLayout(2,2));

    inputPanel2.add(new JLabel("A(i,j) in row form. Separate elements with commas \",\" A =:", JLabel.RIGHT));
    inputPanel2.add(matAField); matAField.setText("3.333,15920,-10.333,15913,2.222,16.71,9.612,28.544,1.5611,5.1791,1.6852,8.4254");

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
    formatter.setMaximumFractionDigits(5);
    formatter.setMinimumFractionDigits(5);
  }

 
  }
    
   
