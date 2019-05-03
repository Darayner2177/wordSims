
public class Heap {
	
	public HeapNode[] H;
	public int size;
	
     
 	public Heap(int capacity) {
     H = new HeapNode[capacity + 1]; //We need to add 1 since we’re ’wasting’ H[0]
     this.size = 0;
     }
 	
 	/*
 	 * Insert into heap, creating min heap 
 	 * Swap if parent is larger
 	 * 
 	 * 
 	 */
     
 	public void insert(HeapNode node){
 	  //Use node.similarity * -1 when inserting ’node’ into the heap.
      //We have to do this because our heap puts at the top the node with the
      //smallest value, and we actually want to achieve the opposite.
      //We want the words with the greatest similarity value to be at the top.
      //Because of this, you must use node.similarity * -1
      //as the node’s value when inserting it into the heap.
      //...
 		if(this.size >= H.length-1) {
			return;
		}
		this.size++;
		int i = this.size;
		while((i>1) && (node.similarity * -1 < H[i/2].similarity * -1)){
			H[i] = H[i/2];
			i = i/2;
		}
		H[i] = node;
		return;
	}
 	
 	public void print(){
 		System.out.println("Heap size" + this.size);
 		System.out.print("H:");
 		for(int i = 1; i <= this.size; i++){
 			System.out.print(H[i].word0 + " " + H[i].word1);
 			System.out.print(" " + H[i].similarity);
 		System.out.println();
 		}
 		
 	}
 	
 	/* Swap first and last element, decrement, to remove
 	 * compare left and right children with parent to find smaller element
 	 * Make successive swaps until heap property is established parents node is smallest 
 	 *
 	 */
 	
 	public HeapNode extractMin() {
      //Use node.similarity * -1 in this method as well.
      //...
 		HeapNode minV = H[1];
 		H[1] = H[this.size];
 		this.size--;
 		int min = 1;
 		boolean b = false;
 		for(int i = 1; b != true;){
 			int l = 2 * i;
 			int r = l + 1;
 			if(l <= this.size && H[min].similarity * -1 > H[l].similarity * -1){
 				min = l;
 			}
 			if(r <= this.size && H[min].similarity * -1 > H[r].similarity * -1){
 				min = r;
 			}
 			if(min == i){
 				b = true;
 				continue;
 			}
 			
 			i = heapify(i, min);
 			
 		}
 		return minV;
	
   } 
 	
 	
 	public int heapify(int i, int min){
 		HeapNode swap = H[i];
 		H[i] = H[min];
 		H[min] = swap;
 		i = min;
 		
 		return i;
 	}
 	
 	
 	
 	
 	
   
   
}


