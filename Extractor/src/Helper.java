import java.util.ArrayList;

public class Helper {
	public static boolean contains(final ArrayList<SoSSecObject> list, final String name){
	    return list.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent();
	}
}
