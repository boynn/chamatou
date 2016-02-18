package cn.chamatou.biz.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

/**
 * @author shiner
 */
public class AsyncUtil {
	private static final String TAG = "AsyncUtil";

	private static final Handler handler = new Handler(Looper.getMainLooper());

	public static <T> AsyncHandler goAsync(final Callable<T> callable,
			final Callback<T> callback) {
		final AsyncHandler h = new AsyncHandler();
		new Thread() {
			public void run() {
				try {
					onStart(callback);
					T result = callable.call();

					if (!h.canceled()) {
						handle(callback, result);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					complete(callback);
				}
			};
		}.start();
		return h;

	}

	public static <T> void complete(final Callback<T> callback) {
		if (callback == null)
			return;
		if (Looper.getMainLooper().getThread().getId() == Thread
				.currentThread().getId()) {
			callback.onComplete();
		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					callback.onComplete();
				}
			});
		}

	}

	public static <T> void handle(final Callback<T> callback, final T data) {
		if (callback == null)
			return;
		if (Looper.getMainLooper().getThread().getId() == Thread
				.currentThread().getId()) {
			callback.onHandle(data);
		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					callback.onHandle(data);
				}
			});
		}

	}

	public static <T> void onStart(final Callback<T> callback) {
		if (callback == null)
			return;
		if (Looper.getMainLooper().getThread().getId() == Thread
				.currentThread().getId()) {
			callback.onStart();
		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					callback.onStart();
				}
			});
		}
	}

	private static ExecutorService es = Executors.newFixedThreadPool(8);

	public static <T> AsyncHandler goAsyncInPool(final Callable<T> callable,
			final Callback<T> callback) {
		AsyncHandler h = new AsyncHandler();
		es.submit(new ExecuteCallable<T>(callable, callback, h));
		return h;
	}

	private static class ExecuteCallable<T> implements Runnable {
		private Callable<T> task;
		private Callback<T> callback;
		private AsyncHandler h;

		public ExecuteCallable(Callable<T> task, Callback<T> callback,
				AsyncHandler h) {
			super();
			this.task = task;
			this.callback = callback;
			this.h = h;
		}

		@Override
		public void run() {
			try {
				onStart(callback);
				T result = task.call();

				if (!h.canceled()) {
					handle(callback, result);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				complete(callback);
			}
		}
	}
}
