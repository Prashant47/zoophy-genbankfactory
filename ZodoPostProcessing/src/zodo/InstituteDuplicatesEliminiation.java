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

public class InstituteDuplicatesEliminiation {

	public static void main(String[] args) {
		
		
		String DB_URL = "jdbc:postgresql://localhost/hashmap_per_seq_file";
		
		String USER_NAME = "postgres";
		String PASSWORD = "postgres";
		Connection con = null;
		
		
		int institiuteCount = 0;
		
		try {
			con = DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);
			
			String query = "select count(*) from \"Institution\"   ";
			
			PreparedStatement selectQuery = con.prepareStatement(query);
			
			ResultSet rs = selectQuery.executeQuery();
			
			while (rs.next()) {
				institiuteCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println( "institiuteCount:  " + institiuteCount);
		
		String [][] institute = new String[institiuteCount][2];
		int count = -1;
		try {
			String query = "select * from \"Institution\" order by \"Institution\"  ";
			PreparedStatement selectQuery = con.prepareStatement(query);
			ResultSet rs = selectQuery.executeQuery();
			
			while (rs.next()) {
				if (rs.getString(2) != null){
					institute[++count][0] = String.valueOf(rs.getInt(1));
					institute[count][1] = rs.getString(2);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<Integer,List<Integer>> duplicateMap = new HashMap<Integer,List<Integer>>();
		
		
		int j = 0, k = 1; 
		while (j < count && k < count) {
			
			while (institute[j][1].equals(institute[k][1] )) {
								
				if(!duplicateMap.containsKey(Integer.parseInt( institute[j][0]) )) {
					
					List<Integer> dups = new ArrayList<Integer>();
					dups.add( Integer.parseInt(institute[k][0]) );
					duplicateMap.put( Integer.parseInt(institute[j][0]) , dups);
				}
				else {
					duplicateMap.get( Integer.parseInt(institute[j][0])).add( Integer.parseInt(institute[k][0])   ); 
				}
				k += 1;
			}
			j = k;
			k += 1;
		}
	
		// printiting duplicate map
		/*for (Integer temp: duplicateMap.keySet()) {
			System.out.println( temp +" : " +  duplicateMap.get(temp).toString()   );
		}*/
		System.out.println("size of map :" + duplicateMap.size()   );
		
		String deleteInstituteQuery = "delete  from \"Institution\" where \"Institution_ID\" in  ";
		String updateInstituteQuery = "update \"Author_Institution\" SET \"Institution_ID\" = ? where \"Institution_ID\" in ";
		
		for (Integer original: duplicateMap.keySet()) {
			
			//debug print
			//System.out.println("Setting key: " + original + "-" + Arrays.toString(duplicateMap.get(original).toArray() ) );
			
			// making of in cluase in query, this cluase contains ids of all duplicates institution
			StringBuilder inClause = new StringBuilder();
			
			inClause.append('(');
			
			for(int t = 0; t < duplicateMap.get(original).size(); t++) {
				inClause.append(duplicateMap.get(original).get(t));
				if (t != duplicateMap.get(original).size()-1)
					inClause.append(',');	
			}
			inClause.append(')');
			
			String completeQuery = updateInstituteQuery + inClause.toString();
			try {
				// update Author_institution
				PreparedStatement updateQueryStmt = con.prepareStatement(completeQuery);
				updateQueryStmt.setInt(1, original);	
				updateQueryStmt.executeUpdate();
				
				// delete from Institution
				PreparedStatement deleteQueryStmt = con.prepareStatement(deleteInstituteQuery+inClause.toString());
				deleteQueryStmt.executeUpdate();
				
			
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		
		
		}
	}

}
