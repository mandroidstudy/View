package android.coolweather.com.handlerdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Author:Mao
 * Time:2018/9/11  15:23
 * Description:CircleProgressbar
 */
public class CircleProgressbar extends ProgressBar {
    private int mReachHeight= (int) dip2px(3);
    private int mReachColor=Color.BLUE;
    private int mUnReachHeight=(int) dip2px(3);
    private int mUnReachColor=0XFFD3D6DA;
    private int mTextSize= (int) sp2px(15);
    private int mTextColor=Color.BLUE;

    private int mRadius= (int) dip2px(80);
    private int mMaxPaintWidth=0;

    private RectF mRectF;

    private Paint mPaint=new Paint();
    public CircleProgressbar(Context context) {
        this(context,null);
    }

    public CircleProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setDither(true);
        mPaint.setTextSize(sp2px(20));
        mReachHeight= (int) (mUnReachHeight*2.5f);
        TypedArray ta=getContext().obtainStyledAttributes(attrs,R.styleable.CircleProgressbar);
        mReachHeight= (int) ta.getDimension(R.styleable.CircleProgressbar_cReachHeight,mReachHeight);
        mReachColor= ta.getColor(R.styleable.CircleProgressbar_cReachColor,mReachColor);
        mUnReachHeight= (int) ta.getDimension(R.styleable.CircleProgressbar_cUnReachHeight,mUnReachHeight);
        mUnReachColor=  ta.getColor(R.styleable.CircleProgressbar_cUnReachColor,mUnReachColor);
        mTextSize= (int) ta.getDimension(R.styleable.CircleProgressbar_cTextSize,mTextSize);
        mTextColor=  ta.getColor(R.styleable.CircleProgressbar_cTextColor,mTextColor);
        mRadius= (int) ta.getDimension(R.styleable.CircleProgressbar_radius,mRadius);
        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth=Math.max(mReachHeight,mUnReachHeight);

        int expect=getPaddingLeft()+mRadius*2+mMaxPaintWidth+getPaddingRight();

        int width=resolveSize(expect,widthMeasureSpec);
        int height=resolveSize(expect,heightMeasureSpec);

        int realWidth=Math.min(width,height);
        mRadius=(realWidth-getPaddingRight()-getPaddingLeft()-mMaxPaintWidth)/2;

        setMeasuredDimension(realWidth,realWidth);
        Log.d("MVE","onMeasure");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF=new RectF(0,0,mRadius*2,mRadius*2);
        Log.d("MVE","onSizeChanged");
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        String text=getProgress()+"%";
        int textWidth= (int) mPaint.measureText(text);
        int textHeight= (int) (mPaint.descent()+mPaint.ascent());
        canvas.save();
        canvas.translate(getPaddingLeft()+mMaxPaintWidth/2,getPaddingTop()+mMaxPaintWidth/2);
        //draw unreach
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);

        //draw reach
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        int sweepAngle= (int) (getProgress()*1.0f/getMax()*360);
        canvas.drawArc(mRectF,0,sweepAngle,false,mPaint);

        //draw text
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text,mRadius-textWidth/2,mRadius-textHeight/2,mPaint);
        canvas.restore();
    }

    private float sp2px(int spVal){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,getResources().getDisplayMetrics());
    }
    private float dip2px(int dipVal){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dipVal,getResources().getDisplayMetrics());
    }
}
