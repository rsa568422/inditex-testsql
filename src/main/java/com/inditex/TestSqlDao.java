package com.inditex;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

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
     */
    public Hashtable<Long, Long> getMaxUserOrderId(long idTienda) throws Exception {

        String query = String.format("SELECT ID_PEDIDO, ID_USUARIO FROM PEDIDOS WHERE ID_TIENDA = %s", idTienda);
        Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        maxOrderUser = new Hashtable<Long, Long>();

        while (rs.next()) {

            long idPedido = rs.getInt("ID_PEDIDO");
            long idUsuario = rs.getInt("ID_USUARIO");

            if (!maxOrderUser.containsKey(idUsuario)) {

                maxOrderUser.put(idUsuario, idPedido);

            } else if (maxOrderUser.get(idUsuario) < idPedido) {

                maxOrderUser.put(idUsuario, idPedido);
            }
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
     */
    public void getUserMaxOrder(long idTienda, long userId, long orderId, String name, String address) throws Exception {

        String query = String.format("SELECT U.ID_USUARIO, P.ID_PEDIDO, P.TOTAL, U.NOMBRE, U.DIRECCION FROM PEDIDOS AS P "
                + "INNER JOIN USUARIOS AS U ON P.ID_USUARIO = U.ID_USUARIO WHERE P.ID_TIENDA = %", idTienda);
        Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        double total = 0;

        while (rs.next()) {

            if (rs.getLong("TOTAL") > total) {

                total = rs.getLong("TOTAL");
                userId = rs.getInt("ID_USUARIO");
                orderId = rs.getInt("ID_PEDIDO");
                name = rs.getString("NOMBRE");
                address = rs.getString("DIRECCION");
            }
        }
    }

    private Connection getConnection() {

        // return JDBC connection
        return null;
    }
}
