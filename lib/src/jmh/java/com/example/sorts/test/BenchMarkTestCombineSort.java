package com.example.sorts.test;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.example.sorts.CombineSortElemental03;
import com.example.sorts.CombineSortElemental04;
import com.example.sorts.CombineSortElemental05;
import com.example.sorts.CombineSortElemental06;

/**
 * @author ravindra-hasyagar
 * @since 06-Jan-2026
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 1, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@Measurement(iterations = 1, timeUnit = TimeUnit.MILLISECONDS, time = 10000)
//@Fork(value = 2, jvmArgs = { "-Xms2g", "-Xmx2g", "-XX:+UseG1GC" })
public class BenchMarkTestCombineSort {
	/*
	private static final String[] JVM_ARGS = { "-Xms2g", "-Xmx2g", "-XX:+UseG1GC" };
	private static final int FORKS = 2;
	private static final int WARMUP_ITERATIONS = 5;
	private static final int MEASUREMENT_ITERATIONS = 10;
	*/

	int[] input;
	
	int[] arraysSortInput;
	int[] customSort01Input;
	int[] customSort02Input;
	int[] customSort03Input;
	int[] customSort04Input;
//	int[] customSort05Input;
	
	int[] commonInput;

	@Param({ "1000000", "10000000", "100000000" })
//	@Param({ "100", "1000", "10000" })
	int size;
	
	private enum INPUT_TYPES {
		RANDOM, REVERSE;
	}
	
	@Param({"RANDOM", "REVERSE"})
	String types;

	public static void main(String[] args) throws IOException, RunnerException {
		
		/*
		 * --//--run-jmh-task-from-gradle...
		 */

		/*
		Options options = new OptionsBuilder().include(CombineSortElemental03.class.getSimpleName())
				.include(CombineSortElemental04.class.getSimpleName())
				.include(CombineSortElemental05.class.getSimpleName())
				.include(CombineSortElemental06.class.getSimpleName()).build();
		Runner runner = new Runner(options);
		Collection<RunResult> result = runner.run();

		if (result != null) {
			for (RunResult runResult : result) {
				String resultString = runResult.getPrimaryResult().toString();
				System.out.println(resultString);
			}
		}
		*/

	}

	@Setup(Level.Iteration)
	public void setup() {
		System.out.println("Executing setup ...");

		if(INPUT_TYPES.RANDOM.name().equals(types)) {
			this.input = generateInput(INPUT_TYPES.RANDOM);
		} else {
			this.input = generateInput(INPUT_TYPES.REVERSE);
		}
		

	}
	
	private int[] generateInput(INPUT_TYPES types) {

		int[] input = new int[size];

		if (types == INPUT_TYPES.RANDOM) {

			SecureRandom random = new SecureRandom();

			for (int i = 0; i < size; i++) {
				input[i] = random.nextInt();
			}

		} else {

			for (int i = 0; i < size; i++) {
				input[i] = Integer.MAX_VALUE - i;
			}
		}

		return input;
	}
	
	/*
	@Setup(Level.Iteration)
	public void setupAlternating() {
		System.out.println("Executing alternating setup ...");
//		SecureRandom random = new SecureRandom();
		int[] input = new int[size];

		for (int i = 0; i < size; i++) {
			input[i] = (i % 2 == 0) ? i : (size - i);
		}

		this.input = input;

	}
	*/
	
	
	@Setup(Level.Invocation)
	public void copy() {
		
		System.out.println("Executing copy..");
		
		commonInput = new int[size];
		/*
		arraysSortInput = new int[size];
		customSort01Input = new int[size];
		customSort02Input = new int[size];
		customSort03Input = new int[size];
		*/

		/*
		System.arraycopy(input, 0, arraysSortInput, 0, input.length);
		System.arraycopy(input, 0, customSort01Input, 0, input.length);
		System.arraycopy(input, 0, customSort02Input, 0, input.length);
		System.arraycopy(input, 0, customSort03Input, 0, input.length);
		*/
		
		System.arraycopy(input, 0, commonInput, 0, input.length);
		
		
		arraysSortInput = commonInput;
		customSort01Input = commonInput;
		customSort02Input = commonInput;
		customSort03Input = commonInput;
		customSort04Input = commonInput;
//		customSort05Input = commonInput;
		

		
	}

	@Benchmark
	public void javaArraySort() {
//		System.out.println("Executing java-array-so̧rt.... " + arraysSortInput);
		Arrays.sort(arraysSortInput);
	}

	/* 
	 * 
	 * --*/
	
	@Benchmark
	public void combineSortV1InsertionSortOptimised() {
//		System.out.println("Executing custom-sort..̧̧.. " + customSortInput);
		CombineSortElemental03.sort(customSort01Input);
	}
	
	
	
/*
 * 
 * ---//--no-noticable-improvement.. (2-3 sec gain .. 17sec instead of 19sec for 100m)
 * 
 * 
 *  /* -- */
	
	@Benchmark
	public void combineSortV2InsertionAndStandardSortOptimised() {
//		System.out.println("Executing custom-sort..̧̧.. " + customSortInput);
//		InPlaceCountingRadixHybridSort05ForJMH.sort(customSortInput);
		CombineSortElemental04.sort(customSort02Input);
	}
	
	
	@Benchmark
	public void combineSortV3ParallisedAlongWithInsertionSortOptimization() {
//		System.out.println("Executing custom-sort..̧̧.. " + customSortInput);
//		InPlaceCountingRadixHybridSort05ForJMH.sort(customSortInput);
		CombineSortElemental05.sort(customSort03Input);
	}
	/*--*/
	
	
	@Benchmark
	public void combineSortV5InsertionSortAndWedgeSortOptimised() {
		CombineSortElemental06.sort(customSort04Input);
	}

}
