
import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;

import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

public class HeatE
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  
  private JEP myParser;

  private JButton startButton, space;  // button objects
  private JTextField pointFX, maxFT, alphaCon, intN, intM, exprField;
  private JTextArea solution;
  private double W[], L[], U[], Z[], FT, FX, ALPHA, H, K, VV, T, X;
  private int N, M, M1, M2, N1, FLAG, I1, I, J;
  private boolean OK;
  private NumberFormat formatter;
  private double xValue;

  public void init() {
    myParser = new JEP();
    myParser.initFunTab(); // clear the contents of the function table
    myParser.addStandardFunctions();
    myParser.setTraverse(true);

   setVisualComponent();    
   
 }

  public void actionPerformed(ActionEvent e) {


    if (e.getSource() == startButton) {

      try {
                 W = new double[25] ; L = new double[25];
                 U = new double[25] ; Z = new double[25];
                 FX = Double.parseDouble(pointFX.getText());
                 FT = Double.parseDouble(maxFT.getText());
                 ALPHA = Double.parseDouble(alphaCon.getText());
                 M = Integer.parseInt(intM.getText());
                 N = Integer.parseInt(intN.getText());
                 OK  = true; 
            solution.setText("");             

            if(inputCheck()){
            solution.setText("");             
      M1 = M - 1;
      M2 = M - 2;
      N1 = N - 1;
      /* STEP 1 */
      H = FX / M;
      K = FT / N;
      VV = ALPHA * ALPHA * K / ( H * H );
      /* STEP 2 */
      for (I=1; I<=M1; I++) W[I-1] = functnF( I * H );
      /* STEP 3 */
      /* STEPS 3 through 11 solve a tridiagonal linear system
         using Algorithm 6.7 */
      L[0] = 1.0 + 2.0 * VV;
      U[0] = -VV / L[0];
      /* STEP 4 */
      for (I=2; I<=M2; I++) {
         L[I-1] = 1.0 + 2.0 * VV + VV * U[I-2];
         U[I-1] = -VV / L[I-1];
      }  
      /* STEP 5 */
      L[M1-1] = 1.0 + 2.0 * VV + VV * U[M2-1];
      /* STEP 6 */
      for (J=1; J<=N; J++) {
         /* STEP 7 */
         /* current t(j) */
         T = J * K;
         Z[0] = W[0] / L[0];
         /* STEP 8 */
         for (I=2; I<=M1; I++)
            Z[I-1] = ( W[I-1] + VV * Z[I-2] ) / L[I-1];
         /* STEP 9 */
         W[M1-1] = Z[M1-1];
         /* STEP 10 */
         for (I1=1; I1<=M2; I1++) {
            I = M2 - I1 + 1;
            W[I-1] = Z[I-1] - U[I-1] * W[I];
         }  
      }
      /* STEP 11 */
      OUTPUT();
               }
          }
      catch (NumberFormatException ex)
        {
        solution.append("Error");
        }
      }
    }


   public void parseExpression() {
		myParser.initSymTab(); // clear the contents of the symbol table
		myParser.addStandardConstants();
		myParser.addVariable("x", xValue);
                myParser.setImplicitMul(true);
		myParser.parseExpression(exprField.getText());
	}


  public double functnF(double point)
   {
    double f;
    xValue = point;  
    parseExpression();
    f = myParser.getValue();  // example   g = sqrt(10.0 / (X + 4.0));
    return f;
   }
  
  public boolean inputCheck()
   {
        String errorInfo;
          if (FX <= 0 ){
            solution.append("Righthand endpoint must be positive.\n");
            OK = false;}
          if (FT <= 0 ){
            solution.append("Maximum value of the time variable T must be positive.\n");
            OK = false;}

          if ((N <= 0 )||(M<=2)){
            solution.append("M and N must be positive and M must be greater than 2\n");
            OK = false;}

            parseExpression();
          if ((errorInfo = myParser.getErrorInfo()) != null){
            solution.append(errorInfo);
            OK =false;}
          return OK;
   }     

  public void OUTPUT()
   {
     solution.append("This is the Backward-Difference Method \n\n");             
     solution.append("I\tX(I)\tW(X(I),"+FT+")\n");             
     for(I=1; I<=M1; I++){
        X = I * H;
        solution.append(" "+I+"\t"+formatter.format(X)+"\t"+formatter.format(W[I-1])+"\n");             
       }
    }
 
  public void setVisualComponent()
    {
    startButton = new JButton("Start");
    pointFX = new JTextField(4);
    maxFT = new JTextField(4);
    intM = new JTextField(4);
    intN = new JTextField(4);
    alphaCon = new JTextField(4);
    exprField = new JTextField(20);
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

    inputPanel.add(new JLabel("Function f(x) =:", JLabel.RIGHT));
    inputPanel.add(exprField); exprField.setText("sin(pi*x)");
    inputPanel.add(new JLabel("endPoint X :", JLabel.RIGHT));
    inputPanel.add(pointFX);pointFX.setText("1");
    inputPanel.add(new JLabel("ALPHA =:", JLabel.RIGHT));
    inputPanel.add(alphaCon);alphaCon.setText("1");

    inputPanel.add(new JLabel("intervals M:", JLabel.RIGHT));
    inputPanel.add(intM);intM.setText("10");
    inputPanel.add(new JLabel("intervals N:", JLabel.RIGHT));
    inputPanel.add(intN);intN.setText("50");
    inputPanel.add(new JLabel("max time var T:", JLabel.RIGHT));
    inputPanel.add(maxFT);maxFT.setText("0.5");


    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));

    JPanel buttonPanel = new JPanel(new GridLayout(1,6));
    buttonPanel.add(startButton);
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(inputPanel, "North");
    mainPanel.add(scrollPane, "Center");
    mainPanel.add(buttonPanel, "South");

    getContentPane().add(mainPanel);
    
    formatter = NumberFormat.getNumberInstance();
    formatter.setMaximumFractionDigits(9);
    formatter.setMinimumFractionDigits(9);
  }


  }
    
   
