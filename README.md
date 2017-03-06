Android Infinite scroll endless recyclerview example
- rxjava 2
- rxbinding
- rxandroid



Observable for paging
```
RxRecyclerView.scrollEvents(mRecyclerView)
        .sample(100, TimeUnit.MILLISECONDS)
        .map(new Function<RecyclerViewScrollEvent, PagingScrollEvent>() {
            @Override
            public PagingScrollEvent apply(@NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) throws Exception {
                return new PagingScrollEvent((LinearLayoutManager) mRecyclerView.getLayoutManager());
            }
        })
        .filter(new Predicate<PagingScrollEvent>() {
            @Override
            public boolean test(@NonNull PagingScrollEvent pagingScrollEvent) throws Exception {
                return pagingScrollEvent.shouldLoadMore(20);
            }
        })
        .map(new Function<PagingScrollEvent, PagingScrollEvent.Page>() {
            @Override
            public PagingScrollEvent.Page apply(@NonNull PagingScrollEvent pagingScrollEvent) throws Exception {
                return pagingScrollEvent.toPage(20);
            }
        })
        .distinct()
        .doOnNext(new Consumer<PagingScrollEvent.Page>() {
            @Override
            public void accept(@NonNull PagingScrollEvent.Page page) throws Exception {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mItemAdapter.setLoading(true);
                    }
                });
            }
        })
        .flatMap(new Function<PagingScrollEvent.Page, Observable<List<Item>>>() {
            @Override
            public Observable<List<Item>> apply(@NonNull PagingScrollEvent.Page page) throws Exception {
                return mItemService.getItems(page.mStart, page.mSize);
            }
        })
        .observeOn(AndroidSchedulers.mainThread());

```

References
-  https://github.com/lenguyenthanh/Rx-Endless-RecyclerView
