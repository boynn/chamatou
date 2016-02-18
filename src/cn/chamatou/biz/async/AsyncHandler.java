package cn.chamatou.biz.async;

/**
 * @author shiner
 */
public class AsyncHandler {
	private boolean cancel = false;

	public boolean canceled() {
		return cancel;
	}

	public void cancel() {
		this.cancel = true;
	}

}
