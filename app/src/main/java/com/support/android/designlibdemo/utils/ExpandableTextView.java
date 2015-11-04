package com.support.android.designlibdemo.utils;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.support.android.designlibdemo.R;

public class ExpandableTextView extends TextView {
    private static final int DEFAULT_TRIM_LENGTH = 150;
    private static final String ELLIPSIS = "..." + System.getProperty("line.separator") + "Read more >>>";

    private CharSequence originalText;
    private CharSequence trimmedText;
    private BufferType bufferType;
    private boolean trim = true;
    private int trimLength;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.trimLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
        typedArray.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trim = !trim;
                setText();
                requestFocusFromTouch();
            }
        });
    }

    private void setText() {
        CharSequence currentText = getDisplayableText();
        Spannable blueText = makeTextBlue(currentText);

        if(trim){

        }

        super.setText(blueText, bufferType);
    }

    private CharSequence getDisplayableText() {
        return trim ? trimmedText : originalText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText(text);
        bufferType = type;
        setText();
    }

    //make text blue and noticable
    private Spannable makeTextBlue(CharSequence sText){
        Spannable userNameCommentBlue = new SpannableString(sText);
        if(sText.length()>13 && trim){
            userNameCommentBlue.setSpan(new ForegroundColorSpan(Color.BLUE), (sText.length()-13), sText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return userNameCommentBlue;
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (originalText != null && originalText.length() > trimLength) {

            return new SpannableStringBuilder(originalText, 0, trimLength + 1).append(ELLIPSIS);
        } else {
            return originalText;
        }
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText(originalText);
        setText();
    }

    public int getTrimLength() {
        return trimLength;
    }

}
