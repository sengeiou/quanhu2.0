/*
 * Copyright 2013, Edmodo, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" 
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */

package com.appyvet.rangebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.TypedValue;

/**
 * This class represents the underlying gray bar in the RangeBar (without the
 * thumbs).
 */
public class Bar {

    // Member Variables ////////////////////////////////////////////////////////

    private final Paint mBarPaint;

    private final Paint mBarEndPaint;

    private final Paint mTickPaint;

    // Left-coordinate of the horizontal bar.
    private final float mLeftX;

    private final float mRightX;

    private final float mY;

    private int mNumSegments;

    private float mTickDistance;

    private final float mTickHeight;

    private final boolean isLine;

    // Constructor /////////////////////////////////////////////////////////////


    /**
     * Bar constructor
     *
     * @param ctx          the context
     * @param x            the start x co-ordinate
     * @param y            the y co-ordinate
     * @param length       the length of the bar in px
     * @param tickCount    the number of ticks on the bar
     * @param tickHeightDP the height of each tick
     * @param tickColor    the color of each tick
     * @param barWeight    the weight of the bar
     * @param barColor     the color of the bar
     */
    public Bar(Context ctx,
               float x,
               float y,
               float length,
               int tickCount,
               float tickHeightDP,
               int tickColor,
               boolean tickLine,
               float barWeight,
               int barColor) {

        mLeftX = x;
        mRightX = x + length;
        mY = y;

        isLine = tickLine;

        mNumSegments = tickCount - 1;
        mTickDistance = length / mNumSegments;
//        mTickHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                tickHeightDP,
//                ctx.getResources().getDisplayMetrics());
        mTickHeight = tickHeightDP;

        // Initialize the paint.
        mBarPaint = new Paint();
        mBarPaint.setColor(barColor);
        mBarPaint.setStrokeWidth(barWeight);
        mBarPaint.setAntiAlias(true);

        mBarEndPaint = new Paint();
        mBarEndPaint.setColor(barColor);
        mBarEndPaint.setStrokeWidth(barWeight);
        mBarEndPaint.setAntiAlias(true);

        mTickPaint = new Paint();
        mTickPaint.setColor(tickColor);
        mTickPaint.setStrokeWidth(barWeight);
        if (isLine)
            mTickPaint.setStrokeWidth((float) 5.0);
        mTickPaint.setAntiAlias(true);
    }

    // Package-Private Methods /////////////////////////////////////////////////

    /**
     * Draws the bar on the given Canvas.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     *               View#onDraw()}
     */
    public void draw(Canvas canvas) {
        canvas.drawCircle(mLeftX, mY, RadiusUtils.getRadius(mTickHeight), mBarEndPaint);
        canvas.drawLine(mLeftX, mY, mRightX, mY, mBarPaint);
        canvas.drawCircle(mRightX, mY, RadiusUtils.getRadius(mTickHeight), mBarEndPaint);
    }

    /**
     * Get the x-coordinate of the left edge of the bar.
     *
     * @return x-coordinate of the left edge of the bar
     */
    public float getLeftX() {
        return mLeftX;
    }

    /**
     * Get the x-coordinate of the right edge of the bar.
     *
     * @return x-coordinate of the right edge of the bar
     */
    public float getRightX() {
        return mRightX;
    }

    /**
     * Gets the x-coordinate of the nearest tick to the given x-coordinate.
     *
     * @param thumb the thumb to find the nearest tick for
     * @return the x-coordinate of the nearest tick
     */
    public float getNearestTickCoordinate(PinView thumb) {

        final int nearestTickIndex = getNearestTickIndex(thumb);

        return mLeftX + (nearestTickIndex * mTickDistance);
    }

    /**
     * Gets the zero-based index of the nearest tick to the given thumb.
     *
     * @param thumb the Thumb to find the nearest tick for
     * @return the zero-based index of the nearest tick
     */
    public int getNearestTickIndex(PinView thumb) {

        return (int) ((thumb.getX() - mLeftX + mTickDistance / 2f) / mTickDistance);
    }


    /**
     * Set the number of ticks that will appear in the RangeBar.
     *
     * @param tickCount the number of ticks
     */
    public void setTickCount(int tickCount) {

        final float barLength = mRightX - mLeftX;

        mNumSegments = tickCount - 1;
        mTickDistance = barLength / mNumSegments;
    }

    // Private Methods /////////////////////////////////////////////////////////

    /**
     * Draws the tick marks on the bar.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     *               View#onDraw()}
     */
    public void drawTicks(Canvas canvas) {
        if (isLine) {
            // Loop through and draw each tick (except final tick).
            for (int i = 1; i < mNumSegments; i++) {
                final float x = i * mTickDistance + mLeftX;
                canvas.drawLine(x, mY - mTickHeight / 2, x, mY + mTickHeight / 2, mTickPaint);
            }
        } else {
            // Loop through and draw each tick (except final tick).
            for (int i = 0; i < mNumSegments; i++) {
                final float x = i * mTickDistance + mLeftX;
                canvas.drawCircle(x, mY, mTickHeight, mTickPaint);
            }
            // Draw final tick. We draw the final tick outside the loop to avoid any
            // rounding discrepancies.
            canvas.drawCircle(mRightX, mY, mTickHeight, mTickPaint);
        }
    }
}
