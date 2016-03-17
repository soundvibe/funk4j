package funk4j.tuples;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static funk4j.tuples.Pair.pair;
import static org.junit.Assert.*;

/**
 * @author OZY on 2016.03.14.
 */
public class PairTest {

    @Test
    public void isAssigned_With_Correct_Values() throws Exception {
        Pair<String, Integer> pair = Pair.of("foo", 1);
        assertEquals("foo", pair._1);
        assertEquals(1, (int) pair._2);
    }

    @Test
    public void isAssigned_With_Correct_ValuesWhenPairIsCalled() throws Exception {
        Pair<String, Integer> pair = pair("foo", 1);
        assertEquals("foo", pair._1);
        assertEquals(1, (int) pair._2);
    }

    @Test
    public void separate_Instances_Are_Equal_If_Same_Values() throws Exception {
        assertEquals(defaultPair(), defaultPair());
    }

    @Test
    public void maybe() throws Exception {
        assertTrue(defaultPair().maybe1().isPresent());
        assertTrue(defaultPair().maybe2().isPresent());
    }

    @Test
    public void canFindInMaps() throws Exception {
        Map<Pair<String, String>, String> map = new HashMap<>();
        map.put(defaultPair(), "1");
        map.put(new Pair<>("bar", "foo"), "2");
        assertEquals("1", map.get(defaultPair()));
        assertEquals("2", map.get(new Pair<>("bar", "foo")));
    }

    @Test
    public void shouldBeEqual() throws Exception {
        final Pair<String, String> left = Pair.of("foo", "bar");
        final Pair<String, String> right = Pair.of("foo", "bar");
        final Pair<String, String> notRight = Pair.of("NotFoo", "NotBar");
        final Pair<String, String> withRightNull = Pair.of("NotFoo", null);
        final Pair<String, String> withLeftNull = Pair.of(null, "notBar");

        assertEquals(left, left);
        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left, notRight);
        assertNotEquals(left, null);
        assertNotEquals(left, "foo");
        assertNotEquals(left.hashCode(), notRight.hashCode());

        assertNotEquals(withLeftNull, withRightNull);
        assertNotEquals(withLeftNull.hashCode(), withRightNull.hashCode());
    }

    @Test
    public void toString_Prints_Values() throws Exception {
        assertEquals("Pair{_1=foo, _2=bar}", defaultPair().toString());
    }

    @Test
    public void update_Only_Second_Value() throws Exception {
        assertEquals(new Pair<>("foo", "copy"), defaultPair().update2("copy"));
    }

    @Test
    public void update_Only_First_Value() throws Exception {
        assertEquals(new Pair<>("copy", "bar"), defaultPair().update1("copy"));
    }

    @Test
    public void shouldForeach() throws Exception {
        final ArrayList<String> list = new ArrayList<>(2);
        Pair.forEach(defaultPair(), list::add);
        assertEquals(2, list.size());
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
    }

    @Test
    public void add_Returns_Triplet() throws Exception {
        Triplet<String, String, String> triplet = defaultPair().add("1");
        assertEquals("foo", triplet._1);
        assertEquals("bar", triplet._2);
        assertEquals("1", triplet._3);
    }

    @Test
    public void map_To_Triplet() throws Exception {
        assertEquals(new Triplet<>("foo", "bar", "1"), defaultPair().map(pair -> pair.add("1")));
    }

    @Test
    public void map1() throws Exception {
        Pair<Integer, String> actual = Pair.of("one", "two").map1(one -> 1);
        assertEquals(Pair.of(1, "two"), actual);
    }

    @Test
    public void map2() throws Exception {
        Pair<String, Integer> actual = Pair.of("one", "two").map2(two -> 2);
        assertEquals(Pair.of("one", 2), actual);
    }

    private Pair<String, String> defaultPair() {
        return new Pair<>("foo", "bar");
    }


}
