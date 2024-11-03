import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;


public class Hse
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  

  private JButton startButton, space;  // button objects
  private JTextField nDegree, matAField;
  private JTextArea solution;
  private int N, I, J, K, KK, L; 
  private boolean OK, inputCancel;
  private NumberFormat formatter;
  private double A[][], V[], U[], Z[];
  private double Q, S, RSQ, PROD;
  

  public void init() {

    setVisualComponent();    
   
    }

  public void actionPerformed(ActionEvent e) {


    if (e.getSource() == startButton) {

      try {
                A = new double[10][10]; U = new double[10];
                V = new double[10]; Z = new double[10];

                N = Integer.parseInt(nDegree.getText());
                OK = true; inputCancel = false;
                arrayInput(A, matAField.getText());
                solution.setText("");       

           if(inputCheck())
             {
            solution.setText("");       
      /* STEP 1 */
      for (K=1; K<=N-2; K++) {
         Q = 0.0;
         KK = K + 1;
         /* STEP 2 */
         for (I=KK; I<=N; I++) Q = Q + A[I-1][K-1] * A[I-1][K-1];
         /* STEP 3 */
         /* S is used in place of alpha.  */
         if (Math.abs(A[K][K-1]) <= 0) 
            S = Math.sqrt(Q);
         else 
            S = A[K][K-1] / Math.abs(A[K][K-1]) * Math.sqrt(Q);
         /* STEP 4 */
         RSQ = (S + A[K][K-1]) * S;
         /* STEP 5 */
         V[K-1] = 0.0;
         V[K] = A[K][K-1]+S;
         for (J=K+2; J<=N; J++) V[J-1] = A[J-1][K-1];
         /* STEP 6 */
         for (J=K; J<=N; J++) {
            U[J-1] = 0.0;
            for (I=KK; I<=N; I++) U[J-1] = U[J-1] + A[J-1][I-1]*V[I-1];
            U[J-1] = U[J-1] / RSQ;
         }  
         /* STEP 7 */
         PROD = 0.0;
         for (I=K+1; I<=N; I++) PROD = PROD + V[I-1]*U[I-1];
         /* STEP 8 */
         for (J=K; J<=N; J++) Z[J-1] = U[J-1] - 0.5*PROD*V[J-1]/RSQ;
         /* STEP 9 */
         for (L=K+1; L<=N-1; L++) {
            /* STEP 10 */
            for (J=L+1; J<=N; J++) {
               A[J-1][L-1] = A[J-1][L-1]-V[L-1]*Z[J-1]-V[J-1]*Z[L-1];
               A[L-1][J-1] = A[J-1][L-1];
            }  
            /* STEP 11 */
            A[L-1][L-1] = A[L-1][L-1] - 2.0*V[L-1]*Z[L-1];
         }  
         /* STEP 12 */
         A[N-1][N-1] = A[N-1][N-1]-2.0*V[N-1]*Z[N-1];
         /* STEP 13 */
         for (J=K+2; J<=N; J++) {
            A[K-1][J-1] = 0.0;
            A[J-1][K-1] = 0.0;
         }  
         /* STEP 14 */
         A[K][K-1] = A[K][K-1]-V[K]*Z[K-1];
         A[K-1][K] = A[K][K-1];
      }  
      /* STEP 15 */
      outPut(N, A);
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

  public void outPut(int N, double A[][])
       {
        solution.append("Householder Method\n\n");
        solution.append("The Similar tridiagonal matrix follows - output by rows\n\n");
        for(I = 1; I<=N; I++){
           for(J = 1; J<=N; J++) solution.append(" "+formatter.format(A[I-1][J-1])+"    ");
           solution.append("\n");
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
            solution.append("Dimension must be a positive integer. \n");
          OK=false;}
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
    matAField = new JTextField(40);
    solution = new JTextArea(17,40);
    JScrollPane scrollPane = new JScrollPane(solution);   
    startButton.addActionListener(this);

    JPanel inputPanel = new JPanel(new GridLayout(2,6));

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

    JPanel inputPanel2 = new JPanel(new GridLayout(2,2));

    inputPanel2.add(new JLabel("A(i,j) in row form. Separate elements with commas \",\" A =:", JLabel.RIGHT));
    inputPanel2.add(matAField); matAField.setText("-4,1, -2, 2, 1,2,0,1,-2,0,3,-2,2,1,-2,-1");
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
    
   
