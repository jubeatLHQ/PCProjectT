package tmcit.hokekyo1210.SolverUI.Util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SkipListSet<E> extends SkipList<E> implements Set<E> {
    private static final long serialVersionUID = 3736529741483124077L;

    private Map<E, Entry<E>> map = new HashMap<E, Entry<E>>();

    @Override
    public boolean add(E element) {
        if (map.containsKey(element)) {
            return false;
        }
        Entry<E> entry = addBefore(element, head);
        map.put(element, entry);
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (map.containsKey(element)) {
            return;
        }
        Entry<E> entry;
        if (index == size) {
            entry = addBefore(element, head);
        } else {
            Entry<E> next = getEntry(index);
            entry = addBefore(element, next);
        }
        map.put(element, entry);
    }

    @Override
    public E set(int index, E value) {
        Entry<E> entry = getEntry(index);
        E oldValue = entry.value;
        entry.value = value;
        map.remove(oldValue);
        map.put(value, entry);
        return oldValue;
    }

    @Override
    public void clear() {
        super.clear();
        map.clear();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public int indexOf(Object o) {
        Entry<E> entry = map.get(o);
        if (entry == null) {
            return -1;
        }
        return getIndex(entry);
    }

    @Override
    public int lastIndexOf(Object o) {
        return indexOf(o);
    }

    @Override
    public boolean remove(Object o) {
        Entry<E> entry = map.get(o);
        if (entry == null) {
            return false;
        }
        removeEntry(entry);
        map.remove(o);
        return true;
    }

    @Override
    public E remove(int index) {
        E item = get(index);
        remove(item);
        return item;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        for (Object e : c) {
            map.remove(e);
        }
        return result;
    }
}
