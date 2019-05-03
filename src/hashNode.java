
public class hashNode {
	   public String word;
	   public float[] embedding;
	   public hashNode  next;
	   
	   public hashNode(String S, float[] E, hashNode N){
	      word = S;
	      embedding = new float[50];
	      for (int i=0;i<50;i++)
	         embedding[i] = E[i];
	      next = N;
	   }
}
