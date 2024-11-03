import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;

import org.nfunk.jep.*;
import org.nfunk.jep.type.*;


public class Nev
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  
  private JEP myParser;
  private JButton startButton, space;  // button objects
  private JTextField pointX0, nDegree, xValues, fxValues, exprField;
  private JTextArea solution;
  private double x0; 
  private int I, J, n; 
  private boolean OK;
  private NumberFormat formatter;
  private double xValue;

  private double q[][], xx[], temp[], d[];

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

                x0 = Double.parseDouble(pointX0.getText());
                 n = Integer.parseInt(nDegree.getText());
                OK  = true;
                    xx = new double[26];                  
                    toArray(xValues.getText(), xx); //convert values in text field into array elements
                 q = new double[26][26]; 
                 d = new double[26];
                 

            solution.setText("");            

           if(inputCheck())
             {
              if(fxValues.getText()== null || fxValues.getText().equals("")) //no values entered for function
                 {

                    for(I = 0; I <= n ; I++){q[I][0] = functnF(xx[I]);} 
                 }
                 else
                  {                            //values has been entered for f(x)
                   temp = new double[26];                  
                     toArray(fxValues.getText(), temp);
                    for(I=0; I<=n; I++){q[I][0] = temp[I];}
                  }
 
               d[0] = x0 - xx[0];
               for(I=1; I<=n; I++)
                    {
                     d[I] = x0 - xx[I];
                     for(J = 1; J <= I; J++)
                        {
                         q[I][J] = (d[I]*q[I-1][J-1] - d[I-J]*q[I][J-1])/(d[I] - d[I-J]);
                         } 
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

                solution.append(" "+"i\t"+"x\t"); 
                for(I=0; I<=n; I++) solution.append(""+"Qi"+I+"\t"); 
                solution.append("\n"); 
                for(I=0; I<=n; I++)
                    {
                     solution.append(" "+I+"\t"+formatter.format(xx[I])+"\t"); 
                     for(J = 0; J <= I; J++){ solution.append(formatter.format(q[I][J])+"\t");} 
                     solution.append("\n"); 
                    }
                solution.append("\n "+ "f("  + x0 +  ") = ");
                solution.append(formatter.format(q[n][n])); 
      } 

  public void setVisualComponent()
    {
    startButton = new JButton("Start");
    nDegree = new JTextField(2);
    xValues = new JTextField("1, 1.3, 1.6, 1.9, 2.2");
    fxValues= new JTextField("0.7651977, 0.6200860, 0.4554022, 0.2818186, 0.1103623");
    pointX0 = new JTextField(4);
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
    inputPanel1.add(new JLabel("point x =:", JLabel.RIGHT));
    inputPanel1.add(pointX0);

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
    
   
