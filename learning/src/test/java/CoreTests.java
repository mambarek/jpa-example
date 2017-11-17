import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CoreTests {

    interface Formula {
        double calculate(int a);

        default double sqrt(int b) {
            return Math.sqrt(b);
        }
    }

    @FunctionalInterface
    interface Converter<F, T> {
        T convert(F from);
    }

    static class Something {

        public String startsWith(String s) {
            return String.valueOf(s.charAt(0));
        }
    }

    static class Person {
        String firstName;
        String lastName;
        int age;

        Person() {
        }

        public Person(String firstName, String lastName) {
            this.lastName = lastName;
            this.firstName = firstName;
        }

        public Person(String firstName, String lastName, int age) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.age = age;
        }

        @Override
        public String toString() {
            return this.firstName + " " +this.lastName + " (" + this.age + ")";
        }
    }

    interface PersonFactory<P extends Person> {
        P create(String firstName, String lastName);
    }

    interface PersonFactory2<P extends Person> {
        P create();
    }

    interface PersonFactory3<P extends Person> {
        P create(Object firstName, Object lastName);
    }

    public static void main(String[] args) {


        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return a * 100;
            }
        };
        final double calculated = formula.calculate(5);
        final double sqrt = formula.sqrt(3);
        System.out.println("calculated = " + calculated);
        System.out.println("sqrt = " + sqrt);

        Formula formula1 = (arg) -> {return arg*3;};
//        Formula formula1 = arg -> arg*3;
        final double calculate = formula1.calculate(2);
        System.out.println("calculate = " + calculate);
        final double sqrt1 = formula1.sqrt(2);
        System.out.println("sqrt1 = " + sqrt1);

        // @TIP sout oder soutv --> print String oder value to System.out

        // strg+Alt+v Variable hinzufügen, wird vom Code extrahiert
        final List<String> names = Arrays.asList("Peter", "Anna", "Mike", "Xenia");

        /*names.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });*/

//        names.sort((o1, o2) -> {return o1.compareTo(o2);});
//        names.sort(Comparator.naturalOrder());
        names.stream().sorted().forEach(System.out::println);
        System.out.println("-- origin Order");
        names.forEach(System.out::println);

        Converter<Integer, String> converter = from -> String.valueOf(from);
        final String converted = converter.convert(100);
        System.out.println("converted = " + converted);

/*        outer:
        for (int i = 0; i < 10; i++) {
            inner:
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    System.out.println("i + j + k = " + i + "/" + j + "/" + k);
                    break inner;
                }
            }
        }*/

        Something something = new Something();
        Converter<String, String> converter2 = something::startsWith;
        final String firstChar = converter2.convert("Java");
        System.out.println("s = " + firstChar);


        PersonFactory<Person> personFactory = (n1, n2) -> new Person(n1, n2);
        Person person = personFactory.create("Peter", "Parker");

        PersonFactory2<Person> personFactory2 = Person::new;
        Person person2 = personFactory2.create();

