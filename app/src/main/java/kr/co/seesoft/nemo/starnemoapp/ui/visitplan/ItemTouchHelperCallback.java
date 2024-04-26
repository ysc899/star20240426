package kr.co.seesoft.nemo.starnemoapp.ui.visitplan;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperListener listener;

    private boolean touchFlag = false;

    public boolean isTouchFlag() {
        return touchFlag;
    }

    public void setTouchFlag(boolean touchFlag) {
        this.touchFlag = touchFlag;
    }

    public ItemTouchHelperCallback(ItemTouchHelperListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipFlag = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlag, swipFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwip(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
//        return super.isLongPressDragEnabled();
        return this.isTouchFlag();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
//        return super.isItemViewSwipeEnabled();
        return this.isTouchFlag();
    }
}
