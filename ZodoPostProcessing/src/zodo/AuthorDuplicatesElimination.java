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

public class AuthorDuplicatesElimination {

	public static void main(String[] args) {
		
		
		String DB_URL = "jdbc:postgresql://localhost/hashmap_per_seq_file";
		
		String USER_NAME = "postgres";
		String PASSWORD = "postgres";
		Connection con = null;
		Statement stmt = null;
		
		int institiuteCount = 0;
		int realCount = 0;
		
		
		List<Author> sortedAuthor = new ArrayList<Author>();
		
		long startTime = System.nanoTime();
		int i = 0;
		try {
			con = DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);
			
			String query = "select * from \"Author\" order by \"Last_Name\", \"Initials\"  ";
			
			PreparedStatement selectQuery = con.prepareStatement(query);
						
			ResultSet rs = selectQuery.executeQuery();
			
			while (rs.next()) {
				Author auth = new Author();
				auth.setAuthorID(rs.getInt(1));
				auth.setFirstName(rs.getString(2));
				auth.setInitial(rs.getString(3));
				auth.setLastName(rs.getString(4));;		
				sortedAuthor.add(auth);
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
			
			while (k < i && sortedAuthor.get(j).equals(sortedAuthor.get(k))) {
				//System.out.println( institute[j][1]+ "***" +institute[k][1]  );
				
				if(!duplicateMap.containsKey(sortedAuthor.get(j).getAuthorID()) ) {
					
					List<Integer> dups = new ArrayList<Integer>();
					dups.add( sortedAuthor.get(k).getAuthorID() );
					duplicateMap.put( sortedAuthor.get(j).getAuthorID() , dups);
				}
				else {
					duplicateMap.get( sortedAuthor.get(j).getAuthorID()).add( sortedAuthor.get(k).getAuthorID()   ); 
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
		String deleteQuery = "delete  from \"Author\" where \"Author_ID\" in  ";
		String updateAuthorPubQuery = "update \"Author_Publication\" SET \"Author_ID\" = ? where \"Author_ID\" in ";
		String updateAuthorInstQuery = "update \"Author_Institution\" SET \"Author_ID\" = ? where \"Author_ID\" in ";
		String updateSubmissionQuery = "update \"Submission\" SET \"Author_ID\" = ? where \"Author_ID\" in ";
		
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
			String completeAuthInstQuery = updateAuthorInstQuery + inClause.toString();
			String completeSubmissionQuery = updateSubmissionQuery + inClause.toString();
			try {
				// update Author_Publication
				PreparedStatement updateAuthorPubQueryStmt = con.prepareStatement(completeAuthorPubQuery);
				updateAuthorPubQueryStmt.setInt(1, original);	
				updateAuthorPubQueryStmt.executeUpdate();
				
				// update Sequence_Institution
				PreparedStatement updateAuthorInstQueryStmt = con.prepareStatement( completeAuthInstQuery );
				updateAuthorInstQueryStmt.setInt(1, original);
				updateAuthorInstQueryStmt.executeUpdate();
				
				
				// update Submission
				PreparedStatement updateSubmissionQueryStmt = con.prepareStatement( completeSubmissionQuery );
				updateSubmissionQueryStmt.setInt(1, original);
				updateSubmissionQueryStmt.executeUpdate();
				
				
				// delete from Author
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
