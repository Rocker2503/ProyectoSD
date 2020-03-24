
import java.util.Date;

/**
 *
 * @author Nicolas
 */
public class LogSurtidor 
{
    private Date fecha;
    private Date horaInicioCaida;
    private Date horaFinCaida;
    private int tiempoCaida;

    public LogSurtidor() {
        fecha = new Date();
        horaInicioCaida = new Date();
        horaFinCaida = new Date();
        tiempoCaida = 0;
        
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHoraInicioCaida() {
        return horaInicioCaida;
    }

    public void setHoraInicioCaida(Date horaInicioCaida) {
        this.horaInicioCaida = horaInicioCaida;
    }

    public Date getHoraFinCaida() {
        return horaFinCaida;
    }

    public void setHoraFinCaida(Date horaFinCaida) {
        this.horaFinCaida = horaFinCaida;
    }

    public int getTiempoCaida() {
        return tiempoCaida;
    }

    public void setTiempoCaida(int tiempoCaida) {
        this.tiempoCaida = tiempoCaida;
    }
}
