package com.mooo.sestus.indoor_locator.viewfloorplan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.mooo.sestus.indoor_locator.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PinView extends SubsamplingScaleImageView {
    private Bitmap selectedPin;
    private Bitmap newPin;
    private Bitmap basicPin;
    private Bitmap locatedPoint;
    private PointF prevLocatedPoint;
    private List<PointF> pins;
    private Map<PointF, Bitmap> pinIcons;

    public PinView(Context context) {
        this(context, null);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        pins = new ArrayList<>();
        pinIcons = new HashMap<>();
        basicPin = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_pin_black);
        newPin = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_pin_red);
        selectedPin = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_pin_blue);
        locatedPoint = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_location);
        initialise();
    }

    public void addNewPin(PointF pin) {
        addPin(pin, newPin);
        initialise();
        invalidate();
    }

    public void setAddedPin(PointF pin) {
        pinIcons.put(pin, basicPin);
        initialise();
        invalidate();
    }

    public void setSelectedPin(PointF pin) {
        pinIcons.put(pin, selectedPin);
        initialise();
        invalidate();
    }

    private void addPin(PointF pin, Bitmap bitmap) {
        pins.add(pin);
        pinIcons.put(pin, bitmap);
    }


    public void addLocatedPin(PointF point) {
        if (prevLocatedPoint != null) {
            pinIcons.remove(prevLocatedPoint);
            pins.remove(prevLocatedPoint);
        }
        pins.add(point);
        pinIcons.put(point, locatedPoint);
        prevLocatedPoint = point;
        initialise();
        invalidate();
    }

    public void setPins(Collection<PointF> pins) {
        for (PointF pin: pins)
            addPin(pin, basicPin);
        initialise();
        invalidate();
    }

    public Collection<PointF> getPins() {
        return Collections.unmodifiableCollection(pins);
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        float w = (density/420f) * basicPin.getWidth();
        float h = (density/420f) * basicPin.getHeight();
        basicPin = Bitmap.createScaledBitmap(basicPin, (int)w, (int)h, true);
        newPin = Bitmap.createScaledBitmap(newPin, (int)w, (int)h, true);
        selectedPin = Bitmap.createScaledBitmap(selectedPin, (int)w, (int)h, true);
        locatedPoint = Bitmap.createScaledBitmap(locatedPoint, (int)w, (int)h, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw basicPin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        for (PointF pin : pins) {
            Bitmap pinIcon = pinIcons.get(pin);
            if (pin != null && pinIcon != null) {
                PointF vPin = sourceToViewCoord(pin);
                float vX = vPin.x - (pinIcon.getWidth()/2);
                float vY = vPin.y - pinIcon.getHeight();
                canvas.drawBitmap(pinIcon, vX, vY, paint);
            }
        }
    }

    public void removePin(PointF pin) {
        pins.remove(pin);
    }

}
