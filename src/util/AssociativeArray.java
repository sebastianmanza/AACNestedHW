package util;

import static java.lang.reflect.Array.newInstance;
import java.util.NoSuchElementException;

/**
 * A basic implementation of Associative Arrays with keys of type K and values of type V.
 * Associative Arrays store key/value pairs and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Sebastian Manza
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({"unchecked"})
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(), DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> newArr = new AssociativeArray<K, V>();
    newArr.size = this.size;
    newArr.pairs = java.util.Arrays.copyOf(this.pairs, this.size);
    // for (int i = 0; i <= this.size; i++) {
    // newArr.pairs[i] = this.pairs[i];
    // }
    return newArr;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    StringBuilder returnStr = new StringBuilder();
    for (int i = 0; i < this.size; i++) {
      returnStr.append(this.pairs[i].toString());
      returnStr.append(", ");
    } //for
    return "{" + returnStr.toString() + "}";
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to get(key) will return value.
   *
   * @param key The key whose value we are seeting.
   * @param value The value of that key.
   *
   * @throws NullKeyException If the client provides a null key.
   */
  public void set(K key, V value){
    if (this.pairs.length == this.size) {
      this.expand();
    } //if
    if (this.hasKey(key)) {
      for (int i = 0; i < this.size; i++) {
        if (this.pairs[i].key.equals(key)) {
          this.pairs[i].val = value;
        } //if
      } //for
    } else {
      this.pairs[this.size] = new KVPair<K, V>(key, value);
    } //if/else
    this.size++;
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key A key
   *
   * @return The corresponding value
   *
   * @throws NoSuchElementException swhen the key is null or does not appear in the associative array.
   */
  public V get(K key) throws NoSuchElementException {
    return this.pairs[this.find(key)].val;
  } // get(K)

  public K getKey(int i) {
    return this.pairs[i].key;
  } 

  /**
   * Determine if key appears in the associative array. Should return false for the null key, since
   * it cannot appear.
   *
   * @param key The key we're looking for.
   *
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return true;
      } // if
    } // for
    return false;
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls to get(key) will throw an
   * exception. If the key does not appear in the associative array, does nothing.
   *
   * @param key The key to remove.
   */
  public void remove(K key) {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        this.pairs[i] = this.pairs[this.size];
        if (i == this.size) {
          this.pairs[i] = null;
        } //if
        this.size--;
      } //if
    } //for
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key. If no such entry is found,
   * throws an exception.
   *
   * @param key The key of the entry.
   *
   * @return The index of the key, if found.
   *
   * @throws NoSuchElementException If the key does not appear in the associative array.
   */
  int find(K key) throws NoSuchElementException {
    int index = -1;
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        index = i;
      } //if
    } //for
    if (index == -1) {
      throw new NoSuchElementException("Key was not found");
    } //if
    return index;
  } // find(K)

} // class AssociativeArray