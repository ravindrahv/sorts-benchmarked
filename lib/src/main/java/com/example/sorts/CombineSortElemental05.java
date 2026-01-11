package com.example.sorts;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 
 * 
 * <p> In-Place, Stable, Parallelizable sorting algorithm </p>
 * <p> Forks sub-tasks into virtual threads for sizes exceeding the specified threshold (1M as of writing) </p>
 * @author ravindra-hasyagar
 * @since 06-Jan-2026
 */
public class CombineSortElemental05 {
	
	private static final int INSERTION_SORT_THRESHOLD = 11;
	private static final int FORK_THRESHOLD = 1024 * 1024;
	
	public static void main(String[] args) {
		
		System.out.println("Start..");
		testOne();
//		testTwo();
		System.out.println("Done..");
	}
	
	
	private static void testTwo() {
//		int[] input01 = {7, 22, 5, 19, 18, 23, 24, 0, 5, 6, 2, 13, 9, 23, 20, 8, 8, 11, 23, 1, 24, 20, 21, 5, 19};
//		int[] input02 = {22, 7, 20, 23, 12, 5, 9, 24, 23, 4, 25, 3, 19, 20, 19, 5, 12, 15, 7, 18, 21, 16, 2, 7, 3, 25, 22, 27, 11, 21};
		
//		int[] input03 = {1, 4, 0, 2, 1, 2};
//		int[] input04 = {7, 0, 1, 3, 7, 7, 7, 8, 7};
//		int[] input05 = {2, 6, 0, 5, 3, 6, 2};
//		int[] input06 = {0, 6, 3, 0, 7, 2, 6, 2};
//		int[] input07 = {1, 0};
//		int[] input08 = {1, 2, 1};
//		int[] input09 = {1, 6, 5, 2, 7, 0, 5, 4};
//		int[] input10 = {1, 0, 2, 0};
//		int[] input11 = {3, 0, 3, 0};
//		int[] input12 = {6, 3, 4, 0, 6, 6, 5, 5};
//		int[] input13 = {3, 1, 2, 3};
//		int[] input14 = {2, 4, 0, 3, 4, 2, 1};
		
//		handleTest(input14);
		
		
	}
	
	
	private static void testOne() {

		
		int[] inputSizes = {5,10,15,20,25,30,35,40,45,50};
		int iterations = 1000;
		SecureRandom random = new SecureRandom();
		
		for(int i=0;i<iterations;i++) {
			
			int[] arrTemp = new int[random.nextInt(inputSizes.length)];
			
			for (int j = 0; j < arrTemp.length; j++) {
				arrTemp[j] = random.nextInt(arrTemp.length);
			}
			
			handleTest(arrTemp);
			

		}
		
	
	}
	
