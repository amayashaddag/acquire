package model.tools;

public class Couple<E, F> {

    private E key;
    private F value;

    public Couple(E key, F value) {
        this.key = key;
        this.value = value;
    }

    public E getKey() {
        return key;
    }

    public F getValue() {
        return value;
    }

}