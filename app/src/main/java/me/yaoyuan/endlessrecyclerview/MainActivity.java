package me.yaoyuan.endlessrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private ItemService mItemService = new ItemService();

    private ItemAdapter mItemAdapter;

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecyclerView();

        initItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPagingFlow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void initPagingFlow() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            return;
        }
        paging().subscribe(new Observer<List<Item>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(List<Item> items) {
                mItemAdapter.setLoading(false);
                mItemAdapter.addItems(items);
            }

            @Override
            public void onError(Throwable e) {
                initPagingFlow();
            }

            @Override
            public void onComplete() {
            }
        });
    }


    private Observable<List<Item>> paging() {
        return RxRecyclerView.scrollEvents(mRecyclerView)
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
                        return pagingScrollEvent.shouldLoadMore(6);
                    }
                })
                .map(new Function<PagingScrollEvent, PagingScrollEvent.Page>() {
                    @Override
                    public PagingScrollEvent.Page apply(@NonNull PagingScrollEvent pagingScrollEvent) throws Exception {
                        return pagingScrollEvent.toPage(6);
                    }
                })
                .distinct()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<PagingScrollEvent.Page>() {
                    @Override
                    public void accept(@NonNull PagingScrollEvent.Page page) throws Exception {
                        mItemAdapter.setLoading(true);
                    }
                })
                .observeOn(Schedulers.computation())
                .flatMap(new Function<PagingScrollEvent.Page, Observable<List<Item>>>() {
                    @Override
                    public Observable<List<Item>> apply(@NonNull PagingScrollEvent.Page page) throws Exception {
                        return mItemService.getItems(page.mStart, page.mSize);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void initItems() {
        mItemService.getItems(0, 6)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        mItemAdapter.setItems(items);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mItemAdapter = new ItemAdapter(new ArrayList<Item>(0));
        mRecyclerView.setAdapter(mItemAdapter);
    }
}
