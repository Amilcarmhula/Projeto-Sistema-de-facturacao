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
package jminvsm;

/**
 *
 * @author JM-Tecnologias
 */
public class ConfigurationManager {

    private static volatile ConfigurationManager instance; // The single instance of the class
    private String databaseUrl;
    private int portNumber;

    // Private constructor to prevent direct instantiation
    private ConfigurationManager(String databaseUrl, int portNumber) {
        this.databaseUrl = databaseUrl;
        this.portNumber = portNumber;
    }

    // Public static method to get the single instance
    public static ConfigurationManager getInstance(String databaseUrl, int portNumber) {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    // Create the instance only if it doesn't exist
                    instance = new ConfigurationManager(databaseUrl, portNumber);
                }
            }
        }
        return instance;
    }

    // Getters for the variables
//    public String getDatabaseUrl() {
//        return databaseUrl;
//    }
//
//    public int getPortNumber() {
//        return portNumber;
//    }
    // Example of how to use the Singleton
    public static void main(String[] args) {
        // First time calling getInstance, an instance is created with the provided values
        ConfigurationManager config1 = ConfigurationManager.getInstance("jdbc:mysql://localhost:3306/mydb", 8080);
//        System.out.println("Config 1 - Database URL: " + config1.getDatabaseUrl() + ", Port: " + config1.getPortNumber());
//        config1.databaseUrl = "";
        System.out.println("g:" + config1.databaseUrl);

        // Second time calling getInstance, the existing instance is returned
        ConfigurationManager config2 = ConfigurationManager.getInstance("jdbc:postgresql://remotehost:5432/otherdb", 9090);
//        System.out.println("Config 2 - Database URL: " + config2.getDatabaseUrl() + ", Port: " + config2.getPortNumber());

        // Verify that both references point to the same instance
        System.out.println("Are config1 and config2 the same instance? " + (config1 == config2));
    }
}
