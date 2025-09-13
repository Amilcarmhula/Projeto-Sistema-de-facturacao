/*
 * The MIT License
 *
 * Copyright 2025 JM-Tecnologias.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jminvsm.util;

/**
 *
 * @author JM-Tecnologias
 */
import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class LeitorSerialListener {

    public interface CodigoListener {
        void onCodigoLido(String codigo);
    }

    private SerialPort porta;
    private CodigoListener listener;

    public LeitorSerialListener(String nomePorta, CodigoListener listener) {
        this.listener = listener;
        porta = SerialPort.getCommPort(nomePorta);
        porta.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        porta.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
    }

    public void iniciar() {
        if (porta.openPort()) {
            System.out.println("Porta aberta com sucesso. Aguardando códigos...");

            // Thread para escutar a porta sem travar a aplicação principal
            new Thread(() -> {
                Scanner scanner = new Scanner(porta.getInputStream());
                while (scanner.hasNextLine()) {
                    String codigo = scanner.nextLine().trim();
                    // Dispara o listener sempre que um código é lido
                    listener.onCodigoLido(codigo);
                }
                scanner.close();
            }).start();

        } else {
            System.out.println("Não foi possível abrir a porta.");
        }
    }

    public void parar() {
        if (porta.isOpen()) {
            porta.closePort();
            System.out.println("Porta fechada.");
        }
    }

    // Exemplo de uso
//    public static void main(String[] args) {
//        LeitorSerialListener leitor = new LeitorSerialListener("COM3", codigo -> {
//            System.out.println("Código lido via listener: " + codigo);
//        });
//
//        leitor.iniciar();
//
//        // Aqui você poderia manter a aplicação rodando, ou criar lógica para parar depois
//        // leitor.parar(); // Chame quando quiser fechar a porta
//    }
}
