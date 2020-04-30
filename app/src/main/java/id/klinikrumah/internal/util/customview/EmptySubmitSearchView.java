package id.klinikrumah.internal.util.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import id.klinikrumah.internal.R;

public class EmptySubmitSearchView extends SearchView {
    /*
     *  Created by: Jens Klingenberg (jensklingenberg.de)
     *  GPLv3
     *  https://dev.to/foso/how-to-use-a-searchview-with-an-empty-query-text-submit-4afh
     *  This SearchView gets triggered even when the query submit is empty
     *
     * */
    SearchView.SearchAutoComplete tvSearch;
    OnQueryTextListener listener;

    public EmptySubmitSearchView(Context context) {
        super(context);
    }

    public EmptySubmitSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptySubmitSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnQueryTextListener(final OnQueryTextListener listener) {
        super.setOnQueryTextListener(listener);
        this.listener = listener;
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.END;
        ImageView ivSearch = findViewById(androidx.appcompat.R.id.search_button);
        ivSearch.setLayoutParams(layoutParams);
        ivSearch.setImageResource(R.drawable.ic_call_black_24dp);

        tvSearch = findViewById(androidx.appcompat.R.id.search_src_text);
//        tvSearch.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Lato-Regular.ttf"));
//        tvSearch.setTextSize(12);
//        tvSearch.setTextColor(getResources().getColor(R.color.red));
        tvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (listener != null) {
                    listener.onQueryTextSubmit(getQuery().toString());
                }
                return true;
            }
        });
    }
}