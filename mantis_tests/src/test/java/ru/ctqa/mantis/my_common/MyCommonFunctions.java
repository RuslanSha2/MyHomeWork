package ru.ctqa.mantis.my_common;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyCommonFunctions {
    public static String randomString(int length) {
        var my_rnd = new Random();
        Supplier<Integer> randomNumbers = () -> my_rnd.nextInt(26);
        return Stream.generate(randomNumbers)
                .limit(length)
                .map(i -> 'a' + i)
                .map(Character::toString)
                .collect(Collectors.joining());
    }
}
