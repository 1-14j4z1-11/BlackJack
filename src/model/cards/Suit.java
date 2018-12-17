package model.cards;

/**
 * マークのEnum
 */
public enum Suit
{
	/** スペード */
	SPADE(true, "S"),
	
	/** ハート */
	HEART(true, "H"),
	
	/** ダイア */
	DIAMOND(true, "D"),
	
	/** クラブ */
	CLUB(true, "C"),
	
	/** ジョーカー */
	JOKER(false, "J");
	
	private final boolean numeric;
	private final String symbol;
	
	/**
	 * コンストラクタ
	 * @param numeric 数字を持つかどうか
	 */
	private Suit(boolean numeric, String symbol)
	{
		this.numeric = numeric;
		this.symbol = symbol;
	}
	
	/**
	 * 数字を持つマークかどうかを判定します。
	 */
	public boolean hasNumber()
	{
		return this.numeric;
	}
	
	/**
	 * シンボルの文字列を取得します。
	 */
	public String getSymbol()
	{
		return this.symbol;
	}
}
