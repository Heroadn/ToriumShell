import org.example.IO.ShellConsole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class ShellConsoleTest {

    private ByteArrayOutputStream buffer;
    private ShellConsole console;

    @BeforeEach
    void setup() {
        buffer = new ByteArrayOutputStream();
        console = new ShellConsole(new PrintStream(buffer));
    }

    private String output() {
        return buffer.toString();
    }

    // -------------------------------------------------------------------------
    // print
    // -------------------------------------------------------------------------

    @Test
    void printEscreveSemQuebraLinha() {
        console.print("hello");
        assertEquals("hello", output());
    }

    @Test
    void printNaoAdicionaQuebraLinha() {
        console.print("hello");
        assertFalse(output().contains("\n"));
    }

    @Test
    void printStringVazia() {
        console.print("");
        assertEquals("", output());
    }

    @Test
    void printSequencial() {
        console.print("a");
        console.print("b");
        console.print("c");
        assertEquals("abc", output());
    }

    // -------------------------------------------------------------------------
    // println
    // -------------------------------------------------------------------------

    @Test
    void printlnAdicionaQuebraLinha() {
        console.println("hello");
        assertEquals("hello" + System.lineSeparator(), output());
    }

    @Test
    void printlnStringVazia() {
        console.println("");
        assertEquals(System.lineSeparator(), output());
    }

    @Test
    void printlnSequencial() {
        console.println("linha1");
        console.println("linha2");
        String[] linhas = output().split(System.lineSeparator());
        assertEquals("linha1", linhas[0]);
        assertEquals("linha2", linhas[1]);
    }

    // -------------------------------------------------------------------------
    // combinado
    // -------------------------------------------------------------------------

    @Test
    void printSeguindoDePrintln() {
        console.print("a");
        console.println("b");
        assertTrue(output().startsWith("ab"));
    }

    @Test
    void consolePadraoUsaSystemOut() {
        // apenas verifica que o construtor sem args não lança exceção
        assertDoesNotThrow(() -> new ShellConsole().println("ok"));
    }

    // -------------------------------------------------------------------------
    // error
    // -------------------------------------------------------------------------

    @Test
    void errorImprimeMensagemDaExcecao() {
        console.error(new Exception("arquivo não encontrado"));
        assertTrue(output().contains("arquivo não encontrado"));
    }

    @Test
    void errorContemPrefixoErro() {
        console.error(new Exception("algo deu errado"));
        assertTrue(output().startsWith("ERRO:"));
    }

    @Test
    void errorAdicionaQuebraLinha() {
        console.error(new Exception("falha"));
        assertTrue(output().contains(System.lineSeparator()));
    }

    @Test
    void errorComMensagemNulaNaoLancaExcecao() {
        assertDoesNotThrow(() -> console.error(new Exception()));
    }

    @Test
    void errorNaoInterferePrintlnSeguinte() {
        console.error(new Exception("erro"));
        console.println("ok");
        assertTrue(output().contains("ok"));
    }
}