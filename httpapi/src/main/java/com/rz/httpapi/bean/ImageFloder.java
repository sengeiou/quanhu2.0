package com.rz.httpapi.bean;

/**
 * 图片文件夹
 */
public class ImageFloder {

	/**
	 * 图片的文件夹路径
	 */
	private String mPicDir;

	/**
	 * 第一张图片的路径
	 */
	private String mFirstPicPath;

	/**
	 * 文件夹的名称
	 */
	private String mPicName;

	/**
	 * 图片的数量
	 */
	private int mPicCount;

	/**
	 * 第几行被选中
	 */
	private int mPicSelect;

	/**
	 * 图片的文件夹路径
	 */
	public String getmPicDir() {
		return mPicDir;
	}

	/**
	 * 设置图片的文件夹路径以及添加文件夹后缀名
	 */
	public void setmPicDir(String mPicDir) {
		this.mPicDir = mPicDir;
		int lastIndexOf = this.mPicDir.lastIndexOf("/");
		this.mPicName = this.mPicDir.substring(lastIndexOf);
	}

	/**
	 * 第一张图片的路径,以显示图片封图
	 */
	public String getmFirstPicPath() {
		return mFirstPicPath;
	}

	/**
	 * 设置第一张图片的路径,以显示图片封图
	 */
	public void setmFirstPicPath(String mFirstPicPath) {
		this.mFirstPicPath = mFirstPicPath;
	}

	public void setmPicName(String mPicName) {
		this.mPicName = mPicName;
	}

	public String getmPicName() {
		return mPicName;
	}

	public int getmPicCount() {
		return mPicCount;
	}

	public void setmPicCount(int mPicCount) {
		this.mPicCount = mPicCount;
	}

	public int getmPicSelect() {
		return mPicSelect;
	}

	public void setmPicSelect(int mPicSelect) {
		this.mPicSelect = mPicSelect;
	}
}
