package edu.umd.cschroe2.mydrawingtool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import yuku.ambilwarna.AmbilWarnaDialog;


public class MainActivity extends ActionBarActivity {
    Drawing drawing;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_layout);
        drawing = (Drawing) findViewById(R.id.drawing_view);
        drawing.setBackgroundColor(Color.WHITE);
        showDialog("Welcome!", "Welcome to My Drawing Tool!\n" +
                "Use the toolbar to customize your paint brush, use the eraser, or clear and start over.\n" +
                "You can also shake your phone to clear as well!\n" +
                "Happy Painting!", this);


        //CLEAR BUTTON
        Button clear_button = (Button) findViewById(R.id.clear);
        clear_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                drawing.clear = true;
                drawing.postInvalidate();
            }
        });

        //COLOR_CHANGER
        final Button change_color_button = (Button) findViewById(R.id.Color_change);
        change_color_button.setBackgroundColor(drawing.getCurrentColor());
        change_color_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //drawing.changeColor(0x0000CC);
                Ambildialog();

            }


        });

        //OPACITY SEEKBAR
        final SeekBar seek=(SeekBar) findViewById(R.id.opacity);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Alpha ranges from 0(fully transparent) to 255 (fully opaque)
                if (progress == 100) {
                    drawing.setOpacity(255);
                } else {
                    drawing.setOpacity(progress + 100);
                }
            }
        });

        //ERASER
        final TextView brush_text = (TextView) findViewById(R.id.brush_text);
        final ToggleButton erase = (ToggleButton) findViewById(R.id.erase);
        erase.setBackgroundColor(Color.RED);
        erase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            Paint p;
            int color;
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    erase.setBackgroundColor(Color.GREEN);
                    brush_text.setText("Eraser Size");
                    p = drawing.curr_color;
                    color = p.getColor();
                    drawing.changeColor(0xFFFFFF,255);
                    change_color_button.setClickable(false);
                    change_color_button.setBackgroundColor(0x000000);
                    change_color_button.setText("");
                    seek.setEnabled(false);
                } else {
                    erase.setBackgroundColor(Color.RED);
                    brush_text.setText("Brush Size");
                    drawing.changeColor(color,p.getAlpha());
                    change_color_button.setClickable(true);
                    change_color_button.setBackgroundColor(color);
                    change_color_button.setText("Color Changer");
                    seek.setEnabled(true);
                }
            }
        });

        //SHAKE TO CLEAR
        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                drawing.clear = true;
                drawing.postInvalidate();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    /* adapted from http://developer.android.com/intl/zh-tw/guide/topics/ui/controls/radiobutton.html*/
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.small:
                if (checked){
                    drawing.changeBrushSize(5f);
                }
                    break;
            case R.id.medium:
                if (checked){
                    drawing.changeBrushSize(20f);
                }
                    break;
            case R.id.large:
                if (checked){
                    drawing.changeBrushSize(30f);
                }
                    break;
        }
    }

    //adapted from http://developer.android.com/intl/zh-tw/guide/topics/ui/dialogs.html
    public Dialog showDialog(String title, String msg, final Activity activity) {

        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

        return alertDialog;

    }


    public AmbilWarnaDialog Ambildialog(){
        //int color_change = 0;
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, drawing.getCurrentColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                drawing.changeColor(color,-1);
                Button change_color_button = (Button) findViewById(R.id.Color_change);
                change_color_button.setBackgroundColor(drawing.getCurrentColor());
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }


        });
        dialog.show();
        return dialog;
    }





}
