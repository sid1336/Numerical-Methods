
import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;


public class Horn
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  

  private JButton startButton, space;  // button objects
  private JTextField pointX0, nDegree, listField;
  private JTextArea solution;
  private double x0, y, z; 
  private int I, J, n, mm, iterations; 
  private boolean OK;
  private NumberFormat formatter;

  private double array[], pbXo[], pb[], qbXo[], qb[];

  public void init() {

    setVisualComponent();    
   
    }

  public void actionPerformed(ActionEvent e) {


    if (e.getSource() == startButton) {

      try {


                x0 = Double.parseDouble(pointX0.getText());
                 n = Integer.parseInt(nDegree.getText());
                OK  = true; 
                 toArray();    //convert values in text field into array elements
                 pbXo = new double[51]; //eg b4Xo for p(x)
                 pb = new double[51];
                 qbXo = new double[51];
                 qb = new double[51];


            solution.setText("");            

            if(inputCheck()){

             y = array[n];
             pb[n]=y;
             if ( n==0){ z=0;}
               else{ z = array[n]; qb[n]=z;}
              mm = n-1;
            for(I=1; I<=mm; I++) {
              J = n - I;
              y = y*x0 ;
              pbXo[J] = y;
              y = y + array[J];
              pb[J] = y;
              z = z*x0;
              qbXo[J] = z;
              z = z + y;
              qb[J] = z;}
               
            if(n != 0) { pbXo[0]=y*x0; y = y*x0 + array[0]; pb[0]=y;}

            solution.append("  "+"Exponents:\t");
       for( I=0 ; I <= n ;I++){solution.append( I +"\t");}
            solution.append("\n  "+"Coefficients:\t ");
       for( I=0 ; I <= n ;I++){solution.append( array[I] +"\t");}
 
           solution.append("\n "+" bX0:\t");       
       for( I=0 ; I <= n-1 ;I++){solution.append( formatter.format(pbXo[I]) +"\t");}           
            solution.append("\n\t=========================================================");
            solution.append("\n "+" b :\t");
       for( I=0 ; I <= n ;I++){solution.append( formatter.format(pb[I]) +"\t");} 
          solution.append("\n P("+x0+") = "+ formatter.format(y) );

          solution.append("\n "+" qbX0:\t\t");       
       for( I=1 ; I <= n-1 ;I++){solution.append( formatter.format(qbXo[I]) +"\t");}           
            solution.append("\n\t=========================================================");
            solution.append("\n "+" qb :\t\t");
       for( I=1 ; I <= n ;I++){solution.append( formatter.format(qb[I]) +"\t");} 

          solution.append("\n P'("+x0+") = "+ formatter.format(z) +"\n");

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
  public void toArray()
   {
    array = new double[51];

    String arr1 = listField.getText(); //arr.toString();

    String[] charArray = StringtoArray(arr1,","); //.split(",\\s*"); 
    for(int i=0;i<charArray.length; ++i)
       {
         array[i] = Double.parseDouble(charArray[i]);
       }
  
    
   }
  
  public boolean inputCheck()
   {

          if (n <=0 ){
            solution.append("Degree must be a positive integer. \n");
            OK = false;}
          return OK;
   }     

 
  public void setVisualComponent()
    {
    startButton = new JButton("Start");
    pointX0 = new JTextField(4);
    nDegree = new JTextField(2);
    listField = new JTextField(20);
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
    inputPanel.add(new JLabel("Coefficients:", JLabel.RIGHT));
    inputPanel.add(listField); listField.setText("-4,3,-3,0,2");
    inputPanel.add(new JLabel("Point Xo =:", JLabel.RIGHT));
    inputPanel.add(pointX0);
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("Degree n =:", JLabel.RIGHT));
    inputPanel.add(nDegree);
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel.add(new JLabel("           ", JLabel.RIGHT));
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
    formatter.setMaximumFractionDigits(3);
    formatter.setMinimumFractionDigits(3);
  }

 
  }
    
   
