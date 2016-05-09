package main;

import java.util.*;

import points.MetaPoint;
import shapes.*;
import variators.*;

public class IOManager {
	private static final String ERROR = "?";
	public static final String SEPARATOR = ";";
	public static final String SUBSEPARATOR = ",";
	public static final String DATASEPARATOR = ":";
	public static final String NAMESEPARATOR = "=";
	
	public static final TreeMap<String, MetaObject> CIDMAP = new TreeMap<String, MetaObject>(new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			int c = arg0.substring(0, 1).compareTo(arg1.substring(0, 1));
			if (c != 0) {return c;}
			int n0 = Integer.parseInt(arg0.substring(1));
			int n1 = Integer.parseInt(arg1.substring(1));
			return (int)Math.signum(n0 - n1);
		}
	});
	public static final HashMap<String, MetaObject.IOFactory<? extends MetaObject>> CHARMAP = createCharMap();
	
	private static void saveFile(List<String> out, Class<? extends MetaObject> clasz) {
		int i = 0;   String str;
		for (Object o : Statics.MP.getTheSpace().getAllMetas(clasz)) {
			str = stringifyMeta((MetaObject) o, ++i);
			out.add(str);
		}
	}
	
	public static void saveFile(String fileName) {
		List<String> out = new ArrayList<String>();
		saveFile(out, MetaPoint.class);
		saveFile(out, Variator.class);
		saveFile(out, MetaShape.class);
		saveFile(out, MetaStructure.class);
		saveFile(out, Gene.class);
		Calc.writeToFile(out, fileName);
	}

	private static String stringifyMeta(MetaObject o, int i) {
		return o.cid() + "=" + o.getName() + DATASEPARATOR + o.saveString();
	}
	public static String getMetaCharId(MetaObject meta) {
		return getMetaCharId(meta.getClass());
	}
	
	private static HashMap<String, MetaObject.IOFactory<? extends MetaObject>> createCharMap() {
		HashMap<String, MetaObject.IOFactory<? extends MetaObject>> map = new HashMap<String, MetaObject.IOFactory<? extends MetaObject>>();
		map.put(getMetaCharId(MetaPoint.class), MetaPoint.getIOFactory());
		map.put(getMetaCharId(Variator.class), Variator.getIOFactory());
		map.put(getMetaCharId(MetaBridge.class), MetaBridge.getIOFactory());
		map.put(getMetaCharId(MetaCurve.class), MetaCurve.getIOFactory());
		map.put(getMetaCharId(MetaEllipse.class), MetaEllipse.getIOFactory());
		map.put(getMetaCharId(MetaCircle.class), MetaCircle.getIOFactory());
		map.put(getMetaCharId(MetaPolygon.class), MetaPolygon.getIOFactory());
		map.put(getMetaCharId(MetaStructure.class), MetaStructure.getIOFactory());
		map.put(getMetaCharId(Gene.class), Gene.getIOFactory());
		return map;
	}
	public static <T extends MetaObject> String getMetaCharId(Class<T> metaClass) {
		if (metaClass.isAssignableFrom(MetaPoint.class)) return "*";
		if (metaClass.isAssignableFrom(SpaceVariator.class)) return "v";
		if (metaClass.isAssignableFrom(MetaBridge.class)) return "u";
		if (metaClass.isAssignableFrom(MetaCurve.class)) return "c";
		if (metaClass.isAssignableFrom(MetaEllipse.class)) return "O";
		if (metaClass.isAssignableFrom(MetaCircle.class)) return "o";
		if (metaClass.isAssignableFrom(MetaPolygon.class)) return "P";
		if (metaClass.isAssignableFrom(MetaStructure.class)) return "S";
		if (metaClass.isAssignableFrom(Gene.class)) return "g";
		return ERROR;
	}
	private static boolean storeMeta(String in) {
		String c = in.substring(0, 1);
		String[] parts = in.split(DATASEPARATOR);
		String[] id = parts[0].split(NAMESEPARATOR);
		MetaObject mo = null;
		try {mo = CHARMAP.get(c).createFromString(id[1], parts[1].split(SEPARATOR));}
		catch(Exception e){
			System.out.println(mo);
		}
		if (mo == null) {return false;}
		CIDMAP.put(id[0], mo);
		return true;
	}
	
	public static void loadFile(String fileName) {
		CIDMAP.clear();
		List<String> objs = Calc.readFromFile(fileName);
		while(!objs.isEmpty()) {
			for (Iterator<String> iter = objs.iterator(); iter.hasNext(); ) {
				String in = iter.next();
				boolean stored = storeMeta(in);
				if (stored) {iter.remove();}
			}
		}
		for (String key : CIDMAP.keySet()) {
			Statics.MP.getTheSpace().addMeta(CIDMAP.get(key));
		}
		Statics.MP.getTheSpace().restore();
		CIDMAP.clear();
	}
	
	
}
