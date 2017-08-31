package com.rz.httpapi.bean;

/**
 * 
 */
public class PicModel {

	private boolean isSelect;

	private String picPath;

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	@Override
	public String toString() {
		return "PicModel [isSelect=" + isSelect + ", picPath=" + picPath + "]";
	}
}
