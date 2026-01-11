package com.example.sorts;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 
 * <p>
 * The byte (8-bits) variation of in-place radix-counting sort hybrid is the
 * best of all other variations as per observations
 * </p>
 * <p>
 * The below implementation is slower than the java-standard sort in nearly all
 * input sets except for completely random set with size of about 100 million
 * integers
 * </p>
 * 
 * @author ravindra-hasyagar
 * @since 20-Dec-2025
 */
public class InPlaceCountingRadixHybridSort05ForJMH {
	
	
	private static final int BYTE_BUCKET_SIZE = 256;
	
	private static final int INSERTION_SORT_THRESHOLD = 13;
	
	private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();
	private static int ELEMENT_COUNT = 0;
	private static int ELEMENT_COUNT_V2 = 0;
	private static final AtomicInteger SWAP_COUNT = new AtomicInteger();
	
	
	/*
	 * -- // -- URL-Safe-Chars ... 
	 * 
	 * 
	 * ------ a d e f g h i j k p q r t y 3 4 7 9 A E F H J K L M N P R T Y - .
	 */
	
	public static void main(String[] args) {

		
		testOne();
		testTwo();
//		testFour();

		testThree();

		int loop = 1; // 100
		for (int i = 0; i < loop; i++) {
			testThree();
		}
		
		System.out.println("*****");
		
		testFive();
		
		testSix();


	}
	
	
	private static void testOne() {
		
		int countStart = ATOMIC_INTEGER.get();

		int[] arr01 = { -1, -2, -3, -4, 0, 4, 3, 2, 1 };
		int[] arr02 = { 1, 2, 3, 4, 0, -4, -3, -2, -1 };
		int[] arr03 = { -10, 2, 3, 4, 0, 4, -3, -2, -1, 7, -5 };
		int[] arr04 = { 7, 9, 8, 6, 5, 4, 3, 2, 1, 0 };
		int[] arr05 = { -7, -9, -8, -6, -5, -4, -3, -2, -1 };

		List<int[]> arraysList = Arrays.asList(arr01, arr02, arr03, arr04, arr05);

		for (int[] arrTemp : arraysList) {

			int[] input = arrTemp;
			int index = sortBySign(input);
			System.out.println(" i : " + index + ", Arr :" + Arrays.toString(input));
		}
		
		
		int countEnd = ATOMIC_INTEGER.get();
		
		System.out.println("Count-For-Test-One :" + (countEnd - countStart));

	}
	
	
	private static void testTwo() {

		int[] arr01 = { 1 };
		int[] arr02 = { 2, 1 };
		int[] arr03 = { 3, 2, 1 };
		int[] arr04 = { 4, 3, 2, 1 };
		int[] arr05 = { 5, 4, 3, 2, 1 };

		List<int[]> arraysList = Arrays.asList(arr01, arr02, arr03, arr04, arr05);

		for (int[] arrTemp : arraysList) {

			int[] input = arrTemp;
			insertionSort(input, 0, input.length);
			System.out.println("Result :" + Arrays.toString(input));

		}

	}
	
