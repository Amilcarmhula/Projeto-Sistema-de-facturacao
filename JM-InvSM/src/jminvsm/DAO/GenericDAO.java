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
package jminvsm.dao;

import java.sql.*;
import java.util.*;
import java.lang.reflect.*;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 * @param <T>
 */
public abstract class GenericDAO<T> {

    protected Connection con;
    private final Class<T> type;
    private final String tableName;
    private final String[] columns;

    public GenericDAO(Class<T> type, String tableName, String[] columns) throws SQLException {
        this.con = Conexao.getConexao();
        this.type = type;
        this.tableName = tableName;
        this.columns = columns;
    }

    // ---------------------------
    // CREATE
    // ---------------------------
    public void save(T obj) throws Exception {
        String placeholders = String.join(",", Collections.nCopies(columns.length, "?"));
        String cols = String.join(",", columns);
        String sql = "INSERT INTO " + tableName + " (" + cols + ") VALUES (" + placeholders + ")";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            for (int i = 0; i < columns.length; i++) {
                Field field = type.getDeclaredField(columns[i]);
                field.setAccessible(true);
                stmt.setObject(i + 1, field.get(obj));
            }
            stmt.executeUpdate();
        }
    }

    // ---------------------------
    // READ ALL
    // ---------------------------
    public List<T> listAll() throws Exception {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                T obj = type.getDeclaredConstructor().newInstance();
                for (String col : columns) {
                    Field field = type.getDeclaredField(col);
                    System.out.println(col+": "+rs.getObject(col));
                    field.setAccessible(true);
//                    field.set(col, rs.getObject(col));
                }
                list.add(obj);
            }
        }
        return list;
    }
    // READ ALL
// ---------------------------
//    public List<T> listAll() throws Exception {
//        List<T> list = new ArrayList<>();
//        String sql = "SELECT * FROM " + tableName;
//
//        try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                T obj = type.getDeclaredConstructor().newInstance();
//                for (String col : columns) {
//                    Field field = type.getDeclaredField(col);
//                    field.setAccessible(true);
//
//                    Object value = rs.getObject(col);
//
//                    // Se o campo for primitivo e o valor estiver null, atribui um default
//                    if (value == null && field.getType().isPrimitive()) {
//                        if (field.getType().equals(int.class)) {
//                            value = 0;
//                        } else if (field.getType().equals(double.class)) {
//                            value = 0.0;
//                        } else if (field.getType().equals(long.class)) {
//                            value = 0L;
//                        } else if (field.getType().equals(boolean.class)) {
//                            value = false;
//                        } else if (field.getType().equals(float.class)) {
//                            value = 0f;
//                        } else if (field.getType().equals(short.class)) {
//                            value = (short) 0;
//                        } else if (field.getType().equals(byte.class)) {
//                            value = (byte) 0;
//                        } else if (field.getType().equals(char.class)) {
//                            value = '\u0000';
//                        }
//                    }
//
//                    field.set(obj, value);
//                }
//                list.add(obj);
//            }
//        }
//        return list;
//    }

    // ---------------------------
    // READ BY ID
    // ---------------------------
    public T findById(int id) throws Exception {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                T obj = type.getDeclaredConstructor().newInstance();
                for (String col : columns) {
                    Field field = type.getDeclaredField(col);
                    field.setAccessible(true);
                    field.set(obj, rs.getObject(col));
                }
                return obj;
            }
        }
        return null;
    }

    // ---------------------------
    // UPDATE
    // ---------------------------
    public boolean update(T obj, int id) throws Exception {
        String setClause = String.join(" = ?, ", columns) + " = ?";
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            for (int i = 0; i < columns.length; i++) {
                Field field = type.getDeclaredField(columns[i]);
                field.setAccessible(true);
                stmt.setObject(i + 1, field.get(obj));
            }
            stmt.setInt(columns.length + 1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // ---------------------------
    // DELETE
    // ---------------------------
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
