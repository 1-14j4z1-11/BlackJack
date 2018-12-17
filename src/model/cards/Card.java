package model.cards;

import java.util.Objects;

/**
 * カードクラス
 */
public class Card
{
	private final Suit suit;
	private final int number;
	
	/**
	 * コンストラクタ
	 * @param suit マーク
	 * @param number 数字
	 */
	private Card(Suit suit, int number)
	{
		Objects.requireNonNull(suit);
		
		this.suit = suit;
		this.number = number;
	}
	
	/**
	 * マークを取得します。
	 */
	public Suit getSuit()
	{
		return suit;
	}
	
	/**
	 * 数字を取得します。
	 */
	public int getNumber()
	{
		return number;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (number != other.number)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}
	
	/**
	 * カードを生成します。
	 * @param suit マーク
	 * @param number 数字
	 * @return カード
	 */
	public static Card of(Suit suit, int number)
	{
		return new Card(suit, number);
	}
	
	/**
	 * ジョーカーのカードを生成します。
	 * @return ジョーカーカード
	 */
	public static Card ofJoker()
	{
		return new Card(Suit.JOKER, 0);
	}
}
