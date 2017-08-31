package com.rz.common.widget;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 3D旋转动画
 */
public class Rotate3dAnimation extends Animation {

	private final float mCenterX;

	private final float mCenterY;

	private Camera mCamera;

	public Rotate3dAnimation(float centerX, float centerY) {
		this.mCenterX = centerX;
		this.mCenterY = centerY;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		float degrees = 360 * interpolatedTime;
		float centerX = mCenterX;
		float centerY = mCenterY;
		Camera camera = mCamera;
		Matrix matrix = t.getMatrix();
		// 将当前的摄像头位置保存下来，以便变换进行完成恢复成原位
		camera.save();

		// 是给我们的View加上旋转效果，在移动的过程中，视图还会移X轴为中心进行旋转。
		camera.rotateY(degrees);

		// 这个是将我们刚才定义的一系列变换应用到变换矩阵上面，调用完这句之后，我们就可以将camera的位置恢复了，以便下一次再使用。
		camera.getMatrix(matrix);
		// camera位置恢复
		camera.restore();

		// 以View的中心点为旋转中心,如果不加这两句，就是以（0,0）点为旋转中心
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}
