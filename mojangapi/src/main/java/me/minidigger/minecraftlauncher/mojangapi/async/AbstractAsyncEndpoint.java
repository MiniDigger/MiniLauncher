package me.minidigger.minecraftlauncher.mojangapi.async;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AbstractAsyncEndpoint {

	private static ExecutorService service = Executors.newFixedThreadPool(10);

	protected static <O> CompletableFuture<O> get(CheckedSupplier<O> method) {
		CompletableFuture<O> future = new CompletableFuture<>();

		service.execute(() -> {
			try {
				O output = method.supply();
				future.complete(output);
			} catch (IOException e) {
				future.completeExceptionally(e);
			}
		});

		return future;
	}

	protected static <I, O> CompletableFuture<O> get(I input, CheckedFunction<I, O> method) {
		CompletableFuture<O> future = new CompletableFuture<>();

		service.execute(() -> {
			try {
				O output = method.apply(input);
				future.complete(output);
			} catch (IOException e) {
				future.completeExceptionally(e);
			}
		});

		return future;
	}

	protected static <I1, I2, O> CompletableFuture<O> get(I1 input1, I2 input2, CheckedBiFunction<I1, I2, O> method) {
		CompletableFuture<O> future = new CompletableFuture<>();

		service.execute(() -> {
			try {
				O output = method.apply(input1, input2);
				future.complete(output);
			} catch (IOException e) {
				future.completeExceptionally(e);
			}
		});

		return future;
	}

	protected static <I> CompletableFuture<Void> getVoid(I input, CheckedConsumer<I> method) {
		CompletableFuture<Void> future = new CompletableFuture<>();

		service.execute(() -> {
			try {
				method.consume(input);
				future.complete(null);
			} catch (IOException e) {
				future.completeExceptionally(e);
			}
		});

		return future;
	}

	protected static <I1, I2> CompletableFuture<Void> getVoid(I1 input1, I2 input2, CheckedBiConsumer<I1, I2> method) {
		CompletableFuture<Void> future = new CompletableFuture<>();

		service.execute(() -> {
			try {
				method.consume(input1, input2);
				future.complete(null);
			} catch (IOException e) {
				future.completeExceptionally(e);
			}
		});

		return future;
	}

	@FunctionalInterface
	public interface CheckedSupplier<O> {
		O supply() throws IOException;
	}

	@FunctionalInterface
	public interface CheckedFunction<I, O> {
		O apply(I input) throws IOException;
	}

	@FunctionalInterface
	public interface CheckedBiFunction<I1, I2, O> {
		O apply(I1 input1, I2 input2) throws IOException;
	}

	@FunctionalInterface
	public interface CheckedConsumer<I> {
		void consume(I input) throws IOException;
	}

	@FunctionalInterface
	public interface CheckedBiConsumer<I1, I2> {
		void consume(I1 input1, I2 input2) throws IOException;
	}
}
