package funk4j.tuples;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author OZY on 2016.03.14.
 */
public class TripletTest {

    @Test
    public void isAssignedWithCorrectValues() throws Exception {
        Triplet<String, String, String> triplet = defaultTriplet();
        assertEquals("foo", triplet._1);
        assertEquals("bar", triplet._2);
        assertEquals("1", triplet._3);
    }

    @Test
    public void separate_Instances_Are_Equal_If_Same_Values() throws Exception {
        assertEquals(defaultTriplet(), defaultTriplet());
    }

    @Test
    public void maybe() throws Exception {
        assertTrue(defaultTriplet().maybe1().isPresent());
        assertTrue(defaultTriplet().maybe2().isPresent());
        assertTrue(defaultTriplet().maybe3().isPresent());
    }

    @Test
    public void canFindInMaps() throws Exception {
        Map<Triplet<String, String, String>, String> map = new HashMap<>();
        map.put(defaultTriplet(), "1");
        map.put(new Triplet<>("bar", "foo", "1"), "2");
        assertEquals("1", map.get(defaultTriplet()));
        assertEquals("2", map.get(new Triplet<>("bar", "foo", "1")));
    }

    @Test
    public void shouldBeEqual() throws Exception {
        final Triplet<String, String, String> left = Triplet.of("foo", "bar", "1");
        final Triplet<String, String, String> right = Triplet.of("foo", "bar", "1");
        final Triplet<String, String, String> notRight = Triplet.of("NotFoo", "NotBar", "2");
        final Triplet<String, String, String> firstNull = Triplet.of(null, "NotBar", "2");
        final Triplet<String, String, String> secondNull = Triplet.of("foo", null, "2");
        final Triplet<String, String, String> thirdNull = Triplet.of("foo", "NotBar", null);

        assertEquals(left, left);
        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left, firstNull);
        assertNotEquals(left, secondNull);
        assertNotEquals(left, thirdNull);
        assertNotEquals(left.hashCode(), notRight.hashCode());
        assertNotEquals(left.hashCode(), firstNull.hashCode());
        assertNotEquals(left.hashCode(), secondNull.hashCode());
        assertNotEquals(left.hashCode(), thirdNull.hashCode());
    }

    @Test
    public void toString_Prints_Values() throws Exception {
        assertEquals("Triplet{_1=foo, _2=bar, _3=1}", defaultTriplet().toString());
    }

    @Test
    public void map_To_Pair() throws Exception {
        assertEquals(new Pair<>("foo", "bar"), defaultTriplet().map(triplet -> new Pair<>(triplet._1, triplet._2)));
    }

    @Test
    public void shouldForeach() throws Exception {
        final ArrayList<String> list = new ArrayList<>(3);
        Triplet.forEach(defaultTriplet(), list::add);
        assertEquals(3, list.size());
        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
        assertEquals("1", list.get(2));
    }

    @Test
    public void update_Only_Second_Value() throws Exception {
        assertEquals(new Triplet<>("foo", "copy", "1"), defaultTriplet().update2("copy"));
    }

    @Test
    public void update_Only_First_Value() throws Exception {
        assertEquals(new Triplet<>("copy", "bar", "1"), defaultTriplet().update1("copy"));
    }

    @Test
    public void update_Only_Third_Value() throws Exception {
        assertEquals(new Triplet<>("foo", "bar", "copy"), defaultTriplet().update3("copy"));
    }

    private Triplet<String, String, String> defaultTriplet() {
        return Triplet.of("foo", "bar", "1");
    }

}
