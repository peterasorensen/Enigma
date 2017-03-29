package enigma;

import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Peter Sorensen
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _allRotors = allRotors;
        _slots = new Rotor[numRotors];
        _pawls = pawls;

    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _slots.length;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return this._pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < _slots.length; i++) {
            for (Rotor each : _allRotors) {
                if (each.name().equalsIgnoreCase(rotors[i])) {
                    _slots[i] = each;
                    break;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < numRotors(); i++) {
            _slots[i].set(_alphabet.toInt(setting.charAt(i - 1)));

        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plug = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        c = _plug.permute(c);
        for (int i = _slots.length - _pawls; i < _slots.length - 1; i++) {
            if (_slots[i] instanceof MovingRotor) {
                if (_slots[i + 1].atNotch() || _slots[i].atNotch()) {
                    _slots[i].advance();
                }
            }
        }
        _slots[_slots.length - 1].advance();
        for (int i = _slots.length - 1; i >= 0; i--) {
            c = _slots[i].convertForward(c);
        }
        for (int i = 1; i < _slots.length; i++) {
            c = _slots[i].convertBackward(c);
        }
        return _plug.permute(c);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll(" ", "").toUpperCase();
        int blanks = 0;
        String ret = "";
        for (int i = 0; i < msg.length(); i++) {
            if (blanks == 5) {
                ret += " ";
                i--;
                blanks = 0;
            } else {
                ret += _alphabet.toChar(convert(
                        _alphabet.toInt(msg.charAt(i))));
                blanks++;
            }
        }
        return ret;
    }

    /** Returns whether or not an rotor is set to it's notch to start. */
    boolean startsNotch = false;

    /** Returns the array of rotors in the slots of this Enigma Machine. */
    Rotor[] getSlots() {
        return _slots;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The Rotors contained in the slots of this Enigma Machine. */
    private Rotor[] _slots;

    /** The number of Pawls in this Enigma machine. */
    private int _pawls;

    /** A collection of all available rotors in this Enigma machine. */
    private Collection<Rotor> _allRotors;

    /** The plugboard of this Enigma machine. */
    private Permutation _plug;
}
