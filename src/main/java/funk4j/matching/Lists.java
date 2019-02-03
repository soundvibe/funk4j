package funk4j.matching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

interface Lists {

    static <T> List<T> of(T item) {
        return add(new ArrayList<T>(), item);
    }

    static <T> List<T> add(List<T> list, T item) {
        list.add(item);
        return list;
    }

    static <T> T head(Collection<T> collection) {
        return head(collection, 0);
    }

    static <T> T head(Collection<T> collection, int index) {
        int current = 0;
        for (T t : collection) {
            if (current == index) {
                return t;
            }
            current++;
        }
        return null;
    }

    static <T> Collection<T> tail(Collection<T> collection) {
        return tail(collection, 1);
    }

    static <T> Collection<T> tail(Collection<T> collection, int index) {
        if (collection.size() > index) {
            final List<T> coll = new ArrayList<>(collection);
            return coll.subList(index, coll.size());
        }
        return Collections.emptyList();
    }

}
