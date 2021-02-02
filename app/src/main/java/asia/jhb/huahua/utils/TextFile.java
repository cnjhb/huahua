package asia.jhb.huahua.utils;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

public class TextFile implements Iterable<String>{
	public List<String> arr= new ArrayList<String>();
	public Iterator<String> iterator(){
		return arr.iterator();
	}
	public TextFile(File file) throws IOException{
		BufferedReader in= new BufferedReader(
				new FileReader(file));
		String s;
		while((s=in.readLine())!=null)
			arr.add(s);
		in.close();
	}
	public TextFile(String filename) throws IOException{
		this(new File(filename));
	}
	public static String read(File file) throws IOException{
		StringBuilder sb=new StringBuilder();
		for(String str: new TextFile(file))
			sb.append(str).append("\n");
		return sb.toString();
	}
	public static String read(String filename) throws IOException{
		return read(new File(filename));
	}
	public static void main(String[] args) throws IOException{
		for(String str: new TextFile("./src/util/TextFile.java"))
			System.out.println(str);
	}
}
