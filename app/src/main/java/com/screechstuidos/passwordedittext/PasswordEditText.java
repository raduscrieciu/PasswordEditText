package com.screechstuidos.passwordedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Displays a Password EditText with a SHOW/HIDE button at the right-hand side.
 */
public class PasswordEditText extends EditText implements View.OnTouchListener {

    private static final String TEXT_SHOW = "SHOW";
    private static final String TEXT_HIDE = "HIDE";
    private static final int ACTION_COLOR = Color.BLUE;
    private static final float ACTION_SIZE = 11.0f;

    private OnTouchListener onTouchListener;
    private boolean isPasswordVisible = false;

    private String showActionText;
    private String hideActionText;
    private int actionLabelColor;
    private float actionLabelDimension;

    /**
     * Creates the PasswordEditText
     *
     * @param context Context
     */
    public PasswordEditText(Context context) {
        super(context);
        init(null);
    }

    /**
     * Creates the PasswordEditText
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * Creates the PasswordEditText
     *
     * @param context  Context
     * @param attrs    AttributeSet
     * @param defStyle int
     */
    public PasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public String getShowActionText() {
        return showActionText;
    }

    public void setShowActionText(String showActionText) {
        this.showActionText = showActionText;
        setPasswordVisibilityDrawable(isPasswordVisible);
    }

    public String getHideActionText() {
        return hideActionText;
    }

    public void setHideActionText(String hideActionText) {
        this.hideActionText = hideActionText;
        setPasswordVisibilityDrawable(isPasswordVisible);
    }

    public int getActionLabelColor() {
        return actionLabelColor;
    }

    public void setActionLabelColor(int actionLabelColor) {
        this.actionLabelColor = actionLabelColor;
        setPasswordVisibilityDrawable(isPasswordVisible);
    }

    public float getActionLabelDimension() {
        return actionLabelDimension;
    }

    public void setActionLabelDimension(float actionLabelDimension) {
        this.actionLabelDimension = actionLabelDimension;
        setPasswordVisibilityDrawable(isPasswordVisible);
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - getCompoundDrawables()[2].getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isPasswordVisible) {
                        setTransformationMethod(new PasswordTransformationMethod());
                        isPasswordVisible = false;
                    } else {
                        setTransformationMethod(null);
                        isPasswordVisible = true;
                    }
                    setSelection(getText().length());
                    setPasswordVisibilityDrawable(isPasswordVisible);
                }
                return true;
            }
        }
        if (onTouchListener != null) {
            return onTouchListener.onTouch(v, event);
        }
        return false;
    }

    private void init(AttributeSet attrs) {

        //Set default attributes
        setActionLabelColor(ACTION_COLOR);
        setActionLabelDimension(ACTION_SIZE);

        if (attrs != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordEditText, 0, 0);

            try {
                setShowActionText(typedArray.getString(R.styleable.PasswordEditText_showActionText));
                setHideActionText(typedArray.getString(R.styleable.PasswordEditText_hideActionText));
                setActionLabelColor(typedArray.getColor(R.styleable.PasswordEditText_actionLabelColor, ACTION_COLOR));
                setActionLabelDimension(typedArray.getDimension(R.styleable.PasswordEditText_actionLabelSize, ACTION_SIZE));
            } finally {
                typedArray.recycle();
            }
        }

        if (getShowActionText() == null) {
            setShowActionText(TEXT_SHOW);
        }
        if (getHideActionText() == null) {
            setHideActionText(TEXT_HIDE);
        }

        setTransformationMethod(new PasswordTransformationMethod());
        setPasswordVisibilityDrawable(isPasswordVisible);

        super.setOnTouchListener(this);
    }

    private void setPasswordVisibilityDrawable(boolean isPasswordVisible) {
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getActionLabelDimension());
        textView.setTextColor(getActionLabelColor());
        if (isPasswordVisible) {
            textView.setText(getHideActionText());
        } else {
            textView.setText(getShowActionText());
        }

        textView.setDrawingCacheEnabled(true);
        // Without this, the view will have a dimension of 0,0 and the bitmap will be null
        textView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        textView.buildDrawingCache(true);

        try {
            Bitmap bitmap = Bitmap.createBitmap(textView.getDrawingCache());
            // clear drawing cache
            textView.setDrawingCacheEnabled(false);

            Drawable showHide = new BitmapDrawable(getResources(), bitmap);
            showHide.setBounds(0, 0, showHide.getIntrinsicWidth(), showHide.getIntrinsicHeight());
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], showHide, getCompoundDrawables()[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}