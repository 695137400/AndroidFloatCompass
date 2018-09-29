package chao.ha.com.falgutil.compass;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import chao.ha.com.falgutil.R;
/**
 * Created with IntelliJ IDEA.<br/>
 * User: <br/>
 * Date: 2018-9-27-0027<br/>
 * Time: 12:36:42<br/>
 * Author:<br/>
 * Description: <span style="color:#63D3E9">指南针</span><br/>
 */
public class CompassView extends View implements Runnable {

    public CompassView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }


    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }


    private Paint _mPaint = new Paint();
    /**
     * 角度
     */
    public static float _decDegree;
    /**
     * 维度
     */
    public static String GPS_X = "";
    /**
     * 经度
     */
    public static String GPS_Y = "";
    /**
     * 速度
     */
    public static String GPS_S = "";
    private Bitmap _compass;

    void init() {
        // 载入图片
        _compass = BitmapFactory.decodeResource(getResources(), R.mipmap.kd);
        // 开启线程否则无法更新画面
        new Thread(this).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        // 实现图像旋转
        Matrix mat = new Matrix();

        mat.reset();
        mat.setTranslate(0, (getHeight() - getWidth()) * 0.5f);
        mat.setScale(getWidth() / (float) _compass.getWidth(), getWidth() / (float) _compass.getHeight());

        mat.preRotate(-_decDegree, _compass.getWidth() * 0.5f, _compass.getHeight() * 0.5f);

        // 绘制图像
        canvas.drawBitmap(_compass, mat, _mPaint);
        _mPaint.setColor(Color.WHITE);
        _mPaint.setTextSize(16);
        canvas.drawText(GPS_X.length() > 0 ? "维度：" + GPS_X : "", 0, 35, _mPaint);
        canvas.drawText(GPS_Y.length() > 0 ? "经度：" + GPS_Y : "", 0, 55, _mPaint);
        canvas.drawText(GPS_S.length() > 0 ? "速度：" + GPS_S + "km/h" : "", 0, 75, _mPaint);
        String text = "";
        if (_decDegree <= 15 || _decDegree >= 345) {
            text = "北：";
        } else if (_decDegree > 15 && _decDegree <= 75) {
            text = "东北：";
        } else if (_decDegree > 75 && _decDegree <= 105) {
            text = "东：";
        } else if (_decDegree > 105 && _decDegree <= 165) {
            text = "东南：";
        } else if (_decDegree > 165 && _decDegree <= 195) {
            text = "南：";
        } else if (_decDegree > 195 && _decDegree <= 255) {
            text = "西南：";
        } else if (_decDegree > 255 && _decDegree <= 285) {
            text = "西：";
        } else if (_decDegree > 285 && _decDegree < 345) {
            text = "西北：";
        }
        String tx = text + (int) _decDegree + "°";
        canvas.drawText(tx, 0, 14, _mPaint);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(30);
                postInvalidate();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            postInvalidate();
        }
    }
}
