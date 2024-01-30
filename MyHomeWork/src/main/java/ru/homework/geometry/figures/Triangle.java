package ru.homework.geometry.figures;

import java.util.Objects;

public record Triangle (double side_a, double side_b, double side_c) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return (Double.compare(this.side_a, triangle.side_a) == 0 && Double.compare(this.side_b, triangle.side_b) == 0 && Double.compare(this.side_c, triangle.side_c) == 0)
            || (Double.compare(this.side_a, triangle.side_a) == 0 && Double.compare(this.side_c, triangle.side_b) == 0 && Double.compare(this.side_b, triangle.side_c) == 0)
            || (Double.compare(this.side_b, triangle.side_a) == 0 && Double.compare(this.side_a, triangle.side_b) == 0 && Double.compare(this.side_c, triangle.side_c) == 0)
            || (Double.compare(this.side_b, triangle.side_a) == 0 && Double.compare(this.side_c, triangle.side_b) == 0 && Double.compare(this.side_a, triangle.side_c) == 0)
            || (Double.compare(this.side_c, triangle.side_a) == 0 && Double.compare(this.side_a, triangle.side_b) == 0 && Double.compare(this.side_b, triangle.side_c) == 0)
            || (Double.compare(this.side_c, triangle.side_a) == 0 && Double.compare(this.side_b, triangle.side_b) == 0 && Double.compare(this.side_a, triangle.side_c) == 0);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public Triangle  {
        if (side_a < 0 || side_b < 0 || side_c < 0) {
            throw new IllegalArgumentException("Triangle side should be non-negative");
        }

        if (side_a + side_b < side_c || side_a + side_c < side_b || side_b + side_c < side_a) {
            throw new IllegalArgumentException("The sum of the lengths of two sides of a triangle" +
                                               " must always be greater than the length of the third side");
        }
    }

    public void printProperties() {
        var text = String.format("Triangle is %f x %f x %f", this.side_a, this.side_b, this.side_c);
        System.out.println(text);
    }

    public double perimeter() {
        return this.side_a + this.side_b + this.side_c;
    }

    public double area() {
        var p = this.perimeter() / 2;
        var s = Math.sqrt(p * (p - this.side_a)
                            * (p - this.side_b)
                            * (p - this.side_c)
                         );
        return s;
    }
}
