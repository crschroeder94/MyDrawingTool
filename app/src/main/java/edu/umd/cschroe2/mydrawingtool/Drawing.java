package edu.umd.cschroe2.mydrawingtool;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Christine Schroeder on 3/2/2016.
 */

public class Drawing extends View {
    Paint paint;
    Path path = new Path();
    Path curr_path;
    Paint curr_color;
    Boolean clear = false;
    Boolean erase = false;
    //public static HashMap<Path, Paint> paths = new HashMap<Path, Paint>();
    public static List<PathObject> paths = new ArrayList<PathObject>();
    AlertDialog.Builder builder1;

    public Drawing(Context context, AttributeSet attrs){
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setAlpha(255);

        paths.add(new PathObject(path, paint));
        curr_path = path;
        curr_color = paint;
        builder1 = new AlertDialog.Builder(context);
    }



    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(clear){
            for (PathObject p : paths) {
                p.path.reset();
            }
            clear = false;
        }else {
            for (PathObject p : paths) {
                canvas.drawPath(p.path,p.paint);

            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();



        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                curr_path.moveTo(x,y);
            case MotionEvent.ACTION_MOVE:
                curr_path.lineTo(x,y);
            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate();
        return true;
    }


    public void changeColor(int color, int alpha){
        Path temp = new Path();
        Paint changed_paint = new Paint();
        changed_paint.setAntiAlias(true);
        changed_paint.setStyle(Paint.Style.STROKE);
        changed_paint.setColor(color);


        changed_paint.setStrokeWidth(curr_color.getStrokeWidth());
        if(alpha == -1) {
            changed_paint.setAlpha(curr_color.getAlpha());
        }else{
            changed_paint.setAlpha(alpha);
        }
        paths.add(new PathObject(temp, changed_paint));
        curr_path = temp;
        curr_color = changed_paint;

    }

    public void changeBrushSize(float f){
        Path temp = new Path();
        Paint changed_paint = new Paint();
        changed_paint.setAntiAlias(true);
        changed_paint.setStyle(Paint.Style.STROKE);


        int color = curr_color.getColor();

        changed_paint.setAlpha(curr_color.getAlpha());
        changed_paint.setColor(color);
        changed_paint.setStrokeWidth(f);
        paths.add(new PathObject(temp, changed_paint));
        curr_path = temp;
        curr_color= changed_paint;
    }

    public void setOpacity(int f){
        Path temp = new Path();
        Paint changed_paint = new Paint();
        changed_paint.setAntiAlias(true);
        changed_paint.setStyle(Paint.Style.STROKE);


        int color = curr_color.getColor();

        changed_paint.setStrokeWidth(curr_color.getStrokeWidth());
        changed_paint.setColor(color);
        changed_paint.setAlpha(f);
        paths.add(new PathObject(temp, changed_paint));
        curr_path = temp;
        curr_color= changed_paint;
    }

    
    public int getCurrentColor(){
        return curr_color.getColor();
    }


}
