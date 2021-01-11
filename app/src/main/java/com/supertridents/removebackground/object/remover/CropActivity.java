package com.supertridents.removebackground.object.remover;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.supertridents.removebackground.object.remover.selffie.edit.HoverView;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;


public class CropActivity extends AppCompatActivity {
    private Bitmap mBitmap;
    private SomeView mSomeView;
    Canvas canvas;
    float x, y;
    HoverView mHoverView;
    LinearLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        getSupportActionBar().hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.main));

        ImageView help = findViewById(R.id.crophelp);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CropActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.crop);
                dialog.setCancelable(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                (dialog.findViewById(R.id.closecrop)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);
            }
        });
//        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ks2);
//
//        mSomeView = new SomeView(this,mBitmap);
//        LinearLayout layout = findViewById(R.id.layout);
//        LinearLayout.LayoutParams lp =
//                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);
//        layout.addView(mSomeView, lp);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), 1);
        layout = findViewById(R.id.layout);


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        layout = findViewById(R.id.layout);

        if (resultCode == RESULT_OK && requestCode == 1) {
            InputStream inputStream = null;
            try {

                inputStream = getContentResolver().openInputStream(data.getData());
                mBitmap = BitmapFactory.decodeStream(inputStream);
                mSomeView = new SomeView(this, mBitmap);
                LinearLayout.LayoutParams lp =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.addView(mSomeView, lp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            startActivity(new Intent(CropActivity.this,MainActivity.class));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void cropImage() {
        //setContentView(R.layout.activity_picture_preview);
        //ImageView imageView = findViewById(R.id.image);

        Bitmap fullScreenBitmap =
                Bitmap.createBitmap(mSomeView.getWidth(), mSomeView.getHeight(), mBitmap.getConfig());

        canvas = new Canvas(fullScreenBitmap);
        Rect dstRect = new Rect();
        canvas.getClipBounds(dstRect);
        Path path = new Path();
        List<Point> points = mSomeView.getPoints();
        for (int i = 0; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }

        // Cut out the selected portion of the image
        Paint paint = new Paint();
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBitmap, null, dstRect, paint);

        // Frame the cut out portion...
//        paint.setColor(Color.WHITE);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(10f);
//        canvas.drawPath(path, paint);

        // Create a bitmap with just the cropped area.
        Region region = new Region();
        Region clip = new Region(0, 0, 700, 800);
       // Region clip = new Region(0, 0, 0, 0);
        region.setPath(path, clip);
        Rect bounds = region.getBounds();
        Bitmap croppedBitmap =
                Bitmap.createBitmap(fullScreenBitmap, bounds.left, bounds.top,
                        bounds.width(), bounds.height());

        String fileName = "myImage";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            fullScreenBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }

        Intent intent = new Intent(this, EraserActivity.class);
        //intent.putExtra("BitmapImage", croppedBitmap);
        startActivity(intent);
        //imageView.setImageBitmap(croppedBitmap);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to stop editing?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Intent home = new Intent(CropActivity.this, MainActivity.class);
                    startActivity(home);
                    finish();
                })
                .setNegativeButton("No", (arg0, arg1) -> arg0.dismiss())
                .show();
    }
}