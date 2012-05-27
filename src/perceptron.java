import java.lang.*;
	import java.util.*;
import java.io.*;

public class perceptron {
	
	
	//static File train_Hloc=new File("Z:/HW3/enron1/train/ham");	
	//static File train_Sloc=new File("Z:/HW3/enron1/train/spam");
	//static File test_Hloc=new File("Z:/HW3/enron1/test/ham");	
	//static File test_Sloc=new File("Z:/HW3/enron1/test/spam");
	
	
	
		static File train_Hloc=new File("Z:/School/Machine Learning/HW2/train/ham");	
		static File train_Sloc=new File("Z:/School/Machine Learning/HW2/train/spam");
		static File test_Hloc=new File("Z:/School/Machine Learning/HW2/test/ham");	
		static File test_Sloc=new File("Z:/School/Machine Learning/HW2/test/spam");
		static File trainH[] = train_Hloc.listFiles();
		static File trainS[]= train_Sloc.listFiles();
		static File testH[] = test_Hloc.listFiles();
		static File testS[]= test_Sloc.listFiles();
		static String s,t;
		static int Sstored=0,Hstored=0,Tstored=0;
		static String[]hammer= new String[50000];
		static int[] hammer_index= new int[50000];
		static String[]spammer= new String[50000];
		static int[] spammer_index= new int[50000];
		static String[]hammerT= new String[50000];
		static int[] hammer_indexT= new int[50000];
		static String[]spammerT= new String[50000];
		static int[] spammer_indexT= new int[50000];
		static double[] score=new double[2];
		static int[][] PR = new int[2000][1000];
		static int[][] PRtest = new int[2000][1000];
		
		
		public static double dotProd(int row, double[] W, int[][] LR, int nowords){
			double expval=W[0];
			for(int i=1;i<=nowords;i++)
				{expval=expval+W[i]*LR[row][i];
				//System.out.println(expval);
				}
			return ((expval+0.5)/2);
		}
		
