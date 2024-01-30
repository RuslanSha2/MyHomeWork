package ru.homework.geometry;

import ru.homework.geometry.figures.Triangle;

public class MyGeometry {
    public static void main(String[] args) {
        var my_triangle = new Triangle(5.5, 6.7, 4.2);

        my_triangle.printProperties();
        System.out.println("Perimeter is " + my_triangle.perimeter());
        System.out.println("Area is " + my_triangle.area());
    }
}
