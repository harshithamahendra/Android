package com.robometrix;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JoystickController extends View
{
	public interface JoystickControllerListener
	{
		public void onMoved(float x, float y);	
	}

	private int mBackgroundColor;
	private int mForegroundColor;

	private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private RectF mBackgroundBounds = new RectF();
	private RectF mForegroundBounds = new RectF();

	private float startLeft;
	private float currentLeft;
	private float startTop;
	private float currentTop;
	private float diameter;
	private float diameterSmall;

	private JoystickControllerListener theListener;

	// Constructor that allows the layout editor to create and edit 
	// an instance of this view
	public JoystickController(Context context, AttributeSet attrs) 
	{
		super(context, attrs);

		setFocusable(true);

		// Set the custom attributes
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.JoystickController,
				0, 0);

		try {
			setBackgroundColor(a.getColor(R.styleable.JoystickController_backgroundColor, Color.BLUE));
			setForegroundColor(a.getColor(R.styleable.JoystickController_foregroundColor, Color.BLACK));
		} finally {
			a.recycle();
		}    

	}


	// Allow the custom attributes to be set after the View is initialized
	public int getForegroundColor() 
	{
		return mForegroundColor;
	}
	public int getBackgroundColor() 
	{
		return mBackgroundColor;
	}

	public void setForegroundColor(int color) 
	{
		mForegroundColor = color;
		mForegroundPaint.setStyle(Paint.Style.FILL);
		mForegroundPaint.setColor(mForegroundColor);
		invalidate();
		// The change in color does not affect the size or shape of the
		// view, so we do not need to call:
		//requestLayout();
	}
	public void setBackgroundColor(int color) 
	{
		mBackgroundColor = color;
		mBackgroundPaint.setStyle(Paint.Style.FILL);
		mBackgroundPaint.setColor(mBackgroundColor);
		invalidate();
		// The change in color does not affect the size or shape of the
		// view, so we do not need to call:
		//requestLayout();
	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		super.onSizeChanged(w, h, oldw, oldh);

		// Account for padding
		float xpad = (float)(getPaddingLeft() + getPaddingRight());
		float ypad = (float)(getPaddingTop() + getPaddingBottom());

		float ww = (float)w - xpad;
		float hh = (float)h - ypad;

		// Figure out how big we can make the pie.
		diameter = Math.min(ww, hh);
		float left = (float) Math.max(0.0, (ww-hh)/2.0) + getPaddingLeft();
		float top = (float) Math.max(0.0, (hh-ww)/2.0) + getPaddingTop();
		mBackgroundBounds.set(left, top, left + diameter, top + diameter);

		diameterSmall = (float) Math.min(ww/4.0, hh/4.0);
		currentLeft = startLeft = (float) ((ww - diameterSmall) / 2.0) + getPaddingLeft();
		currentTop = startTop = (float) ((hh - diameterSmall) / 2.0) + getPaddingTop();
		mForegroundBounds.set(currentLeft, currentTop,
				currentLeft + diameterSmall, currentTop + diameterSmall);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawOval(mBackgroundBounds, mBackgroundPaint);

		canvas.drawOval(mForegroundBounds, mForegroundPaint);

	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction())
		{
		case MotionEvent.ACTION_MOVE:
			float maxDist = (float) ((diameter - diameterSmall)/2.0);

			currentLeft = event.getX();                     
			currentTop = event.getY();
			if (Math.abs(currentLeft - startLeft) > Math.abs(currentTop - startTop))
			{
				currentTop = startTop;
				currentLeft = Math.min(currentLeft, startLeft + maxDist);
				currentLeft = Math.max(currentLeft, startLeft - maxDist);
			}
			else
			{
				currentLeft = startLeft;
				currentTop = Math.min(currentTop, startTop + maxDist);
				currentTop = Math.max(currentTop, startTop - maxDist);
			}

			mForegroundBounds.set(currentLeft,currentTop,currentLeft + diameterSmall, currentTop + diameterSmall);
			if (theListener != null) {
				theListener.onMoved((currentLeft-startLeft) / diameter * 2.0f, (currentTop - startTop)  / diameter * 2.0f);
			}

			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			returnHandleToCenter();
			break;
		}
		return true;
	}

	private void returnHandleToCenter() {

		Handler handler = new Handler();
		int numberOfFrames = 8;
		final double intervalsX = (startLeft - currentLeft) / numberOfFrames;
		final double intervalsY = (startTop - currentTop) / numberOfFrames;

		for (int i = 0; i < numberOfFrames; i++) {
			
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					currentLeft += intervalsX;
					currentTop += intervalsY;
					mForegroundBounds.set(currentLeft,currentTop,
							currentLeft + diameterSmall,currentTop + diameterSmall);
					invalidate();
					if (theListener != null) {
						theListener.onMoved((currentLeft-startLeft) / diameter * 2.0f, (currentTop - startTop)  / diameter * 2.0f);
					}
				}
			}, i * 40);
		}
	}    

	public void setJoystickControllerListener(JoystickControllerListener jcl)
	{
		theListener = jcl;
	}
}

