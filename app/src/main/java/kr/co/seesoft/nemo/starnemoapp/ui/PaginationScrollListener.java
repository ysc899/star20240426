package kr.co.seesoft.nemo.starnemoapp.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.seesoft.nemo.starnemoapp.util.AndroidUtil;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    private int visibleItemCount;
    private int totalItemCount;
    private int firstVisibleItemPosition;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 1; // 여기서는 1개의 아이템이 남았을 때 새 페이지를 요청합니다.
    private int currentPage = 1; // 첫 페이지를 나타냅니다.

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
            // 이곳에서 다음 페이지를 로드하는 작업을 수행합니다.
            currentPage++;
            loadNextPage(currentPage);

            loading = true;
        }
    }

    public abstract void loadNextPage(int page);
}
