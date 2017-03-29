package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Peter Sorensen
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        try {
            _m = readConfig();
            setUp(_m, _input.nextLine());
            if (!(_m.getSlots()[0] instanceof Reflector)) {
                throw error("first rotor must be a reflector");
            }
            while (_input.hasNextLine() || _input.hasNext()) {
                if (_input.hasNext("\\*")) {
                    String store = _input.nextLine();
                    while (store.trim().equals("")) {
                        _output.println();
                        store = _input.nextLine();
                    }
                    setUp(_m, store);
                } else {
                    _output.println(_m.convert(_input.nextLine()));
                }
            }
        } catch (ArrayIndexOutOfBoundsException
                | NoSuchElementException
                | NullPointerException excp) {
            throw error("Invalid Configuration, Settings, or Input.");
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine());
            if (_alphabet.size() == 0) {
                throw error("configuration file truncated");
            }
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            ArrayList<Rotor> coll = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                coll.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, coll);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String second = _config.next();
            String notches = second.substring(1);
            char type = second.charAt(0);
            String cycles = "";
            while (_config.hasNext("\\(.*")) {
                String store = _config.next();
                if (!store.contains(")")) {
                    throw error("Parentheses weren't closed!");
                }
                cycles += store + " ";
            }
            if (type == 'M') {
                return new MovingRotor(name, new Permutation(
                        cycles.trim(), _alphabet), notches);
            } else if (type == 'N') {
                return new FixedRotor(name, new Permutation(
                        cycles.trim(), _alphabet));
            } else if (type == 'R') {
                return new Reflector(name, new Permutation(
                        cycles.trim(), _alphabet));
            } else {
                throw error("Rotor type unreadable");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        try {
            if (!settings.startsWith("*")) {
                throw error("no rotors in machine");
            }
            int plugIndex = settings.indexOf("(");
            if (plugIndex >= 0) {
                String plug = settings.substring(plugIndex);
                M.setPlugboard(new Permutation(plug, _alphabet));
                settings = settings.trim().substring(1, plugIndex).trim();
            } else {
                M.setPlugboard(new Permutation("", _alphabet));
                settings = settings.trim().substring(1).trim();
            }
            String[] rotlist;
            String four = settings.substring(
                    settings.lastIndexOf(" ") + 1, settings.length());
            if (four.length() != M.numRotors() - 1) {
                throw error("Settings is an incorrectly sized string!");
            }
            rotlist = settings.substring(
                    0, settings.lastIndexOf(" ")).trim().split(" ");
            if (rotlist.length != M.numRotors()) {
                throw error("Wrong number of arguments");
            }
            M.insertRotors(rotlist);
            int k = 0;
            for (Rotor each : M.getSlots()) {
                if (each instanceof MovingRotor) {
                    k++;
                }
            }
            if (k != M.numPawls()) {
                throw error("Too many MovingRotors for number of pawls!");
            }
            M.setRotors(four);
        } catch (StringIndexOutOfBoundsException
                | NoSuchElementException
                | NullPointerException excp) {
            throw error("Invalid Configuration, Settings, or Input.");
        }
    }

    /** This machine. */
    private Machine _m;

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