	private static void handleTest(int[] arr) {

		int[] arrCopy01 = new int[arr.length];
		int[] arrCopy02 = new int[arr.length];

		System.arraycopy(arr, 0, arrCopy01, 0, arr.length);
		System.arraycopy(arr, 0, arrCopy02, 0, arr.length);

		sort(arrCopy01);
		Arrays.sort(arrCopy02);

		boolean success = Arrays.equals(arrCopy01, arrCopy02);
		if (!success) {
			System.out.println("Sort-Failed-For : " + Arrays.toString(arr));
			System.out.println("Final-State : " + Arrays.toString(arrCopy01));
			throw new RuntimeException("Sort-Failed..");
		} else {
			System.out.println("Sort-Success !!");
		}

	}
	
	
	public static void sort(int[] arr) {
		split(arr, 0, arr.length);
	}
	
	
	/**
	 * 
	 * @param arr
	 * @param low (index, inclusive)
	 * @param high (index, exclusive)
	 */
	private static void split(int[] arr, int low, int high) {
		
		int diff = (high-low);
		if(diff < 2) {
			return;
		}
		
		if ((diff <= INSERTION_SORT_THRESHOLD)) {
			insertionSort(arr, low, high - 1);
			return;
		}
		
		int mid = low + (high - low) / 2;
		
		if (diff >= FORK_THRESHOLD) {

			ForkJoinPool.commonPool().invoke(new RecursiveAction() {

				@Override
				protected void compute() {

					RecursiveAction left = new RecursiveAction() {
						@Override
						protected void compute() {
							split(arr, low, mid);
						};
					};

					RecursiveAction right = new RecursiveAction() {
						@Override
						protected void compute() {
							split(arr, mid, high);
						};
					};

					invokeAll(left, right);

					merge(arr, low, mid, high);
				}
			});

		} else {
			
			split(arr, low, mid);
			split(arr, mid,high);
			merge(arr, low, mid, high);
			
		}
		
	}
	
	
	/**
	 * <pre>
	 * Given - two pre-sorted sub-sets..
	 * {1, 3, 5, 7, 9, 11}, {2, 4, 6, 8}
	 * merges such that effectively four new sorted sub-arrays are formed.. with a improved distribution..
	 * 
	 * {(1, 3, 5), (2, 4, 6)}, {(7, 9, 11), (8)}
	 * ...
	 * 
	 * the above process is repeated recursively.. 
	 * 
	 * </pre>
	 * @param arr
	 * @param low (index, inclusive)
	 * @param mid (index, low-exclusive, high-inclusive)
	 * @param high (index, exclusive)
	 */

	private static void merge(int[] arr, int low, int mid, int high) {

		int diffAbs = (high - low);
		int diffLHS = (mid - low);
		int diffRHS = (high - mid);

		if (diffAbs < 2 || low == mid || mid == high) {
			return;
		}

		if (arr[mid - 1] <= arr[mid]) {
			// pre-sorted..
			return;
		}

		/*
		if (diffAbs <= STANDARD_SORT_THRESHOLD) {
			Arrays.sort(arr, low, high);
			return;
		}
		*/
		if ((diffLHS <= INSERTION_SORT_THRESHOLD) || (diffRHS <= INSERTION_SORT_THRESHOLD)) {
			insertionSort(arr, low, high - 1);
			return;
		}

		int offset = 0;
		while ((offset < diffLHS) && (offset < diffRHS) && (arr[mid - 1 - offset] > arr[mid + offset])) {
			offset++;
		}

		
//			System.out.println("Arr : " + Arrays.toString(arr));
//			System.out.println("Low : " + low + ", Mid : " + mid + ", High : " + high + ", Offset : " + offset);
//			handlePrint(arr, low, high," Before serial swap...");
		for (int i = 0; i < offset; i++) {
			int tempVal = arr[mid - offset + i];
			arr[mid - offset + i] = arr[mid + i];
			arr[mid + i] = tempVal;
		}
//			handlePrint(arr, low, high," After serial swap...");
		
		merge(arr, low, mid - offset, mid);
		merge(arr, mid, mid + offset, high);

	}
	
	
	/**
	 * 
	 * @param arr
	 * @param low - index, inclusive
	 * @param high - index, inclusive
	 */
	private static void insertionSort(int[] arr, int low, int high) {

		if ((high - low) < 1) {
			return;
		}
		
//		handlePrint(arr, low, high+1," Before insertion sort...");
		
//		System.out.println("insertion-sort-for-range : low : " + low + ", high : " + high);

		for (int i = low+1; i <= high; i++) {
			int valueTemp = arr[i];
			int j = i - 1;
			
			while (j >= low && arr[j] > valueTemp) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = valueTemp;
		}
		
//		handlePrint(arr, low, high+1," After insertion sort...");
	}
	
	private static void handlePrint(int[] arr, int low, int high, String message) {
		System.out.println("------------------------------------");

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = low; i < high; i++) {
			sb.append(arr[i]);
			sb.append(", ");
		}
		sb.append("]");
		System.out.println(message + " ### " + sb.toString());
		System.out.println("------------------------------------");
	}
	
	
}
