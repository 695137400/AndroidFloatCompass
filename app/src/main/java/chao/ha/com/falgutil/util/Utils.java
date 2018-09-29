package chao.ha.com.falgutil.util;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: <br/>
 * Date: 2018-9-26-0026<br/>
 * Time: 17:10:11<br/>
 * Author:<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class Utils {
    /**
     * dp转换成px
     */
    public static  int pd2px(Context context, float dpValue){
        float scale=context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }




}
