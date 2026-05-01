import org.example.api.Lexer.Lexer;
import org.example.api.Parser.Token;
import org.example.api.Parser.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    private Lexer lexer;

    @BeforeEach
    void setup() {
        lexer = new Lexer();
    }

    // -------------------------------------------------------------------------
    // tokenizer()
    // -------------------------------------------------------------------------

    @Test
    void tokenizerRetornaListaVaziaParaInputVazio() {
        lexer.setInput("");
        List<Token> tokens = lexer.tokenizer();
        assertTrue(tokens.isEmpty());
    }

    @Test
    void tokenizerRetornaListaVaziaParaEspacosApenas() {
        lexer.setInput("   ");
        List<Token> tokens = lexer.tokenizer();
        assertTrue(tokens.isEmpty());
    }

    @Test
    void tokenizerComandoSimples() {
        lexer.setInput("mkdir");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.STRING, tokens.get(0).key());
        assertEquals("mkdir", tokens.get(0).value());
    }

    @Test
    void tokenizerComandoComArgumento() {
        lexer.setInput("mkdir novapasta");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(2, tokens.size());
        assertEquals("mkdir",     tokens.get(0).value());
        assertEquals("novapasta", tokens.get(1).value());
    }

    @Test
    void tokenizerResetaPosAposTokenizar() {
        lexer.setInput("ls src");
        lexer.tokenizer();
        // tokenizar de novo deve funcionar normalmente (pos foi resetado)
        List<Token> tokens = lexer.tokenizer();
        assertEquals(2, tokens.size());
        assertEquals("ls", tokens.get(0).value());
    }

    // -------------------------------------------------------------------------
    // STRING
    // -------------------------------------------------------------------------

    @Test
    void stringComLetras() {
        lexer.setInput("ls");
        Token t = lexer.nextToken();
        assertEquals(TokenType.STRING, t.key());
        assertEquals("ls", t.value());
    }

    @Test
    void stringComUnderscoreNoInicio() {
        lexer.setInput("_var");
        Token t = lexer.nextToken();
        assertEquals(TokenType.STRING, t.key());
        assertEquals("_var", t.value());
    }

    @Test
    void stringComLetrasEDigitos() {
        lexer.setInput("pasta123");
        Token t = lexer.nextToken();
        assertEquals(TokenType.STRING, t.key());
        assertEquals("pasta123", t.value());
    }

    @Test
    void stringsMultiplasComEspaco() {
        lexer.setInput("cd home");
        List<Token> tokens = lexer.tokenizer();
        assertEquals("cd",   tokens.get(0).value());
        assertEquals("home", tokens.get(1).value());
        assertTrue(tokens.stream().allMatch(tk -> tk.key() == TokenType.STRING));
    }

    // -------------------------------------------------------------------------
    // NUMBER
    // -------------------------------------------------------------------------

    @Test
    void numeroInteiro() {
        lexer.setInput("42");
        Token t = lexer.nextToken();
        assertEquals(TokenType.NUMBER, t.key());
        assertEquals("42", t.value());
    }

    @Test
    void numeroDecimal() {
        lexer.setInput("3.14");
        Token t = lexer.nextToken();
        assertEquals(TokenType.NUMBER, t.key());
        assertEquals("3.14", t.value());
    }

    @Test
    void numerosMultiplos() {
        lexer.setInput("1 2 3");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(3, tokens.size());
        tokens.forEach(tk -> assertEquals(TokenType.NUMBER, tk.key()));
    }

    // -------------------------------------------------------------------------
    // Flags
    // -------------------------------------------------------------------------

    @Test
    void flagR() {
        lexer.setInput("-r");
        Token t = lexer.nextToken();
        assertEquals(TokenType.STRING, t.key());
        assertEquals("-r", t.value());
    }

    @Test
    void flagP() {
        lexer.setInput("-p");
        Token t = lexer.nextToken();
        assertEquals(TokenType.STRING, t.key());
        assertEquals("-p", t.value());
    }

    @Test
    void flagHifenIsoladoNaoTrava() {
        // "-" sem letra em seguida → STRING com valor "-", pos avança
        lexer.setInput("- ");
        Token t = lexer.nextToken();
        assertNotEquals(TokenType.UNDEFINED, t.key());
    }

    @Test
    void comandoComFlag() {
        lexer.setInput("rm -r pasta");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(3, tokens.size());
        assertEquals("rm",    tokens.get(0).value());
        assertEquals("-r",    tokens.get(1).value());
        assertEquals("pasta", tokens.get(2).value());
    }

    @Test
    void multiplasFlagsEArg() {
        lexer.setInput("mkdir -p novapasta");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(3, tokens.size());
        assertEquals("-p", tokens.get(1).value());
    }

    // -------------------------------------------------------------------------
    // Símbolos individuais
    // -------------------------------------------------------------------------

    @Test
    void asterisco() {
        lexer.setInput("*");
        Token t = lexer.nextToken();
        assertEquals(TokenType.ASTERISK, t.key());
        assertEquals("*", t.value());
    }

    @Test
    void parentesesEsquerdo() {
        lexer.setInput("(");
        Token t = lexer.nextToken();
        assertEquals(TokenType.LPAREM, t.key());
    }

    @Test
    void parentesesDireito() {
        lexer.setInput(")");
        Token t = lexer.nextToken();
        assertEquals(TokenType.RPAREM, t.key());
    }

    @Test
    void virgula() {
        lexer.setInput(",");
        Token t = lexer.nextToken();
        assertEquals(TokenType.COMMA, t.key());
    }

    @Test
    void pontoEVirgula() {
        lexer.setInput(";");
        Token t = lexer.nextToken();
        assertEquals(TokenType.SEMICOLON, t.key());
    }

    @Test
    void igual() {
        lexer.setInput("=");
        Token t = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, t.key());
        assertEquals("=", t.value());
    }

    @Test
    void maiorQue() {
        lexer.setInput(">");
        Token t = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, t.key());
        assertEquals(">", t.value());
    }

    @Test
    void menorQue() {
        lexer.setInput("<");
        Token t = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, t.key());
        assertEquals("<", t.value());
    }

    @Test
    void exclamacao() {
        lexer.setInput("!");
        Token t = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, t.key());
        assertEquals("!", t.value());
    }

    // -------------------------------------------------------------------------
    // Operadores compostos
    // -------------------------------------------------------------------------

    @Test
    void operadorIgualIgual() {
        lexer.setInput("==");
        Token t = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, t.key());
        assertEquals("==", t.value());
    }

    @Test
    void operadorDiferente() {
        lexer.setInput("!=");
        Token t = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, t.key());
        assertEquals("!=", t.value());
    }

    @Test
    void operadorMenorIgual() {
        lexer.setInput("<=");
        Token t = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, t.key());
        assertEquals("<=", t.value());
    }

    @Test
    void operadorMaiorIgual() {
        lexer.setInput(">=");
        Token t = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, t.key());
        assertEquals(">=", t.value());
    }

    // -------------------------------------------------------------------------
    // Espaços e quebras de linha
    // -------------------------------------------------------------------------

    @Test
    void ignoraEspacosEntreTokens() {
        lexer.setInput("ls    src");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(2, tokens.size());
        assertEquals("ls",  tokens.get(0).value());
        assertEquals("src", tokens.get(1).value());
    }

    @Test
    void ignoraQuebrasDeLinha() {
        lexer.setInput("ls\nsrc");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(2, tokens.size());
        assertEquals("ls",  tokens.get(0).value());
        assertEquals("src", tokens.get(1).value());
    }

    @Test
    void ignoraEspacosNoInicio() {
        lexer.setInput("   ls");
        Token t = lexer.nextToken();
        assertEquals("ls", t.value());
    }

    // -------------------------------------------------------------------------
    // nextToken além do fim
    // -------------------------------------------------------------------------

    @Test
    void nextTokenAlemDoFimRetornaUndefined() {
        lexer.setInput("x");
        lexer.nextToken(); // consome "x"
        Token t = lexer.nextToken();
        assertEquals(TokenType.UNDEFINED, t.key());
        assertEquals("", t.value());
    }

    // -------------------------------------------------------------------------
    // setInput reseta estado
    // -------------------------------------------------------------------------

    @Test
    void setInputResetaPos() {
        lexer.setInput("mkdir novapasta");
        lexer.tokenizer();
        lexer.setInput("ls");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(1, tokens.size());
        assertEquals("ls", tokens.get(0).value());
    }

    // -------------------------------------------------------------------------
    // Casos realistas
    // -------------------------------------------------------------------------

    @Test
    void comandoRmRecursivo() {
        lexer.setInput("rm arquivo -r");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(3, tokens.size());
        assertEquals("rm",      tokens.get(0).value());
        assertEquals("arquivo", tokens.get(1).value());
        assertEquals("-r",      tokens.get(2).value());
    }

    @Test
    void comandoComNumeroNoNome() {
        lexer.setInput("mkdir pasta2");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(2, tokens.size());
        assertEquals("pasta2", tokens.get(1).value());
    }

    @Test
    void tokenToStringFormatado() {
        Token t = new Token(TokenType.STRING, "mkdir");
        assertEquals("[STRING : \"mkdir\"]", t.toString());
    }

    @Test
    void lerCaminhosRelativosESubPastas() {
        lexer.setInput("mkdir pasta/sub /pasta/sub/sub1 ../parent/pasta/sub/");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(4, tokens.size());
        assertEquals("mkdir",               tokens.get(0).value());
        assertEquals("pasta/sub",           tokens.get(1).value());
        assertEquals("/pasta/sub/sub1",     tokens.get(2).value());
        assertEquals("../parent/pasta/sub/",tokens.get(3).value());
    }

    @Test
    void lerCaracteresDesconhecidos() {
        lexer.setInput("@");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.UNDEFINED, tokens.get(0).key());
    }

    @Test
    void inputTerminadoEmWhiteSpace() {
        lexer.setInput("ls ");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.STRING, tokens.get(0).key());
        assertEquals("ls", tokens.get(0).value());
    }

    @Test
    void inputComecadoEmWhiteSpace() {
        lexer.setInput(" ls");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.STRING, tokens.get(0).key());
        assertEquals("ls", tokens.get(0).value());
    }

    @Test
    void testeComandoRemover() {
        lexer.setInput("rm pasta -r");
        List<Token> tokens = lexer.tokenizer();
        assertEquals(3, tokens.size());
    }
}