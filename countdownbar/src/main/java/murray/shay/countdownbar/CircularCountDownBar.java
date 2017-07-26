package murray.shay.countdownbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Simple single android view component that can be used to showing a circular count down bar.
 * It can be customized with size, stroke size, colors and text etc.
 * Progress(time seconds) change will be animated.
 * Created by Murray Shay
 */
public class CircularCountDownBar extends View {
    private final static String LT = CircularCountDownBar.class.getSimpleName();
    private int mViewWidth;
    private int mViewHeight;

    private final float mStartAngle = -90;      // Always start from top (default is: "3 o'clock on a watch.")
    private float mSweepAngle = 0;              // How long to sweep from mStartAngle
    private float mMaxSweepAngle = 360;         // Max degrees to sweep = full circle
    private int mAnimationDuration = 400;       // Animation duration for progress change

    private int mMaxProgress;              // Max progress to use  (has setter countdown second)
    private int mStrokeWidth;              // Width of outline (has setter)
    private boolean mDrawText;           // Set to true if progress text should be drawn (has setter)
    private boolean mRoundedCorners;     // Set to true if rounded corners should be applied to outline ends (has setter)
    private int mProgressColor = Color.BLACK;   // Outline color (has setter)
    private int mTextColor = Color.BLACK;       // Progress text color (has setter)
    private int paddingTop = 0;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingBottom = 0;
    private final int paddingOffset = 10;

    private RectF outerOval;
    private final Paint mCircularPaint = new Paint();
    private final Paint mSecondPaint = new Paint();

    public static final class Builder {
        private Context mContext;
        private int mMaxProgress = 10;
        private int mStrokeWidth = 10;
        private boolean mDrawText = true;
        private boolean mRoundedCorners = true;
        private int mProgressColor = Color.BLACK;
        private int mTextColor = Color.BLACK;
        private int paddingTop = 5;
        private int paddingLeft = 5;
        private int paddingRight = 0;
        private int paddingBottom = 0;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setMaxProgress(int maxProgress) {
            mMaxProgress = maxProgress;
            return this;
        }

        public Builder setStrokeWidth(int strokeWidth) {
            mStrokeWidth = strokeWidth;
            return this;
        }

        /**
         * Toggle this if you don't want rounded corners on circular count down progress.
         * Default is true.
         * @param drawTextEnable true if you want to show count down text of false otherwise.
         */
        public Builder setDrawText(boolean drawTextEnable) {
            mDrawText = drawTextEnable;
            return this;
        }

        /**
         * Toggle this if you don't want rounded corners on circular count down progress.
         * Default is true.
         * @param roundedCorners true if you want rounded corners of false otherwise.
         */
        public Builder setRoundedCorners(boolean roundedCorners) {
            mRoundedCorners = roundedCorners;
            return this;
        }

        public Builder setProgressColor(int progressColor) {
            mProgressColor = progressColor;
            return this;
        }

        public Builder setTextColor(int textColor) {
            mTextColor = textColor;
            return this;
        }

        public Builder setPadding(int top, int bottom, int left, int right) {
            paddingTop = top;
            paddingLeft = bottom;
            paddingRight = left;
            paddingBottom = right;
            return this;
        }

        public CircularCountDownBar build() {
            return new CircularCountDownBar(this);
        }
    }

    private CircularCountDownBar(Builder builder) {
        super(builder.mContext);
        mMaxProgress = builder.mMaxProgress;
        mSweepAngle = calcSweepAngleFromProgress(mMaxProgress);
        mStrokeWidth = builder.mStrokeWidth;
        mDrawText = builder.mDrawText;
        mRoundedCorners = builder.mRoundedCorners;
        mProgressColor = builder.mProgressColor;
        mTextColor = builder.mTextColor;
        paddingTop = builder.paddingTop + paddingOffset;
        paddingLeft = builder.paddingLeft + paddingOffset;
        paddingRight = builder.paddingRight;
        paddingBottom = builder.paddingBottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(LT,"Call onMeasure(widthMeasureSpec, heightMeasureSpec)");

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width < height){
            height = width;
        } else {
            width = height;
        }

        setMeasuredDimension(width,height);
    }

    @Deprecated
    private int getEdgeSize(int defaultSize, int measureSpec){
        int sideLength = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode){
            case MeasureSpec.UNSPECIFIED:
                Log.i(LT,"MeasureSpec.UNSPECIFIED");
                sideLength = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                Log.i(LT,"MeasureSpec.AT_MOST");
                sideLength = size;
                break;
            case MeasureSpec.EXACTLY:
                Log.i(LT,"MeasureSpec.EXACTLY");
                sideLength = size;
                break;
        }

        return sideLength;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(LT, "Call onSizeChanged(w:" + w + ", h:" + h + ", oldw:" + oldw + ", oldh:" + oldh + ")");
        mViewWidth = w;
        mViewHeight = h;

        setupPaints();
        setupBounds();
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        mCircularPaint.setColor(mProgressColor);
        mCircularPaint.setStrokeWidth(mStrokeWidth);
        mCircularPaint.setAntiAlias(true);
        mCircularPaint.setStrokeCap(mRoundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        mCircularPaint.setStyle(Paint.Style.STROKE);


        mSecondPaint.setTextSize(Math.min(mViewWidth, mViewHeight) / 5f);
        mSecondPaint.setTextAlign(Paint.Align.CENTER);
        mSecondPaint.setStrokeWidth(0);
        mSecondPaint.setColor(mTextColor);
        mSecondPaint.setShadowLayer(5, 5, 5, 0xFF6C6C6C);

    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        final int diameter = Math.min(mViewWidth, mViewHeight) - (mStrokeWidth * 2);
        outerOval= new RectF(mStrokeWidth + paddingLeft, mStrokeWidth + paddingTop, diameter-paddingRight, diameter-paddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(LT,"Call onDraw(canvas)");
        drawOutlineArc(canvas);

        if (mDrawText) {
            drawText(canvas);
        }
    }


    private void drawOutlineArc(Canvas canvas) {
        Log.i(LT,"Call drawOutlineArc(canvas)");
        canvas.drawArc(outerOval, mStartAngle, mSweepAngle, false, mCircularPaint);
    }

    private void drawText(Canvas canvas) {
        Log.i(LT,"Call drawText(canvas)");
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mSecondPaint.descent() + mSecondPaint.ascent()) / 2)) ;

        canvas.drawText(calcProgressFromSweepAngle(mSweepAngle) + "s", xPos, yPos, mSecondPaint);
    }

    /**
     * Exchange progress(time seconds) to angle
     * @param progress time seconds unit
     * @return angle
     */
    private float calcSweepAngleFromProgress(int progress) {
        return (mMaxSweepAngle / mMaxProgress) * progress;
    }

    /**
     * Exchange sweepAngle to progress(time seconds)
     * @param sweepAngle
     * @return progress time seconds unit
     */
    private int calcProgressFromSweepAngle(float sweepAngle) {
        return (int) ((sweepAngle * mMaxProgress) / mMaxSweepAngle);
    }

    /**
     * Set progress of the circular progress bar.
     * @param progress progress between 0 and 100.
     */
    public void setProgress(int progress) {
        ValueAnimator animator = ValueAnimator.ofFloat(mSweepAngle, calcSweepAngleFromProgress(progress));
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(mAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSweepAngle = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }
}