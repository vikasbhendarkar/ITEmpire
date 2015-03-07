package com.jobPortal.library;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ArialTextView extends TextView
{

    public ArialTextView(Context context)
    {
        super(context);
    }

    public ArialTextView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public void setTypeface(Typeface typeface)
    {
        super.setTypeface(EmiApplication.arialFont);
    }
}
