package com.devdroid.sleepassistant.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.devdroid.sleepassistant.R;
import com.devdroid.sleepassistant.SleepState;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotLegend;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Gaolei on 2017/4/26.
 */

public class PieChartView  extends BaseChartView implements Runnable{

    private String TAG = "PieChart01View";
    private PieChart chart = new PieChart();
    private ArrayList<PieData> chartData = new ArrayList<PieData>();
    private int mSelectedID = -1;


    public PieChartView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView();
    }

    public PieChartView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        chartRender();

        //綁定手势滑动事件
//        this.bindTouch(this,chart);
//        new Thread(this).start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w,h);
    }


    private void chartRender()
    {
        try {

            //设置绘图区默认缩进px值
            int [] ltrb = getPieDefaultSpadding();
            float right = DensityUtil.dip2px(getContext(), 100);
            chart.setPadding(ltrb[0], ltrb[1], right, ltrb[3]);

            //设置起始偏移角度(即第一个扇区从哪个角度开始绘制)
            //chart.setInitialAngle(90);

            //标签显示(隐藏，显示在中间，显示在扇区外面)
            chart.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
            chart.getLabelPaint().setColor(Color.WHITE);

//            //标题
//            chart.setTitle("饼图-Pie Chart");
//            chart.addSubtitle("(XCL-Charts Demo)");
//            chart.setTitleVerticalAlign(XEnum.VerticalAlign.BOTTOM);

            //chart.setDataSource(chartData);

            //激活点击监听
            chart.ActiveListenItemClick();
            chart.showClikedFocus();

            //设置允许的平移模式
            //chart.enablePanMode();
            //chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

            //显示图例
            PlotLegend legend = chart.getPlotLegend();
            legend.show();
            legend.setType(XEnum.LegendType.COLUMN);
            legend.setHorizontalAlign(XEnum.HorizontalAlign.RIGHT);
            legend.setVerticalAlign(XEnum.VerticalAlign.MIDDLE);
            legend.showBox();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }

    public void chartDataSet(Map<String, Integer> sleepResult) {
        double geratPercent = sleepResult.get(SleepState.GREAT.name());
        double warnPercent = sleepResult.get(SleepState.WARN.name());
        double badPercent = sleepResult.get(SleepState.BAD.name());
        chartData.add(new PieData(getContext().getString(R.string.gerat), geratPercent + "%" , geratPercent, ContextCompat.getColor(getContext(), R.color.color_calendar_card_gerat)));
        chartData.add(new PieData(getContext().getString(R.string.warn), warnPercent + "%" , warnPercent, ContextCompat.getColor(getContext(), R.color.color_calendar_card_warn)));
        chartData.add(new PieData(getContext().getString(R.string.bad), badPercent + "%" , badPercent, ContextCompat.getColor(getContext(), R.color.color_calendar_card_bad)));

    }

    @Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            chartAnimation();
        }
        catch(Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private void chartAnimation()
    {
        try {

            chart.setDataSource(chartData);
            int count = 360 / 10;

            for(int i=1;i<count ;i++)
            {
                Thread.sleep(40);

                chart.setTotalAngle(10 * i);

                //激活点击监听
                if(count - 1 == i)
                {
                    chart.setTotalAngle(360);

                    chart.ActiveListenItemClick();
                    //显示边框线，并设置其颜色
                    chart.getArcBorderPaint().setColor(Color.YELLOW);
                    chart.getArcBorderPaint().setStrokeWidth(3);
                }

                postInvalidate();
            }

        }
        catch(Exception e) {
            Thread.currentThread().interrupt();
        }

    }

	/*
	 * 另一种动画
	private void chartAnimation()
	{
		  try {

			  	float sum = 0.0f;
			  	int count = chartData.size();
	          	for(int i=0;i< count ;i++)
	          	{
	          		Thread.sleep(150);

	          		ArrayList<PieData> animationData = new ArrayList<PieData>();

	          		sum = 0.0f;

	          		for(int j=0;j<=i;j++)
	          		{
	          			animationData.add(chartData.get(j));
	          			sum = (float) MathHelper.getInstance().add(
	          									sum , chartData.get(j).getPercentage());
	          		}

	          		animationData.add(new PieData("","",  MathHelper.getInstance().sub(100.0f , sum),
	          											  Color.argb(1, 0, 0, 0)));
	          		chart.setDataSource(animationData);

	          		//激活点击监听
	    			if(count - 1 == i)
	    			{
	    				chart.ActiveListenItemClick();
	    				//显示边框线，并设置其颜色
	    				chart.getArcBorderPaint().setColor(Color.YELLOW);
	    				chart.getArcBorderPaint().setStrokeWidth(3);
	    			}

	          		postInvalidate();
	          }

          }
          catch(Exception e) {
              Thread.currentThread().interrupt();
          }

	}
	*/


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            if(chart.isPlotClickArea(event.getX(),event.getY()))
            {
                triggerClick(event.getX(),event.getY());
            }
        }
        return true;
    }


    //触发监听
    private void triggerClick(float x,float y)
    {
        if(!chart.getListenItemClickStatus())return;

        ArcPosition record = chart.getPositionRecord(x,y);
        if( null == record) return;
		/*
		PieData pData = chartData.get(record.getDataID());
		Toast.makeText(this.getContext(),
				" key:" +  pData.getKey() +
				" Label:" + pData.getLabel() ,
				Toast.LENGTH_SHORT).show();
		*/

        //用于处理点击时弹开，再点时弹回的效果
        PieData pData = chartData.get(record.getDataID());
        if(record.getDataID() == mSelectedID )
        {
            boolean bStatus = chartData.get(mSelectedID).getSelected();
            chartData.get(mSelectedID).setSelected(!bStatus);
        }else{
            if(mSelectedID >= 0)
                chartData.get(mSelectedID).setSelected(false);
            pData.setSelected(true);
        }
        mSelectedID = record.getDataID();
        this.refreshChart();

		/*
		boolean isInvaldate = true;
		for(int i=0;i < chartData.size();i++)
		{
			PieData cData = chartData.get(i);
			if(i == record.getDataID())
			{
				if(cData.getSelected())
				{
					isInvaldate = false;
					break;
				}else{
					cData.setSelected(true);
				}
			}else
				cData.setSelected(false);
		}
		if(isInvaldate)this.invalidate();
		*/

    }

    //Demo中bar chart所使用的默认偏移值。
    //偏移出来的空间用于显示tick,axistitle....
    protected int[] getPieDefaultSpadding() {
        int [] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 30); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 30); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 0); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 50); //bottom
        return ltrb;
    }
}
