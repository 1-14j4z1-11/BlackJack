package model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 手札のクラス
 */
public class Hand
{
	private final List<Card> cards = new ArrayList<>();
	protected final Deck deck;
	
	/**
	 * コンストラクタ
	 * @param deck 山札
	 */
	public Hand(Deck deck)
	{
		Objects.requireNonNull(deck);
		this.deck = deck;
	}
	
	/**
	 * 現在保持しているカード枚数を取得します。
	 */
	public int getCount()
	{
		return this.cards.size();
	}
	
	/**
	 * 現在保持しているカード一覧を取得します。<br>
	 * 取得したListは構造を変更できません。
	 */
	public List<Card> getCards()
	{
		return Collections.unmodifiableList(this.cards);
	}
	
	/**
	 * カードを手札に加えます。
	 * @param card 加えるカード
	 */
	public void add(Card card)
	{
		Objects.requireNonNull(card);
		this.cards.add(card);
	}
	
	/**
	 * 指定したカードを除外します。 
	 * @param index 除外するカードのIndex
	 * @return 除外したカード
	 */
	public Card removeAt(int index)
	{
		return this.cards.remove(index);
	}
	
	/**
	 * 山札からカードを加えます。<br>
	 * 指定枚数分のカードが山札にない場合は処理を行わず終了します。
	 * @param num 加えるカード枚数。
	 * @return カードを加えることができたかどうか
	 */
	public boolean drawFromDeck(int num)
	{
		if(this.deck.getCount() < num)
			return false;
		
		for(int i = 0; i < num; i++)
		{
			this.add(this.deck.draw());
		}
		
		return true;
	}
}
