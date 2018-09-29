package chao.ha.com.falgutil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import chao.ha.com.falgutil.compass.CompassView;
import chao.ha.com.falgutil.util.Utils;

import java.text.DecimalFormat;

public class MainActivity extends Activity implements SensorEventListener {

    private boolean mRegisteredSensor;
    //定义SensorManager
    private SensorManager mSensorManager;
    private CompassView _compassView;
    // 定义水平仪的仪表盘
    //定义水平仪的仪表盘
    private SpiritView show;
    // 定义水平仪能处理的最大倾斜角，超过该角度，气泡将直接在位于边界。
    float MAX_ANGLE = 70;
    static View displayView = null;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRegisteredSensor = false;
        //取得SensorManager实例
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

        updateToNewLocation(location);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1, 1, new LocationListener() {//监听参数500ms更新一次或者1米更新一次
            public void onStatusChanged(String provider, int status, Bundle extras) {
                System.out.println("onStatusChanged");
            }

            public void onProviderEnabled(String provider) {
                System.out.println("onProviderEnabled");
            }

            public void onProviderDisabled(String provider) {
                System.out.println("onProviderDisabled");

            }

            public void onLocationChanged(Location location) {
                Toast.makeText(MainActivity.this, "位置更新", Toast.LENGTH_SHORT).show();
                updateToNewLocation(location);//进入更新程序
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册指南针
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        if (mRegisteredSensor) {
            //如果调用了registerListener
            //这里我们需要unregisterListener来卸载/取消注册
            mSensorManager.unregisterListener(this);
            mRegisteredSensor = false;
        }
        super.onPause();
    }

    //当进准度发生改变时
    //sensor->传感器
    //accuracy->精准度
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // 当传感器在被改变时触发
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (null != show && null != _compassView) {
            // 接受方向感应器的类型
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                // 这里我们可以得到数据，然后根据需要来处理
                float x = event.values[SensorManager.DATA_X];

                _compassView._decDegree = x;
            }
            float values[] = event.values;
            //获取传感器的类型
            int sensorType = event.sensor.getType();
            switch (sensorType) {
                case Sensor.TYPE_ORIENTATION:
                    //获取与Y轴的夹角
                    float yAngle = values[1];
                    //获取与Z轴的夹角
                    float zAngle = values[2];
                    //气泡位于中间时（水平仪完全水平）
                    float x = 20.5f;
                    float y = 20.5f;
                    x += (x * zAngle / MAX_ANGLE);
                    y += (y * yAngle / MAX_ANGLE);
                    //如果计算出来的X，Y坐标还位于水平仪的仪表盘之内，则更新水平仪气泡坐标
                    if (true) {
                        SpiritView.bubbleX = Utils.pd2px(this, x);
                        SpiritView.bubbleY = Utils.pd2px(this, y);
                        //Toast.makeText(Spirit.this, "在仪表盘内", Toast.LENGTH_SHORT).show();
                    }
                    //通知组件更新
                    show.postInvalidate();
                    break;
            }
        } else {
            if (null == displayView) {
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                displayView = layoutInflater.inflate(R.layout.image_display, null);
                show = (SpiritView) displayView.findViewById(R.id.show);
                _compassView = (CompassView) displayView.findViewById(R.id.mycompassView);
                startService(new Intent(MainActivity.this, MainService.class));
            }
            this.finish();
        }
    }

    private void updateToNewLocation(Location location) {

        if (location != null) {
            double x = location.getLatitude();//维度
            CompassView.GPS_X = "" + getTwoDecimal(x);
            double y = location.getLongitude();//经度
            CompassView.GPS_Y = "" + getTwoDecimal(y);
            float speed = location.getSpeed();//取得速度
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format(speed * 3.6);//format 返回的是字符串
            CompassView.GPS_S = p;
        } else {
            CompassView.GPS_X = "";
            CompassView.GPS_Y = "";
            CompassView.GPS_S = "";
        }

    }

    private double getTwoDecimal(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String yearString = dFormat.format(num);
        Double temp = Double.valueOf(yearString);
        return temp;
    }

}
