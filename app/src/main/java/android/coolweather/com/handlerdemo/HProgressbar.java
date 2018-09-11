package android.coolweather.com.handlerdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Author:Mao
 * Time:2018/9/10  17:43
 * Description:HProgressbar
 */
public class HProgressbar extends ProgressBar {

    protected int mReachHeight= (int) dip2px(3);
    protected int mReachColor=Color.BLUE;
    protected int mUnReachHeight=(int) dip2px(3);
    protected int mUnReachColor=0XFFD3D6DA;
    protected int mTextSize= (int) sp2px(15);
    protected int mTextColor=Color.BLACK;
    private int mTextBothsidesOffset=(int) dip2px(3);
    protected Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private int realWidth;


    public HProgressbar(Context context) {
        this(context,null);
    }

    public HProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setTextSize(mTextSize);
        abtainStyleAttrs(attrs);
    }

    private void abtainStyleAttrs(AttributeSet attrs) {
        TypedArray ta=getContext().obtainStyledAttributes(attrs,R.styleable.HProgressbar);
        mReachHeight= (int) ta.getDimension(R.styleable.HProgressbar_reachHeight,mReachHeight);
        mReachColor= ta.getColor(R.styleable.HProgressbar_reachColor,mReachColor);
        mUnReachHeight= (int) ta.getDimension(R.styleable.HProgressbar_unReachHeight,mUnReachHeight);
        mUnReachColor=  ta.getColor(R.styleable.HProgressbar_unReachColor,mUnReachColor);
        mTextSize= (int) ta.getDimension(R.styleable.HProgressbar_pTextSize,mTextSize);
        mTextColor=  ta.getColor(R.styleable.HProgressbar_pTextColor,mTextColor);
        mTextBothsidesOffset= (int) ta.getDimension(R.styleable.HProgressbar_textBothsidesOffset,mTextBothsidesOffset);
        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        int mode=MeasureSpec.getMode(heightMeasureSpec);
        int res;
        if (mode==MeasureSpec.EXACTLY){
            res=height;
        }else
        {
            int textHeight= (int) (mPaint.descent()-mPaint.ascent());
            res=getPaddingBottom()+Math.max(Math.max(mReachHeight,mUnReachHeight),Math.abs(textHeight))+getPaddingTop();
            if (mode==MeasureSpec.AT_MOST){
                res=Math.min(height,res);
            }
        }
        setMeasuredDimension(widthSize,res);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        realWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);

        boolean noNeedUnreach=false;
        String text=getProgress()+"%";
        float textWidth=mPaint.measureText(text);

        float radio=getProgress()*1.0f/getMax();
        float progressX=radio*realWidth;
        if (progressX+textWidth>realWidth){
            progressX=realWidth-textWidth;
            noNeedUnreach=true;
        }
        float endX=progressX-mTextBothsidesOffset/2;
        if (endX>0){
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }
        //draw text
        mPaint.setColor(mTextColor);
        int y= (int) (-(mPaint.descent()+mPaint.ascent())/2);
        canvas.drawText(text,progressX,y,mPaint);
        if (!noNeedUnreach){
            int startX= (int) (progressX+textWidth+mTextBothsidesOffset/2);
            mPaint.setStrokeWidth(mUnReachHeight);
            mPaint.setColor(mUnReachColor);
            canvas.drawLine(startX,0,realWidth,0,mPaint);
        }
        canvas.restore();
    }

    private float sp2px(int spVal){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,getResources().getDisplayMetrics());
    }
    private float dip2px(int dipVal){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dipVal,getResources().getDisplayMetrics());
    }
}
