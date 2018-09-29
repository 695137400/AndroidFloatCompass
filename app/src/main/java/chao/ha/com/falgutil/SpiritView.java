package chao.ha.com.falgutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import chao.ha.com.falgutil.util.Utils;

/**
 * Created by Hades on 16/10/9.
 */
public class SpiritView extends View  implements Runnable{
    //定义水平仪中气泡图标
    Bitmap bubble;
    //定义水平仪中气泡的X、Y坐标
    public static float bubbleX;
    public static float bubbleY;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SpiritView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //加载气泡图片

        bubbleX = Utils.pd2px(context, 20.5f);
        bubbleY = Utils.pd2px(context, 20.5f);
        bubble = BitmapFactory.decodeResource(getResources(), R.mipmap.level_image);
        new Thread(this).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //根据气泡坐标绘制气泡
        canvas.drawBitmap(bubble, bubbleX, bubbleY, null);
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
