package com.javagyeongmin.MVCboard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.javagyeongmin.MVCboard.dto.BDto;
import com.javagyeongmin.MVCboard.util.Constant;

public class BDao {

	DataSource dataSource; // servlet에서 만들어서 넣어줬기 때문에 필요없다
	JdbcTemplate template; // 이제 이걸 사용함 4/27 jdbc 관련해서 수업

	public BDao() {
		super();
		// TODO Auto-generated constructor stub
		
		this.template = Constant.template; // 내가 만든 Constant 클래스로 가져와서? 초기화한다?
		
		
//		try {
//			Context context = new InitialContext();
//			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle11g");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	public ArrayList<BDto> list() { // 리스트가 반환 -> 글들 여러개라 배열로 반환 -> 글 하나하나 dto -> Bdto
		
		String query = "SELECT * FROM MVC_board order by bGroup desc, bStep asc"; // desc : 내림차순 / asc : 오름차순 / order : 정렬
		return (ArrayList<BDto>) template.query(query, new BeanPropertyRowMapper(BDto.class)); // Dto로 만든 애들로 빈 프로포티를 하겠다 + Array로 만들어서 형변환 해준다 
		
		
		
//		ArrayList<BDto> dtos = new ArrayList<BDto>();
//		
//		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null; // ResultSet은 셀럭트 하려고 설정하는거임 나머지 두개로는 업데이트 가능 
//		// 셀렉트는 결과를 반환해준다 (sql문 기본 4개 : SELECT, INSERT, UPDATE, DELETE )
//		
//		try {
//			conn = dataSource.getConnection();
//			String query = "SELECT * FROM MVC_board order by bGroup desc, bStep asc"; // desc : 내림차순 / asc : 오름차순 / order : 정렬
//			pstmt = conn.prepareStatement(query); // sql문 실행
//			rs = pstmt.executeQuery();
//			
//			while(rs.next()) { // next() : 다음 데이터 있으면 참 / 없으면 거짓 
//				int bId = rs.getInt("bId");
//				String bName = rs.getString("bName");
//				String bTitle = rs.getString("bTitle");
//				String bContent = rs.getString("bContent");
//				Timestamp bDate = rs.getTimestamp("bDate");
//				int bHit = rs.getInt("bHit");
//				int bGroup = rs.getInt("bGroup");
//				int bStep = rs.getInt("bStep");
//				int bIndent = rs.getInt("bIndent");
//				
//				BDto dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
//				dtos.add(dto);
//			}
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				if(rs != null) {
//					rs.close();
//				}
//				if(pstmt != null) {
//					pstmt.close();
//				}	
//				if(conn != null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
//		
//		
//		return dtos;
//		
	}
	
	
	public void write(final String bName, final String bTitle, final String bContent) {
		

		this.template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub

				String query = "INSERT INTO mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0)";
				PreparedStatement pstmt = con.prepareStatement(query);
				
				pstmt.setString(1, bName);
				pstmt.setString(2, bTitle);
				pstmt.setString(3, bContent);
				
				return pstmt;
			}
		});
		
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		
//		String query = "INSERT INTO mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0)";
//		
//		try {
//			conn = dataSource.getConnection(); // 연결 만들고
//			pstmt = conn.prepareStatement(query); // sql문 실행시키고
//			
//			pstmt.setString(1, bName);
//			pstmt.setString(2, bTitle);
//			pstmt.setString(3, bContent);
//			
//			int dbFlag = pstmt.executeUpdate(); // 성공이면 1 반환
//			System.out.println(dbFlag);
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally { // try/catch문 한번 더 들어가야됨
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
		
	}
	
	public BDto contentView(String strId) {
		
		upHit(strId); // 조회수 증가
		
		String query = "SELECT * FROM MVC_board where bId=" + strId;
		
		return template.queryForObject(query, new BeanPropertyRowMapper(BDto.class));
		
//		BDto dto = null;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			conn = dataSource.getConnection();
//			String query = "SELECT * FROM MVC_board where bId=?";
//			pstmt = conn.prepareStatement(query);
//			pstmt.setInt(1, Integer.parseInt(strId)); // 문자열인 strId를 int로 변환
//			rs = pstmt.executeQuery();
//			
//			while(rs.next()) { // next() : 다음 데이터 있으면 참 / 없으면 거짓 
//				int bId = rs.getInt("bId");
//				String bName = rs.getString("bName");
//				String bTitle = rs.getString("bTitle");
//				String bContent = rs.getString("bContent");
//				Timestamp bDate = rs.getTimestamp("bDate");
//				int bHit = rs.getInt("bHit");
//				int bGroup = rs.getInt("bGroup");
//				int bStep = rs.getInt("bStep");
//				int bIndent = rs.getInt("bIndent");
//				
//				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
//				
//			}
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				if(rs != null) {
//					rs.close();
//				}
//				if(pstmt != null) {
//					pstmt.close();
//				}	
//				if(conn != null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
//		
//		return dto;
		
	}
	
	// 수정
	public void modify(final String bId, final String bName, final String bTitle, final String bContent) {
		
		String query = "UPDATE mvc_board SET bName=?, bTitle=?, bContent=? where bId=?";
		
		this.template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
				
				pstmt.setString(1, bName);
				pstmt.setString(2, bTitle);
				pstmt.setString(3, bContent);
				pstmt.setInt(4, Integer.parseInt(bId));
				
			}
		});
