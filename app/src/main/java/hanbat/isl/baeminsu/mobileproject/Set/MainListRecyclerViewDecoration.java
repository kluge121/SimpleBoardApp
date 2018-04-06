package hanbat.isl.baeminsu.mobileproject.Set;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

/**
 * Created by baeminsu on 2017. 12. 14..
 */

public class MainListRecyclerViewDecoration extends RecyclerView.ItemDecoration {

    private final int divHeight;

    public MainListRecyclerViewDecoration(int divHeight) {
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        outRect.top = divHeight;
        outRect.bottom=divHeight;
    }
}
