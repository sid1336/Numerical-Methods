
import javax.swing.*;  // load visual swing classes
import java.awt.*;     // load layout classes
import java.awt.event.*;  // load event handling classes
import java.text.*;


public class Mull
       extends JApplet  // inherits properties of JFrame class
       implements ActionListener{  // implements event handling
  
  private JButton startButton, space;  // button objects
  private JTextField pointX0, pointX1, pointX2, tTolerance, nIteration, exprField;
  private JTextArea solution;
  private double A[], ZR[], ZI[], GR[], GI[], F[], X[], CH1R[],CH1I[],H[];
  private double CDEL1R[], CDEL1I[], DEL1[];
  private double CDELR, CDELI, CBR, CBI, CDR, CDI, CER, CEI, DEL, B, D, E, TOL, QR, QI, ER, EI, FR, FI, P;
  private double PE, PF, PC, PD ;
  private int N, M, I, J, K, ISW, KK, iterations, NSize;
  private boolean OK, hd;
  private NumberFormat formatter;
  private double ZERO = 1.0E-20;


  public void init() {

   setVisualComponent();    
   
 }

  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == startButton) {

      try {
                 A = new double[50]; ZR = new double[4]; ZI = new double[4]; GR = new double[4]; GI = new double[4];
                 F = new double[4]; X = new double[4]; CH1R = new double[3]; CH1I = new double[3]; H = new double[3];
                 CDEL1R = new double[2]; CDEL1I = new double[2]; DEL1 = new double[2];
                 X[0] = Double.parseDouble(pointX0.getText());
                 X[1] = Double.parseDouble(pointX1.getText());
                 X[2] = Double.parseDouble(pointX2.getText());
                TOL = Double.parseDouble(tTolerance.getText());
                 M = Integer.parseInt(nIteration.getText());
                 solution.setText("");             
                 OK  = true; 
                 inputCheck();
                 toArray(exprField.getText(), A);
                 N = NSize;
                if(A[N-1]==0){solution.append("Leading coeficient is 0 -error in input\n"); OK = false;}
                if(N == 2){P = -A[0]/A[1];solution.append("Polynomial is linear: root is "+formatter.              format(P)+"\n");OK=false;} 
        if(OK){
         solution.setText("");
         solution.append("I"+"\t"+"X(I)"+"\t"+"f(X(I))"+"\n");                    for (I=1; I<=3; I++) {
      /* Evaluate F using Horner's Method and save in the vector F */
         F[I-1] = A[N-1];
         for (J=2; J<=N; J++) {
            K = N-J+1;
            F[I-1] = F[I-1]*X[I-1]+A[K-1];
         }
      }
      /* Variable ISW is used to note a switch to complex arithmetic
         ISW=0 means real arithmetic, and ISW=1 means complex arithmetic */
      ISW = 0;
      /* STEP 1 */ 
      H[0] = X[1]-X[0];
      H[1] = X[2]-X[1];
      DEL1[0] = (F[1]-F[0])/H[0];
      DEL1[1] = (F[2]-F[1])/H[1];
      DEL = (DEL1[1]-DEL1[0])/(H[1]+H[0]);
      I = 3;
      OK = true;
      /* STEP 2 */ 
      while ((I <= M) && OK) {
         /* STEPS 3-7 for real arithmetic */ 
         if (ISW == 0) {
            /* STEP 3 */ 
            B = DEL1[1]+H[1]*DEL;
            D = B*B-4.0*F[2]*DEL;
            /* test to see if need complex arithmetic */
            if (D >= 0.0) {
               /* real arithmetic - test to see if straight line */
               if (absval(DEL) <= ZERO) {
                  /* straight line - test if horizontal line */
                  if (absval(DEL1[1]) <= ZERO) {
                     solution.append("Horizontal Line\n");
                     OK = false;
                  }  
                  else {
                     /* straight line but not horizontal */
                     X[3] = (F[2]-DEL1[1]*X[2])/DEL1[1];
                     H[2] = X[3]-X[2];
                  }  
               }
               else {
                  /* not straight line */
                  D = Math.sqrt(D);
                  /* STEP 4 */ 
                  E = B+D;
                  if (absval(B-D) > absval(E)) E = B-D;
                  /* STEP 5 */
                  H[2] = -2.0*F[2]/E;
                  X[3] = X[2]+H[2];
               }  
               if (OK) {
                  /* evaluate f(x(I))=F[3] by Horner's method */ 
                  F[3] = A[N-1];
                  for (J=2; J<=N; J++) {
                     K = N-J+1;
                     F[3] = F[3]*X[3]+A[K-1];
                  }   
                  solution.append(""+I+"\t"+formatter.format(X[3])+"\t"+formatter.format(F[3])+"\n");
                  /* STEP 6 */
                  if (absval(H[2]) < TOL) {
                     solution.append("\nMethod Succeeds\n");
                     solution.append("Approximation is within "+TOL+" \n");
                     solution.append("in "+ I +"iterations\n");
                     OK = false;
                  }  
                  else {
                     /* STEP 7 */ 
                     for (J=1; J<=2; J++) {
                        H[J-1] = H[J];
                        X[J-1] = X[J];
                        F[J-1] = F[J];
                     }  
                     X[2] = X[3];
                     F[2] = F[3];
                     DEL1[0] = DEL1[1];
                     DEL1[1] = (F[2]-F[1])/H[1];
                     DEL = (DEL1[1]-DEL1[0])/(H[1]+H[0]);
                  }  
               }  
            }  
            else {
               /* switch to complex arithmetic */
               ISW = 1; hd = true;
               for (J=1; J<=3; J++) {
                  ZR[J-1] = X[J-1]; ZI[J-1] = 0.0;
                  GR[J-1] = F[J-1]; GI[J-1] = 0.0;
               }  
               for (J=1; J<=2; J++) {
                  CDEL1R[J-1] = DEL1[J-1]; CDEL1I[J-1] = 0.0;
                  CH1R[J-1] = H[J-1]; CH1I[J-1] = 0.0;
               }
               CDELR = DEL; CDELI = 0.0;
            }  
         }  
         if ((ISW == 1) && OK) {
            /* STEPS 3-7 complex arithmetic */
            /* test if straight line */
            if(hd == true){
             solution.setText("");
             solution.append("\nDepending on the input,the algorithm gives complex solutions.\n\n"); 
             solution.append("I"+"\t"+"real X(I)"+"\t"+"imaginary X(I)"+"\t"+"real f(X(I))"+"\t"+"imaginary f(X(I))"+"\n"); 
             hd = false;}
            if (CABS(CDELR,CDELI) <= ZERO) {
               /* straight line - test if horizontal line */
               if (CABS(CDEL1R[0],CDEL1I[0]) <= ZERO) {
                  solution.append("horizontal line - complex\n");
                  OK = false;
               }  
               else {
                  /* straight line but not horizontal */
                  solution.append("line - not horizontal\n");
                  CMULT(CDEL1R[1],CDEL1I[1],ZR[2],ZI[2]);ER =PE; EI =PF;
                  CSUB(GR[2],GI[2],ER,EI); FR = PE; FI=PF;
                  CDIV(FR,FI,CDEL1R[1],CDEL1I[1]);ZR[3]=PE; ZI[3]=PF; 
                  CSUB(ZR[3],ZI[3],ZR[2],ZI[2]); CH1R[2]=PE; CH1I[2]=PF;
               }  
            }  
            else {
               /* not straight line */
               /* STEP 3 */
               CMULT(CH1R[1],CH1I[1],CDELR,CDELI); ER=PE; EI=PF;
               CADD(CDEL1R[1],CDEL1I[1],ER,EI); CBR=PE; CBI=PF;
               CMULT(GR[2],GI[2],CDELR,CDELI); ER=PE; EI=PF;
               CMULT(ER,EI,4.0,0.0); FR= PE; FI=PF;
               QR = CBR; QI = CBI;
               CMULT(CBR,CBI,QR,QI);ER=PE; EI=PF;
               CSUB(ER,EI,FR,FI);CDR =PE; CDI=PF;
               CSQRT(CDR,CDI); FR =PC; FI =PD;
               /* STEP 4 */
               CDR = FR; CDI = FI;
               CADD(CBR,CBI,CDR,CDI);CER =PE; CEI=PF;
               CSUB(CBR,CBI,CDR,CDI);FR= PE; FI=PF;
               if (CABS(FR,FI) > CABS(CER,CEI))
                  CSUB(CBR,CBI,CDR,CDI);CER =PE; CEI=PF;
               /* STEP 5 */
               CDIV(GR[2],GI[2],CER,CEI);ER=PE; EI=PF;
               CMULT(ER,EI,-2.0,0.0);CH1R[2]=PE; CH1I[2]=PF;
               CADD(ZR[2],ZI[2],CH1R[2],CH1I[2]);ZR[3]=PE; ZI[3]=PF;
            }  
            if (OK) {
               /* evaluate f(x(i))=f(3) by Horner's method */
               GR[3] = A[N-1]; GI[3] = 0.0;
               for (J=2; J<=N; J++) {
                  K = N-J+1;
                  CMULT(GR[3],GI[3],ZR[3],ZI[3]);ER =PE; EI =PF;
                  GR[3] = ER+A[K-1];
                  GI[3] = EI;
               }  
               solution.append(""+ I+"\t"+formatter.format(ZR[3])+"\t"+formatter.format(-1*ZI[3])+"\t"+formatter.format(GR[3])+"\t"+formatter.format(-1*GI[3])+"\n");
               /* STEP 6*/ 
               if (CABS(CH1R[2],CH1I[2]) <= TOL) {
                  solution.append("\nMethod Succeeds\n");
                  solution.append("Approximation is within "+TOL+" \n");
                  solution.append("in "+I+" iterations\n");
                  OK = false;
               }  
               else {
                  /* STEP 7 */
                  for (J=1; J<=2; J++) {
                     CH1R[J-1] = CH1R[J];
                     CH1I[J-1] = CH1I[J];
                     ZR[J-1] = ZR[J];
                     ZI[J-1] = ZI[J];
                     GR[J-1] = GR[J];
                     GI[J-1] = GI[J];
                  } 
                  ZR[2] = ZR[3];
                  ZI[2] = ZI[3];
                  GR[2] = GR[3];
                  GI[2] = GI[3];
                  CDEL1R[0] = CDEL1R[1];
                  CDEL1I[0] = CDEL1I[1];
                  CSUB(GR[2],GI[2],GR[1],GI[1]);ER=PE;EI=PF;
                  CDIV(ER,EI,CH1R[1],CH1I[1]);CDEL1R[1]=PE;CDEL1I[1]=PF;
                  CSUB(CDEL1R[1],CDEL1I[1],CDEL1R[0],CDEL1I[0]);ER=PE;EI=PF;
                  CADD(CH1R[1],CH1I[1],CH1R[0],CH1I[0]);FR=PE;FI=PF;
                  CDIV(ER,EI,FR,FI);CDELR=PE;CDELI=PF;
               }  
            }  
         }  
         /* STEP 7 CONTINUED */
         I++;
      }  
      /* STEP 8 */
      if ((I > M) && OK)
       solution.append("Method failed to give accurate approximation.\n");

         }
        }
     
      catch (NumberFormatException ex)
        {
        solution.append("Error");
        }
      }
    }


