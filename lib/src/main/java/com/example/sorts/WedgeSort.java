package com.example.sorts;

import java.util.Arrays;

/**
 * 
 * <p>
 * Insertion sort variation. Wedges a smaller block of sorted elements into a
 * larger one by comparing the element at the rear of the wedge to the element
 * in front of the tip.
 * </p>
 * 
 * <p>
 * This is thought to be a new approach since block insertion sort uses merge
 * sort approach but here we shift elements in blocks and focus on placing the
 * largest value in its correct position. This should help reduce the overall
 * count of shifts as compared to block-insertionsort.
 * </p>
 * 
 * <p>
 * Required (and created) for Combine-Sort to avoid stack-overflow errors for
 * situations where a small set of elements are required to be merged into a
 * much larger one (due to excessive recursion) as identified during benchmark
 * tests !!
 * </p>
 * 
 * <p>
 * PS - Not battle-tested. May be buggy. But intended and included as high-level
 * approach for combine-sort as a better alternative to insertion-sort (and
 * since the standard sort may not be in-place always w.r.t JDK-8) !!
 * </p>
 * 
 * @author ravindra-hasyagar
 * @since 10-Jan-2026
 * 
 * 
 */
public class WedgeSort {

	public static void main(String[] args) {
		
		testOne();
		System.out.println("************************************************************************************");
		testTwo();
		
	}
	
	
	private static void testOne() {
		
		int[] input = { 1, 3, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 0, 2, 4, 8, 16 };
		System.out.println("Input : " + Arrays.toString(input));
		handleLeftWedge(input, 0, 12, input.length-1);
		System.out.println("Result : " + Arrays.toString(input));

	}
	
	private static void testTwo() {
		
		int[] input = { 0, 8, 12, 14, 16, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 15 };
		System.out.println("Input : " + Arrays.toString(input));
		handleRightWedge(input, 0, 4, input.length-1);
		System.out.println("Result : " + Arrays.toString(input));

	}
	
	
	/**
	 * 
	 * @param arr
	 * @param left (inclusive)
	 * @param mid (wedge-low-inclusive)
	 * @param right (inclusive)
	 */
	public static void handleLeftWedge(int[] arr, int left, int mid, int right) {
		
		
		if( left == mid || mid == right) {
			//already sorted.. or no-elements-remaining in wedge..
			return;
		}
		
		/*
		 * --//The idea is to use the analogy of a ship sailing upstream starting from
		 * the sea unloading cargo as it goes.
		 */
		
		int wedgeMin = mid;
		int wedgeMax = right;
		
//		System.out.println("Left : " + left + ", Mid : " + mid + ", Right : " + right);
		
		while ((wedgeMax - wedgeMin) >= 0 && (wedgeMin > left)) {
			
//			handlePrint(arr, left, right + 1);
			
//			System.out.println("Wedge-Max : " + wedgeMax + ", Wedge-Min : " + wedgeMin);
			
			if (arr[wedgeMax] < arr[wedgeMin - 1]) {
//				System.out.println("..");

				int valueAheadOfWedgeTip = arr[wedgeMin - 1];
				/*
				 * --//element-adjacent-to-wedge-tip-larger.. backup-value.. move-wedge-forward.. restore-value..(at-wedge-base)
				 */
				
				/*
				 * --//move-wedge-forward
				 */
				for (int i = (wedgeMin - 1); i < wedgeMax; i++) {
					arr[i] = arr[i + 1];
				}

				/*
				 * --//restore-value
				 */
				arr[wedgeMax] = valueAheadOfWedgeTip;

				/*
				 * --//update-indices.. (represents-forward-movement)
				 */
				wedgeMax--;
				wedgeMin--;

			} else {
//				System.out.println("...");
				/*
				 * --//right-most-element-is-largest.. leave-it-in-place.. reduce-wedge-size..
				 */


				/*
				 * --//update-indices.. (represents-wedge-size-reduction)
				 */
				wedgeMax--;

			}
			

		}
		
//		System.out.println("Wedge-Max-Final : " + wedgeMax + ", Wedge-Min-Final : " + wedgeMin);
		
		
	}
	
	
	private static void handlePrint(int[] arr, int low, int high) {
		
		System.out.println("--------------------------------------");
		System.out.println();
		for (int i = low; i < high; i++) {
			System.out.print(" "+arr[i]);
		}
		System.out.println();
		System.out.println("--------------------------------------");
		
	}
	
	
	/**
	 * 
	 * @param arr
	 * @param left (inclusive)
	 * @param mid (wedge-high-inclusive)
	 * @param right (inclusive)
	 */
	public static void handleRightWedge(int[] arr, int left, int mid, int right) {
		
		
		if( left == mid || mid == right) {
			//already sorted.. or no-elements-remaining in wedge..
			return;
		}
		
		/*
		 * --//The idea is to use the analogy of a ship sailing downstream starting from
		 * the source unloading cargo as it goes.
		 */
		
		int wedgeMin = left;
		int wedgeMax = mid;
		
//		System.out.println("Left : " + left + ", Mid : " + mid + ", Right : " + right);
		
		while ((wedgeMax - wedgeMin) >= 0 && (wedgeMax < right)) {
			
//			handlePrint(arr, left, right + 1);
			
//			System.out.println("Wedge-Max : " + wedgeMax + ", Wedge-Min : " + wedgeMin);
			
			if (arr[wedgeMin] > arr[wedgeMax + 1]) {
//				System.out.println("..");

				int valueBehindWedgeBase = arr[wedgeMax + 1];
				/*
				 * --//element-adjacent-to-wedge-base-smaller.. backup-value.. move-wedge-backwards.. restore-value..(at-wedge-tip)
				 */
				
				/*
				 * --//move-wedge-backward
				 */
				for (int i = (wedgeMax + 1); i > wedgeMin; i--) {
					arr[i] = arr[i - 1];
				}

				/*
				 * --//restore-value
				 */
				arr[wedgeMin] = valueBehindWedgeBase;

				/*
				 * --//update-indices.. (represents-rearwards-movement)
				 */
				wedgeMax++;
				wedgeMin++;

			} else {
//				System.out.println("...");
				/*
				 * --//left-most-element-is-smallest.. leave-it-in-place.. reduce-wedge-size..
				 */


				/*
				 * --//update-indices.. (represents-wedge-size-reduction)
				 */
				wedgeMin++;

			}
			

		}
		
//		System.out.println("Wedge-Max-Final : " + wedgeMax + ", Wedge-Min-Final : " + wedgeMin);
		
		
	}
	

}
