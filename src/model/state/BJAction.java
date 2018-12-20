package model.state;

import model.blackjack.BJHand;

public enum BJAction
{
	/** ヒット(カードを1枚引く) */
	HIT
	{
		@Override
		public void doActionTo(BJHand hand)
		{
			hand.hit();
		}
	},
	
	/** スタンド(手札を固定する) */
	STAND
	{
		@Override
		public void doActionTo(BJHand hand)
		{
			hand.stand();
		}
	},
	
	/** スプリット(手札を分ける、条件あり) */
	SPLIT
	{
		@Override
		public void doActionTo(BJHand hand)
		{
			hand.split();
		}
	};
	
	public abstract void doActionTo(BJHand hand);
}
