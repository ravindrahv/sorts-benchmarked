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

import com.example.sorts.InPlaceCountingRadixHybridSort05ForJMH;
import com.example.sorts.QSortSAVPISortHybrid;
import com.example.sorts.QSortSAVPISortHybrid02;

/**
 * @author ravindra-hasyagar
 * @since 20-Dec-2025
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 1, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@Measurement(iterations = 1, timeUnit = TimeUnit.MILLISECONDS, time = 10000)
//@Fork(value = 2, jvmArgs = { "-Xms2g", "-Xmx2g", "-XX:+UseG1GC" })
public class BenchMarkTestQSortSAVPHybrid {
	/*
	private static final String[] JVM_ARGS = { "-Xms2g", "-Xmx2g", "-XX:+UseG1GC" };
	private static final int FORKS = 2;
	private static final int WARMUP_ITERATIONS = 5;
	private static final int MEASUREMENT_ITERATIONS = 10;
	*/

	int[] input;
	int[] arraysSortInput;
	int[] customSortInputType01;
	int[] customSortInputType02;
	
	int[] commonInput;

	@Param({ "1000000", "10000000", "100000000" })
//	@Param({ "100", "1000", "10000" })
	int size;

	public static void main(String[] args) throws IOException, RunnerException {

		
		/*
		 * --//--run-jmh-task-from-gradle...
		 */

		/*
		Options options = new OptionsBuilder().include(QSortSAVPISortHybrid.class.getSimpleName())
				.include(QSortSAVPISortHybrid02.class.getSimpleName()).build();
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
		SecureRandom random = new SecureRandom();
		int[] input = new int[size];

		for (int i = 0; i < size; i++) {
			input[i] = random.nextInt();
		}

		this.input = input;

	}
	
	
	@Setup(Level.Invocation)
	public void copy() {
		
		System.out.println("Executing copy..");
		
		/*
		arraysSortInput = new int[size];
		customSortInputType01 = new int[size];
		customSortInputType02 = new int[size];

		System.arraycopy(input, 0, arraysSortInput, 0, input.length);
		System.arraycopy(input, 0, customSortInputType01, 0, input.length);
		System.arraycopy(input, 0, customSortInputType02, 0, input.length);
		*/
		
		commonInput = new int[size];
		System.arraycopy(input, 0, commonInput, 0, input.length);
		
		arraysSortInput=commonInput;
		customSortInputType01=commonInput;
		customSortInputType02=commonInput;
		


		
	}

	@Benchmark
	public void javaArraySort() {
//		System.out.println("Executing java-array-so̧rt.... " + arraysSortInput);
		Arrays.sort(arraysSortInput);
//		QSortSAVPISortHybrid.sort(customSortInputType02);
	}

	@Benchmark
	public void quickSortWithVirtualMidPointPivot() {
//		System.out.println("Executing custom-sort..̧̧.. " + customSortInput);
//		InPlaceCountingRadixHybridSort05ForJMH.sort(customSortInput);
		QSortSAVPISortHybrid.sort(customSortInputType01);
	}
	
	@Benchmark
	public void quickSortWithVirutalMeanPivot() {
//		System.out.println("Executing custom-sort..̧̧.. " + customSortInput);
//		InPlaceCountingRadixHybridSort05ForJMH.sort(customSortInput);
		QSortSAVPISortHybrid02.sort(customSortInputType02);
	}

}
