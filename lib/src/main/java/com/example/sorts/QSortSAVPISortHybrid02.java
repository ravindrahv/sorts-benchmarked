package com.example.sorts;

import java.security.SecureRandom;
//import java.util.Arrays;
import java.util.Random;

/**
 *
 *
 * <pre>
Copyright [2013] [Ravindra V Hasyagar]
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 * </pre>
 * 
 * <p>
 * Simple Averaged Virtual Pivot - Quick Sort
 * </p>
 *
 * <p>
 * Unstable, Inplace, Big-O-Notation Classification : n*log(n)
 * </p>
 *
 * <p>
 * A variation of quick sort where the pivot is calculated using simple average
 * of highest and lowest values.
 * </p>
 *
 *
 *
 * @author Ravindra HV
 * @version 0.3
 * @since 2013, 2025
 */
public class QSortSAVPISortHybrid02 {

	/*
	 * The pivot calculation works only for numbers with the same sign. As such,
	 * first step is to partition positive and negative numbers, thus preventing
	 * arithmetic overflow
	 */
	private static final int INITIAL_PIVOT = 0;

	private static volatile int RECURSION_COUNT = 0;

	private static volatile int INSERTION_SORT_THRESHOLD = 17; // exclusive

	private static final int[] POWERS_OF_TWO = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384,
			32768, 65536 };

	public static void main(String[] args) {

//        int[] arr = {0,4,2,6,-1,-5,-3,-7};
//        int[] arr = {0,4,2,6,-1,-5,-3,-7,35,41,2,6,-34,76};
//        int[] arr = {1,2,4,8,16,32,64,128,256,512};
//        int[] arr = {1024,32,64,1,2,4,8,16,128,256,512};
//        int[] arr = {-256,-512,-1,-2,-4,-8,-16,-32,-64,-128,};
//        int[] arr = {1,2,4,8,16,32,64,128,256,512,1,2,4,8,16,32,64,128,256,512,1,2,4,8,16,32,64,128,256,512,1,2,4,8,16,32,64,128,256,512,1,2,4,8,16,32,64,128,256,512,1,2,4,8,16,32,64,128,256,512,1,2,4,8,16,32,64,128,256,512,1,2,4,8,16,32,64,128,256,512,1,2,4,8,16,32,64,128,256,512,1,2,4,8,16,32,64,128,256,512};
//        int[] arr = {-2,1,1,1,1,1,-1,-1,-1,-1,-1,0,0,0,0,0,-1,-1,-1,-1,-1,0,0,0,0,0,1,1,1,1,1,2};

		int[] arr = new int[1024 * 1024 * 100];
		Random random = new SecureRandom();
		for (int i = 0; i < arr.length; i++) {
			arr[i] = random.nextInt(arr.length);
//            arr[i] = i;
//            arr[i] = arr.length-i;
//            arr[i] = arr[i] % 1024;

//            int j = i % POWERS_OF_TWO.length;
//            arr[i] = POWERS_OF_TWO[j];

//            if( i % 2 == 0) {
//                arr[i] = arr.length-i;
//            }
//            else {
//                arr[i] = random.nextInt(arr.length);
//            }
		}
		/* */
//        handlePrintLine(Arrays.toString(arr));
		long start = System.currentTimeMillis();
		sort(arr);
//        Arrays.sort(arr);
		long end = System.currentTimeMillis();
		System.out.println("Time taken : " + (end - start) + "...  Recursion count :" + RECURSION_COUNT);
//        handlePrintLine(Arrays.toString(arr));
		validate(arr);
//        handlePrintLine("Recursion count : "+RECURSION_COUNT );

	}

	/**
	 * Sorts the given array in ascending order
	 * 
	 * @param arr
	 */
	public static void sort(int[] arr) {
		if (arr.length < 2) {
			return;
		}
		sort(arr, INITIAL_PIVOT, 0, arr.length, true);
	}

	/**
	 * @param arr
	 * @param createCopy (to ensure correctness in the event of concurrent
	 *                   modification of given array)
	 * @return
	 */
	public static int[] sort(int[] arr, boolean createCopy) {

		int[] resArr = null;
		if (createCopy) {
			int[] tempArr = new int[arr.length];
			System.arraycopy(arr, 0, tempArr, 0, tempArr.length);
			sort(tempArr);
			resArr = tempArr;
		} else {
			sort(arr);
			resArr = arr;
		}

		return resArr;
	}

	private static void sort(int[] arr, double pivotVal, int lowIndex, int highIndex, boolean firstIteration) {
		RECURSION_COUNT++;

//        handlePrintLine("First Print Statement");
//        handlePrintLine("Low-Index : "+lowIndex);
//        handlePrintLine("High-Index : "+highIndex);
//        print(arr, lowIndex, highIndex);
//        handlePrintLine("Pivot : "+pivotVal);

		int elementCount = (highIndex - lowIndex);
		if (elementCount < INSERTION_SORT_THRESHOLD) {
			insertionSort(arr, lowIndex, highIndex);
		} else {
			int tempLowIndex = lowIndex;
			int tempHighIndex = highIndex;

			while (tempLowIndex < tempHighIndex) {

				while ((tempLowIndex < highIndex) && (arr[tempLowIndex] <= pivotVal)) {
					tempLowIndex++;
				}

				if (!firstIteration && tempLowIndex == highIndex) {
//                    handlePrintLine("Returning...");
					return; // all entries in given range are less than or equal to pivot..
				}

				while ((tempHighIndex > tempLowIndex) && (arr[tempHighIndex - 1] > pivotVal)) {
					tempHighIndex--;
				}

				if (tempLowIndex < tempHighIndex) {
					swap(arr, tempLowIndex, tempHighIndex - 1);
					tempLowIndex++;
					tempHighIndex--;
				}
			}
//            handlePrintLine("Final-Low-Index : "+tempLowIndex);
//            handlePrintLine("Final-High-Index : "+tempHighIndex);
//            handlePrintLine("Second Print Statement");
//            print(arr, lowIndex, highIndex);

			if ((tempLowIndex - lowIndex) > 1) {
				double leftPartPivotVal = determinePivotV2(arr, lowIndex, tempLowIndex);
				sort(arr, leftPartPivotVal, lowIndex, tempLowIndex, false);
			}

			if ((highIndex - tempLowIndex) > 1) {
				double rightPartPivotVal = determinePivotV2(arr, tempLowIndex, highIndex);
//				System.out.println("Right-Part-Pivot-Val : " + rightPartPivotVal + ", Temp-Low-Index : " + tempLowIndex
//						+ ", High-Index : " + highIndex);
//				print(arr, tempLowIndex, highIndex);
				sort(arr, rightPartPivotVal, tempLowIndex, highIndex, false);
			}

		}

	}

	/**
	 * <p>
	 * Pivot is calculated as the simple average of the highest and lowest elements,
	 * while ensuring that there is no overflow.
	 * </p>
	 *
	 * @param arr
	 * @param lowIndex
	 * @param highIndex
	 * @return
	 */
	private static int determinePivot(int[] arr, int lowIndex, int highIndex) {

		int pivotVal = 0;
		int lowVal = arr[lowIndex];
		int highVal = lowVal;

		for (int i = lowIndex; i < highIndex; i++) {
			if (arr[i] < lowVal) {
				lowVal = arr[i];
			}

			if (arr[i] > highVal) {
				highVal = arr[i];
			}
		}

		pivotVal = lowVal + ((highVal - lowVal) / 2);
//        pivotVal = lowVal+((highVal-lowVal)>>1);

		return pivotVal;

	}
	
	
	/**
	 * <p>
	 * Pivot is calculated as the average across all elements,
	 * while ensuring that there is no overflow.
	 * </p>
	 *
	 * @param arr
	 * @param lowIndex
	 * @param highIndex
	 * @return
	 */
	private static double determinePivotV2(int[] arr, int lowIndex, int highIndex) {

		double pivotVal = 0;
		int size = (highIndex-lowIndex);
		
		double intermediateResult = arr[lowIndex] / size;

		for (int i = lowIndex+1; i < highIndex; i++) {
			double tempVal = 1.0 * arr[i] / size;
			intermediateResult = intermediateResult + tempVal;
		}

//		System.out.println("Intermediate=Result : " + intermediateResult);
		
		pivotVal = intermediateResult;
//        pivotVal = lowVal+((highVal-lowVal)>>1);
//		print(arr, lowIndex, highIndex);
//		System.out.println("Pivot-Val : " + pivotVal);
		
		return pivotVal;

	}


	private static void swap(int[] arr, int lowIndex, int highIndex) {
		int tempVal = arr[lowIndex];
		arr[lowIndex] = arr[highIndex];
		arr[highIndex] = tempVal;
	}

	private static void print(int[] arr, int lowIndex, int highIndex) {
		System.out.println();
		for (int i = lowIndex; i < highIndex; i++) {
			if (i == 0) {
				System.out.print(arr[i]);
				handlePrint(arr[i]);
			} else {
				System.out.print(" " + arr[i]);
				handlePrint(" " + arr[i]);
			}
		}
		handlePrintLine("");
		System.out.println();
	}

	private static void validate(int[] arr) {
		boolean sorted = true;
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] > arr[i + 1]) {
				sorted = false;
				break;
			}
		}

		if (sorted) {
			System.out.println("SUCCESS : ARRAY SORTED. Length : " + arr.length);
		} else {
			System.out.println("ERROR : ARRAY NOT SORTED. Length : " + arr.length);
			print(arr, 0, arr.length);
		}

	}

	private static void insertionSort(int[] arr, int lowIndex, int highIndex) {

		if ((highIndex - lowIndex) < 2) {
			return;
		}

		for (int i = lowIndex; i < highIndex - 1; i++) {
			int j = i;
			while (j >= lowIndex && arr[j] > arr[j + 1]) {
				int temp = arr[j];
				arr[j] = arr[j + 1];
				arr[j + 1] = temp;
				j--;
			}
		}
	}

	private static void handlePrint(Object object) {
//        System.out.print( object.toString() );
	}

	private static void handlePrintLine(Object object) {
//        System.out.println( object.toString() );
	}

}