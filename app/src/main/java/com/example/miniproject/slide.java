package com.example.miniproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class slide extends AppCompatActivity {

    ViewPager mSLideViewPager;
    LinearLayout mDotLayout;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Edge-to-Edge support for full-screen immersive mode
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_slide);

        // Apply window insets to ensure UI elements are correctly positioned
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ViewPager and indicator layout
        mSLideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);
        mSLideViewPager.setAdapter(viewPagerAdapter);

        // Apply PageTransformer for smoother animations (simplified fade)
        mSLideViewPager.setPageTransformer(true, new FadePageTransformer());

        // Customize scroll speed using reflection
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(this, 600); // Reduced duration
            scrollerField.set(mSLideViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set Offscreen Page Limit to preload pages
        mSLideViewPager.setOffscreenPageLimit(3);

        setUpindicator(0);
        mSLideViewPager.addOnPageChangeListener(viewListener);

        // Skip button to directly go to the main screen
        findViewById(R.id.skipButton).setOnClickListener(v -> {
            Intent i = new Intent(slide.this, dashboard.class);
            startActivity(i);
            finish();
        });
    }

    // Method to set up the page indicators (dots) for the ViewPager
    public void setUpindicator(int position) {
        dots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.gray, getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.black, getApplicationContext().getTheme()));
    }

    // ViewPager listener to manage page changes
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            setUpindicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    // FadePageTransformer class for smoother animation
    private class FadePageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View view, float position) {
            view.setAlpha(1 - Math.abs(position));
        }
    }

    // FixedSpeedScroller class (inner class)
    private class FixedSpeedScroller extends Scroller {
        private int mDuration = 500;  // Reduced duration for smoother transitions

        public FixedSpeedScroller(Context context, int duration) {
            super(context);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
