
import java.io.Serializable;
import java.sql.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zerling
 */
public class LogCaida implements Serializable
{
    private Date fechaI;
    private Date fechaD;
    
    public LogCaida()
    {
        
    }

    public Date getFechaI()
    {
        return fechaI;
    }

    public void setFechaI(Date fechaI)
    {
        this.fechaI = fechaI;
    }

    public Date getFechaD()
    {
        return fechaD;
    }

    public void setFechaD(Date fechaD)
    {
        this.fechaD = fechaD;
    }
    
    
    public long tiempoCaidoMin()
    {
        long tiempo = this.fechaI.getTime() - this.fechaD.getTime();
        return tiempo/(60000);
    }
    
    public long tiempoCaidoSeg()
    {
        long tiempo = this.fechaI.getTime() - this.fechaD.getTime();
        return tiempo/(1000);
    }
    
    
}
