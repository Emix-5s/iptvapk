package com.streampulse.app;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.util.TypedValue;

public class MainActivity extends Activity {
    private static final String STREAM_URL = "https://fresh-delta-d7t4.view.miinideck.com/streampulse-cinema-live-tv-dash-PvDx6GMhDOMG4my3BUqjJpR45Eutess6";
    private int dp(float value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(0);
        buildLayout();
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        buildLayout();
    }

    private boolean isTv() {
        UiModeManager manager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        return manager != null && manager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    private boolean isTablet() {
        return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private void buildLayout() {
        boolean tv = isTv();
        boolean tablet = isTablet() && !tv;
        int pad = dp(tv ? 48 : tablet ? 36 : 22);
        int maxWidth = dp(tv ? 900 : tablet ? 720 : 560);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(11, 17, 26));
        LinearLayout outer = new LinearLayout(this);
        outer.setOrientation(LinearLayout.VERTICAL);
        outer.setGravity(Gravity.CENTER_HORIZONTAL);
        outer.setPadding(pad, pad, pad, pad);
        scroll.addView(outer, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView icon = text("↗", tv ? 48 : 42, Color.WHITE, true);
        icon.setGravity(Gravity.CENTER);
        icon.setBackground(round(Color.rgb(29, 42, 59), dp(20)));
        LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(dp(tv ? 88 : 76), dp(tv ? 88 : 76));
        iconLp.bottomMargin = dp(18);
        outer.addView(icon, iconLp);

        TextView title = text("StreamPulse", tv ? 38 : 30, Color.WHITE, true);
        outer.addView(title);
        TextView subtitle = text("LIVE TV", tv ? 18 : 15, Color.rgb(161, 184, 213), true);
        subtitle.setLetterSpacing(0.12f);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(-2, -2);
        subLp.topMargin = dp(5); subLp.bottomMargin = dp(30);
        outer.addView(subtitle, subLp);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(18), dp(18), dp(18));
        card.setBackground(round(Color.rgb(24, 36, 51), dp(18)));
        TextView label = text("STREAM LINK", tv ? 15 : 13, Color.rgb(157, 181, 210), true);
        label.setLetterSpacing(0.08f);
        card.addView(label);
        TextView url = text(STREAM_URL, tv ? 19 : 15, Color.WHITE, false);
        url.setTextIsSelectable(true);
        LinearLayout.LayoutParams urlLp = new LinearLayout.LayoutParams(-1, -2);
        urlLp.topMargin = dp(10);
        card.addView(url, urlLp);
        LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(Math.min(maxWidth, getResources().getDisplayMetrics().widthPixels - 2 * pad), -2);
        cardLp.bottomMargin = dp(18);
        outer.addView(card, cardLp);

        Button open = new Button(this);
        open.setText("OPEN STREAM  ↗");
        open.setTextSize(TypedValue.COMPLEX_UNIT_SP, tv ? 22 : 17);
        open.setTextColor(Color.WHITE);
        open.setAllCaps(false);
        open.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        open.setFocusable(true);
        open.setBackground(round(Color.rgb(45, 125, 255), dp(14)));
        open.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(STREAM_URL));
            startActivity(intent);
        });
        LinearLayout.LayoutParams buttonLp = new LinearLayout.LayoutParams(Math.min(maxWidth, getResources().getDisplayMetrics().widthPixels - 2 * pad), dp(tv ? 68 : 58));
        buttonLp.bottomMargin = dp(24);
        outer.addView(open, buttonLp);

        String device = tv ? "TV MODE DETECTED" : tablet ? "TABLET MODE DETECTED" : "PHONE MODE DETECTED";
        TextView mode = text("▣  " + device, tv ? 16 : 13, Color.rgb(157, 181, 210), true);
        mode.setGravity(Gravity.CENTER);
        outer.addView(mode);
        setContentView(scroll);
        open.requestFocus();
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextColor(color);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        view.setGravity(Gravity.CENTER);
        view.setTypeface(Typeface.DEFAULT, bold ? Typeface.BOLD : Typeface.NORMAL);
        view.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_SIMPLE);
        return view;
    }

    private GradientDrawable round(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }
}
