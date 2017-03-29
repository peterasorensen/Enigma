package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Peter Sorensen
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void testPermute() {
        perm = new Permutation("(ABC) (DEF) (GH)", UPPER);
        assertEquals(perm.permute(0), 1);
        assertEquals(perm.permute(2), 0);
        assertEquals(perm.permute(25), 25);
    }

    @Test
    public void testaddCycle() {
        perm = new Permutation("(ABC) (DEF) (GH)", UPPER);
        perm.addCycle("ZYX");
        assertEquals(perm.getCycles(), "(ABC) (DEF) (GH) (ZYX)");
    }

    @Test
    public void testcharPermute() {
        perm = new Permutation("(ABCZ)", UPPER);
        assertEquals(perm.permute('A'), 'B');
        assertEquals(perm.permute('Z'), 'A');
    }

    @Test
    public void testcharInvert() {
        perm = new Permutation("(ABCZ)     ", UPPER);
        assertEquals(perm.invert('B'), 'A');
        assertEquals(perm.invert('Z'), 'C');
    }

    @Test
    public void testDerangement() {
        perm = new Permutation("(AB)", UPPER);
        assertEquals(perm.derangement(), false);
        Permutation perm1 = new Permutation(
                "(ABCDEFGHIJKLMNOPQRSTUVWXYZ)", UPPER);
        assertEquals(perm1.derangement(), true);
    }

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

}
