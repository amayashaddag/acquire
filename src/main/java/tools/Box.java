package tools;

/**
 * Like a T* in C
 *
 * @author Arthur Deck
 * @version 1
 */
public class Box<T> {
    public Box(T t) {
        this.t = t;
    }

    private T t;

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }
}