package pl.demo;

/**
 * Created by robertsikora on 06.09.15.
 */
public class MsgConst {

    private MsgConst(){
        throw new AssertionError("Cannot crate instance of object!");
    }

    public static final String FILE_EMPTY = "FILE.IS_EMPTY";
    public static final String FILE_TOO_BIG = "FILE.IS_TOO_BIG";
}
