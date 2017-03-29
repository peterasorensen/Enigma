package enigma;

import ucb.junit.textui;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the enigma package.
 *  @author Peter Sorensen
 */
public class UnitTest {

    /** Returns a new MovingRotor according to NAME, ROTORS and NOTCHES. */
    private MovingRotor setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        return new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                notches);
    }

    /** Returns a new Reflector according to NAME and ROTORS. */
    private Reflector setReflector(
            String name, HashMap<String, String> rotors) {
        return new Reflector(name, new Permutation(rotors.get(name), UPPER));
    }

    /** Returns a new FixedRotor according to NAME and ROTORS. */
    private FixedRotor setFixed(String name, HashMap<String, String> rotors) {
        return new FixedRotor(name, new Permutation(rotors.get(name), UPPER));
    }

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(PermutationTest.class, MovingRotorTest.class,
                UnitTest.class);
    }

    /* ***** TESTS ***** */

    @Test
    public void testMachinesetinsert() {
        Rotor one = setRotor("I", NAVALA, "");
        Rotor two = setRotor("II", NAVALA, "");
        Rotor three = setRotor("III", NAVALA, "");
        Rotor four = setRotor("IV", NAVALA, "");
        Rotor five = setRotor("V", NAVALA, "");
        Rotor[] addTo = new Rotor[]{one, two, three, four, five};
        Collection<Rotor> coll = new ArrayList<Rotor>();
        coll.addAll(Arrays.asList(addTo));
        Machine cool = new Machine(UPPER, 5, 3, coll);
        String[] available = new String[]{"I", "II", "III", "IV", "V"};
        cool.insertRotors(available);
        assertEquals(cool.numRotors(), 5);
        cool.setRotors("AXLE");
        assertEquals(cool.getSlots()[0].setting(), 0);
        assertEquals(cool.getSlots()[1].setting(), 0);
        assertEquals(cool.getSlots()[2].setting(), 23);
        assertEquals(cool.getSlots()[3].setting(), 11);
        assertEquals(cool.getSlots()[4].setting(), 4);
    }

    @Test
    public void testConvert() {
        Reflector one = setReflector("B", NAVALA);
        FixedRotor two = setFixed("Beta", NAVALA);
        MovingRotor three = setRotor("III", NAVALA, "V");
        MovingRotor four = setRotor("IV", NAVALA, "J");
        MovingRotor five = setRotor("I", NAVALA, "Q");
        Rotor[] addTo = new Rotor[]{one, two, three, four, five};
        Collection<Rotor> coll = new ArrayList<Rotor>();
        coll.addAll(Arrays.asList(addTo));
        Machine cool = new Machine(UPPER, 5, 3, coll);
        String[] available = new String[]{"B", "Beta", "III", "IV", "I"};
        cool.insertRotors(available);
        cool.setRotors("AXLE");
        cool.setPlugboard(new Permutation("(YF) (ZH)", UPPER));
        assertEquals(cool.convert(24), 25);
    }

    @Test
    public void testMsgconvert() {
        Reflector one = setReflector("B", NAVALA);
        FixedRotor two = setFixed("Beta", NAVALA);
        MovingRotor three = setRotor("III", NAVALA, "V");
        MovingRotor four = setRotor("IV", NAVALA, "J");
        MovingRotor five = setRotor("I", NAVALA, "Q");
        Rotor[] addTo = new Rotor[]{one, two, three, four, five};
        Collection<Rotor> coll = new ArrayList<Rotor>();
        coll.addAll(Arrays.asList(addTo));
        Machine cool = new Machine(UPPER, 5, 3, coll);
        String[] available = new String[]{"B", "Beta", "III", "IV", "I"};
        cool.insertRotors(available);
        cool.setRotors("AXLE");
        cool.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER));
        String input = "FROM his shoulder Hiawatha";
        String output = "QVPQS OKOIL PUBKJ ZPISF XDW";
        assertEquals(cool.convert(input), output);
        input = "Took the camera of rosewood";
        output = "BHCNS CXNUO AATZX SRCFY DGU";
        assertEquals(cool.convert(input), output);
    }

    @Test
    public void testAlphabet() {
        Alphabet alph = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals(alph.toChar(0), 'A');
        assertEquals(alph.toInt('Z'), 25);
        assertEquals(alph.toChar(25), 'Z');
    }
}
