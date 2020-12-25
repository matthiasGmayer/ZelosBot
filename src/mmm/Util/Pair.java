package mmm;

import java.util.Objects;

public class Pair<E,V> {
    public E a;
    public V b;

    public Pair(E e, V v) {
        a=e;
        b=v;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(a, pair.a) &&
                Objects.equals(b, pair.b);
    }

    @Override
    public String toString() {
        return "("+ a.toString() + ", " + b.toString() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
