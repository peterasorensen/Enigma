package enigma;

import static enigma.EnigmaException.*;

/* Extra Credit Only */

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Peter Sorensen
 */
class Alphabet {

    /** String containing all the characters of this Alphabet. */
    private String _strAlpha;

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _strAlpha = chars;
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _strAlpha.trim().length();
    }

    /** Returns true if C is in this alphabet. */
    boolean contains(char c) {
        return _strAlpha.indexOf(c) >= 0;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw error("character index out of range");
        }
        return _strAlpha.charAt(index);
    }

    /** Returns the index of character C, which must be in the alphabet. */
    int toInt(char c) {
        if (_strAlpha.indexOf(c) == -1) {
            throw error("character not in alphabet");
        }
        return _strAlpha.indexOf(c);
    }

}
