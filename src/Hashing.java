/**
 * A partial implementation of a hash table using chaining (binary search trees)
 *
 *  @author COSC 311, Fall '24
 *  @version (10-29-24)
 *  
 *  Note: You must add methods get  -----check
 *  and remove to this class. Also, you must -----Check
 *  
 *        
 *  replace the call to the preOrder method with a call to the levelOrder method
 *  which you will add to the class BST. -----Check
 *        
 *        The method named 'hash' must be changed ----- check
 *        to satisfy the project requirement.
 *   
 *
 */

@SuppressWarnings ("unchecked")
public class Hashing <K extends Comparable <K>, V>{
	private final int SIZE= 37;
	private BST<Pair<K,V>> [] table;
	
	// constructor 
	public Hashing () {
		//table = (BST<Pair<K,V>> [])new Object [SIZE];  //*** this doesn't work!
		setTable(new BST[getSIZE()]);
	}

	// hash the key using division  (replace this hash function whit the one given) ---- check
	public int hash(K key) {
		return ((key.hashCode() * key.hashCode()) >>> 10) % getSIZE();
	}
	
	// add a (key,value) pair into the hash table, make sure this method works!
	public V put (K key , V value) {
		int index = hash(key);
		if (index < 0) index += getTable().length;
		if (getTable()[index] == null) getTable()[index] = new BST<Pair<K,V>> ();
		Pair <K,V> item = new Pair<>(key,value);
		Pair<K,V> other = getTable()[index].find(item);
		if (other == null) {   // item isn't in the table
			getTable()[index].add(item);
			return null;
		}
		// an item with the given key is in the table
		getTable()[index].replace(other,item);
		return other.getValue();
	}	
	
	//print method with level-order traversal
		public void print() { 
		    for (int i = 0; i < getSIZE(); i++) {
		        if (getTable()[i] != null) {
		            System.out.print(i + ": ");
		            getTable()[i].levelOrder();  // Assuming levelOrder method is implemented in BST class
		        }
		    }
		}
	// add the get method to retrieve values based on key
	public V get(K key) {
	    int index = hash(key);
	    if (index < 0) index += getTable().length;
	    if (getTable()[index] == null) return null;
	    Pair<K,V> item = new Pair<>(key, null);  // Only need the key to find the pair
	    Pair<K,V> found = getTable()[index].find(item);  // Assuming find searches by key
	    return found != null ? found.getValue() : null;
	}

	// add the remove method to delete a key-value pair from the table
	public V remove(K key) {
	    int index = hash(key);
	    if (index < 0) index += getTable().length;
	    if (getTable()[index] == null) return null;
	    Pair<K,V> item = new Pair<>(key, null);  // Only need the key to remove the pair
	    Pair<K,V> found = getTable()[index].find(item);
	    if (found == null) return null;
	    getTable()[index].remove(found);  // Assuming remove method is implemented in BST class
	    return found.getValue();
	}

	public BST<Pair<K,V>> [] getTable() {
		return table;
	}

	public void setTable(BST<Pair<K,V>> [] table) {
		this.table = table;
	}

	public int getSIZE() {
		return SIZE;
	}

	


}