public void CADD(double A, double B, double C, double D) 
/* Procedure to perform complex addition:
   (A + Bi) + (C + Di) -> E + Fi  */
{
   PE = A + C;
   PF = B + D;
}

public void CMULT(double A, double B, double C, double D) 
/*  Procedure to perform complex multiplication:
    (A + Bi) * (C + Di) -> E + Fi   */
{
   PE = (A * C) - (B * D);
   PF = A * D + B * C;
}

public void CDIV(double A, double B, double C, double D) 
/*  Procedure to perform complex division:
    (A + Bi) / (C + Di) -> E + Fi    */
{
   double G;
   
   G = C * C + D * D;
   PE = (A * C + B * D) / G;
   PF = (B * C - A * D) / G;
}

public double CABS(double A, double B)
/*   Function to compute complex absolute value:
     | A + Bi | = sqrt(A*A + B*B)   */
{
   double C;

   C = Math.sqrt(A * A + B * B);
   return C;
}

public void CSQRT(double A, double B)
/*   Procedure to compute complex square root:
     Math.sqrt(A + Bi) -> E + Fi    */
{
   double G,R,T,HP;

   HP = 0.5*Math.PI;
   if (absval(A) <= ZERO) {
      if (absval(B) <= ZERO) {
         R = 0.0;
         T = 0.0;
      }
      else {
         T = HP;
         if (B < 0.0) T = -T;
         R = absval(B);
      }
   }
   else {
      R = Math.sqrt(A * A + B * B) ;
      if (absval(B) < ZERO) {
         T = 0.0;
         if (A < 0.0) T = Math.PI;
      }
      else {
         T = Math.atan( B / A );
         if (A < 0.0) T = T + Math.PI;
      }
   }
   G = Math.sqrt(R);
   PC = G * Math.cos(0.5 * T);
   PD = G * Math.sin(0.5 * T);
}

