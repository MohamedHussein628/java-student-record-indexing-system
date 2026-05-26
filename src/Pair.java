public class Pair <K extends Comparable<K>, V> implements Comparable<Pair<K,V>>{
	private K key;
	private V value;
	
	// constructors
	/**
     * Construct a pair and set elements to null
     * @param none
     */
	public Pair() {
		key = null;
		value = null;
	}
	
	/**
     * Construct a pair with the given data values
     * @param one - The data value for first
     * @param two - The data value for second
     */
	public Pair (K one , V two) {
		key = one;
		value = two;
	}
	
	// getters and setters
	/**
     * Assigns a new value to the first element of this pair
     * @param other a value for the first element
     * @returns none
     */
	public void setKey (K other) {
		key = other;
	}
	
	/**
     * Assigns a new value to the second element of this pair
     * @param other a value for the second element
     * @returns none
     */
	public void setValue (V other) {
		value = other;
	}
	
	/**
     * Returns the first element of this pair
     * @param none
     * @returns the first element of this pair
     */
	public K getKey () {
		return key;
	}
	
	/**
     * Returns the second element of this pair
     * @param none
     * @returns the second element of this pair
     */
	public V getValue () {
		return value;
	}
	

	/**
     * Returns a string representing a pair object
     * @param none
     * @returns a string representing a pair
     */
	public String toString() {
		return "(" +key.toString() + "," + value.toString() + ")";
		
	}
	
	
	/**
     * Compares two pair objects based on the value of their first element
     * @param other a pair object
     * @returns 0 if first elements are the same, -1 if first < other.first, and
     *          1 if first > other.first
     */
	public int compareTo(Pair<K,V> other) {
		if (key.compareTo(other.key) < 0)
			return -1;
		else if (key.compareTo(other.key) >0)
			return 1;
		else
			return 0;
	}
	
	
	
	/**
     * Determines whether or not this pair and other pair are identical
     * @param other a pair object
     * @returns true if two pair objects are the same, otherwise returns false
     */
	public boolean equals (Object other) {
		if (other == null) return false;
		else if (getClass() != other.getClass()) return false;
		else {
			@SuppressWarnings("unchecked")
			Pair <K,V> otherPair = (Pair<K,V>) other;
			return key.equals(otherPair.key) && value.equals(otherPair.value);
		}
		
	}
	
}