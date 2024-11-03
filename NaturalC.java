import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;

import org.nfunk.jep.*;
import org.nfunk.jep.type.*;


public class NaturalC
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  
  private JEP myParser;
  private JButton startButton, space;  // button objects
  private JTextField nDegree, xValues, fxValues, exprField;
  private JTextArea solution;
  private double  S, Y;
  private int I, J, n, m; 
  private boolean OK;
  private NumberFormat formatter;
  private double xValue;

  private double X[], A[], B[], C[], D[], H[], XA[], XL[], XU[], XZ[], temp[];

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

                 n = Integer.parseInt(nDegree.getText());
                OK  = true;
                    A = new double[26]; B = new double[26]; C = new double[26]; D = new double[26];
                    H = new double[26]; XA = new double[26]; XL = new double[26]; XU = new double[26];
                    XZ = new double[26]; 
                    X = new double[26];                  
                    toArray(xValues.getText(), X); //convert values in text field into array elements
                                 

            solution.setText("");            

           if(inputCheck())
             {
              if(fxValues.getText()== null || fxValues.getText().equals("")) //no values entered for function
                 {
                   for(I = 0; I < n ; I++) A[I] = functnF(X[I]);
                 }
                 else
                  {                            //values has been entered for f(x)
                   toArray(fxValues.getText(), A);                                      
                  }
 
               m = n - 1;
               for(I=0; I<=m; I++) 
                  H[I] = X[I+1] - X[I];

               for(I = 1; I <= m; I++)
                  XA[I] = 3*(A[I+1]*H[I-1] - A[I]*(X[I+1] - X[I-1]) + A[I-1]*H[I])/(H[I]*H[I-1]);
                                       

                  XL[0] = 1; XU[0] =0; XZ[0] =0;
               for(I = 1; I<=m; I++)
                     {
                       XL[I] = 2*(X[I+1] - X[I-1]) - H[I-1]*XU[I-1];
                       XU[I] = H[I]/XL[I];
                       XZ[I] = (XA[I] - H[I-1]*XZ[I-1])/XL[I];
                      } 
                  XL[n] = 1; XZ[n] =0; C[n] = 0;
                
                for(I=0; I<=m; I++)
                   {
                    J = m - I;
                    C[J] = XZ[J] - XU[J]*C[J+1];
                    B[J] = (A[J+1] - A[J])/H[J] - H[J]*(C[J+1] + 2*C[J])/3;
                    D[J] = (C[J+1] - C[J])/(3*H[J]);
                    }

                 outPut();
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
  

  public void toArray(String s, double arr[])
   {
    
    String[] charArray = StringtoArray(s,","); //.split(",\\s*"); 
    for(int i=0;i<charArray.length; ++i)
       {
         arr[i] = Double.parseDouble(charArray[i]);
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
    f = myParser.getValue();  // example   f = (x + 4.0)*x^2 - 10.0;
    return f;
   }

  public boolean inputCheck()
   {
        String errorInfo;
          if (n <= 0 ){
            solution.append("Degree must be a positive integer. \n");
            OK = false;}
          if(fxValues.getText()== null || fxValues.getText().equals("")){ 
            parseExpression();
          if ((errorInfo = myParser.getErrorInfo()) != null){
            solution.append(errorInfo);
            OK =false;}}
          return OK;

   }     

  public void outPut()
      {

         solution.append(" "+"i\t"+"A[i]\t"+"B[i]\t"+"C[i]\t"+"D[i]\n"); 
         for(I=0; I<=m; I++)
            { 
             solution.append(""+I+"\t"+formatter.format(A[I])+"\t"+formatter.format(B[I])+"\t"+formatter.format(C[I])+"\t"+formatter.format(D[I])+"\n"); 
            }
      } 

  public void setVisualComponent()
    {
    startButton = new JButton("Start");
    nDegree = new JTextField(2);
    xValues = new JTextField("0.1, 0.2, 0.3, 0.4");
    fxValues= new JTextField("-0.62049958, -0.28398668, 0.00660095, 0.24842440");
    exprField = new JTextField("1/x");
    solution = new JTextArea(17,40);
    JScrollPane scrollPane = new JScrollPane(solution);   
    startButton.addActionListener(this);

    JPanel inputPanel1 = new JPanel(new GridLayout(2,6));

    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));

    inputPanel1.add(new JLabel("Xo, X1, X2,..,Xn:", JLabel.RIGHT));
    inputPanel1.add(xValues);
    inputPanel1.add(new JLabel("n =:", JLabel.RIGHT));
    inputPanel1.add(nDegree);
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));


    JPanel inputPanel2 = new JPanel(new GridLayout(4,2));
    inputPanel2.add(new JLabel("function f(x) =:", JLabel.RIGHT));
    inputPanel2.add(exprField);

    inputPanel2.add(new JLabel("OR         ", JLabel.RIGHT));
    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));

    inputPanel2.add(new JLabel("f(x) values =:", JLabel.RIGHT));
    inputPanel2.add(fxValues);

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
    inputArea.add(inputPanel1, "North");
    inputArea.add(inputPanel2, "South");
     
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(inputArea, "North");
    mainPanel.add(scrollPane, "Center");
    mainPanel.add(buttonPanel, "South");

    getContentPane().add(mainPanel);
    
    formatter = NumberFormat.getNumberInstance();
    formatter.setMaximumFractionDigits(7);
    formatter.setMinimumFractionDigits(7);
  }


  }
    
   
