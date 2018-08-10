package zodo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PostProcessing {

	public static void main(String[] args) {
		
	
	    
	/*	String text = " Contact:Toshiki Nishimura Nihon University School of Medicine, Department of Neurology; 30-1, Oyaguchikamimachi, Itabashi-ku, Tokyo 173, Japan";
		String text2 = " Takanobu Kato, Nagoya City University Medical School, Second Department of Medicine; Kawasumi, Mizuho, Nagoya, Aichi 467-8601, Japan (E-mail:takanobu@med.nagoya-cu.ac.jp, Tel:81-52-851-5511, Fax:81-52-852-0849)";
		
		String text3 = " Contact:Tsuguto Fujimoto Hyogo Prefectural Institute of Public Health and Environmental Sciences, Infectious Disease Research Division; 2-1-29, Arata-Cho, Hyogo-Ku, Kobe, Hyogo 652-0032, Japan URL :http://www.iphes.pref.hyogo.jp/";
		
		int urlIndex = text3.indexOf("URL");
		if (urlIndex != -1) 
			text3 = text3.substring(0, urlIndex);
		
		text3 = text3.replaceAll("\\([^()]*\\)", "").replaceAll("\\d|\\-", "");
		System.out.println( text3);
		String [] splitText = text3.split(",");
		int index = splitText[splitText.length-1].indexOf('(');
		if (index != -1)
			System.out.println(splitText[splitText.length -1].substring(index));
		else {
			System.out.println( splitText[splitText.length-1]);
		}
		*/
		String luceneIndex = "/home/prashant/workspace/geoservices/geonames-service/index/geonames";
		String luceneCustom = "/home/prashant/workspace/geoservices/geonames-service/config/custom_mappings.tsv";
		
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		String DB_URL = "jdbc:postgresql://zodo.asu.edu/zodo_new_design_tables";
		
		String USER_NAME = "";
		String PASSWORD = "";
		Connection con = null;
		Statement stmt = null;
		HashMap<Integer,String> inputTable = new HashMap<Integer,String>();
		List<Institute> outputTable = new ArrayList<Institute>();
		
		try {
			con = DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);
			
			String query = "select * from \"Institution\" ";
			
			PreparedStatement selectQuery = con.prepareStatement(query);
			
			
			ResultSet rs = selectQuery.executeQuery();
			
			while (rs.next()) {
				inputTable.put(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String []splitText = null;
		try {
			
			LuceneSearcher luceneIndexer  = new LuceneSearcher(luceneIndex,luceneCustom);
			
			
			for (Integer key: inputTable.keySet()){
				
				if ( inputTable.get(key) != null ) {
					Institute instituteObj = new Institute();
					instituteObj.setInstitutionID(key);
					String institute = inputTable.get(key);
					
					int urlIndex = institute.indexOf("URL");
					if (urlIndex != -1) 
						institute = institute.substring(0, urlIndex);
				
					institute = institute.replaceAll("\\([^()]*\\)", "").replaceAll("\\d|\\-", "");
					splitText = institute.split(",");
					//int index = splitText[splitText.length-1].indexOf('(');
					
					/*if (index != -1) {
						
						instituteObj.setCountry(splitText[splitText.length -1].substring(index));	
					}
					else {
					}*/
					
					instituteObj.setCountry(splitText[splitText.length-1]);
					
					for (int locationIndex = 3; locationIndex > 0; locationIndex-- ) {
						if ( splitText.length < 3 )
							break;
						String location = Arrays.toString(Arrays.copyOfRange(splitText, splitText.length-locationIndex, splitText.length));
						
						Result result = luceneIndexer.searchLocation(location);
					
						if (result.getRecords().size() !=0 ) {
							instituteObj.setCity(splitText[ splitText.length-locationIndex ] );
							instituteObj.setLatitude(Double.parseDouble(result.getRecords().get(0).get("Latitude")) );
							instituteObj.setLongitude(Double.parseDouble(result.getRecords().get(0).get("Longitude")) );
							instituteObj.setGeonameID(Integer.parseInt(result.getRecords().get(0).get("GeonameId")));
							break; 
						}
					}
					if (instituteObj.getLatitude() == null )
						instituteObj.setLatitude(0.0);
					if (instituteObj.getLongitude() == null)
						instituteObj.setLongitude(0.0);
					
					outputTable.add(instituteObj);
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(Arrays.toString(splitText));
		}
		
		System.out.println("done extracting city latitute and longitude");
		
		String UPDATE_INSTITUTION = "UPDATE \"Institution\" SET \"Country\" = ?, \"City\" = ? , \"Latitude\" = ?, \"Longitude\" = ?, \"Geoname_ID\" = ? Where \"Institution_ID\" = ?";
		int count = 0;
		try {
			PreparedStatement preparedStatement = con.prepareStatement(UPDATE_INSTITUTION);
			for (Institute temp :outputTable ) {
				
				preparedStatement.setInt(6, temp.getInstitutionID());
				preparedStatement.setString(1, temp.getCountry());
				preparedStatement.setString(2, temp.getCity());
				preparedStatement.setDouble(3, temp.getLatitude());
				preparedStatement.setDouble(4, temp.getLongitude());
				preparedStatement.addBatch();
				
				count = count + 1;
				if( count == 6000 ) {
					count = 0;
					int[] updateInsitution = preparedStatement.executeBatch();
					preparedStatement = con.prepareStatement(UPDATE_INSTITUTION);
					System.out.println("Updated 6000 records: " );
					
				}
				
			}
			
			int[] updateInsitution = preparedStatement.executeBatch();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		
		
	}

}
