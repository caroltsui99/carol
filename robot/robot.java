package player;

import java.util.ArrayList;
import java.util.List;

import pokerHand.PokerHand;
import card.*;
import factory.ConcreteTable;
import factory.Table;
/**
 * 
 * @author Leqian ZHENG
 *
 */
public class RobotPlayer extends PlayerHand {

	/**
	 * To indicate whether it's in debug mod;
	 */
	private boolean isDebug=false;
	
	/**
	 * Using depth-first-search to find any suitable pokerhand
	 * @param remCards The remaining cards to be searched.
	 * @param comb The current combination.
	 * @param prev Previous poker hand, to validate <i>comb</i>
	 * @return A list of cards that whether is valid or <i>null</i>
	 */
	private List<Card> getSuitableComb(List<Card> remCards, List<Card> comb, PokerHand prev){
		PokerHand tmp = ConcreteTable.getInstance().createPokerHand(comb);
		if(tmp!=null && tmp.compareTo(prev) > 0) {
			List<Card> ret = new ArrayList<Card>();
			ret.addAll(comb);
			return ret;
		}
		for (Card c:remCards) { 
			comb.add(c); 
		 	List<Card> ret = getSuitableComb(remCards.subList(remCards.indexOf(c)+1, remCards.size()), comb, prev); 
		 	if(ret!=null)
		 		return ret;
		 	comb.remove(c);
	    }
		return null;
	}
	
	
	/**
	 * Randomly delay between 3 seconds to 5 seconds.
	 */
	private void delay() {
		try {
			Thread.sleep((int)(Math.random()*3000)+2000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public PokerHand play(PokerHand prev) {
		List<Card> ret;
		Table table = ConcreteTable.getInstance();
		if(!isDebug) {
			table.startTimer();
			try {
				Thread.sleep((int)(Math.random()*3000)+2000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(prev==null) {
			int i=0;
			for(;i<cards.size();++i)
				if(table.createPokerHand(cards.subList(0, i+1))==null)break;
			ret = new ArrayList<Card>(cards.subList(0, i));
		}
		else ret = getSuitableComb(cards, 
				new ArrayList<Card>(), prev);
		if(ret==null) // pass
			return null;
		for(Card c : ret)
			cards.remove(c);
		return Math.random()<0.8 || prev==null?table.createPokerHand(ret):null;
	}

	@Override
	public boolean call() {
		if(!isDebug) {
			ConcreteTable.getInstance().startTimer();
			try {
				Thread.sleep((int)(Math.random()*3000)+2000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		return Math.random()<0.5;
	}

	/**
	 * To set the debug mode.
	 * @param b <i>true</i> is in debug mode, <i>false</i> is not.
	 */
	public void setDebug(boolean b) {
		isDebug= b;
	}
}
