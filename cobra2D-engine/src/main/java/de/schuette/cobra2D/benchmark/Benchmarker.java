package de.schuette.cobra2D.benchmark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Benchmarker implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Benchmarker instance;

	public static Benchmarker getInstance() {
		if (Benchmarker.instance == null) {
			Benchmarker.instance = new Benchmarker();
		}
		return Benchmarker.instance;
	}

	protected ArrayList<Benchmark> benchmarks;

	public Benchmarker() {
		this.benchmarks = new ArrayList<Benchmark>();
	}

	/**
	 * Returns a copy as a new instance of this benchmark.
	 * 
	 * @return
	 */
	public Benchmarker getSnapshot() {
		final Benchmarker newBench = new Benchmarker();
		newBench.benchmarks = new ArrayList<Benchmark>(this.benchmarks);
		return newBench;
	}

	public Benchmark createBenchmark(final String title,
			final String description) {
		final Benchmark bench = new Benchmark(title, description);
		this.benchmarks.add(bench);
		return bench;
	}

	public Benchmark createBenchmarkAndStart(final String title,
			final String description) {
		final Benchmark bench = this.createBenchmark(title, description);
		bench.startBenchmark();
		return bench;
	}

	protected int getNextBenchmarkIndex() {
		final int index = this.benchmarks.size();
		return index;
	}

	public void clear() {
		this.benchmarks.clear();
	}

	public List<Benchmark> getBenchmarks() {
		return benchmarks;
	}

	public String getBenchmarkSummary() {
		String summary = "Benchmarking-Summary: \n";
		for (int i = 0; i < this.benchmarks.size(); i++) {
			final Benchmark bench = this.benchmarks.get(i);
			summary += bench.getBenchmarkInformation() + "\n";
		}
		return summary;
	}

	@Override
	public String toString() {
		return this.getBenchmarkSummary();
	}

	public void startAllBenchmarks() {
		for (int i = 0; i < this.benchmarks.size(); i++) {
			final Benchmark bench = this.benchmarks.get(i);
			bench.startBenchmark();
		}
	}

	public void stopAllBenchmarks() {
		for (int i = 0; i < this.benchmarks.size(); i++) {
			final Benchmark bench = this.benchmarks.get(i);
			bench.stopBenchmark();
		}
	}
}
