import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;


public class LdtF
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  

  private JButton startButton, space;  // button objects
  private JTextField nDegree, exprField;
  private JTextArea solution;
  private int N, I, J, K, NN, JJ, KK; 
  private boolean OK, inputCancel;
  private NumberFormat formatter;
  private double A[][], D[], S, V[];
  

  public void init() {
 
    setVisualComponent();    
   
    }

  public void actionPerformed(ActionEvent e) {


    if (e.getSource() == startButton) {

      try {
                A = new double[10][11]; D = new double[10];
                V = new double[10];
                N = Integer.parseInt(nDegree.getText());
               OK = true;
               inputCancel = false;
              arrayInput(A, exprField.getText());
              solution.setText(""); 

           if(inputCheck())
             {

            solution.setText("");       
     /* STEP 1 */
      for (I=1; I<=N; I++) { 
         /* STEP 2 */
         for (J=1; J<=I-1; J++) V[J-1] = A[I-1][J-1] * D[J-1];
         /* STEP 3 */
         D[I-1] = A[I-1][I-1];
         for (J=1; J<=I-1; J++) D[I-1] = D[I-1] - A[I-1][J-1] * V[J-1];
         /* STEP 4 */
         for (J=I+1; J<=N; J++) {
            for (K=1; K<=I-1; K++)
               A[J-1][I-1] = A[J-1][I-1] - A[J-1][K-1] * V[K-1];
            A[J-1][I-1] = A[J-1][I-1] / D[I-1];
         }  
      }  
      /* STEP 5 */
         outPut(N, D, A);

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

   public void outPut(int N, double D[], double A[][])
       {
        int I, J;

        solution.append("LDL^t Factorization\n\n");
        solution.append("The matrix L output by rows:\n");
        for (I=1; I<=N; I++) {
          for (J=1; J<=I-1; J++) solution.append(""+formatter.format(A[I-1][J-1])+"    ");
          solution.append("\n");
         }
     solution.append("\n\nThe diagonal of D:\n");
     for (I=1; I<=N; I++) solution.append(""+formatter.format(D[I-1])+"    ");

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
  
        for(int j=0;j<N; ++j)
             arr[i][j] = Double.parseDouble(charArray[N*i +j]);
       }
      }
    }
   }
   catch(NumberFormatException ex){ inputCancel= true; }       
  }



  public boolean inputCheck()
   {
        String errorInfo;
          if (N <= 0 ){
            solution.append("Order must be a positive integer. \n");
          OK = false;}
        if (inputCancel){
            solution.append("Dimension is greater than matrix entered\n");
            solution.append("Values must be entered for matrix. \n");
            inputCancel =false;
          OK = false;}
          return OK;

   }     



  public void setVisualComponent()
    {
    startButton = new JButton("Start");
    nDegree = new JTextField(2);
    exprField = new JTextField(40);
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
    inputPanel.add(new JLabel("Dimmension n =:", JLabel.RIGHT));
    inputPanel.add(nDegree);
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));

    JPanel inputPanel2 = new JPanel(new GridLayout(2,2));

    inputPanel2.add(new JLabel("Matrix in row form. Separate elements with commas \",\" A =:", JLabel.RIGHT));
    inputPanel2.add(exprField); exprField.setText("4,-1,1,-1,4.25,2.75,1,2.75,3.5");

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
    
   
