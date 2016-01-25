package com.geteventro.plugin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class ARViewSkin extends View implements Constants {

    private Paint paint;
    private Paint distancePaint;
    private Paint iconPaint;
    private String placeName;
    private String placeNameLong;
    private double latitude;
    private double longitude;
    private boolean isFinished = true;
    private float fromCompassHeading = 0;
    private float fromAccel = 0;
    private float fromRotation = 0;
    private Bitmap bitIconImg = null;
    private float distance = 0;
    private double bearing = 0;
    private String category = "";
    private String iconfile;
    private Paint background;
    private Paint border;
    private int fontSizeParm;
    private Context thisContext;
    private int midSpacerParm;
    private int rightSpacerParm;
    private int imgBorderSpacerParm;

    public int getImgHeightParm() {
        return imgHeightParm;
    }

    public void setImgHeightParm(int imgHeightParm) {
        this.imgHeightParm = imgHeightParm;
    }

    private int imgHeightParm;




    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();

        this.setFinished(true);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlaceNameLong() {
        return placeNameLong;
    }

    public void setPlaceNameLong(String placeNameLong) {
        this.placeNameLong = placeNameLong;
    }

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getFromRotation() {
        return fromRotation;
    }

    public void setFromRotation(float fromRotation) {
        this.fromRotation = fromRotation;
    }

    public float getFromAccel() {
        return fromAccel;
    }

    public void setFromAccel(float fromAccel) {
        this.fromAccel = fromAccel;
    }

    public float getFromCompassHeading() {
        return fromCompassHeading;
    }

    public void setFromCompassHeading(float fromCompassHeading) {
        this.fromCompassHeading = fromCompassHeading;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {

        this.placeName = ellipsize(placeName, Ellipse);
        this.placeNameLong = placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ARViewSkin(Context context) {
        super(context);
    }

    public ARViewSkin(Context context, String parmPlaceName, String parmIconfile, int fontSize, int rightSpacer,
                      int midSpacer, int imgBorderSpacer, int widthImg, int heightImg) {
        super(context);

        thisContext = context;
        fontSizeParm = fontSize;
        midSpacerParm = midSpacer;
        rightSpacerParm = rightSpacer;
        imgBorderSpacerParm = imgBorderSpacer;
        imgHeightParm = heightImg;

        Typeface tfName = Typeface.create("Helvetica", Typeface.BOLD);
        Typeface tfDistance = Typeface.create("Helvetica", Typeface.NORMAL);
        iconfile = parmIconfile;
        placeName = parmPlaceName;

        background = new Paint();
        background.setColor(Color.BLACK);
        background.setStyle(Style.FILL);
        background.setAlpha(BACKGROUND_ALPHA);
        background.setAntiAlias(true);
        background.setFilterBitmap(true);
        background.setDither(true);

        border = new Paint();
        border.setColor(Color.BLACK);
        border.setStrokeWidth(BORDER_STROKE);
        border.setStyle(Style.STROKE);
        border.setAlpha(BORDER_ALPHA);
        border.setAntiAlias(true);
        border.setFilterBitmap(true);
        border.setDither(true);

        paint = new Paint();
        paint.setTypeface(tfName);
        paint.setColor(Color.WHITE);
        paint.setTextSize(fontSizeParm);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        distancePaint = new Paint();
        distancePaint.setTypeface(tfDistance);
        distancePaint.setColor(Color.WHITE);
        distancePaint.setTextSize(fontSizeParm - 2);
        distancePaint.setAntiAlias(true);
        distancePaint.setFilterBitmap(true);
        distancePaint.setDither(true);

        iconPaint = new Paint();
        iconPaint.setAntiAlias(true);
        iconPaint.setFilterBitmap(true);
        iconPaint.setDither(true);
    }

    private Bitmap downsampleBitmap(InputStream is, int sampleSize) throws FileNotFoundException, IOException {

        Bitmap resizedBitmap;
        BitmapFactory.Options outBitmap = new BitmapFactory.Options();
        outBitmap.inJustDecodeBounds = false; // the decoder will return a bitmap
        outBitmap.inSampleSize = sampleSize;

        try {
            is.reset();
        } catch (IOException e) {
            Log.e("tagger", e.getMessage());
        }

        resizedBitmap = BitmapFactory.decodeStream(is, null, outBitmap);
        //is.close();

        if (resizedBitmap.getWidth() < imgHeightParm) {
            resizedBitmap = getResizedBitmap(resizedBitmap, imgHeightParm, imgHeightParm);
        }

        return resizedBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    private int calculateSampleSize(int width, int height, int targetWidth, int targetHeight) {
        int inSampleSize = 1;

        if (height > targetHeight || width > targetWidth) {

            final int heightRatio = Math.round((float) height
                    / (float) targetHeight);
            final int widthRatio = Math.round((float) width / (float) targetWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    protected String ellipsize(String input, int maxLength) {
        if (input == null || input.length() < maxLength) {
            return input;
        }

        return input.substring(0, maxLength) + "... ";
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 20;

        paint.setAntiAlias(true);
        canvas.drawARGB(ZERO, ZERO, ZERO, ZERO);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap adjustAplha(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(ZERO, ZERO, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(ZERO, ZERO, ZERO, ZERO);
        paint.setAlpha(UnderlayAlpha);
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(ZERO, ZERO, getWidth(), getHeight(), background);
        canvas.drawRect(ZERO, ZERO, getWidth(), getHeight(), border);
        canvas.drawBitmap(bitIconImg, imgBorderSpacerParm, imgBorderSpacerParm, iconPaint);
        canvas.drawText(placeName, imgHeightParm + rightSpacerParm, midSpacerParm, paint);
        canvas.drawText(distance + " Km", imgHeightParm + rightSpacerParm, midSpacerParm * 2, distancePaint);
    }

    private Bitmap getDownsampledBitmap(InputStream is, int targetWidth, int targetHeight, BitmapFactory.Options options) {

        Bitmap bitmap = null;
        try {

            int sampleSize = calculateSampleSize(options.outWidth, options.outHeight, targetWidth, targetHeight);
            bitmap = downsampleBitmap(is, sampleSize);

        } catch (Exception e) {

        }

        return bitmap;
    }

    protected void onMeasureCustom(Bitmap bm) {

        BitmapFactory.Options outDimens = null;
        outDimens = new BitmapFactory.Options();
        outDimens.inJustDecodeBounds = false; // the decoder will return null (no bitmap)
        bitIconImg = bm;
    }
}