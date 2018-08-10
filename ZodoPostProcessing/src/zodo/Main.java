package zodo;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Usage: jar_name config_file path" );
			System.out.println("e.g my_jar.jar /home/john/config.properties");
			System.exit(0);
		}
	
		
		String configFileName = args[0];
			
		
		
	/*	LuceneSearcher luceneIndexer = null;
		
		try {
			luceneIndexer = new LuceneSearcher("/home/prashant/workspace/geoservices/geonames-service/index/geonames","/home/prashant/workspace/geoservices/geonames-service/config/custom_mappings.tsv");
			
			Result result = luceneIndexer.searchLocation("Bhopal,Madhya Pradesh,India");
			System.out.println(result.getRecords().get(0).get("Latitude")+ result.getRecords().get(0).get("Longitude")  );
		} catch (LuceneSearcherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidLuceneQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		Properties properties = new Properties();
		InputStream input = null;
		try {
			
			input = new FileInputStream(configFileName);

			
			properties.load(input);
			
			System.out.println(properties.getProperty("DB.Host"));
			
		}catch (IOException e) {
			e.printStackTrace();
			
		}finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		

	}

}