	private static void testThree() {
		
		int countStart = ATOMIC_INTEGER.get();

		SecureRandom random = new SecureRandom();
		int size = 100000000 + random.nextInt(10);
//		size = 1000000 + random.nextInt(10);
		int[] input = new int[size];

		for (int i = 0; i < size; i++) {
			input[i] = random.nextInt();
		}

//		input = new int[] { 19, 68, 75, 67, 12, 19 };
//		input = new int[] { 88, 56, 38, 88, 62 };
//		input = new int[] { 6, 76, 49, 58, 25, 11, 7, 0, 63 };

		int[] copy = Arrays.copyOf(input, input.length);
		int[] original = Arrays.copyOf(input, input.length);

		int swapCountStart = SWAP_COUNT.get();
		long startCustom = System.currentTimeMillis();
		sort(input);
		long stopCustom = System.currentTimeMillis();
		int swapCountEnd = SWAP_COUNT.get();
//		handlePrint("Result : " + Arrays.toString(input));

		long startStandard = System.currentTimeMillis();
		Arrays.sort(copy);
		long stopStandard = System.currentTimeMillis();

//		handlePrint("Copy : " + Arrays.toString(copy));

		boolean match = Arrays.equals(input, copy);
		System.out.println("Match ? " + match + " Custom-Time :" + (stopCustom - startCustom) + ", Standard-Time :"
				+ (stopStandard - startStandard));

		if (!match) {
			throw new RuntimeException("mismatch-for-array :" + Arrays.toString(original));
		}
		
		int countEnd = ATOMIC_INTEGER.get();
		
		System.out.println("Count-For-Test-Three :" + (countEnd - countStart) + ", for size : " + input.length
				+ ", element-count : " + ELEMENT_COUNT + ", element-count-v2 : " + ELEMENT_COUNT_V2 + ", swap-count :"
				+ (swapCountEnd - swapCountStart));
	}
	
	
	private static void testFour() {

		System.out.println("Start test four ... ");
		int[] digits = new int[] { -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
//		int lowVal = -9;
//		int highVal = +9;
//		int maxCount=19;

		int loopCount = 0;
		for (int i = 0; i < digits.length; i++) {
			for (int j = 0; j < digits.length; j++) {
				for (int k = 0; k < digits.length; k++) {
					for (int l = 0; l < digits.length; l++) {
						for (int m = 0; m < digits.length; m++) {
							for (int n = 0; n < digits.length; n++) {
								for (int o = 0; o < digits.length; o++) {

//									System.out.println(i + "," + j + "," + k + "," + l + "," + m + "," + n + "," + o);
									int[] input = new int[] { digits[i], digits[j], digits[k], digits[l], digits[m],
											digits[n], digits[o] };
									int[] copy = Arrays.copyOf(input, input.length);
//									handleThreadSleep();
									sort(input);
									boolean testResult = checkSorted(input);
									if (!testResult) {
										throw new RuntimeException("Sort failed for :" + Arrays.toString(copy));
									}
									loopCount++;
									if (loopCount % 100000 == 0) {
										System.out.println(new Date() + "... testing permutations..." + loopCount);
									}

								}
							}
						}
					}
				}
			}
		}

		System.out.println("End test four ... ");
	}
	
	private static void testFive() {

		int baseValue = 1;

		int[] arr = new int[17];

		arr[0] = 0;
		for (int i = 1; (i < arr.length); i = (i + 2)) {

			arr[i] = baseValue;
			arr[i + 1] = arr[i] * (-1);

			baseValue = baseValue * 2;

		}

		printAsHexAndBinary(arr);

		System.out.println("...");
		sort(arr);
		System.out.println("...");

		printAsHexAndBinary(arr);

	}
	
	
	private static void testSix() {
		
		int countStart = ATOMIC_INTEGER.get();
		
		SecureRandom secureRandom = new SecureRandom();
		
		int size = 128*1024*1024;
		
		int[] input = new int[size];
		
		for (int i = 0; i < size; i++) {
//			input[i] = i;
			
			input[i] = Integer.MAX_VALUE - i;
			
//			input[i] = secureRandom.nextInt();

			/*
			if ((i % 10000 == 0)) {
				input[i] = secureRandom.nextInt();
			}
			*/
			
			/*---*/
		}
		
		
		int[] copy = Arrays.copyOf(input, input.length);
		int[] original = Arrays.copyOf(input, input.length);

		int swapCountStart = SWAP_COUNT.get();
		long startCustom = System.currentTimeMillis();
		sort(input);
		long stopCustom = System.currentTimeMillis();
		int swapCountEnd = SWAP_COUNT.get();

//		handlePrint("Result : " + Arrays.toString(input));

		long startStandard = System.currentTimeMillis();
		Arrays.sort(copy);
		long stopStandard = System.currentTimeMillis();
		
		
		boolean match = Arrays.equals(input, copy);
		System.out.println("Match ? " + match + " Custom-Time :" + (stopCustom - startCustom) + ", Standard-Time :"
				+ (stopStandard - startStandard));

		if (!match) {
			throw new RuntimeException("mismatch-for-array :" + Arrays.toString(original));
		}
		
		int countEnd = ATOMIC_INTEGER.get();
		
		System.out.println("Count-For-Test-Six :" + (countEnd - countStart) + ", for size : " + input.length
				+ ", element-count : " + ELEMENT_COUNT + ", element-count-v2 : " + ELEMENT_COUNT_V2 + ", swap-count :"
				+ (swapCountEnd - swapCountStart));
		
		
	}
	
	private static void printAsHexAndBinary(int[] arr) {

		System.out.println("+++++++");
		for (int i : arr) {
			System.out.println(
					i + " : " + zeroPad(Integer.toHexString(i), 8) + " : " + zeroPad(Integer.toBinaryString(i), 32));
		}
		System.out.println("-------");

	}
	
	private static String zeroPad(String input, int len) {
		int padLen = len - input.length();
		
		StringBuilder stringBuilder = new StringBuilder();
		for(int i=0;i<padLen;i++) {
			stringBuilder.append('0');
		}
		stringBuilder.append(input);
		
		return stringBuilder.toString();
	}
	
	private static boolean checkSorted(int[] arr) {

		for (int i = 0; i < (arr.length - 1); i++) {
			if (arr[i] > arr[i + 1]) {
				return false;
			}
		}

		return true;
	}
	
	
	private static void handleThreadSleep() {
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public enum IntegerMask {

		FIRST_BYTE(0x7F000000, 24), SECOND_BYTE(0x00FF0000, 16), THIRD_BYTE(0x0000FF00, 8), FOURTH_BYTE(0x000000FF, 0),;

		private int bitMask;
		private int bitRightShiftCount;

		private IntegerMask(int bitMask, int bitRightShiftCount) {
			this.bitMask = bitMask;
			this.bitRightShiftCount = bitRightShiftCount;
		}

		public int bitMask() {
			return bitMask;
		}

		public int bitRightShiftCount() {
			return bitRightShiftCount;
		}

		public IntegerMask next() {

			int ordinal = this.ordinal();

			IntegerMask result = null;

			if (ordinal != FOURTH_BYTE.ordinal()) {
				result = IntegerMask.values()[ordinal + 1];
			}

			return result;
		}

	}
	
	
	public static void sort(int[] arr) {

		int index = sortBySign(arr);

		if (index == 0 || index == arr.length) {
			sort(arr, 0, arr.length, IntegerMask.FIRST_BYTE);
		} else {
			sort(arr, 0, index, IntegerMask.FIRST_BYTE);
			sort(arr, index, arr.length, IntegerMask.FIRST_BYTE);
		}

	}
	
	
	
	public static void sort(int[] arr, int low, int high, IntegerMask integerMask) {

//		handlePrint("low : " + low);
//		handlePrint("high : " + high);
//		handlePrint("Arr : " + Arrays.toString(arr));
		
//		printAsHexAndBinary(arr);
		
		ELEMENT_COUNT_V2 = ELEMENT_COUNT_V2 + (high - low);

		/*
		 * --redundant...
		 */
		if (((high - low) < 2) || (integerMask == null)) {
			return;
		}
		
		ATOMIC_INTEGER.incrementAndGet();
		ELEMENT_COUNT = ELEMENT_COUNT+(high-low);

		if ((high - low) <= INSERTION_SORT_THRESHOLD) {
			insertionSort(arr, low, high);
			return;
		}

		int bitMask = integerMask.bitMask();
		int bitShiftCount = integerMask.bitRightShiftCount();
		int[] radixByteCountsRelative = new int[BYTE_BUCKET_SIZE];
		int[] radixByteCountsTrack = new int[BYTE_BUCKET_SIZE];
		int[] radixByteCountsAbsolute = new int[BYTE_BUCKET_SIZE];
//		Arrays.fill(radixByteCountsAbsolute, -1);

		/*
		 * --//determine-range-boundaries
		 */
		for (int i = low; (i < high); i++) {

			int valTemp = arr[i];
			valTemp = valTemp & bitMask;
			valTemp = valTemp >>> bitShiftCount;
//			handlePrint("Val-Temp :" + valTemp);
			radixByteCountsRelative[valTemp]++;

		}

		radixByteCountsAbsolute[0] = low + radixByteCountsRelative[0];

		for (int i = 1; (i < BYTE_BUCKET_SIZE); i++) {

			radixByteCountsAbsolute[i] = radixByteCountsAbsolute[i - 1] + radixByteCountsRelative[i];

		}

//		handlePrint("Mask : " + integerMask);
//		handlePrint("Ranges - Relative : " + Arrays.toString(radixByteCountsRelative));
//		handlePrint("Ranges - Track : " + Arrays.toString(radixByteCountsTrack));
//		handlePrint("Ranges - Absolute : " + Arrays.toString(radixByteCountsAbsolute));

		/*
		 * --//swap-into-ranges...
		 */
		for (int i = low; (i < high); i++) {

			int valTemp = arr[i];

//			handlePrint("i : " + i + ", val : " + valTemp);

			valTemp = valTemp & bitMask;
			valTemp = valTemp >>> bitShiftCount;
			int placedCount = radixByteCountsTrack[valTemp];
			int offsetCount = radixByteCountsRelative[valTemp];
			int absoluteCount = radixByteCountsAbsolute[valTemp];
			int absoluteCountPrevious = (valTemp == 0) ? low : radixByteCountsAbsolute[valTemp - 1];

			int k = absoluteCount - offsetCount + placedCount;
//			int k = absoluteCountPrevious + placedCount - 1;

			/*
			 * handlePrint("low : " + low + " , offset-count : " + offsetCount +
			 * " , absolute-count : " + absoluteCount + ", .. placed-count : " + placedCount
			 * + ", k : " + k + " , val-temp : " + valTemp + ", absolute-count-previous :" +
			 * absoluteCountPrevious); // " , ... abs-val : " + radixByteCountsAbsolute[i]
			 */

//			System.out.println(
//					"k :" + k + ", i:" + i + " placed-count :" + placedCount + ", offsetCount :" + offsetCount);

			/*
			 * --//-for-k-equals-i-the-element-is-already-in-place
			 * --//-regarding-the-pre-positioned-elements-tracked-through-offset-counts-do-
			 * not-allow-count-to-exceed-max-counts...!!
			 */
			if ((k == i) || (placedCount == offsetCount)) { // k == radixByteCountsAbsolute[i]) ||

				if (k == i) {
					radixByteCountsTrack[valTemp]++; // --consider-elements-already-in-place-as-swaps..!!
				}

//				handlePrint("skipping..." + i);
				continue;

			} else {
				
				SWAP_COUNT.incrementAndGet();
				
				// --swap..

				int destVal = arr[k];
				arr[k] = arr[i];
				arr[i] = destVal;

				i--; // --revalidate-new-value-on-swap...

				radixByteCountsTrack[valTemp]++; // --track-the-swaps-only..

//				handlePrint("New-Arr : " + Arrays.toString(arr));
				
				

			}

		}

		/*
		 * --//-recursively-sort-for-remaining-radixes-across-ranges (at this point,
		 * every index from 0-255 has a range..)
		 */
		for (int i = 0; i < BYTE_BUCKET_SIZE; i++) {

			if (i == 0) {
				
				int tempHigh = radixByteCountsAbsolute[0];
				if (!((tempHigh - low) < 2) || (integerMask.next() == null)) {
					sort(arr, low, tempHigh, integerMask.next());
				}
//				sort(arr, low, tempHigh, integerMask.next());
				
			} else {
				int tempHighV2 = radixByteCountsAbsolute[i];
				int tempLowV2 = radixByteCountsAbsolute[i - 1];
				if (!((tempHighV2 - tempLowV2) < 2) || (integerMask.next() == null)) {
					sort(arr, tempLowV2, tempHighV2, integerMask.next());	
				}
//				sort(arr, tempLowV2, tempHighV2, integerMask.next());	
				
			}

		}

	}
	
	
	private static void insertionSort(int[] arr, int low, int high) {

		for (int i = low; i < (high - 1); i++) {
			int j = i;
			while ((j >= low) && (arr[j] > arr[j + 1])) {

				int temp = arr[j];
				arr[j] = arr[j + 1];
				arr[j + 1] = temp;
				j--;

			}
		}
	}
	
	/*
	 * --//use-one-iteration-of-quick-sort-to-split-positive-negative...negative first..
	 */
	private static int sortBySign(int[] arr) {

		int i = 0;
		int j = arr.length - 1;

		while (i <= j) {

			while ((i < (arr.length)) && (arr[i] < 0)) {
				i++;
			}

			while ((j >= 0) && (arr[j] >= 0)) {
				j--;
			}

//			handlePrint(" i : " + i + ", j : " + j + ", arr :" + Arrays.toString(arr));

			/*
			 * --swap-values-at-i-j
			 */
			if ((i < j) && (i >= 0) && (j < arr.length) && (arr[i] > arr[j])) {

				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;

			}
		}

		return i;

	}
	
	
	private static void handlePrint(Object message) {

		System.out.println(message + "");

	}
	
	

}
