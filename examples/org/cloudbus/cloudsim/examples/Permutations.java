package org.cloudbus.cloudsim.examples;

import java.util.Arrays;
import java.util.Iterator;

/**
 * this implementation is based in Steinhaus–Johnson–Trotter algorithm and
 * Shimon Even's improvement;
 * 
 * @see https 
 *      ://en.wikipedia.org/wiki/Steinhaus%E2%80%93Johnson%E2%80%93Trotter_algorithm
 *
 */
public class Permutations implements Iterator<int[]> {
    /**
     * direction[i] = -1 if the element i has to move to the left, +1 to the
     * right, 0 if it does not need to move
     */
    private int[] direction;
    /**
     * inversePermutation[i] is the position of element i in permutation; It's
     * called inverse permutation because if p2 is the inverse permutation of
     * p1, then p1 is the inverse permutation of p2
     */
    private int[] inversePermutation;
    /**
     * current permutation
     */
    private int[] permutation;

    /**
     * @param numElements
     *            >= 1
     */
    public Permutations(int numElements) {
        // initial permutation
        permutation = new int[numElements];
        for (int i = 0; i < numElements; i++) {
            permutation[i] = i;
        }
        // the support elements
        inversePermutation = Arrays.copyOf(permutation, numElements);
        direction = new int[numElements];
        Arrays.fill(direction, -1);
        direction[0] = 0;
    }

    /**
     * Swaps the elements in array at positions i1 and i2
     * 
     * @param array
     * @param i1
     * @param i2
     */
    private static void swap(int[] array, int i1, int i2) {
        int temp = array[i1];
        array[i1] = array[i2];
        array[i2] = temp;
    }

    /**
     * prepares permutation to be the next one to return
     */
    private void buildNextPermutation() {
        // find the largest element with a nonzero direction, and swaps it in
        // the indicated direction
        int index = -1;
        for (int i = 0; i < direction.length; i++) {
            if (direction[permutation[i]] != 0
                    && (index < 0 || permutation[index] < permutation[i])) {
                index = i;
            }
        }
        if (index < 0) {
            // there are no more permutations
            permutation = null;
        } else {
            // element we're moving
            int chosenElement = permutation[index];
            // direction we're moving
            int dir = direction[chosenElement];
            // index2 is the new position of chosenElement
            int index2 = index + dir;

            // we'll swap positions elements permutation[index] and
            // permutation[index2] in permutation, to keep inversePermutation we
            // have to swap inversePermutation's elements at index
            // permutation[index] and permutation[index2]
            swap(inversePermutation, permutation[index], permutation[index2]);
            swap(permutation, index, index2);

            // update directions
            if (index2 == 0 || index2 == permutation.length - 1
                    || permutation[index2 + dir] > permutation[index2]) {
                // direction of chosen element
                direction[chosenElement] = 0;
            }

            // all elements greater that chosenElement set its direction to +1
            // if they're before index-1 or -1 if they're after
            for (int i = chosenElement + 1; i < direction.length; i++) {
                if (inversePermutation[i] > index2) {
                    direction[i] = -1;
                } else {
                    direction[i] = 1;
                }
            }
        }
    }

    public boolean hasNext() {
       
	 return permutation != null;
        
    }

    public int[] next() {
        int[] result = Arrays.copyOf(permutation, permutation.length);
        buildNextPermutation();
        return result;
    }
}