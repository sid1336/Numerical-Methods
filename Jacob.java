import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;


public class Jacob
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  

  private JButton startButton, space;  // button objects
  private JTextField nDegree, X1Field, matAField, tolField, iteratn;
  private JTextArea solution;
  private int N, I, J, K, NN; 
  private boolean OK, inputCancel;
  private NumberFormat formatter;
  private double A[][], X1[], X2[], S, ERR, TOL;
 
  public void init() {
  
    setVisualComponent();    
   
    }

  public void actionPerformed(ActionEvent e) {


    if (e.getSource() == startButton) {

      try {
                A = new double[10][11]; X1 = new double[10];
                X2 = new double[10];
                N = Integer.parseInt(nDegree.getText());
                NN = Integer.parseInt(iteratn.getText());
                TOL = Double.parseDouble(tolField.getText());
                OK = true; inputCancel = false;
                arrayInput(A, matAField.getText());
                toArray(X1Field.getText(), X1);
                solution.setText(""); 
 
           if(inputCheck())
             {
            solution.setText("");       
      /* STEP 1 */
      K = 1;
      OK = false;
      /* STEP 2 */
      while ((!OK) && (K <= NN)) {
         /* err is used to test accuracy - it measures the  
            infinity-norm */
         ERR = 0.0; 
         /* STEP 3 */
         for (I=1; I<=N; I++) {
            S = 0.0;
            for (J=1; J<=N; J++) S = S - A[I-1][J-1] * X1[J-1];
            S = (S + A[I-1][N]) / A[I-1][I-1];
            if (Math.abs(S) > ERR) ERR = Math.abs(S);
            /* use X2 for X */
            X2[I-1] = X1[I-1] + S;
         }
         /* STEP 4 */
         if (ERR <= TOL) OK = true;
         /* process is complete */
         else {
         /* STEP 5 */
         K++;
         /* STEP 6 */
         for(I=1; I<=N; I++) X1[I-1] = X2[I-1]; }
      }
      if (!OK) solution.append("Maximum Number of Iterations Exceeded.\n");
      /* STEP 7 */
      /* procedure completed unsuccessfully */
      else outPut(N, X2, K, TOL);
  
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

   public void outPut(int N, double X2[],int K,double TOL)
       {
        int I;
        solution.append("Jacobi Iterative Method for Linear Systems\n\n");
        solution.append("The solution vector is :\n");
        
        for (I=1; I<=N; I++) solution.append(""+formatter.format(X2[I-1])+"    ");
          solution.append("\nusing "+K+" iterations\n");
          solution.append("with Tolerance "+TOL+" in infinity-norm");
       
        }

  public void arrayInput(double arr[][], String str){

     try{
         int m = N+1;
      String[] charArray = StringtoArray(str,","); 
      if(N>0){
      if(N>(charArray.length/N)) {
 //        solution.append("Dimension is greater than matrix entered\n");
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
    X1Field = new JTextField(40);
    matAField = new JTextField(40);
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

    JPanel inputPanel2 = new JPanel(new GridLayout(3,2));

    inputPanel2.add(new JLabel("A(i,j) in row form. Separate elements with commas \",\" A =:", JLabel.RIGHT));
    inputPanel2.add(matAField); matAField.setText("10,-1,2,0,6,-1,11,-1,3,25,2,-1,10,-1,-11,0,3,-1,8,15");
    inputPanel2.add(new JLabel("Initial Approximation, X(i) 1 <= i <= n. =:", JLabel.RIGHT));
    inputPanel2.add(X1Field); X1Field.setText("0,0,0,0");

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
    
   
