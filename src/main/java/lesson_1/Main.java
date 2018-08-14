package lesson_1;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    /**
     * С подробной формулировкой заданий можно ознакомится в файле README.md
     */
    public static void main(String[] args) {
        // Задание 1
        Integer[] intArr = new Integer[2];

        intArr[0] = 1;
        intArr[1] = 2;

        System.out.println(Arrays.deepToString(intArr));
        swap(intArr, 0, 1);
        System.out.println(Arrays.deepToString(intArr));

        // Задание 2
        ArrayList<Integer> arrList = toArrayList(intArr);

        // Задание 3
        Apple apple1 = new Apple();
        Apple apple2 = new Apple();
        Apple apple3 = new Apple();

        Orange orange1 = new Orange();
        Orange orange2 = new Orange();

        Box<Apple> box1 = new Box<Apple>(apple1, apple2, apple3);
        Box<Orange> box2 = new Box<Orange>(orange1, orange2);

        System.out.println(box1.compare(box2));

        Box<Orange> box3 = new Box<Orange>();
        box2.transfer(box3);
    }

    /**
     * Задание 1
     * Написать метод, который меняет два элемента массива местами.
     * (массив может быть любого ссылочного типа).
     */
    public static void swap(Object[] arr, int index1, int index2) {
        Object tmp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = tmp;
    }

    /**
     * Задание 2
     * Написать метод, который преобразует массив в ArrayList.
     */
    public static <T> ArrayList<T> toArrayList(T[] arr) {
        return new ArrayList<T>(Arrays.asList(arr));
    }
}