//        PersonFactory3<Person> personFactory3 = Person::new;
//        Person person3 = personFactory3.create("Peter", "Parker");

        Predicate<String> predicate = s1 -> s1.length() > 3;
        final boolean ab = predicate.test("ab");
        final boolean abcde = predicate.test("abcde");
        System.out.println("ab = " + ab);
        System.out.println("abcde = " + abcde);

        final Predicate<Object> is4 = Predicate.isEqual(4);

        final Stream<Integer> integerStream = Stream.of(4, 5, 6);
        integerStream.filter((Predicate<Integer>) i -> i == 4).collect(Collectors.toList());

        Consumer<Person> personConsumer = person1 -> System.out.println("Hello Mr. " + person1.firstName + " " + person1.lastName);
        personConsumer.accept(new Person("John","Smith"));


        Map<Integer, String> map = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }
        System.out.println("map.containsKey(9) = " + map.containsKey(9));
        map.computeIfPresent(9, (num, val) -> null);
        final boolean b = map.containsKey(9);
        System.out.println("b = " + b);

        map.put(9, null);
        final String val9 = map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
        System.out.println("val9 = " + val9);

        final List<String> mylist = Arrays.asList("a", "b", "c");
        mylist.stream().findFirst().ifPresent(System.out::println);

        Arrays.stream(new String[] {"a", "b"});

        final int sum = IntStream.of(1, 2, 3, 4).sum();
        System.out.println("sum = " + sum);

        Stream.of("a1","b2","c3")
                .map(s1 -> s1.substring(1))
                .mapToInt(Integer::valueOf)
                .max()
                .ifPresent(System.out::println);

        System.out.println("##########################");
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                })
        .forEach(s -> {
            System.out.println("forEach: " + s);
            //System.out.println;
        });


        if(Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A");
                }) == true);


        List<Person> persons =
                Arrays.asList(
                        new Person("Max", "Muze", 18),
                        new Person("Peter", "Pan",23),
                        new Person("Pamela", "Anderson",23),
                        new Person("David", "Bowi",12));

        List<Person> filtered =
                persons
                        .stream()
                        .filter(p1 -> p1.firstName.startsWith("P"))
                        .collect(Collectors.toList());

        System.out.println(filtered);


        final Map<Integer, List<Person>> personsByAge = persons.stream().collect(Collectors.groupingBy(o -> o.age));
        personsByAge.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

        final Double averageAge = persons.stream().collect(Collectors.averagingInt(value -> value.age));
        System.out.println("averageAge = " + averageAge);

        final IntSummaryStatistics intSummaryStatistics = persons.stream().collect(Collectors.summarizingInt(value -> value.age));
        System.out.println("intSummaryStatistics = " + intSummaryStatistics);

        final String collectPersons = persons.stream().map(person1 -> person1.toString()).collect(Collectors.joining());
        System.out.println("collectPersons = " + collectPersons);

        String phrase = persons
                .stream()
                .filter(p -> p.age >= 18)
                .map(p -> p.firstName)
                .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));

        System.out.println(phrase);

        final Map<Integer, String> stringMap = persons.stream().collect(Collectors.toMap(p -> p.age, p -> p.firstName, (name1, name2) -> name1 + ";" + name2));
        System.out.println("stringMap = " + stringMap);


        Collector<Person, StringJoiner, String> personNameCollector =
                Collector.of(
                        () -> new StringJoiner(" | "),          // supplier
                        (j, p) -> j.add(p.firstName.toUpperCase()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        String collectedPersons = persons
                .stream()
                .collect(personNameCollector);

        System.out.println("collectedPersons = " + collectedPersons);

        class Bar {
            String name;

            Bar(String name) {
                this.name = name;
            }
        }

        class Foo {
            String name;
            List<Bar> bars = new ArrayList<>();

            Foo(String name) {
                this.name = name;
            }
        }

        List<Foo> foos = new ArrayList<>();

        // create foos
        IntStream
                .range(1, 4)
                .forEach(i -> foos.add(new Foo("Foo" + i)));

        // create bars
        foos.forEach(f ->
                IntStream
                        .range(1, 4)
                        .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));

        foos.stream()
                .flatMap(foo -> foo.bars.stream())
                .forEach(bar -> System.out.println(bar.name));

        System.out.println("--------------------- change ------------------------");

        //foos.stream().peek(foo -> foo.bars.stream().peek(bar -> bar.name = "*"))
        foos.stream().peek(foo -> foo.bars.add(new Bar("***")))
                .flatMap(foo -> foo.bars.stream())
                .forEach(bar -> System.out.println(bar.name));

        foos.stream().peek(foo -> foo.bars.forEach(bar -> bar.name = bar.name + "?")).forEach(System.out::println);
        foos.stream().peek(foo -> foo.bars.add(new Bar("**")));

        foos.stream()
                .flatMap(foo -> foo.bars.stream())
                .forEach(bar -> System.out.println(bar.name));

        foos.stream().peek(foo -> foo.bars.add(new Bar("***--")))
                .flatMap(foo -> foo.bars.stream())
                .forEach(bar -> System.out.println(bar.name));

        foos.stream()
                .flatMap(foo -> foo.bars.stream())
                .forEach(bar -> System.out.println(bar.name));

/*        IntStream.range(1, 4)
                .mapToObj(i -> new Foo("Foo" + i))
                .peek(f -> IntStream.range(1, 4)
                        .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
                        .forEach(f.bars::add))
                .flatMap(f -> f.bars.stream())
                .forEach(bar -> System.out.println(bar.name));

        IntStream.range(1, 4)
                .mapToObj(i -> new Foo("Foo" + i))
                .map(foo -> {
                    System.out.println("map call --------------");
                    IntStream.range(1, 4)
                        .mapToObj(i -> new Bar("Bar" + i + " <- " + foo.name))
                        .forEach(foo.bars::add); return foo;})
                .flatMap(f -> f.bars.stream())
                .forEach(bar -> System.out.println(bar.name));*/

        class Inner {
            String foo = "Fooooooooooooooooo!";
        }


        class Nested {
            Inner inner = new Inner();
        }

        class Outer {
            Nested nested = new Nested();
        }


        Optional.of(new Outer())
                .flatMap(outer -> Optional.ofNullable(outer.nested))
                .flatMap(nested -> Optional.ofNullable(nested.inner))
                .flatMap(inner -> Optional.ofNullable(inner.foo))
                .ifPresent(System.out::println);


        Integer ageSum = persons
                .stream()
                .reduce(0, (sum0, p) -> sum0 += p.age, (sum1, sum2) -> sum1 + sum2);

        System.out.println("ageSum = " + ageSum);
    }


}
