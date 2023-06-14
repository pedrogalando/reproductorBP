package basicPlayer;


import java.io.File;
import java.util.Map;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import view.ReproductorFrame;

public class Reproductor implements BasicPlayerListener{
    private double bytesLength;
    private BasicPlayer basicPlayer;
    private String Archivo;
    
    public Reproductor(String ruta) {
        this.Archivo = ruta;
        if (basicPlayer == null) {
            basicPlayer = new BasicPlayer();
            File fil = new File(ruta);
            try {
                bytesLength = fil.length();
                basicPlayer.open(fil);
            } catch (BasicPlayerException ex) {
               
            }
        }
        basicPlayer.addBasicPlayerListener(this);
    }
    
    public void play() {
        try {
            basicPlayer.play();
        } catch (BasicPlayerException e) {
            // TODO Auto-generated catch block  e.printStackTrace();
        }
    }

    public void pause() {
        try {
            basicPlayer.pause();
        } catch (BasicPlayerException e) {
            // TODO Auto-generated catch block  e.printStackTrace();
        }
    }

    public void stop() {
        try {
            basicPlayer.stop();
        } catch (BasicPlayerException e) {
            // TODO Auto-generated catch block  e.printStackTrace();
        }
    }

    public void resume() {
        try {
            basicPlayer.resume();
        } catch (BasicPlayerException e) {
            // TODO Auto-generated catch block  e.printStackTrace();
        }
    }
    
    public void seek(int sek){
        try {
            basicPlayer.seek((long)sek);
        } catch (BasicPlayerException ex) {
            //Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void volumen(int vol){
        double ganancia = vol/100.0;
        try {
            //System.out.println("VOL:"+ganancia);
            basicPlayer.setGain(ganancia);
        } catch (BasicPlayerException ex) {
            //Logger.getLogger(Reproductor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**
     * * Necesario por implementar BasicPlayerListener. Es ejecutado una vez se
     * carga un fichero. En este caso, obtiene el tamaño en bytes del fichero.
     */
    public void opened(Object arg0, Map arg1) {
        //System.out.println("HOLA MARIOLA: " + arg1.toString());
        if (arg1.containsKey("audio.length.bytes")) {
            bytesLength = Double.parseDouble(arg1.get("audio.length.bytes").toString());
        }
    }
    /**
     * * Necesario por implementar BasicPlayerListener. Según la documentación,
     * este método es llamado varias veces por segundo para informar del
     * progreso en la reproducción.
     */
    private long dt = 0;
    private long tv = 0;
    private long tn = 0;

    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
        tn = System.currentTimeMillis();
        dt = tn - tv;
        if (dt > 420 || (bytesLength-bytesread<1000)) {
            float progressUpdate = (float) (bytesread * 1.0f / bytesLength * 1.0f);
            int progressNow = (int) (bytesLength * progressUpdate);
            // Descomentando la siguiente línea se mosrtaría el progreso
            /*System.out.println(" -&CPC; " + properties.toString());
            System.out.println(" -&Progreso; " + progressNow);
            System.out.println(" -&Duracion; " + properties.get("mp3.frame.bitrate"));
            System.out.println(" -&Total; " + (int) bytesLength);
            System.out.println(" -&Microsegundo; " + properties.get("mp3.position.microseconds"));*/
            ReproductorFrame.rellenar(progressNow, (int) bytesLength, new Long(properties.get("mp3.position.microseconds").toString()));
            tv = tn;
        }
    }

    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
        //System.out.println(bpe.toString());
    }

    @Override
    public void setController(BasicController bc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}
