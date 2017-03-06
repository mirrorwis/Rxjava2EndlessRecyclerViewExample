package me.yaoyuan.endlessrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

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
public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> mItems;
    private static final int VIEW_TYPE = 1;
    private static final int VIEW_PROG = 0;

    public ItemAdapter(List<Item> items) {
        mItems = items;
    }

    public void setItems(List<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void addItems(List<Item> items) {
        int preSize = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(preSize, items.size());
    }

    public void setLoading(boolean loading) {
        if (loading) {
            mItems.add(new LoadingItem(-1));
            notifyItemInserted(mItems.size());
        } else {
            mItems.remove(mItems.size() - 1);
            notifyItemRemoved(mItems.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            return new ProgressViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bind(mItems.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof LoadingItem) {
            return VIEW_PROG;
        } else {
            return VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mIDView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mIDView = (TextView) itemView.findViewById(R.id.tv_item_id);
        }

        public void bind(Item item) {
            mIDView.setText(String.valueOf(item.id));
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            mProgressBar.setIndeterminate(true);
        }

    }

    static class LoadingItem extends Item {
        public LoadingItem(int id) {
            super(id);
        }
    }
}
