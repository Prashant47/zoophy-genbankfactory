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

public class PublicationDuplicatesElimination {

	public static void main(String[] args) {
		
		
		String DB_URL = "jdbc:postgresql://localhost/hashmap_per_seq_file";
		
		String USER_NAME = "postgres";
		String PASSWORD = "postgres";
		Connection con = null;
		Statement stmt = null;
		
		int institiuteCount = 0;
		int realCount = 0;
		
		
		List<Publication> sortedPublication = new ArrayList<Publication>();
		
		long startTime = System.nanoTime();
		int i = 0;
		try {
			con = DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);
			
			String query = "select * from \"Publication\" order by \"Title\", \"Journal\"  ";
			
			PreparedStatement selectQuery = con.prepareStatement(query);
						
			ResultSet rs = selectQuery.executeQuery();
			
			while (rs.next()) {
				Publication pub = new Publication();
				pub.setPubId(rs.getInt(1));
				pub.setTitle( rs.getString(4));
				pub.setJournal(rs.getString(5));		
				sortedPublication.add(pub);
				i += 1;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<Integer,List<Integer>> duplicateMap = new HashMap<Integer,List<Integer>>();
		
		
		int j = 0, k = 1; 
		while (j < i && k < i) {
			//System.out.println( institute[j][1]+ "***" +institute[k][1]  );
			
			while (k < i && sortedPublication.get(j).equals(sortedPublication.get(k))) {
				//System.out.println( institute[j][1]+ "***" +institute[k][1]  );
				
				if(!duplicateMap.containsKey(sortedPublication.get(j).getPubId()) ) {
					
					List<Integer> dups = new ArrayList<Integer>();
					dups.add( sortedPublication.get(k).getPubId() );
					duplicateMap.put( sortedPublication.get(j).getPubId() , dups);
				}
				else {
					duplicateMap.get( sortedPublication.get(j).getPubId()).add( sortedPublication.get(k).getPubId()   ); 
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
		String deleteQuery = "delete  from \"Publication\" where \"Pub_ID\" in  ";
		String updateAuthorPubQuery = "update \"Author_Publication\" SET \"Pub_ID\" = ? where \"Pub_ID\" in ";
		String updateSequencePubQuery = "update \"Sequence_Publication\" SET \"Pub_ID\" = ? where \"Pub_ID\" in ";
		
		for (Integer original: duplicateMap.keySet()) {
			
			//debug print
			//System.out.println("Setting key: " + original + "-" + Arrays.toString(duplicateMap.get(original).toArray() ) );
			
			System.out.println(original);
			// making of in cluase in query, this cluase contains ids of all duplicates institution
			StringBuilder inClause = new StringBuilder();
			
			inClause.append('(');
			
			for(int t = 0; t < duplicateMap.get(original).size(); t++) {
				inClause.append(duplicateMap.get(original).get(t));
				if (t != duplicateMap.get(original).size()-1)
					inClause.append(',');	
			}
			inClause.append(')');
			
			String completeAuthorPubQuery = updateAuthorPubQuery + inClause.toString();
			String completeSeqPubQuery = updateSequencePubQuery + inClause.toString();
			try {
				// update Author_institution
				PreparedStatement updateQueryStmt = con.prepareStatement(completeAuthorPubQuery);
				updateQueryStmt.setInt(1, original);	
				updateQueryStmt.executeUpdate();
				
				// update Sequence_Publication
				PreparedStatement updateSeqPubStmt = con.prepareStatement( completeSeqPubQuery );
				updateSeqPubStmt.setInt(1, original);
				updateSeqPubStmt.executeUpdate();
				
				
				// delete from Institution
				PreparedStatement deleteQueryStmt = con.prepareStatement(deleteQuery+inClause.toString());
				deleteQueryStmt.executeUpdate();
				
			
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		
		
		}
		long endTime   = System.nanoTime();
		System.out.println( endTime - startTime);
	}

}
