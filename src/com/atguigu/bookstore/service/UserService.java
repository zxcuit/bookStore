package com.atguigu.bookstore.service;

import java.util.Iterator;
import java.util.Set;

import com.atguigu.bookstore.dao.BookDAO;
import com.atguigu.bookstore.dao.TradeDAO;
import com.atguigu.bookstore.dao.TradeItemDAO;
import com.atguigu.bookstore.dao.UserDAO;
import com.atguigu.bookstore.dao.impl.BookDAOImpl;
import com.atguigu.bookstore.dao.impl.TradeDAOImpl;
import com.atguigu.bookstore.dao.impl.TradeItemDAOImpl;
import com.atguigu.bookstore.dao.impl.UserDAOImpl;
import com.atguigu.bookstore.domain.Trade;
import com.atguigu.bookstore.domain.TradeItem;
import com.atguigu.bookstore.domain.User;

public class UserService {

	private UserDAO userDAO = new UserDAOImpl();
	
	public User getUserByUserName(String username){
		return userDAO.getUser(username);
	}
	
	private TradeDAO tradeDAO = new TradeDAOImpl();
	private TradeItemDAO tradeItemDAO = new TradeItemDAOImpl();
	private BookDAO bookDAO = new BookDAOImpl();
	
	public User getUserWithTrades(String username){
	
//		���� UserDAO �ķ�����ȡ User ����
		User user = userDAO.getUser(username);
		if(user == null){
			return null;
		}
		
//		���� TradeDAO �ķ�����ȡ Trade �ļ��ϣ�����װ��Ϊ User ������
		int userId = user.getUserId();
		
//		���� TradeItemDAO �ķ�����ȡÿһ�� Trade �е� TradeItem �ļ��ϣ�������װ��Ϊ Trade ������
		Set<Trade> trades = tradeDAO.getTradesWithUserId(userId);
		
		if(trades != null){
			Iterator<Trade> tradeIt = trades.iterator();
			
			while(tradeIt.hasNext()){
				Trade trade = tradeIt.next();
				
				int tradeId = trade.getTradeId();
				Set<TradeItem> items = tradeItemDAO.getTradeItemsWithTradeId(tradeId);
				
				if(items != null){
					for(TradeItem item: items){
						item.setBook(bookDAO.getBook(item.getBookId())); 
					}
					
					if(items != null && items.size() != 0){
						trade.setItems(items);						
					}
				}
				
				if(items == null || items.size() == 0){
					tradeIt.remove();	
				}
				
			}
		}
		
		if(trades != null && trades.size() != 0){
			user.setTrades(trades);			
		}
		
		return user;
	}
	
}
