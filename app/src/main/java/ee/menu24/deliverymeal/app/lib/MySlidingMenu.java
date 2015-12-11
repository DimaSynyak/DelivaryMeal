package ee.menu24.deliverymeal.app.lib;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 1 on 13.11.2015.
 */
public class MySlidingMenu extends RelativeLayout {

    private int oldScrollY;
    private int oldEventY;
    private int eventDeltaY;
    private FrameLayout frameLayout;

    private int scrollY;

    private AppCompatActivity activity;
    private int layOutTop;
    private int layOutBottom;
    private FrameLayout slidingFragment;
    private ScrollView scrollView;
    private LinearLayout scrollViewEx;
    private LinearLayout linearLayout;

    private int slidingWidth;
    private int slidingHeight;

    public MySlidingMenu(Context context) {
        super(context);
        activity = (AppCompatActivity) context;
    }

    public void setLayOutTop(int layOutTop) {
        this.layOutTop = layOutTop;
    }

    public void setLayOutBottom(int layOutBottom) {
        this.layOutBottom = layOutBottom;
    }

    public void init(){
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        frameLayout = new FrameLayout(activity);
        frameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        frameLayout.setFocusable(true);
        frameLayout.setFocusableInTouchMode(true);

        linearLayout = new LinearLayout(activity);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        slidingFragment = new FrameLayout(activity);
        slidingFragment.addView((LayoutInflater.from(getContext()).inflate(layOutTop, null)));
        slidingFragment.setLayoutParams(new LayoutParams(slidingWidth, slidingHeight));

        scrollView = new ScrollView(activity);
        scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View view = LayoutInflater.from(getContext()).inflate(layOutBottom, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        scrollViewEx = new LinearLayout(activity);
        scrollViewEx.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        scrollViewEx.addView(view);
        scrollViewEx.setBackgroundColor(Color.RED);

        scrollView.addView(scrollViewEx);
        scrollView.setFocusable(false);
        scrollView.setFocusableInTouchMode(false);

        linearLayout.addView(slidingFragment);
        linearLayout.addView(scrollView);

        this.addView(linearLayout);
        this.addView(frameLayout);

        scrollViewEx.requestLayout();


        frameLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventY = (int) event.getY();

                int deltaY = oldScrollY - scrollY;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE: {
                        eventDeltaY = oldEventY - eventY;
                        scrollY += eventDeltaY;

                        if (scrollY < 0) {
                            scrollY = 0;
                        } else if (scrollY >= (scrollViewEx.getHeight() - frameLayout.getHeight() + slidingFragment.getHeight())) {
                            scrollY = (scrollViewEx.getHeight() - frameLayout.getHeight() + slidingFragment.getHeight());
                        }

                        if (eventDeltaY > 0) {
                            if (slidingFragment.getHeight() > 100) {
                                slidingFragment.getLayoutParams().height = slidingFragment.getHeight() - Math.abs(eventDeltaY);
                            } else {
                                scrollViewEx.scrollTo(0, scrollY - eventDeltaY);
                            }
                        } else {
                            if (false) {
                                slidingFragment.getLayoutParams().height = slidingFragment.getHeight() + Math.abs(eventDeltaY);
                            } else {
                                scrollViewEx.scrollTo(0, scrollY + eventDeltaY);
                            }
                        }
                        break;
                    }
                }

                slidingFragment.requestLayout();

                oldScrollY = scrollY;
                oldEventY = eventY;
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }



    public int getSlidingWidth() {
        return slidingWidth;
    }

    public void setSlidingWidth(int slidingWidth) {
        this.slidingWidth = slidingWidth;
    }

    public int getSlidingHeight() {
        return slidingHeight;
    }

    public void setSlidingHeight(int slidingHeight) {
        this.slidingHeight = slidingHeight;
    }
}
