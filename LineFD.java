
import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;

import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

public class LineFD
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  
  private JEP myParser;

  private JButton startButton, space;  // button objects
  private JTextField endPointA, endPointB, alphaCon, betaCon, integerN, exprField, exprField2, exprField3;
  private JTextArea solution;
  private double  AA, BB, ALPHA, BETA, X, H;
  private double K11, K12, K21, K22, K31, K32, K41, K42;
  private int I, N, M, J;
  private boolean OK;
  private NumberFormat formatter;
  private double xValue;
  private double A[], B[], C[], D[], L[], U[], Z[], W[];

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
                 A = new double[24]; B = new double[24]; C = new double[24];
                 D = new double[24]; L = new double[24]; U = new double[24];
                 Z = new double[24]; W = new double[24];

                 AA = Double.parseDouble(endPointA.getText());
                 BB = Double.parseDouble(endPointB.getText());
                 ALPHA = Double.parseDouble(alphaCon.getText());
                 BETA = Double.parseDouble(betaCon.getText());
                 N = Integer.parseInt(integerN.getText());

                OK  = true; 

            if(inputCheck()){
            solution.setText("");
            solution.append("Linear Finite-Difference Algorithm\n\n");             
            solution.append(" I\tX(I)\tW(I)\n");
      /* STEP 1 */
      H = ( BB - AA ) / ( N + 1.0 );
      X = AA + H;
      A[0] = 2.0 + H * H * functnQ( X );
      B[0] = -1.0 + 0.5 * H * functnP( X );
      D[0] = -H*H*functnR(X)+(1.0+0.5*H*functnP(X))*ALPHA;
      M = N - 1;
      /* STEP 2 */
      for (I=2; I<=M; I++) {
         X = AA + I * H;
         A[I-1] = 2.0 + H * H * functnQ( X );
         B[I-1] = -1.0 + 0.5 * H * functnP( X );
         C[I-1] = -1.0 - 0.5 * H * functnP( X );
         D[I-1] = -H * H * functnR( X );
      }  
      /* STEP 3 */
      X = BB - H;
      A[N-1] = 2.0 + H * H * functnQ( X );
      C[N-1] = -1.0 - 0.5 * H * functnP( X );
      D[N-1] = -H*H*functnR(X)+(1.0-0.5*H*functnP(X))*BETA;
      /* STEP 4 */
      /* STEPS 4 through 8 solve a triagiagonal linear system using
         Algorithm 6.7 */
      L[0] = A[0];
      U[0] = B[0] / A[0];
      Z[0] = D[0] / L[0];
      /* STEP 5 */
      for (I=2; I<=M; I++) {
         L[I-1] = A[I-1] - C[I-1] * U[I-2];
         U[I-1] = B[I-1] / L[I-1];
         Z[I-1] = (D[I-1]-C[I-1]*Z[I-2])/L[I-1];
      }
      /* STEP 6 */
      L[N-1] = A[N-1] - C[N-1] * U[N-2];
      Z[N-1] = (D[N-1]-C[N-1]*Z[N-2])/L[N-1];
      /* STEP 7 */
      W[N-1] = Z[N-1];
      /* STEP 8 */
      for (J=1; J<=M; J++) {
         I = N - J;
         W[I-1] = Z[I-1] - U[I-1] * W[I];
      }
      I = 0;
      /* STEP 9 */
         solution.append(" "+ I +"\t" +formatter.format(AA)+"\t"+ formatter.format(ALPHA)+"\n");
      for (I=1; I<=N; I++) {
         X = AA + I * H;
         solution.append(" "+ I +"\t" +formatter.format(X)+"\t"+ formatter.format(W[I-1])+"\n");

      }  
      I = N + 1;

         solution.append(" "+ I +"\t" +formatter.format(BB)+"\t"+ formatter.format(BETA)+"\n");
               }
          }
      catch (NumberFormatException ex)
        {
        solution.append("Error");
        }
      }
    }


     public void parseExp(String exp) {
		myParser.initSymTab(); // clear the contents of the symbol table
		myParser.addStandardConstants();
		myParser.addVariable("x", xValue);
                myParser.setImplicitMul(true);
                myParser.parseExpression(exp);
	}


  public double functnP(double point)
   {
    double p;
    xValue = point;
    parseExp(exprField.getText());
    p = myParser.getValue();  // example   g = sqrt(10.0 / (X + 4.0));
    return p;
   }
  
  public double functnQ(double point)
   {
    double q;
    xValue = point;
    parseExp(exprField2.getText());
    q = myParser.getValue();  // example   g = sqrt(10.0 / (X + 4.0));
    return q;
   }

  public double functnR(double point)
   {
    double r;
    xValue = point;
    parseExp(exprField3.getText());
    r = myParser.getValue();  // example   g = sqrt(10.0 / (X + 4.0));
    return r;
   }


  public boolean inputCheck()
   {
        String errorInfo;
          if (AA >= BB ){
            solution.append("Lower limit must be less than upper limit \n");
            OK = false;}

          if (N <=0 ){
            solution.append("N must be positive \n");
            OK = false;}
          parseExp(exprField.getText());
          if ((errorInfo = myParser.getErrorInfo()) != null){
            solution.append(errorInfo);
            OK =false;}
          parseExp(exprField2.getText());
          if ((errorInfo = myParser.getErrorInfo()) != null){
            solution.append(errorInfo);
            OK =false;}
          parseExp(exprField3.getText());
          if ((errorInfo = myParser.getErrorInfo()) != null){
            solution.append(errorInfo);
            OK =false;}

          return OK;
   }     

 
  public void setVisualComponent()
    {
    startButton = new JButton("Start");
    endPointA = new JTextField(4);
    endPointB = new JTextField(4);
    alphaCon = new JTextField(4);
    betaCon = new JTextField(4);
    integerN = new JTextField(2);
    exprField = new JTextField(20);
    exprField2 = new JTextField(20);
    exprField3 = new JTextField(20);
    solution = new JTextArea(17,40);
    JScrollPane scrollPane = new JScrollPane(solution);   
    startButton.addActionListener(this);

    JPanel inputPanel = new JPanel(new GridLayout(5,6));

    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));

    inputPanel.add(new JLabel("Function P(x) =:", JLabel.RIGHT));
    inputPanel.add(exprField); exprField.setText("-2/x");
    inputPanel.add(new JLabel("Function Q(x) =:", JLabel.RIGHT));
    inputPanel.add(exprField2); exprField2.setText("2/x^2");
    inputPanel.add(new JLabel("Function R(x) =:", JLabel.RIGHT));
    inputPanel.add(exprField3); exprField3.setText("sin(ln(x))/x^2");

    inputPanel.add(new JLabel(" A =:", JLabel.RIGHT));
    inputPanel.add(endPointA);
    inputPanel.add(new JLabel(" Alpha Y(A) =:", JLabel.RIGHT));
    inputPanel.add(alphaCon);
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));

    inputPanel.add(new JLabel(" B =:", JLabel.RIGHT));
    inputPanel.add(endPointB);
    inputPanel.add(new JLabel(" Beta Y(B) =:", JLabel.RIGHT));
    inputPanel.add(betaCon);
    inputPanel.add(new JLabel(" N =:", JLabel.RIGHT));
    inputPanel.add(integerN);

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
    
   
