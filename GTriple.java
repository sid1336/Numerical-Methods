
import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;

import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

public class GTriple
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  
  private JEP myParser;

  private JButton startButton, space;  // button objects
  private JTextField endPointA, endPointB, integerM, integerN, exprField, exprField2, exprField3;
  private JTextField exprField4, exprField5, integerP;
  private JTextArea solution;
  private double r[][], co[][]; 
  private double A, B, H1, H2, AJ, JX, D1, C1,K1, K2, JY, Z1, Z2, L1, L2, X, Y, Z, Q;
  private int I, J, M, N, K, P;
  private boolean OK;
  private NumberFormat formatter;
  private double xValue, yValue, zValue;

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
                 r = new double[5][5]; co = new double[5][5];
                 A = Double.parseDouble(endPointA.getText());
                 B = Double.parseDouble(endPointB.getText());
                 M = Integer.parseInt(integerM.getText());
                 N = Integer.parseInt(integerN.getText());
                 P = Integer.parseInt(integerP.getText());

                OK  = true; 
            solution.setText("");             

            if(inputCheck()){
	      r[1][0] = 0.5773502692; r[1][1] = -r[1][0]; co[1][0] = 1.0;
              co[1][1] = 1.0; r[2][0] = 0.7745966692; r[2][1] = 0.0;
	      r[2][2] = -r[2][0]; co[2][0] = 0.5555555556; co[2][1] = 0.8888888889;
	      co[2][2] = co[2][0]; r[3][0] = 0.8611363116; r[3][1] = 0.3399810436;
	      r[3][2] = -r[3][1]; r[3][3] = -r[3][0]; co[3][0] = 0.3478548451;
	      co[3][1] = 0.6521451549; co[3][2] = co[3][1]; co[3][3] = co[3][0];
	      r[4][0] = 0.9061798459; r[4][1] = 0.5384693101; r[4][2] = 0.0;
	      r[4][3] = -r[4][1]; r[4][4] = -r[4][0]; co[4][0] = 0.2369268850;
	      co[4][1] = 0.4786286705; co[4][2] = 0.5688888889; co[4][3] = co[4][1];
	      co[4][4] = co[4][0];
	      /* STEP 1 */ 
	      H1 = (B - A) / 2.0;       
	      H2 = (B + A) / 2.0;
	      AJ = 0.0;                         /* Use AJ instead of J. */ 
	      /* STEP 2 */ 
	      for (I=1; I<=M; I++) {    
		 /* STEP 3 */ 
		 X = H1 * r[M-1][I-1] + H2;     
		 JX = 0.0;
		 C1 = functnC(X);
		 D1 = functnD(X);
		 K1 = (D1 - C1) / 2.0;
		 K2 = (D1 + C1) / 2.0;
		 /* STEP 4 */ 
		 for (J=1; J<=N; J++) { 
		    /* STEP 5 */ 
		    Y = K1 * r[N-1][J-1] + K2;  
		    JY = 0.0;
		    /* use Z1 for Beta and Z2 for Alpha */
		    Z1 = functnBeta(X, Y);
		    Z2 = functnAlpha(X, Y); 
		    L1 = (Z1 - Z2) / 2.0;
		    L2 = (Z1 + Z2) / 2.0;
		    /* STEP 6 */ 
		    for (K=1; K<=P; K++) { 
		       Z = L1 * r[P-1][K-1] + L2;
		       Q = functnF(X, Y, Z);
		       JY = JY + co[P-1][K-1] * Q;
		    }
		    /* STEP 7 */ 
		    JX = JX + co[N-1][J-1] * L1 * JY;
		 }
		 /* STEP 8 */ 
		 AJ = AJ + co[M-1][I-1] * K1 * JX; 
	      }
	      /* STEP 9 */ 
	      AJ = AJ * H1; 
	      /* STEP 10 */ 
                 output();
             
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
		myParser.addVariable("y", yValue);
		myParser.addVariable("z", zValue);
                myParser.setImplicitMul(true);
                myParser.parseExpression(exp);
	}



  public double functnF(double point1, double point2, double point3)
   {
    double f;
    xValue = point1;
    yValue = point2;  
    zValue = point3;  
    parseExp(exprField.getText());
    f = myParser.getValue();  // example   g = sqrt(10.0 / (X + 4.0));
    return f;
   }
  
  public double functnC(double point)
   {
    double c;
    xValue = point;
    parseExp(exprField2.getText());
    c = myParser.getValue();  // example   g = sqrt(10.0 / (X + 4.0));
    return c;
   }

  public double functnD(double point)
   {
    double d;
    xValue = point;
    parseExp(exprField3.getText());
    d = myParser.getValue();  // example   g = sqrt(10.0 / (X + 4.0));
    return d;
   }

  public double functnAlpha(double point1, double point2)
   {
    double al;
    xValue = point1;
    yValue = point2;  
    parseExp(exprField4.getText());
    al = myParser.getValue();  // example   g = sqrt(10.0 / (X + 4.0));
    return al;
   }
  public double functnBeta(double point1, double point2)
   {
    double be;
    xValue = point1;
    yValue = point2;  
    parseExp(exprField5.getText());
    be = myParser.getValue();  // example   g = sqrt(10.0 / (X + 4.0));
    return be;
   }

 public void output()
   {
      solution.append("\n\nThe integral of F from alpha(x,y) to beta(x,y), c(x) to d(x) and "+A+" to "+B+" is "+formatter.format(AJ));
      solution.append("\n obtained with M = "+M+", N = "+N+" and P = "+P);
   }      
 

  public boolean inputCheck()
   {
        String errorInfo;
          if (A >= B ){
            solution.append("Lower limit must be less than upper limit \n");
            OK = false;}

          if ((N <=1) || (M <=1) || (N >5) || (M >5)||(P <=1) || (P >5)){
            solution.append("Make sure    1 < M,N,P <= 5  \n");
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
    integerM = new JTextField(2);
    integerN = new JTextField(2);
    integerP = new JTextField(2);
    exprField = new JTextField(20);
    exprField2 = new JTextField(20);
    exprField3 = new JTextField(20);
    exprField4 = new JTextField(20);
    exprField5 = new JTextField(20);
    solution = new JTextArea(17,40);
    JScrollPane scrollPane = new JScrollPane(solution);   
    startButton.addActionListener(this);

    JPanel inputPanel = new JPanel(new GridLayout(6,6));

    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));

    inputPanel.add(new JLabel("Function f(x,y) =:", JLabel.RIGHT));
    inputPanel.add(exprField); exprField.setText("e^(x^2+y^2)");
    inputPanel.add(new JLabel("Alpha(x,y) =:", JLabel.RIGHT));
    inputPanel.add(exprField4); exprField4.setText("-x*y");
    inputPanel.add(new JLabel("Beta(x,y) =:", JLabel.RIGHT));
    inputPanel.add(exprField5); exprField5.setText("x*y");

    inputPanel.add(new JLabel("Function C(x) =:", JLabel.RIGHT));
    inputPanel.add(exprField2); exprField2.setText("0");
    inputPanel.add(new JLabel("Function D(x) =:", JLabel.RIGHT));
    inputPanel.add(exprField3); exprField3.setText("1");
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));


    inputPanel.add(new JLabel(" A =:", JLabel.RIGHT));
    inputPanel.add(endPointA);
    inputPanel.add(new JLabel(" B =:", JLabel.RIGHT));
    inputPanel.add(endPointB);
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));

    inputPanel.add(new JLabel(" 1 < M <= 5 :", JLabel.RIGHT));
    inputPanel.add(integerM);
    inputPanel.add(new JLabel(" 1 < N <= 5 :", JLabel.RIGHT));
    inputPanel.add(integerN);
    inputPanel.add(new JLabel(" 1 < P <= 5 :", JLabel.RIGHT));
    inputPanel.add(integerP);
 
  
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
    
   
