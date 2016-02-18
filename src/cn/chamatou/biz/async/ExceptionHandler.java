package cn.chamatou.biz.async;

import java.io.EOFException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.HttpException;

import android.content.Context;

import cn.chamatou.biz.AppContext;
import cn.chamatou.biz.R;

/**
 * @author shiner
 */
public class ExceptionHandler {
	public static void handleException(Context ctx, Result<?> result) {
		if (result.getError() == Result.OK) {
			result.setError(-1);
		}
		Throwable t = result.getThrowable();
		if (t == null) {
			t = new Exception(AppContext.self.getString(R.string.unknown_error));
			result.setThrowable(t);
		}
		String clsName = t.getClass().getName();
		if (t instanceof Exception) {
			result.setErrorMsg(t.getMessage());
		} else if (clsName.startsWith("java.net")
				|| t instanceof SocketTimeoutException
				|| t instanceof UnknownHostException
				|| t instanceof EOFException || t instanceof HttpException) {
			result.setErrorMsg(ctx.getString(R.string.net_error));
		} else if (result.getErrorMsg() == null) {
			result.setErrorMsg(AppContext.self.getString(R.string.unknown_error)
					+ "\r\n" + t.getMessage());
		}
	}

	public static String getErrorMsg(Context ctx, Throwable t) {
		String msg = null;
		String clsName = t.getClass().getName();
		if (t instanceof Exception) {
			msg = t.getMessage();
		} else if (clsName.startsWith("java.net")
				|| clsName.startsWith("org.apache")
				|| t instanceof EOFException) {
			msg = ctx.getString(R.string.net_error);
		} else {
			msg = AppContext.self.getString(R.string.unknown_error) + "\r\n"
					+ t.getMessage();
		}
		return msg;
	}
}
