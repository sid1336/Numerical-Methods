
import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;

import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

public class LinearS
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  
  private JEP myParser;

  private JButton startButton, space;  // button objects
  private JTextField endPointA, endPointB, alphaCon, betaCon, integerN, exprField, exprField2, exprField3;
  private JTextArea solution;
  private double T, A, B, ALPHA, BETA, X, H, U1, U2, V1, V2, W1, W2, Z;
  private double K11, K12, K21, K22, K31, K32, K41, K42;
  private int I, N;
  private boolean OK;
  private NumberFormat formatter;
  private double xValue;
  private double U[][], V[][];

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
                 U = new double[2][25]; 
                 V = new double[2][25];

                 A = Double.parseDouble(endPointA.getText());
                 B = Double.parseDouble(endPointB.getText());
                 ALPHA = Double.parseDouble(alphaCon.getText());
                 BETA = Double.parseDouble(betaCon.getText());
                 N = Integer.parseInt(integerN.getText());

                OK  = true; 

            if(inputCheck()){
            solution.setText("");
            solution.append("Linear Shooting Method\n\n");             
            solution.append(" I\tX(I)\tW(1,I)\tW(2,I)\n");
      /* STEP 1 */
      H = (B - A) / N;
      U1 = ALPHA;
      U2 = 0.0;
      V1 = 0.0;
      V2 = 1.0;
      /* STEP 2 */
      for (I=1; I<=N; I++) {
         /* STEP 3 */
         X = A + (I - 1.0) * H;
         T = X + 0.5 * H;
         /* STEP 4 */ 
         K11 = H * U2;
         K12 = H * ( functnP( X ) * U2 + functnQ( X ) * U1 + functnR( X ) );
         K21 = H * ( U2 + 0.5 * K12 );
         K22 = H * ( functnP( T ) * ( U2 + 0.5 * K12 ) + functnQ( T ) *
                ( U1 + 0.5 * K11 ) + functnR( T ) );
         K31 = H * ( U2 + 0.5 * K22 );
         K32 = H * ( functnP( T ) * ( U2 + 0.5 * K22 ) + functnQ( T ) *
                ( U1 + 0.5 * K21 ) + functnR( T ) );
         T = X + H;
         K41 = H * ( U2 + K32 );
         K42 = H * ( functnP( T ) * ( U2 + K32 ) + functnQ(T) * ( U1 + K31 ) +
                functnR( T ) );
         U1 = U1 + ( K11 + 2.0 * ( K21 + K31 ) + K41 ) / 6.0;
         U2 = U2 + ( K12 + 2.0 * ( K22 + K32 ) + K42 ) / 6.0;
         K11 = H * V2;
         K12 = H * ( functnP( X ) * V2 + functnQ( X ) * V1 );
         T = X + 0.5 * H;
         K21 = H * ( V2 + 0.5 * K12 );
         K22 = H * ( functnP( T ) * ( V2 + 0.5 * K12 ) + functnQ( T ) *
                ( V1 + 0.5 * K11 ) );
         K31 = H * ( V2 + 0.5 * K22 );
         K32 = H * ( functnP( T ) * ( V2 + 0.5 * K22 ) + functnQ( T ) *
                ( V1 + 0.5 * K21 ) );
         T = X + H;
         K41 = H * ( V2 + K32 );
         K42 = H * ( functnP( T ) * ( V2 + K32 ) + functnQ(T) * ( V1 + K31 ));
         V1 = V1 + ( K11 + 2.0 * ( K21 + K31 ) + K41 ) / 6.0;
         V2 = V2 + ( K12 + 2.0 * ( K22 + K32 ) + K42 ) / 6.0;
         U[0][I-1] = U1;
         U[1][I-1] = U2;
         V[0][I-1] = V1;
         V[1][I-1] = V2;
      }  
      /* STEP 5 */
      W1 = ALPHA;
      Z = (BETA - U[0][N-1]) / V[0][N-1];
      X = A;
      I = 0;
      solution.append(" "+ I +"\t" +formatter.format(X)+"\t"+ formatter.format(W1)+"\t"+formatter.format(Z)+"\n");
      for (I=1; I<=N; I++) {
         X = A + I * H;
         W1 = U[0][I-1] + Z * V[0][I-1];
         W2 = U[1][I-1] + Z * V[1][I-1];
         solution.append(" "+ I +"\t" +formatter.format(X)+"\t"+ formatter.format(W1)+"\t"+formatter.format(W2)+"\n");
               }
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
          if (A >= B ){
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
    
   
