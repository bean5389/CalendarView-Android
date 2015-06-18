package wmlove.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/6/17.
 */
public class DayView extends TextView implements View.OnClickListener{

    private int backgroupColor = Color.GREEN;

    public DayView(Context context) {
        super(context);
        setOnClickListener(this);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setBackgroupColor();
        setTextSize(30);
        setHeight(150);
        setWidth(150);
        setTextColor(Color.BLACK);
        setGravity(Gravity.CENTER);
    }

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setSelectColor(int color){
        this.backgroupColor = color;
        setBackgroupColor();
    }

    private void setBackgroupColor(){
        StateListDrawable drawable = new StateListDrawable();
        ShapeDrawable shape = new ShapeDrawable(new OvalShape());
        shape.setShaderFactory(new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int i, int i1) {
                return new LinearGradient(0,0,0,0,backgroupColor,backgroupColor, Shader.TileMode.REPEAT);
            }
        });
        drawable.addState(new int []{android.R.attr.state_selected},shape);
        setBackground(drawable);
    }

    public void setDay(int day){
        setText(day + "");

    }

    @Override
    public void onClick(View view) {
        postInvalidate();
    }
}