public void CSUB(double A, double B, double C, double D)
/*  Procedure to perform complex subtraction:
    (A + Bi) - (C + Di) -> E + Fi    */
{
   PE = A - C;
   PF = B - D;
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
    NSize =charArray.length;
    for(int i=0;i<charArray.length; ++i)
       {
         arr[i] = Double.parseDouble(charArray[i]);
       }
      
     }

  
  public boolean inputCheck()
   {
        String errorInfo;
          if (TOL <= 0.0){
            solution.append("Tolerance must be positive \n");
            OK = false;}
          if(exprField.getText()==null || exprField.getText().equals("")){
            solution.append("Please input Coeffients of function\n");
            OK = false;}
          if (M <= 0 ){
            solution.append("Number of Iterations must be positive Integer\n");
            OK = false;}
          return OK;
   }     

 public double absval(double val)
  { 
    if( val >= 0) return val;
    else return -val;
  }  
 
   public void setVisualComponent()
    {
    startButton = new JButton("Start");
    pointX0 = new JTextField(4);
    pointX1 = new JTextField(4);
    pointX2 = new JTextField(4);
    tTolerance = new JTextField(6);
    nIteration = new JTextField(2);
    exprField = new JTextField("6, 20, 5,-40,16");
    solution = new JTextArea(17,40);
    JScrollPane scrollPane = new JScrollPane(solution);   
    startButton.addActionListener(this);

    
    JPanel inputPanel1 = new JPanel(new GridLayout(3,3));

    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("f(x) coefs in ascending order =:", JLabel.RIGHT));
    inputPanel1.add(exprField); 
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel1.add(new JLabel("           ", JLabel.RIGHT));

    JPanel inputPanel2 = new JPanel(new GridLayout(3,6));

    inputPanel2.add(new JLabel("Point X0:", JLabel.RIGHT));
    inputPanel2.add(pointX0);
    inputPanel2.add(new JLabel("Point X1:", JLabel.RIGHT));
    inputPanel2.add(pointX1);
    inputPanel2.add(new JLabel("Point X2:", JLabel.RIGHT));
    inputPanel2.add(pointX2);
    inputPanel2.add(new JLabel("TOL:", JLabel.RIGHT));
    inputPanel2.add(tTolerance);
    inputPanel2.add(new JLabel("No Iterations:", JLabel.RIGHT));
    inputPanel2.add(nIteration);
    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));

    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));
    inputPanel2.add(new JLabel("           ", JLabel.RIGHT));


    JPanel buttonPanel = new JPanel(new GridLayout(1,6));
    buttonPanel.add(startButton);
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    
    buttonPanel.add(new JLabel("           ", JLabel.RIGHT));    

    JPanel inputAreaPanel = new JPanel(new BorderLayout());
    inputAreaPanel.add(inputPanel1, "North");
    inputAreaPanel.add(inputPanel2, "South");

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(inputAreaPanel, "North");
    mainPanel.add(scrollPane, "Center");
    mainPanel.add(buttonPanel, "South");

    getContentPane().add(mainPanel);
    
    formatter = NumberFormat.getNumberInstance();
    formatter.setMaximumFractionDigits(8);
    formatter.setMinimumFractionDigits(8);

      }

  }
    
   
