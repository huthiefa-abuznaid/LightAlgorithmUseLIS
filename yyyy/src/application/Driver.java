package application;

import java.util.Arrays;

public class Driver {
  

	
	    public static void main(String[] args) {


	        
	            LIS solver = new LIS();

	            // Sample Input: Index 0 is ignored as per your loop (i = 1)
	            // This represents LEDs: [1, 2, 3, 4, 5] mapped to ports [5, 4, 3, 2, 1]
	            int[] input = {12,2,3,4,5,1,6,7,8,9,10,11}; 
	            
	            System.out.println("--- Testing LIS Logic ---");
	            System.out.println("Input Array: " + Arrays.toString(input));

	            // 1. Test Values (The LEDs that can be connected)
	            int[] values = solver.getLISValues(input);
	            System.out.println("Resulting LED Values: " + Arrays.toString(values));

	            // 2. Test Indices (The positions in the array)
	            int[] indices = solver.indexValueForTabel(input);
	            System.out.println("Resulting Indices:     " + Arrays.toString(indices));

	            // Validation
	            if (values.length == 1) {
	                System.out.println("SUCCESS: Found correct sequence length.");
	            } else {
	                System.out.println("FAILURE: Calculation mismatch.");
	            }
	        }
	    }
	