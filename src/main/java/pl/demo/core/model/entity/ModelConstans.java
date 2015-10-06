package pl.demo.core.model.entity;

/**
 * Created by robertsikora on 26.07.15.
 */
public class ModelConstans {

    private ModelConstans(){
        throw new AssertionError("Cannot create object");
    }

    public final static int TEXT_LENGTH_1=1;
    public final static int TEXT_LENGTH_25=25;
    public final static int TEXT_LENGTH_80=80;
    public final static int TEXT_LENGTH_100=100;
    public final static int TEXT_LENGTH_250=250;
    public final static int TEXT_LENGTH_5_000=5_000;
}
