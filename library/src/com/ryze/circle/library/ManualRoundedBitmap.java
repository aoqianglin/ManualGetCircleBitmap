package com.ryze.circle.library;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 
 * @description 从大图片中选取一定区域截取圆形图像
 * @autor Ryze 2015-5-26 下午3:47:32
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class ManualRoundedBitmap extends ImageView {


  private Bitmap bitmap;
  private Drawable drawable;
  private int radius = 0;
  private int width = 0;
  private int height = 0;
  private Rect drawable_Rect;


  private int screenwidth = 0;
  private int screenheight = 0;

  private Paint drawable_Paint;


  private Paint bg_Paint;
  private Rect bg_Rect;

  private Paint rounded_Paint;


  private int dela_x = 0;
  private int dela_y = 0;

  /**
   * @param context
   * @param attrs
   * @param defStyleAttr
   * @param defStyleRes
   */
  @SuppressLint("NewApi")
  public ManualRoundedBitmap(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  /**
   * @param context
   * @param attrs
   * @param defStyle
   */
  public ManualRoundedBitmap(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  /**
   * @param context
   * @param attrs
   */
  public ManualRoundedBitmap(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  /**
   * @param context
   */
  public ManualRoundedBitmap(Context context) {
    super(context);
    init();
  }


  @SuppressLint("ServiceCast")
  private void init() {

    bg_Paint = new Paint();
    bg_Paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    bg_Paint.setColor(Color.parseColor("#212121"));
    bg_Paint.setAlpha(125);

    drawable_Paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    drawable_Rect = new Rect();


    rounded_Paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    rounded_Paint.setColor(Color.parseColor("#ffffbb"));
    // rounded_Paint.setStyle(Style.STROKE);
    // rounded_Paint.setStrokeWidth(5);


    // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
    if (false) {
      DisplayMetrics outMetrics = new DisplayMetrics();
      getDisplay().getMetrics(outMetrics);
      screenwidth = outMetrics.widthPixels;
      screenheight = outMetrics.heightPixels;
    } else {
      WindowManager manager =
          (WindowManager) (getContext().getSystemService(Context.WINDOW_SERVICE));
      Point outSize = new Point();
      manager.getDefaultDisplay().getSize(outSize);
      screenwidth = outSize.x;
      screenheight = outSize.y;
    }
    radius = screenheight / 8;

    bg_Rect = new Rect(0, 0, screenwidth, screenheight);

  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    width = getWidth();
    height = getHeight();
    if (width == 0 || height == 0) {
      return;
    }

    if (radius > height) {
      radius = height / 2;
    }


    drawable_Rect.set(0, 0, width, height);
    drawable = getDrawable();
    if (drawable == null) {
      return;
    }
    bitmap = ((BitmapDrawable) drawable).getBitmap();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    // super.onDraw(canvas);

    if (bitmap == null || radius == 0) {
      return;
    }
    canvas.drawBitmap(bitmap, drawable_Rect, drawable_Rect, drawable_Paint);

    canvas.drawBitmap(getInnerRoundedBitmap(), 0, 0, drawable_Paint);
  }


  private Bitmap getInnerRoundedBitmap() {
    Bitmap outBitmap = Bitmap.createBitmap(screenwidth, screenheight, Config.ARGB_8888);
    Canvas canvas = new Canvas(outBitmap);


    canvas.drawARGB(140, 21, 21, 21);
    drawable_Paint.setXfermode(new PorterDuffXfermode(Mode.DST_ATOP));

    canvas.drawCircle(width / 2, height / 2, radius, rounded_Paint);

    return outBitmap;
  }

  private float down_x = 0;
  private float down_y = 0;

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {

    if (drawable == null) {
      return false;
    }

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        down_x = event.getX();
        down_y = event.getY();

        break;
      case MotionEvent.ACTION_UP:

        break;
      case MotionEvent.ACTION_MOVE:

        break;
      default:
        break;
    }

    int x = (int) event.getX();
    int y = (int) event.getY();
    // drawable.setBounds(0, 0, x, y);
    // invalidate();
    Log.e(VIEW_LOG_TAG, "x: " + x + "y: " + y);


    return true;
  }
}
