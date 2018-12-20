package model.state;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import model.blackjack.BJHand;
import model.blackjack.BJPlayer;
import model.blackjack.BlackJack;

public class BJFlow
{
	private final BlackJack bj = new BlackJack(1);
	private final IStateChangedListener listener;
	private IState currentState;
	
	public BJFlow(IStateChangedListener listener)
	{
		this.bj.initialize();
		this.listener = listener;
		this.currentState = PlayerTurnState.ofFirst(this.bj);
	}
	
	public BlackJack getField()
	{
		return this.bj;
	}
	
	public int getCurrentPlayer()
	{
		return this.currentState.getPlayerIndex();
	}
	
	public int getHandIndex()
	{
		return this.currentState.getHandIndex();
	}
	
	public List<BJAction> getActions()
	{
		return this.currentState.getActions();
	}
	
	public void doAction(BJAction action)
	{
		IState nextState = this.currentState.doAction(action);
		
		if(this.currentState != nextState)
		{
			this.currentState = nextState;
			this.listener.onStateChanged();
		}
	}
	
	private static class PlayerTurnState implements IState
	{
		private static final List<BJAction> SPLITTABLE_ACTIONS = Collections.unmodifiableList(Arrays.asList(BJAction.HIT, BJAction.STAND, BJAction.SPLIT));
		private static final List<BJAction> UNSPLITTABLE_ACTIONS = Collections.unmodifiableList(Arrays.asList(BJAction.HIT, BJAction.STAND));
		
		private final BlackJack bj;
		private final int playerIndex;
		private final int handIndex;
		
		private PlayerTurnState(BlackJack bj, int playerIndex, int handIndex)
		{
			Objects.requireNonNull(bj);
			
			this.bj = bj;
			this.playerIndex = playerIndex;
			this.handIndex = handIndex;
		}

		@Override
		public int getPlayerIndex()
		{
			return this.playerIndex;
		}

		@Override
		public int getHandIndex()
		{
			return this.handIndex;
		}
		
		@Override
		public List<BJAction> getActions()
		{
			if(!this.getCurrentHand().isActive())
				return Collections.emptyList();
			else if(this.getCurrentHand().canSplit())
				return SPLITTABLE_ACTIONS;
			else
				return UNSPLITTABLE_ACTIONS;
		}

		@Override
		public IState doAction(BJAction action)
		{
			if(action == null)
				return this;
			
			BJHand hand = this.getCurrentHand();
			action.doActionTo(hand);
			
			return hand.isActive() ? this : this.getNextState();
		}
		
		private IState getNextState()
		{
			if(this.handIndex + 1 < this.getCurrentPlayer().getHands().size())
			{
				return new PlayerTurnState(this.bj, this.playerIndex, this.handIndex + 1);
			}
			
			if(this.playerIndex + 1 < this.bj.getPlayers().size())
			{
				return new PlayerTurnState(this.bj, this.playerIndex + 1, 0);
			}
			
			return new ParentTurnState(this.bj);
		}
		
		private BJPlayer getCurrentPlayer()
		{
			return this.bj.getPlayers().get(this.playerIndex);
		}
		
		private BJHand getCurrentHand()
		{
			return this.getCurrentPlayer().getHands().get(this.handIndex);
		}
		
		public static IState ofFirst(BlackJack bj)
		{
			return new PlayerTurnState(bj, 0, 0);
		}
	}
	
	private static class ParentTurnState implements IState
	{
		public static final int PARENT_FIX_SCORE = 17;
		private final BlackJack bj;
		
		@Override
		public int getPlayerIndex()
		{
			return -1;
		}
		
		@Override
		public int getHandIndex()
		{
			return 0;
		}
		
		public ParentTurnState(BlackJack bj)
		{
			Objects.requireNonNull(bj);
			this.bj = bj;
		}
		
		@Override
		public List<BJAction> getActions()
		{
			return Collections.emptyList();
		}
		
		@Override
		public IState doAction(BJAction action)
		{
			BJHand hand = this.bj.getParent().getHands().get(0);
			
			while(hand.isActive() && hand.isValid() && hand.getScore() < PARENT_FIX_SCORE)
			{
				hand.hit();
			}
			
			return null;	// TODO 最終状態を返す
		}
	}
}
