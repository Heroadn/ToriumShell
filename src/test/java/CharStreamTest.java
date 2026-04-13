import org.example.Lexer.CharStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharStreamTest {

    CharStream stream;

    @BeforeEach
    void setup() {}

    private void prepare(String input) {
        stream = new CharStream(input);
    }

    // -------------------------------------------------------------------------
    // peek
    // -------------------------------------------------------------------------

    @Test
    void peekValido() {
        prepare("rm primeiro.txt");
        assertEquals('r', stream.peek());
    }

    @Test
    void peekFalha() {
        prepare("rm primeiro.txt");
        assertNotEquals('m', stream.peek()); // peek sem advance retorna 'r', não 'm'
    }

    @Test
    void peekNaoAvancaPos() {
        prepare("abc");
        stream.peek();
        stream.peek();
        assertEquals('a', stream.peek());
    }

    @Test
    void peekInputVazioRetornaNull() {
        prepare("");
        assertEquals('\0', stream.peek()); // convenção: \0 quando não há mais chars
    }

    // -------------------------------------------------------------------------
    // consume
    // -------------------------------------------------------------------------

    @Test
    void consumeValido() {
        prepare("rm primeiro.txt segundo.txt terceiro.txt");

        char ch = stream.peek();
        stream.advance();
        assertEquals('r', ch);
        assertEquals('m', stream.peek());

        assertEquals('m', stream.consume());
        assertEquals(' ', stream.consume());
        assertEquals('p', stream.consume());
    }

    @Test
    void consumeFalha() {
        prepare("rm primeiro.txt segundo.txt terceiro.txt");

        char ch = stream.peek();
        stream.advance();
        assertEquals('r', ch);
        assertEquals('m', stream.peek());

        assertEquals('m', stream.consume());
        assertNotEquals('m', stream.consume()); // próximo é ' ', não 'm'
        assertEquals('p', stream.consume());
    }

    @Test
    void consumeAvancaPos() {
        prepare("abc");
        assertEquals('a', stream.consume());
        assertEquals('b', stream.consume());
        assertEquals('c', stream.consume());
    }

    @Test
    void consumeAposUltimoCharRetornaNull() {
        prepare("a");
        stream.consume();
        assertEquals('\0', stream.consume());
    }

    // -------------------------------------------------------------------------
    // advance
    // -------------------------------------------------------------------------

    @Test
    void advanceValido() {
        prepare("abc");
        stream.advance();
        assertEquals('b', stream.peek());
    }

    @Test
    void advanceFalha() {
        prepare("abc");
        stream.advance();
        assertNotEquals('a', stream.peek()); // avançou, não pode ser 'a' ainda
    }

    @Test
    void advanceSequencial() {
        prepare("abc");
        stream.advance();
        stream.advance();
        assertEquals('c', stream.peek());
    }

    @Test
    void advanceAlemDoFimNaoLancaExcecao() {
        prepare("a");
        stream.advance(); // consome 'a'
        assertThrows(Exception.class, () -> stream.advance());
    }

    // -------------------------------------------------------------------------
    // hasNext
    // -------------------------------------------------------------------------

    @Test
    void hasNextValido() {
        prepare("abc");
        assertTrue(stream.hasNext());
    }

    @Test
    void hasNextFalha() {
        prepare("");
        assertFalse(stream.hasNext());
    }

    @Test
    void hasNextFalsoAposConsumirTudo() {
        prepare("ab");
        stream.consume();
        stream.consume();
        assertFalse(stream.hasNext());
    }

    @Test
    void hasNextVerdadeiroAntesDoFim() {
        prepare("ab");
        stream.consume();
        assertTrue(stream.hasNext());
    }

    // -------------------------------------------------------------------------
    // skip
    // -------------------------------------------------------------------------

    @Test
    void skipPulaEspacos() {
        prepare("   abc");
        stream.skip(Character::isWhitespace);
        assertEquals('a', stream.peek());
    }

    @Test
    void skipSemEspacosNaoAvanca() {
        prepare("abc");
        stream.skip(Character::isWhitespace);
        assertEquals('a', stream.peek());
    }

    @Test
    void skipPulaQuebrasDeLinha() {
        prepare("\n\nabc");
        stream.skip(c -> c == '\n');
        assertEquals('a', stream.peek());
    }

    @Test
    void skipInputVazioNaoLancaExcecao() {
        prepare("");
        assertDoesNotThrow(() -> stream.skip(Character::isWhitespace));
    }

    @Test
    void skipParaNoFirstCharQueNaoSatisfazPredicado() {
        prepare("   a   b");
        stream.skip(Character::isWhitespace);
        assertEquals('a', stream.peek());
    }

    // -------------------------------------------------------------------------
    // reset
    // -------------------------------------------------------------------------

    @Test
    void resetValido() {
        prepare("abc");
        stream.consume();
        stream.consume();
        stream.reset();
        assertEquals('a', stream.peek());
    }

    @Test
    void resetFalha() {
        prepare("abc");
        stream.consume();
        assertNotEquals('a', stream.peek()); // avançou, sem reset não volta
    }

    @Test
    void resetAposConsumirTudo() {
        prepare("ab");
        stream.consume();
        stream.consume();
        assertFalse(stream.hasNext());
        stream.reset();
        assertTrue(stream.hasNext());
        assertEquals('a', stream.peek());
    }

    @Test
    void resetNaoAlteraInput() {
        prepare("abc");
        stream.consume();
        stream.reset();
        assertEquals('a', stream.consume());
        assertEquals('b', stream.consume());
        assertEquals('c', stream.consume());
    }
}