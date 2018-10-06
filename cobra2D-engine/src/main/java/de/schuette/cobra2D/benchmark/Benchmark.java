package de.schuette.cobra2D.benchmark;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Benchmark implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected long nanoStart;
	protected long nanoEnd;

	protected boolean benchmarkRunning;
	protected boolean benchmarkReady;

	protected String title;
	protected String description;

	/**
	 * @param title
	 * @param description
	 */
	public Benchmark(final String title, final String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public void startBenchmark() {
		if (this.benchmarkRunning) {
			throw new IllegalStateException("Benchmark already running.");
		}
		this.benchmarkReady = false;
		this.benchmarkRunning = true;
		this.nanoStart = System.nanoTime();
	}

	public void stopBenchmark() {
		if (!this.benchmarkRunning) {
			throw new IllegalStateException(
					"Benchmark not running. Start it before stopping.");
		}
		this.nanoEnd = System.nanoTime();
		this.benchmarkRunning = false;
		this.benchmarkReady = true;
	}

	public long getNanoDifference() {
		if (!this.benchmarkReady) {
			throw new IllegalStateException("Benchmark was not performed!");
		}
		return this.nanoEnd - this.nanoStart;
	}

	public double getMillisecondsDifference() {
		final long diff = this.getNanoDifference();
		return diff / 1000000.0;
	}

	public String getFormattedDifference() {
		try {
			return this.title + " was running "
					+ TimeUnit.NANOSECONDS.toMillis(this.getNanoDifference())
					+ "ms.";
		} catch (final IllegalStateException e) {
			return this.title + " is still running.";
		}
	}

	public String getBenchmarkInformation() {
		return this.getFormattedDifference() + "\n\t" + this.description;
	}

	@Override
	public String toString() {
		return this.getBenchmarkInformation();
	}

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public long getNanoStart() {
		return this.nanoStart;
	}

	public long getNanoEnd() {
		return this.nanoEnd;
	}

}
