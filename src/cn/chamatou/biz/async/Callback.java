package cn.chamatou.biz.async;

public abstract class Callback<T> {
	public void onStart() {
	};

	abstract public void onHandle(T param);

	public void onComplete() {
	};
}
