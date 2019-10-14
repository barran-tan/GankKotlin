package shivam.developer.featuredrecyclerview;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FeatureLinearLayoutManager extends LinearLayoutManager {

    public FeatureLinearLayoutManager(Context context) {
        super(context);
    }

    public FeatureLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        return 1000;
    }
}
