package wumpus;

import com.sun.org.apache.xpath.internal.SourceTree;
import java.util.ArrayList;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App {

    private int qtdPosicoes = 4;
    private String[][] posicoes = new String[qtdPosicoes][qtdPosicoes];
    private Random rand = new Random();
    private int qtdAgente = 0;
    private String nomeAgente = "";
    private int pontuacao = 0;

    public void posicionarCenario() {
        /**marcando a posição dos buracos */
        for (int i = 0; i < 3; i++) {
            int numBuraco = i + 1;
            this.posicionarObjetos("buraco " + numBuraco);
        }

        this.posicionarObjetos("Wumpus");
        this.posicionarObjetos("ouro");
        this.posicionarAgente("agente 1");

        this.posicionarAgente("agente 2");
        this.posicionarAgente("agente 3");
    }

    public void posicionarAgente(String tipo) {
        this.qtdAgente++;
        this.pontuacao = 0;//zera pontuação para novo agente entrar na caverna
        this.posicoes[0][0] = tipo;//setando a posição do agente no começo da caverna
        this.nomeAgente = tipo;
        System.out.println("Agente (" + tipo + ") começou o jogo");
        this.caminhaRobo();//movimentando o agente perante a caverna.
    }

    /**
     * para posicionar objetos perante o jogo
     */
    public void posicionarObjetos(String objetoJogo) {
        int gerado1, gerado2;
        while (true) {
            gerado1 = this.rand.nextInt(3);
            gerado2 = this.rand.nextInt(qtdPosicoes - 1);
            if (this.posicoes[gerado1][gerado2] == null || this.posicoes[gerado1][gerado2].isEmpty()) {
                this.posicoes[gerado1][gerado2] = objetoJogo;
                break;
            }
        }
        System.out.println("Posição do " + objetoJogo + " ficou em: " + this.apresentaContador(gerado1) + ","
                + this.apresentaContador(gerado2));
    }

    public int apresentaContador(int num) {
        return num + 1;
    }

    /** faz a caminhada do robo perante ao que o agente faz */
    public void caminhaRobo() {
        int qtdPasso = 0;
        boolean pegoOuro = false;
        boolean ultimaCasa = false;
        boolean wumpusMorto = false;
        int posicaoAnterior1 = 0, posicaoAnterior2 = 0;
        System.out.println("\n== Agente (" + nomeAgente + ") começando seu jogo ==");
        while (true) {
            int posicaoAtual1, posicaoAtual2;
            //guardando a posição atual do agente ao se movimentar
            posicaoAtual1 = posicaoAnterior1;
            posicaoAtual2 = posicaoAnterior2;

            if (this.posicoes[posicaoAnterior1][posicaoAnterior2] == null) {
                this.posicoes[posicaoAnterior1][posicaoAnterior2] = "";//inicializa a posição
            }
            if (this.posicoes[posicaoAnterior1][posicaoAnterior2].contains("agente")) {
                this.posicoes[posicaoAnterior1][posicaoAnterior2] = "";//zera posição anterior do agente
            }

            if (this.posicoes[posicaoAnterior1][posicaoAnterior2] != null) {
                String paraOndeVai = "";

                if (this.posicoes[posicaoAnterior1][posicaoAnterior2] != null) {
                    paraOndeVai = this.posicoes[posicaoAnterior1][posicaoAnterior2];
                    if (paraOndeVai.isEmpty()) {
                        pontuacao = pontuacao - 2; // retirada de pto - casa sem percepção nenhuma
                    } else {
                        if (paraOndeVai.contains("buraco")) {
                            pontuacao = pontuacao - 5; // retirada de pto - casa com brisa de vento do buraco
                            if (posicaoAnterior1 < 3) {
                                pontuacao = pontuacao - 10;
                                System.out.println("Agente (" + nomeAgente + ") - achou buraco em: "
                                        + this.apresentaContador(posicaoAnterior1) + ", "
                                        + this.apresentaContador(posicaoAnterior2));
                                if (posicaoAnterior1 < 3) {
                                    posicaoAnterior2 = posicaoAtual2;// sem isso ele subiria na vertical indo na parte de cima do buraco
                                    posicaoAnterior1++;//para subir na vertical caso a horizontal contenha buraco
                                }

                                if (this.posicoes[posicaoAnterior1][posicaoAnterior2] == null
                                        || this.posicoes[posicaoAnterior1][posicaoAnterior2] == "") {
                                    this.posicoes[posicaoAnterior1][posicaoAnterior2] = nomeAgente;// inicializando posição de subida na vertical
                                    System.out.println("Agente (" + nomeAgente + ") - desviou do buraco indo para: "
                                            + this.apresentaContador(posicaoAnterior1) + ", "
                                            + this.apresentaContador(posicaoAnterior2));
                                    continue;
                                } else if (this.posicoes[posicaoAnterior1][posicaoAnterior2] != "") {
                                    paraOndeVai = this.posicoes[posicaoAnterior1][posicaoAnterior2];//marca aqui caso suba verticalmente e encontre alguma casa preenchida
                                }
                            } else {
                                pontuacao = pontuacao - 1000;
                                System.out.println("Agente (" + nomeAgente + ") caiu no buraco em: "
                                        + this.apresentaContador(posicaoAnterior1) + ", "
                                        + this.apresentaContador(posicaoAnterior2));
                                break;
                            }
                        }
                        if (paraOndeVai.contains("ouro")) {
                            pegoOuro = true;
                            pontuacao = pontuacao + 1000;
                            System.out.println("Agente (" + nomeAgente + ") ganhou ouro em: "
                                    + this.apresentaContador(posicaoAnterior1) + ", "
                                    + this.apresentaContador(posicaoAnterior2));
                        }

                        if (paraOndeVai.contains("Wumpus")) {
                            /** o agente sempre joga a flecha porém horas acerta e horas não*/
                            int valorRand = this.rand.nextInt(3);
                            boolean cond = valorRand == 2;
                            pontuacao = pontuacao - 5; // retirada de pto - casa com fedor de wumpus
                            System.out.println("== Wumpus gritou == ");
                            System.out.println("Agente (" + nomeAgente + ") - pontuação: " + this.pontuacao
                                    + " jogou a flecha em Wumpus da casa: " + this.apresentaContador(posicaoAnterior1)
                                    + ", " + this.apresentaContador(posicaoAnterior2));
                            if (cond) {
                                wumpusMorto = true;
                                pontuacao = pontuacao - 10;//retirada de pontos ao jogar a flecha
                                System.out.println("Agente (" + nomeAgente
                                        + ") acertou a flecha no Wumpus - pontuação: " + this.pontuacao
                                        + " - o agente estavam em: " + this.apresentaContador(posicaoAtual1) + ", "
                                        + this.apresentaContador(posicaoAtual2) + " - o Wumpus estava em: "
                                        + this.apresentaContador(posicaoAnterior1) + ", "
                                        + this.apresentaContador(posicaoAnterior2));
                            } else {
                                pontuacao = pontuacao - 1000;
                                System.out.println("Agente (" + nomeAgente + ") - pontuação: " + pontuacao
                                        + " - errou a flecha do Wumpus e foi comido por Wumpus em: "
                                        + this.apresentaContador(posicaoAnterior1) + ", "
                                        + this.apresentaContador(posicaoAnterior2));
                                break;
                            }
                        }
                    }
                }

                this.posicoes[posicaoAnterior1][posicaoAnterior2] = nomeAgente;//coloca o agente na nova posição q foi
                System.out.println("Agente (" + nomeAgente + ") - pontuação: " + pontuacao + " foi para posição: "
                        + this.apresentaContador(posicaoAnterior1) + ", " + this.apresentaContador(posicaoAnterior2));

                if (posicaoAnterior2 <= 3) {//aqui faz ele andar entre a horizontal, colocado nesta posição para poder passar pela pos 1,1
                    posicaoAnterior2 = posicaoAnterior2 + 1;
                }

                if (ultimaCasa) {
                    if(pegoOuro == false){
                        System.out.println("Agente (" + nomeAgente + ") - não pegou ouro perdeu - 1");
                        pontuacao = pontuacao - 1;
                    }
                    System.out.println("Agente (" + nomeAgente + ") finalizou sua caminhada com " + pontuacao
                            + " pontos voltando a posição inicial");
                    
                    if(wumpusMorto){
                        System.out.println("\n == Agente (" + nomeAgente + ")  antes de sair da caverna avisou outros que Wumpus morreu ==\n");
                    }        
                    break;
                }
                if (posicaoAnterior1 == 3 && posicaoAnterior2 == 3) {
                    ultimaCasa = true;
                }
                if (posicaoAnterior2 == 3 && posicaoAnterior1 < 3) {//pulando de dimensão no vetor
                    posicaoAnterior2 = 0;
                    posicaoAnterior1++;//faz a caminhada para prox linha
                }

            }

            qtdPasso++;
        }
    }

    public static void main(String[] args) {
        System.out.println("== Jogando Wumpus ==\n");
        App app = new App();

        System.out.println("== Distribuindo o cenário randomicamente ==\n");
        app.posicionarCenario();
    }
}
