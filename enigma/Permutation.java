package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Peter Sorensen
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters not
     *  included in any cycle map to themselves. Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        _cyclesList = cycles.replaceAll("[ (]", "").split("\\)");
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    void addCycle(String cycle) {
        _cycles += " (" + cycle + ")";
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char in = _alphabet.toChar(wrap(p));
        for (String temp : _cyclesList) {
            if (temp.indexOf(in) >= 0) {
                int index = temp.indexOf(in);
                if (index == temp.length() - 1) {
                    return _alphabet.toInt(temp.charAt(0));
                }
                return _alphabet.toInt(temp.charAt(index + 1));
            }
        }
        return wrap(p);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char in = _alphabet.toChar(wrap(c));
        for (String temp : _cyclesList) {
            if (temp.indexOf(in) >= 0) {
                int index = temp.indexOf(in);
                if (index == 0) {
                    return _alphabet.toInt(temp.charAt(temp.length() - 1));
                }
                return _alphabet.toInt(temp.charAt(index - 1));
            }
        }
        return wrap(c);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int cycleSize = 0;
        for (String temp : _cyclesList) {
            cycleSize += temp.length();
        }
        if (cycleSize == size()) {
            return true;
        }
        return false;
    }

    /** Returns the the String _cycles of this permutation. */
    String getCycles() {
        return _cycles;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** A String in the form of "(cccc) (cc) ..." where the c's are
     *  the characters in the given alphabet. Excluded characters map
     *  to themselves. */
    private String _cycles;

    /** A list of split _cycles with no blanks and no parentheses. */
    private String[] _cyclesList;
}

