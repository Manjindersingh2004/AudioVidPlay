package com.example.multimediamanager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;

public class CustomMediaController extends MediaController {

    public CustomMediaController(Context context) {
        super(context);
    }

    public CustomMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void show() {
        super.show(0);
    }

    @Override
    public void hide() {
        super.hide();
    }

    // Add a method to toggle the visibility of the MediaController
    public void toggleController() {
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }
}
