package model.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import model.cards.Deck;

/**
 * BlackJackのプレイヤークラス
 */
public class BJPlayer
{
	private static final int DEFAULT_CARDS = 2;
	private final List<BJHand> hands = new ArrayList<>();
	private final Deck deck;
	
	/**
	 * コンストラクタ
	 * @param deck 山札
	 */
	public BJPlayer(Deck deck)
	{
		Objects.requireNonNull(deck);
		this.deck = deck;
	}
	
	/**
	 * 状態を初期化します。
	 */
	public void initialize()
	{
		BJHand hand = new BJHand(this.deck, this::addHand);
		hand.drawFromDeck(DEFAULT_CARDS);
		
		this.hands.clear();
		this.hands.add(hand);
	}
	
	/**
	 * このプレイヤーが操作可能な状態かどうかを判定します。<br>
	 * trueを返した場合、このプレイヤーの保持するいずれかの手札が操作可能です。
	 */
	public boolean isActive()
	{
		for(BJHand hand : this.hands)
		{
			if(hand.isActive())
				return true;
		}
		
		return false;
	}
	
	/**
	 * 保持している手札の一覧を取得します。<br>
	 * 取得したListは構造を変更できません。
	 */
	public List<BJHand> getHands()
	{
		return Collections.unmodifiableList(this.hands);
	}
	
	/**
	 * 手札を追加します
	 * @param hand 追加する手札
	 */
	private void addHand(BJHand hand)
	{
		Objects.requireNonNull(hand);
		this.hands.add(hand);
	}
}
