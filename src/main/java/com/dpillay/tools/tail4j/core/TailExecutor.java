package com.dpillay.tools.tail4j.core;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TailExecutor {
	public <T, S> void execute(List<TailedReader<T, S>> tailedFiles,
			TailPrinter<T> printer) {
		ExecutorService executor = Executors.newFixedThreadPool(tailedFiles
				.size() + 1);
		for (TailedReader<T, S> tailedFile : tailedFiles) {
			executor.submit(tailedFile);
		}
		executor.submit(printer);
	}
}
