package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    /** TextViews to be filled in */
    private TextView mNameTextView, mAlsoKnownTextView, mIngredientsTextView,
                mOriginTextView, mDescriptionTextView;

    /** Labels for AlsoKnownAs and Origin TextViews */
    private TextView mAlsoKnownLabel, mOriginLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mNameTextView = findViewById(R.id.sandwhich_name_tv);
        mAlsoKnownTextView = findViewById(R.id.also_known_tv);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);
        mOriginTextView = findViewById(R.id.origin_tv);
        mDescriptionTextView = findViewById(R.id.description_tv);
        mAlsoKnownLabel = findViewById(R.id.also_known_tv_label);
        mOriginLabel = findViewById(R.id.origin_tv_label);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.sandwhich_placeholder)
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        mNameTextView.setText(sandwich.getMainName());
        mDescriptionTextView.setText(sandwich.getDescription());

        // Checks if "AlsoKnownAs" is empty,
        // if it's empty then it hides the views
        // Otherwise, displays on TextView
        if (sandwich.getAlsoKnownAs().isEmpty()) {
            mAlsoKnownLabel.setVisibility(View.GONE);
            mAlsoKnownTextView.setVisibility(View.GONE);
        } else {
            List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();
            mAlsoKnownTextView.setText(getStringFromList(alsoKnownAsList));
        }

        // Set Ingredients list on TextView
        List<String> ingredientsList = sandwich.getIngredients();
        mIngredientsTextView.setText(getStringFromList(ingredientsList));

        // Checks if "PlaceOfOrigin" is empty,
        // if it's empty then it hides the views
        // Otherwise, displays on TextView
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            mOriginTextView.setVisibility(View.GONE);
            mOriginLabel.setVisibility(View.GONE);
        } else {
            mOriginTextView.setText(sandwich.getPlaceOfOrigin());
        }


    }

    /**
     * Helper method to receive a String object from a List
     */
    private static String getStringFromList(List<String> list) {
        String string = TextUtils.join(", ", list);

        return string;
    }
}
