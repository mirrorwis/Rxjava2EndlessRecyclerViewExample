package me.yaoyuan.endlessrecyclerview;

import android.support.v7.widget.LinearLayoutManager;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by Andy on 2017.
 */

public class PagingScrollEvent {

    private int mDisplayCount;
    private int mTotalCount;
    private int mFirstVisibleIndex;

    public PagingScrollEvent(LinearLayoutManager layoutManager) {
        mDisplayCount = layoutManager.getChildCount();
        mTotalCount = layoutManager.getItemCount();
        mFirstVisibleIndex = layoutManager.findFirstVisibleItemPosition();
    }

    public boolean shouldLoadMore(int threshold) {
        return mTotalCount - mDisplayCount <= mFirstVisibleIndex + threshold;
    }

    public Page toPage(int size) {
        return new Page(mTotalCount, size);
    }

    public static class Page {
        public int mStart;
        public int mSize;

        public Page(int start, int size) {
            this.mStart = start;
            this.mSize = size;
        }
    }
}
