package com.yryz.yunxinim.uikit.common.media.audioplayer;

public interface Playable {
	long getDuration();
	String getPath();
	boolean isAudioEqual(Playable audio);
}