//		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		
//		
//		String query = "UPDATE mvc_board SET bName=?, bTitle=?, bContent=? where bId=?";
//		
//		try {
//			conn = dataSource.getConnection(); // 연결 만들고
//			pstmt = conn.prepareStatement(query); // sql문 실행시키고
//			
//			pstmt.setString(1, bName);
//			pstmt.setString(2, bTitle);
//			pstmt.setString(3, bContent);
//			pstmt.setInt(4, Integer.parseInt(bId));
//			
//			int dbFlag = pstmt.executeUpdate(); // 성공이면 1 반환
//			System.out.println(dbFlag);
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally { // try/catch문 한번 더 들어가야됨
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
	}
	
	// 삭제
	public void delete(final String bId) {
		
		String query = "delete from mvc_board where bId=?";
	
		this.template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
				
				pstmt.setInt(1, Integer.parseInt(bId));
				
			}
		});
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		
//		
//		String query = "delete from mvc_board where bId=?";
//		
//		try {
//			conn = dataSource.getConnection(); // 연결 만들고
//			pstmt = conn.prepareStatement(query); // sql문 실행시키고
//			
//			pstmt.setInt(1, Integer.parseInt(bId));
//			
//			int dbFlag = pstmt.executeUpdate(); // 성공이면 1 반환
//			System.out.println(dbFlag);
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally { // try/catch문 한번 더 들어가야됨
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
	}
	
	public BDto reply_view(String strId) {
		
		String query = "SELECT * FROM MVC_board where bId=?";
		
		return template.queryForObject(query, new BeanPropertyRowMapper(BDto.class));
		
//		BDto dto = null;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			conn = dataSource.getConnection();
//			String query = "SELECT * FROM MVC_board where bId=?";
//			pstmt = conn.prepareStatement(query);
//			pstmt.setInt(1, Integer.parseInt(strId)); // 문자열인 strId를 int로 변환
//			rs = pstmt.executeQuery();
//			
//			while(rs.next()) { // next() : 다음 데이터 있으면 참 / 없으면 거짓 
//				int bId = rs.getInt("bId");
//				String bName = rs.getString("bName");
//				String bTitle = rs.getString("bTitle");
//				String bContent = rs.getString("bContent");
//				Timestamp bDate = rs.getTimestamp("bDate");
//				int bHit = rs.getInt("bHit");
//				int bGroup = rs.getInt("bGroup");
//				int bStep = rs.getInt("bStep");
//				int bIndent = rs.getInt("bIndent");
//				
//				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
//				
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				if(rs != null) {
//					rs.close();
//				}
//				if(pstmt != null) {
//					pstmt.close();
//				}	
//				if(conn != null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
//		
//		return dto;
		}
	
	public void reply(final String bId, final String bName, final String bTitle, final String bContent, final String bGroup, final String bStep, final String bIndent) {
		
		replyShape(bGroup, bStep); // step 값 셋팅 함수
		
		String query = "INSERT INTO mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, ?, ?, ?)";
		
		this.template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
			
				pstmt.setString(1, bName);
				pstmt.setString(2, bTitle);
				pstmt.setString(3, bContent);
				pstmt.setInt(4, Integer.parseInt(bGroup));
				pstmt.setInt(5, Integer.parseInt(bStep)+1);
				pstmt.setInt(6, Integer.parseInt(bIndent)+1);
				
			}
		});
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		
//		String query = "INSERT INTO mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, ?, ?, ?)";
//		
//		try {
//			conn = dataSource.getConnection(); // 연결 만들고
//			pstmt = conn.prepareStatement(query); // sql문 실행시키고
//			
//			pstmt.setString(1, bName);
//			pstmt.setString(2, bTitle);
//			pstmt.setString(3, bContent);
//			pstmt.setInt(4, Integer.parseInt(bGroup));
//			pstmt.setInt(5, Integer.parseInt(bStep)+1);
//			pstmt.setInt(6, Integer.parseInt(bIndent)+1);
//			
//			
//			int dbFlag = pstmt.executeUpdate(); // 성공이면 1 반환
//			System.out.println(dbFlag);
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally { // try/catch문 한번 더 들어가야됨
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
	}
	
	public void replyShape(final String strGroup, final String strStep) {
		
		String query = "UPDATE mvc_board SET bStep = bStep + 1 WHERE bGroup = ? and bStep > ?";
		
		this.template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
			
				pstmt.setInt(1, Integer.parseInt(strGroup));
				pstmt.setInt(2, Integer.parseInt(strStep));
				
			}
		});
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		
//		String query = "UPDATE mvc_board SET bStep = bStep + 1 WHERE bGroup = ? and bStep > ?";
//		
//		try {
//			conn = dataSource.getConnection(); // 연결 만들고
//			pstmt = conn.prepareStatement(query); // sql문 실행시키고
//			
//			pstmt.setInt(1, Integer.parseInt(strGroup));
//			pstmt.setInt(2, Integer.parseInt(strStep));
//			
//			
//			int dbFlag = pstmt.executeUpdate(); // 성공이면 1 반환
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally { // try/catch문 한번 더 들어가야됨
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
	}
	
	public void upHit(final String strId) {
		
		String query = "UPDATE mvc_board SET bHit = bHit + 1 WHERE bId = ?";
		
		this.template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				// TODO Auto-generated method stub
				
				pstmt.setInt(1, Integer.parseInt(strId));
				
			}
		});
		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		
//		String query = "UPDATE mvc_board SET bHit = bHit + 1 WHERE bId = ?";
//		
//		try {
//			conn = dataSource.getConnection(); // 연결 만들고
//			pstmt = conn.prepareStatement(query); // sql문 실행시키고
//			
//			pstmt.setInt(1, Integer.parseInt(strId));
//			
//			
//			int dbFlag = pstmt.executeUpdate(); // 성공이면 1 반환
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally { // try/catch문 한번 더 들어가야됨
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(conn != null) {
//					conn.close();
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
//		
//	}
	
	}
}



