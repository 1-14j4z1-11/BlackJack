package model.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 山札のクラス
 */
public class Deck
{
	private static final int MIN_NUMBER = 1;
	private static final int MAX_NUMBER = 13;
	private final List<Card> cards = new ArrayList<>();
	
	/**
	 * コンストラクタ
	 * @param cards 山札を構成するカード一覧
	 */
	private Deck(Collection<Card> cards)
	{
		Objects.requireNonNull(cards);
		this.cards.addAll(cards);
	}
	
	/**
	 * 山札の枚数を取得します。
	 */
	public int getCount()
	{
		return this.cards.size();
	}
	
	/**
	 * 山札にカードがあるかどうかを判定します。
	 */
	public boolean hasCard()
	{
		return !this.cards.isEmpty();
	}
	
	/**
	 * 山札からカードを引きます。<br>
	 * 引いたカードは山札には含まれない状態になります。
	 * @return 山札から引いたカード
	 * @exception UnsupportedOperationException 山札にカードがない場合
	 */
	public Card draw()
	{
		if(!this.hasCard())
			throw new UnsupportedOperationException();
		
		return this.cards.remove(this.cards.size() - 1);
	}
	
	/**
	 * 山札をシャッフルします。
	 */
	public void shuffle()
	{
		Collections.shuffle(this.cards);
	}
	
	/**
	 * 山札を生成します。
	 * @param jokerNum 山札に含めるジョーカーの枚数
	 * @return 山札
	 */
	public static Deck create(int jokerNum)
	{
		List<Card> cards = new ArrayList<Card>(Suit.values().length * (MAX_NUMBER - MIN_NUMBER + 1));
		
		for(Suit suit : Suit.values())
		{
			if(!suit.hasNumber())
				continue;
			
			for(int n = MIN_NUMBER; n <= MAX_NUMBER; n++)
			{
				cards.add(Card.of(suit, n));
			}
		}
		
		for(int i = 0; i < jokerNum; i++)
		{
			cards.add(Card.ofJoker());
		}
		
		return new Deck(cards);
	}
}
