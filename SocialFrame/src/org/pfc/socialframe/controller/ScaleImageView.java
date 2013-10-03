package org.pfc.socialframe.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ScaleImageView extends ImageView implements OnTouchListener {
        private float MAX_SCALE = 3f;
        private int DOUBLE_TAP_SECOND = 400;
        private Matrix mMatrix;
        private final float[] mMatrixValues = new float[9];
        private int mWidth;
        private int mHeight;
        private int mIntrinsicWidth;
        private int mIntrinsicHeight;
        private float mScale;
        private float mMinScale;
        private long mLastTime = 0;
        private boolean isDoubleTap;
        private int mDoubleTapX;
        private int mDoubleTapY;
        private float mPrevDistance;
        private boolean isScaling;
        private int mPrevMoveX;
        private int mPrevMoveY;
        //Imagen scalable
        public ScaleImageView(Context context, AttributeSet attr) {
                super(context, attr);
                initialize();
        }
        public ScaleImageView(Context context) {
                super(context);
                initialize();
        }
        @Override
        public void setImageBitmap(Bitmap bm) {
                super.setImageBitmap(bm);
                this.initialize();
        }
        //Inicializar matriz para hacer zoom
        private void initialize() {
                this.setScaleType(ScaleType.MATRIX);
                this.mMatrix = new Matrix();
                Drawable d = getDrawable();
                if (d != null) {
                        mIntrinsicWidth = d.getIntrinsicWidth();
                        mIntrinsicHeight = d.getIntrinsicHeight();
                        setOnTouchListener(this);
                }
        }
        @Override
        protected boolean setFrame(int l, int t, int r, int b) {
                mWidth = r-l;
                mHeight = b-t;
                mMatrix.reset();
                mScale = (float) r / (float) mIntrinsicWidth;
                int paddingHeight = 0;
                int paddingWidth = 0;
                // escalado vertical
                if (mScale * mIntrinsicHeight > mHeight) {
                        mScale = (float) mHeight / (float) mIntrinsicHeight;
                        mMatrix.postScale(mScale, mScale);
                        paddingWidth = (r - mWidth) / 2;
                        paddingHeight = 0;
                        // escalado horizontal
                } else {
                        mMatrix.postScale(mScale, mScale);
                        paddingHeight = (b - mHeight) / 2;
                        paddingWidth = 0;
                }
                mMatrix.postTranslate(paddingWidth, paddingHeight);
                setImageMatrix(mMatrix);
                mMinScale = mScale;
                zoomTo(mScale, mWidth / 2, mHeight / 2);
                cutting();
                return super.setFrame(l, t, r, b);
        }
        protected float getValue(Matrix matrix, int whichValue) {
                matrix.getValues(mMatrixValues);
                return mMatrixValues[whichValue];
        }
        protected float getScale() {
                return getValue(mMatrix, Matrix.MSCALE_X);
        }
        protected float getTranslateX() {
                return getValue(mMatrix, Matrix.MTRANS_X);
        }
        protected float getTranslateY() {
                return getValue(mMatrix, Matrix.MTRANS_Y);
        }
        protected void maxZoomTo(int x, int y) {
                if (mMinScale != getScale() && (getScale() - mMinScale) > 0.1f) {
                        float scale = mMinScale / getScale();
                        zoomTo(scale, x, y);
                } else {
                        float scale = MAX_SCALE / getScale();
                        zoomTo(scale, x, y);
                }
        }
        //Realizar zoom
        protected void zoomTo(float scale, int x, int y) {
                if (getScale() * scale < mMinScale) {
                        return;
                }
                if (scale >= 1 && getScale() * scale > MAX_SCALE) {
                        return;
                }
                mMatrix.postScale(scale, scale);
                mMatrix.postTranslate(-(mWidth * scale - mWidth) / 2,-(mHeight * scale - mHeight) / 2);
                mMatrix.postTranslate(-(x - (mWidth / 2)) * scale, 0);
                mMatrix.postTranslate(0, -(y - (mHeight / 2)) * scale);
                setImageMatrix(mMatrix);
        }
        public void cutting() {
                int width = (int) (mIntrinsicWidth * getScale());
                int height = (int) (mIntrinsicHeight * getScale());
                if (getTranslateX() < -(width - mWidth)) {
                        mMatrix.postTranslate(-(getTranslateX() + width - mWidth), 0);
                }
                if (getTranslateX() > 0) {
                        mMatrix.postTranslate(-getTranslateX(), 0);
                }
                if (getTranslateY() < -(height - mHeight)) {
                        mMatrix.postTranslate(0, -(getTranslateY() + height - mHeight));
                }
                if (getTranslateY() > 0) {
                        mMatrix.postTranslate(0, -getTranslateY());
                }
                if (width < mWidth) {
                        mMatrix.postTranslate((mWidth - width) / 2, 0);
                }
                if (height < mHeight) {
                        mMatrix.postTranslate(0, (mHeight - height) / 2);
                }
                setImageMatrix(mMatrix);
        }
        private float distance(float x0, float x1, float y0, float y1) {
                float x = x0 - x1;
                float y = y0 - y1;
                return FloatMath.sqrt(x * x + y * y);
        }
        //Evento para hacer zoom
        @Override
        public boolean onTouchEvent(MotionEvent event) {
                int touchCount = event.getPointerCount();
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_1_DOWN:
                case MotionEvent.ACTION_POINTER_2_DOWN:
                	//Se ha pulsado dos veces
                        if (touchCount >= 2) {
                                float distance = distance(event.getX(0), event.getX(1),event.getY(0), event.getY(1));
                                mPrevDistance = distance;
                                isScaling = true;
                        } else {
                                if (System.currentTimeMillis() <= mLastTime + DOUBLE_TAP_SECOND) {
                                        if (30 > Math.abs(mPrevMoveX - event.getX())+ Math.abs(mPrevMoveY - event.getY())) {
                                                isDoubleTap = true;
                                                mDoubleTapX = (int) event.getX();
                                                mDoubleTapY = (int) event.getY();
                                        }
                                }
                                mLastTime = System.currentTimeMillis();
                                mPrevMoveX = (int) event.getX();
                                mPrevMoveY = (int) event.getY();
                        }
                        break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_POINTER_2_UP:
                	//Se ha vuelto a pulsar dos veces
                        if (event.getPointerCount() <= 1) {
                                isScaling = false;
                                if (isDoubleTap) {
                                        if (30 > Math.abs(mDoubleTapX - event.getX())+ Math.abs(mDoubleTapY - event.getY())) {
                                                maxZoomTo(mDoubleTapX, mDoubleTapY);
                                                cutting();
                                        }
                                }
                        }
                        isDoubleTap = false;
                        break;
                }
                return true;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
                return super.onTouchEvent(event);
        }
}
