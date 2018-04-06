package hanbat.isl.baeminsu.mobileproject.Set;

/**
 * Created by baeminsu on 2017. 12. 7..
 */

public class Network {

    private final static String ADDRESS = "http://52.79.108.145/project/";
    private final static String IMAGE_PATH = "http://52.79.108.145/project/image";

    public static String getAddress (String name){
        return ADDRESS+name;
    }

    public static String getAddress2 (String name){

        int index = name.lastIndexOf("/");
        name = name.substring(index);
        return IMAGE_PATH+name;
    }

}
