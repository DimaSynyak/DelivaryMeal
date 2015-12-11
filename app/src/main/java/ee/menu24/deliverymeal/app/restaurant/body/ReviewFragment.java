package ee.menu24.deliverymeal.app.restaurant.body;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import ee.menu24.deliverymeal.app.R;
import ee.menu24.deliverymeal.app.main.service.Restaurant;

/**
 * Created by 1 on 02.12.2015.
 */
public class ReviewFragment extends Fragment {

    private TextView name;
    private RatingBar ratingBar;
    private TextView review;
    private Restaurant restaurant;
    private TextView time;

    private String nameText;
    private String ratingBarText;
    private String reviewText;
    private String timeText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reviews_fragment, container, false);

        Typeface geometric = Typeface.createFromAsset(getActivity().getAssets(), "fonts/geometric/geometric_706_black.ttf");
        Typeface arimo = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arimo/Arimo_Regular.ttf");

        name = (TextView) view.findViewById(R.id.name);
        name.setTypeface(geometric);
        name.setText(nameText);

        review = (TextView) view.findViewById(R.id.review);
        review.setTypeface(arimo);
        review.setText(reviewText);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        time = (TextView) view.findViewById(R.id.time);
        time.setTypeface(arimo);
        time.setText(timeText);



        return view;
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getRatingBarText() {
        return ratingBarText;
    }

    public void setRatingBarText(String ratingBarText) {
        this.ratingBarText = ratingBarText;
    }
}
