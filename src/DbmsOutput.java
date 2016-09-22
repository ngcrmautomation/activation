
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DbmsOutput {

    private CallableStatement enable_stmt;
    private CallableStatement disable_stmt;
    private CallableStatement show_stmt;

    public DbmsOutput(Connection conn) throws SQLException {
        enable_stmt = conn.prepareCall("begin dbms_output.enable(:1); end;");
        disable_stmt = conn.prepareCall("begin dbms_output.disable; end;");

        show_stmt = conn.prepareCall(
                "declare "
                + " l_line varchar2(32767); "
                + " l_done number; "
                + " l_buffer long; "
                + "begin "
                + " loop "
                + " exit when length(l_buffer)+32767 > :maxbytes OR l_done = 1; "
                + " dbms_output.get_line( l_line, l_done ); "
                + " l_buffer := l_buffer || l_line || chr(10); "
                + " end loop; "
                + " :done := l_done; "
                + " :buffer := l_buffer; "
                + "end;");
    }

    public void enable(long size) throws SQLException {
        enable_stmt.setLong(1, size);
        enable_stmt.executeUpdate();
    }

    public void disable() throws SQLException {
        disable_stmt.executeUpdate();
    }

    public void show() throws SQLException {
        int done = 0;
        String temp = "";

        show_stmt.registerOutParameter(2, java.sql.Types.INTEGER);
        show_stmt.registerOutParameter(3, java.sql.Types.VARCHAR);

        for (;;) {
            show_stmt.setInt(1, 32767);
            show_stmt.executeUpdate();
            System.out.print(show_stmt.getString(3));
            if ((done = show_stmt.getInt(2)) == 1) {
                break;
            }
        }
    }

    public String getHash() throws SQLException {
        int done = 0;
        String temp = "";
        String result = "";

        show_stmt.registerOutParameter(2, java.sql.Types.INTEGER);
        show_stmt.registerOutParameter(3, java.sql.Types.VARCHAR);

        for (;;) {
            show_stmt.setInt(1, 32767);
            show_stmt.executeUpdate();
            temp = show_stmt.getString(3);
            if (temp.indexOf("encrypted string:") > -1) {
                result = temp.substring(18);
            }
            if ((done = show_stmt.getInt(2)) == 1) {
                break;
            }
        }
        return result;
    }

    public void close() throws SQLException {
        enable_stmt.close();
        disable_stmt.close();
        show_stmt.close();
    }
}