		public static void main(String args[]) throws IOException
		{
			
			for(int i=0;i<1500;i++)
				for(int j=1;j<500;j++)
					PR[i][j]=0; // diagonal elements zero
			
			for(int i=0;i<500;i++)
				PR[i][0]=1;   // 1st column zero
			
//START OF HAM READER
			int count =0; 
			int j=0;int flag=0;
			for(int i=0;i<trainH.length;i++)
				{
				try{
					
				FileReader ham_file = new FileReader(trainH[i]);
				BufferedReader hbr = new BufferedReader(ham_file);
				s=hbr.readLine();
				
				while(hbr.readLine()!=null)
				{	StringTokenizer st = new StringTokenizer(s," ");
					
					while (st.hasMoreTokens())
					{	flag=0;
						String temp=st.nextToken();
						for(int z=0;z<Hstored;z++)
							{
							if(temp.equals(hammer[z])) 
							{
							hammer_index[z]++;
							//System.out.println("duplicate at: "+z);
							flag=1;
							PR[i][j]=hammer_index[z];
							}
							}
						if (flag==0)
						{	hammer[j]=temp;
							hammer_index[j]=0;
							PR[i][j]=hammer_index[j];
							//System.out.println(hammer[j]+"   "+hammer_index[j]+"   ");
							j++;Hstored++;
						}			
						

					}
					
				}
				}catch (Exception e){}
				count=i;}
			
			
//START OF SPAM READER
			
			int j1=0;int flag1=0;Sstored=0;
			for(int i=0;i<trainS.length;i++)
				{
				try{
					
				FileReader spam_file = new FileReader(trainS[i]);
				BufferedReader sbr = new BufferedReader(spam_file);
				s=sbr.readLine();
				
				while(sbr.readLine()!=null)
				{	StringTokenizer st = new StringTokenizer(s," ");
					
					while (st.hasMoreTokens())
					{flag1=0;
						String temp=st.nextToken();
						for(int z=0;z<Sstored;z++)
							{
							if(temp.equals(spammer[z])) 
							{
							spammer_index[z]++;
							//System.out.println("duplicate at: "+z);
							flag1=1;
							PR[count][j1]=hammer_index[z]; count++;
							}
							}
						if (flag1==0)
						{	spammer[j1]=temp;
							spammer_index[j1]=0;
							PR[count][j1]=hammer_index[j1];count++;
							//System.out.println(spammer[j1]+"   "+spammer_index[j1]+"   ");
							j1++;Sstored++;
						}			
						

					}
					
				}
			
	

				}catch(Exception e){}
				
				}
			
			int nowords=Hstored+Sstored;
			int nofiles=trainS.length+trainH.length;
			for(int i=0;i<nofiles;i++)
				PR[i][nowords]=0;
/////////////////////////////////////////////////////// TESTING READ
			
			//START OF HAM READER
			count =0; 
			j=0; flag=0;
			for(int i=0;i<testH.length;i++)
				{
				try{
					
				FileReader ham_file = new FileReader(testH[i]);
				BufferedReader hbr = new BufferedReader(ham_file);
				s=hbr.readLine();
				
				while(hbr.readLine()!=null)
				{	StringTokenizer st = new StringTokenizer(s," ");
					
					while (st.hasMoreTokens())
					{	flag=0;
						String temp=st.nextToken();
						for(int z=0;z<Hstored;z++)
							{
							if(temp.equals(hammerT[z])) 
							{
							hammer_indexT[z]++;
							flag=1;
							PRtest[i][z]=hammer_indexT[z];
							//System.out.println("duplicate at: "+z+"   "+PRtest[i][z]);
							}
							}
						if (flag==0)
						{	hammerT[j]=temp;
							hammer_indexT[j]=0;
							PRtest[i][j]=hammer_indexT[j];
							//System.out.println(PRtest[i][j]+"  New word");
							j++;Hstored++;
						}			
						

					}
					
				}
				}catch (Exception e){}
				count=i;}
			
			
//START OF SPAM READER
			
			j1=0; flag1=0;Sstored=0;
			for(int i=0;i<testS.length;i++)
				{
				try{
					
				FileReader spam_file = new FileReader(testS[i]);
				BufferedReader sbr = new BufferedReader(spam_file);
				s=sbr.readLine();
				
				while(sbr.readLine()!=null)
				{	StringTokenizer st = new StringTokenizer(s," ");
					
					while (st.hasMoreTokens())
					{flag1=0;
						String temp=st.nextToken();
						for(int z=0;z<Sstored;z++)
							{
							if(temp.equals(spammerT[z])) 
							{
							spammer_indexT[z]++;
							//System.out.println("duplicate at: "+z);
							flag1=1;
							PRtest[count][z]=hammer_indexT[z]; count++;
							}
							}
						if (flag1==0)
						{	spammerT[j1]=temp;
							spammer_indexT[j1]=0;
							PRtest[count][j1]=hammer_indexT[j1];count++;
							//System.out.println(spammer[j1]+"   "+spammer_index[j1]+"   ");
							j1++;Sstored++;
						}			
						

					}
					
				}
			
	

				}catch(Exception e){}
				
				}
			
			int nowordsT=Hstored+Sstored;
			int nofilesT=trainS.length+trainH.length;
			for(int i=0;i<nofiles;i++)
				PRtest[i][nowords]=0;
	
			///////////////////////////////////////////////////////////////// TESTING DATA READ END
			double[] W = new double[nowords+1];
			for(int i=0;i<nowords+1;i++)
				W[i]=0;
			
			double[] Pr = new double[nofiles];

			int pred;
			double maxaccuracy = 0;
			for(int iter=0;iter<20;iter++) // <---------- No Of iterations....!!!
			{
				int hit=0,miss=0;
				
				
				for(j=0;j<nofiles;j++){
					
					double expval = dotProd(j, W, PR, nowords);			
					Pr[j]=(double)1/(double)(1+Math.exp(-expval));
					//System.out.println(Pr[j]+"      exp val="+expval);
				}
			}
			
			double[] dw = new double[nowords+1];
			for(int i=0;i<=nowords;i++)
				dw[i]=0;
			
			for(int i=0;i<=nowords;i++)
				for( j=0;j<nofiles;j++)
					dw[i]=dw[i]+PR[j][i]*(PR[j][nowords+1]-Pr[j]);
			double net=0.0105;
			double lamb=0.001;
			for(int i=0;i<=nowords;i++)
				{W[i]=W[i]+net*(dw[i]-lamb*W[i]);
				//System.out.println(W[i]);
				}
			
			int hit=0,miss=0;
			for( j=0;j<nofiles;j++){
				double expval=dotProd(j, W, PRtest, nowords);		
				//if((expval+19)>0)System.out.println(expval+19);
				if((expval+19)<0)
					pred = 0;
				else
					pred = 1;
				
				if(pred == PRtest[j][nowords+1])
					{hit++;
					//System.out.println("Hit!! "+hit);
					}
				else
					{miss++;
					//System.out.println("Miss!! "+miss);
					}	
			}
			double accuracy = (double) hit / (double) (nofiles) *100;
			System.out.println("No of Iterations= "+20+"\nValue of Lambda= "+lamb);
			System.out.println("Accuracy= "+accuracy);

		}// end of main
}//end of class
