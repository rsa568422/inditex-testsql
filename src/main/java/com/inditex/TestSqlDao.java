package com.inditex;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Mejorar cada uno de los métodos a nivel SQL y código cuando sea necesario
 * Razonar cada una de las mejoras que se han implementado
 * No es necesario que el código implementado funcione
 */
public class TestSqlDao {

    private static TestSqlDao instance = new TestSqlDao();
    private Hashtable<Long, Long> maxOrderUser;

    private TestSqlDao() {

    }

    private static TestSqlDao getInstance() {

        return instance;
    }

    /**
     * Obtiene el ID del último pedido para cada usuario
     * Mejoras:
     *  - Se cambia el tipo devuelto a uno más genérico (en este caso la interfaz Map)
     *  - Se cambia el tipo de excepción lanzada a uno más específico (en este caso SQLException)
     *  - Se obtiene la fila con el máximo ID_PEDIDO desde la consulta, en lugar de traerse todos los registros
     *  - Se cambia a una consulta parametrizada para evitar la inyección de SQL
     *  - Se verifica connection != null
     */
    public Map<Long, Long> getMaxUserOrderId(long idTienda) throws SQLException {
        String query = "SELECT MAX(ID_PEDIDO) AS ID_PEDIDO, ID_USUARIO FROM PEDIDOS WHERE ID_TIENDA = ? GROUP BY ID_USUARIO";
        Connection connection = getConnection();
        if (Objects.isNull(connection)) throw new SQLException("Something was wrong getting data base connection");
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setLong(1, idTienda);
        ResultSet rs = stmt.executeQuery();
        maxOrderUser = new Hashtable<>();

        while (rs.next()) {
            long idPedido = rs.getInt("ID_PEDIDO");
            long idUsuario = rs.getInt("ID_USUARIO");
            maxOrderUser.put(idUsuario, idPedido);
        }

        return maxOrderUser;
    }

    /**
     * Copia todos los pedidos de un usuario a otro
     */
    public void copyUserOrders(long idUserOri, long idUserDes) throws Exception {

        String query = String.format("SELECT FECHA, TOTAL, SUBTOTAL, DIRECCION FROM PEDIDOS WHERE ID_USUARIO = %s", idUserOri);
        Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            String insert = String.format("INSERT INTO PEDIDOS (FECHA, TOTAL, SUBTOTAL, DIRECCION) VALUES (%s, %s, %s, %s)",
                    rs.getTimestamp("FECHA"),
                    rs.getDouble("TOTAL"),
                    rs.getDouble("SUBTOTAL"),
                    rs.getString("DIRECCION"));

            Connection connection2 = getConnection();
            connection2.setAutoCommit(false);
            PreparedStatement stmt2 = connection2.prepareStatement(insert);
            stmt2.executeUpdate();
            connection2.commit();
        }
    }

    /**
     * Obtiene los datos del usuario y pedido con el pedido de mayor importe para la tienda dada
     * Mejoras:
     *  - En la implementación actual se obtiene el resultado por los parámetros de la cabecera del procedimiento,
     *    pero los campos long se pasan por valor y no por referencia, por lo que no serán actualizados. Para solucionar
     *    este problema se genera una clase auxiliar Order que será la contenedora de los campos a devolver como salida
     *    de la función, dando como salida un Optional<Order> por si no se encuentran resultados
     *  - Se eliminan los parámetros de salida de la cabecera
     *  - Se obtiene la fila con el máximo TOTAL desde la consulta, en lugar de traerse todos los registros
     *  - Se cambia el tipo de excepción lanzada a uno más específico (en este caso SQLException)
     *  - Se cambia a una consulta parametrizada para evitar la inyección de SQL
     *  - Se verifica connection != null
     */
    public Optional<Order> getUserMaxOrder(long idTienda) throws SQLException {
        String query = "SELECT U.ID_USUARIO, P.ID_PEDIDO, P.TOTAL, U.NOMBRE, U.DIRECCION "
                .concat("FROM PEDIDOS AS P INNER JOIN USUARIOS AS U ON P.ID_USUARIO = U.ID_USUARIO ")
                .concat("WHERE P.ID_TIENDA = ? ORDER BY P.TOTAL DESC LIMIT 1");
        Connection connection = getConnection();
        if (Objects.isNull(connection)) throw new SQLException("Something was wrong getting data base connection");
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setLong(1, idTienda);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return Optional.of(new Order(
                    rs.getInt("ID_USUARIO"),
                    rs.getInt("ID_PEDIDO"),
                    rs.getString("NOMBRE"),
                    rs.getString("DIRECCION")
            ));
        } else {
            return Optional.empty();
        }
    }

    private Connection getConnection() {

        // return JDBC connection
        return null;
    }

    @Getter
    @AllArgsConstructor
    public static class Order {
        private final long userId;
        private final long orderId;
        private final String name;
        private final String address;
    }
}
