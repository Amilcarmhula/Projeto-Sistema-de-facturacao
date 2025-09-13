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

public class LeitorSerial {
    public static void main(String[] args) {
        // Abra a porta correta (ex: "COM3" no Windows ou "/dev/ttyUSB0" no Linux)
        SerialPort porta = SerialPort.getCommPort("COM3");
        porta.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        porta.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        if (porta.openPort()) {
            System.out.println("Porta aberta com sucesso. Passe um código de barras...");

            Scanner scanner = new Scanner(porta.getInputStream());

            while (scanner.hasNextLine()) {
                String codigo = scanner.nextLine().trim();
                System.out.println("Código lido: " + codigo);
            }

            scanner.close();
            porta.closePort();
        } else {
            System.out.println("Não foi possível abrir a porta.");
        }
    }
}
