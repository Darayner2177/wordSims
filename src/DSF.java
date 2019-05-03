import java.util.Arrays;
import java.util.Collections;

public class DSF {
	 private int[] S;
     public DSF(int n){
     /* Initialize disjoint set forest with n elements. Each element is a root */
        S = new int[n];
        for(int i=0;i<n;i++)
        	S[i] = -1;
     }
     public int[] set(){
    	 return S;
     }
     /*
      * Adjoins sets, the smaller set is added to the larger set in union
      * 
      */
     public int union_by_size(int i, int j){
        int ri = find_c(i);
        int rj = find_c(j);
        if(ri==rj)
           return -1;
        if(S[ri]>S[rj]){
           S[rj]+=S[ri];
           S[ri]=rj;
        } 			
        else{
              S[ri]+=S[rj];
              S[rj]=ri; 
        }
        return 1; 
     	}
     /*
      * finds root, through path compression
      * build path directly to root
      * Traverses through dsf once root is found, position is not set to root position, 
      * directly assigning 
      * 
      */
     public int find_c(int i){
    	 if(S[i] < 0){
    		 return i;
    	 }
    	int root = find_c(S[i]);
    	S[i] = root;
    	return root;
     }
     
     /*
      * Creates an array of largest word cluster sets 
      * Traverse dsf array 
      * count number of negative values, then create new array of that length
      * Traverse dsf array again, assigning the roots
      * Then sort the array, such that the roots are in ascending order.
      * 
      */
     
     public int[] sortRoot(){
    	 int count = 0;
    	 for(int i = 0; i < S.length; i++){
    		if(S[i] < 0){
    		 count++;
    		}
    	 }
    	 int[] rootDsf = new int[count];
    	 int j = 0;
    	 for(int i = 0; i < S.length; i++){
    		 if(S[i] < 0){
    			 rootDsf[j] = S[i];
    			 j++;
    		 }
    	 }
    	 Arrays.sort(rootDsf);
    	 return rootDsf;
     }
   
     public void print(){
           for(int i=0;i<S.length;i++)
              System.out.print(S[i]+" ");
           System.out.println();
     } 	
    
    
}

