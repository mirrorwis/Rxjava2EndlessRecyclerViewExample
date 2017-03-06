package me.yaoyuan.endlessrecyclerview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

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

public class ItemService {
    private static final int TOTAL_COUNT = 2000;

    public Observable<List<Item>> getItems(int start, int size) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            // ignore
        }
        if (start + size > TOTAL_COUNT) {
            return Observable.just(Collections.<Item>emptyList());
        }
        List<Item> items = new ArrayList<>(size);
        for (int i = start; i <= start + size; i++) {
            items.add(new Item(i));
        }
        return Observable.just(items);
    }
}
