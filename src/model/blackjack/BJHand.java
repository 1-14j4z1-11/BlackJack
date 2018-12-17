package model.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import model.cards.Card;
import model.cards.Deck;
import model.cards.Hand;

/**
 * BlackJack用の手札クラス
 */
public class BJHand extends Hand
{
	private static final Consumer<BJHand> EMPTY_SPLIT_LISTENER = x -> { };
	private static final int VALID_MAX_SCORE = 21;
	private static final int DEFAULT_HANDS = 2;
	private static final int SPLIT_NUM = DEFAULT_HANDS;
	private final Consumer<BJHand> splitListener;
	private boolean active = true;
	
	/**
	 * コンストラクタ
	 * @param deck 山札
	 * @param splitListener Split発生時に呼び出されるListener
	 */
	// package
	BJHand(Deck deck, Consumer<BJHand> splitListener)
	{
		super(deck);
		this.splitListener = (splitListener != null) ? splitListener : EMPTY_SPLIT_LISTENER;
	}
	
	/**
	 * 手札が操作可能かどうかを判定します。<br>
	 * 返り値がfalseの場合、手札固定されており、変更操作を行えません。
	 */
	public boolean isActive()
	{
		return active;
	}
	
	/**
	 * 手札が有効かどうかを判定します。<br>
	 * Bustしている場合は無効と判断されます。
	 */
	public boolean isValid()
	{
		return (this.getScore() <= VALID_MAX_SCORE);
	}
	
	/**
	 * 現在のスコアを取得します。
	 */
	public int getScore()
	{
		int score = 0;
		int aceNum = 0;
		
		// 11以上を10としてスコアを計算、Aceの数も数える
		for(Card c : this.getCards())
		{
			score += getValue(c);
			aceNum += (c.getNumber() == 1) ? 1 : 0;
		}
		
		// スコアが21を超えない範囲でAceを11に置き換える
		for(int i = 0; i < aceNum; i++)
		{
			if(score + 10 > VALID_MAX_SCORE)
				break;
			
			// 1を加算済みなので、10を加算して11扱いにする
			score+= 10;
		}
		
		return score;
	}
	
	/**
	 * 引数の手札との勝敗結果を取得します。
	 * @param other 対戦相手の手札
	 * @return 勝敗結果
	 * @exception 引数がnullの場合
	 */
	public BJResult getResultWith(BJHand other)
	{
		Objects.requireNonNull(other);
		
		if(!this.isValid() && !other.isValid())
			return BJResult.DRAW;

		if(this.isValid() && !other.isValid())
			return BJResult.WIN;
		
		if(!this.isValid() && other.isValid())
			return BJResult.LOSE;
		
		if(this.getScore() > other.getScore())
			return BJResult.WIN;
		
		if(this.getScore() < other.getScore())
			return BJResult.LOSE;
		
		if((this.getScore() < VALID_MAX_SCORE) || (other.getScore() < VALID_MAX_SCORE))
			return BJResult.DRAW;
		
		if((this.getCount() == DEFAULT_HANDS) && (other.getCount() != DEFAULT_HANDS))
			return BJResult.WIN;

		if((this.getCount() != DEFAULT_HANDS) && (other.getCount() == DEFAULT_HANDS))
			return BJResult.LOSE;
		
		return BJResult.DRAW;
	}
	
	/**
	 * この手札がSplit可能かどうかを判定します。
	 */
	public boolean canSplit()
	{
		if(!this.active || (this.getCount() != SPLIT_NUM))
			return false;
		
		int firstValue = getValue(this.getCards().get(0));
		return this.getCards().stream().map(c -> getValue(c)).skip(1)
				.allMatch(v -> v == firstValue);
	}
	
	/**
	 * 手札をSplitします。<br>
	 * この{@see BJHand}を保持する{@see BJPlayer}にSplitした手札が追加されます。
	 * @exception Split可能でない場合に呼び出された場合
	 */
	public void split()
	{
		if(!this.canSplit())
			throw new UnsupportedOperationException();
		
		List<BJHand> splittedHands = new ArrayList<>(SPLIT_NUM);
		splittedHands.add(this);
		
		// 手札を分割する
		for(int i = 1; i < SPLIT_NUM; i++)
		{
			BJHand hand = new BJHand(this.deck, this.splitListener);
			hand.add(this.removeAt(1));
			this.splitListener.accept(hand);
			splittedHands.add(hand);
		}
		
		// 全ての分割した手札にカードを加える
		for(BJHand hand : splittedHands)
		{
			hand.drawFromDeck(1);
		}
	}
	
	/**
	 * ヒットします。<br>
	 * 山札からカードを引き手札に加えます。Bustした場合は、手札が固定され以降カードを引くことはできません。
	 * @return 手札がBJとして有効かどうか(Bustしていないかどうか)
	 * @exception 操作不可能な状態で呼び出された場合
	 */
	public void hit()
	{
		if(!this.active)
			throw new UnsupportedOperationException();
		
		this.drawFromDeck(1);
	}
	
	/**
	 * スタンドします。
	 * 手札が固定され、以降カードを引くことはできません。
	 */
	public void stand()
	{
		this.active = false;
	}
	
	@Override
	public boolean drawFromDeck(int num)
	{
		if(!this.active || !super.drawFromDeck(num))
			return false;
		
		int score = this.getScore();
		
		if(score > VALID_MAX_SCORE)
		{
			this.active = false;
		}
		else if(score == VALID_MAX_SCORE)
		{
			this.active = false;
		}
		
		return true;
	}
	
	/**
	 * カードからスコア値を取得します。<br>
	 *  - 11以上 : 10
	 *  - 1~10 : 数字と同じ値
	 *  - (それ以外 : 0)
	 * @param card カード
	 * @return スコア値
	 */
	private static int getValue(Card card)
	{
		return Math.max(Math.min(10, card.getNumber()), 0);
	}
}
