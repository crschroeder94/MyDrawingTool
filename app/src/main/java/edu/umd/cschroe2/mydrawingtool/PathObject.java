package edu.umd.cschroe2.mydrawingtool;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Christine Schroeder on 3/6/2016.
 */
public class PathObject {
        public Path path;
        public Paint paint;

        public PathObject(Path p, Paint paint_1){
            super();
            path = p;
            paint = paint_1;
        }

    public Paint getPaint(){
        return paint;
    }

}

