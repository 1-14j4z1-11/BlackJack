package console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import model.blackjack.BJHand;
import model.blackjack.BJPlayer;
import model.blackjack.BlackJack;
import model.cards.Card;
import model.state.BJAction;

public class BJFlow
{
	private static final int PARENT_STOP_SCORE = 17;
	private static final int PLAYER_NUM = 2;
	private static final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	private static final Map<BJAction, String> ACTION_KEY_MAP = new HashMap<BJAction, String>()
	{
		{
			put(BJAction.HIT, "h");
			put(BJAction.STAND, "s");
			put(BJAction.SPLIT, "p");
		}
	};
	
	public void start()
	{
		BlackJack bj = new BlackJack(PLAYER_NUM);
		bj.initialize();
		
		this.showPlayer("Parent", bj.getParent(), true);
		
		for(BJPlayer player : bj.getPlayers())
		{
			this.showPlayer("Player", player, false);
		}
		
		while(true)
		{
			BJPlayer player = bj.getPlayers().stream().filter(p -> p.isActive()).findFirst().orElse(null);
			
			if(player == null)
				break;
			
			this.showText("Playerのターンです%n");
			this.playUntilFixed(player, this::selectActionOfPlayer);
		}

		this.showText("親のターンです%n");
		this.playUntilFixed(bj.getParent(), this::selectActionOfParent);
		this.judge(bj);
	}
	
	private void playUntilFixed(BJPlayer player, Function<BJHand, BJAction> actionSelector)
	{
		while(true)
		{
			BJHand hand = player.getHands().stream().filter(h -> h.isActive()).findFirst().orElse(null);
			
			if(hand == null)
				return;
			
			BJAction action = actionSelector.apply(hand);
			
			switch(action)
			{
				case HIT:
					hand.hit();
					break;
				case STAND:
					hand.stand();
					break;
				case SPLIT:
					if(hand.canSplit())
					{
						hand.split();
					}
					break;
				default:
					throw new AssertionError();
			}
			
			this.showHand(hand, false, action.toString());
		}
	}
	
	private BJAction selectActionOfPlayer(BJHand hand)
	{
		List<BJAction> actions = hand.canSplit()
			? Arrays.asList(BJAction.HIT, BJAction.STAND, BJAction.SPLIT)
			: Arrays.asList(BJAction.HIT, BJAction.STAND);
		
		String usage = actions.stream().map(x -> x + " : '" + ACTION_KEY_MAP.get(x) + "'")
			.reduce((x, y) -> x + ", " + y).orElse("");
		
		this.showText("以下の手札を操作して下さい%n");
		this.showHand(hand, false);
		
		while(true)
		{
			try
			{
				this.showText("%s%n>> ", usage);
				
				String line = stdin.readLine();
				Map.Entry<BJAction, String> kv = ACTION_KEY_MAP.entrySet().stream()
					.filter(e -> e.getValue().equals(line)).findFirst().orElse(null);
				
				if((kv != null) && actions.contains(kv.getKey()))
					return kv.getKey();
			}
			catch (IOException e)
			{ }
		}
	}
	
	private BJAction selectActionOfParent(BJHand hand)
	{
		return (hand.getScore() >= PARENT_STOP_SCORE) ? BJAction.STAND : BJAction.HIT;
	}

	private void judge(BlackJack bj)
	{
		this.showText("最終結果%n");
		
		this.showPlayer("Parent", bj.getParent(), false);
		
		for(BJPlayer player : bj.getPlayers())
		{
			this.showText("Player%n");
			for(BJHand hand : player.getHands())
			{
				this.showHand(hand, false, hand.getResultWith(bj.getParent().getHands().get(0)).toString());
			}
		}
	}

	private void showPlayer(String name, BJPlayer player, boolean mask)
	{
		this.showText("%s%n", name);
		
		for(BJHand hand : player.getHands())
		{
			this.showHand(hand, mask);
		}
	}

	private void showHand(BJHand hand, boolean mask)
	{
		this.showHand(hand, mask, "");
	}
	
	private void showHand(BJHand hand, boolean mask, String endString)
	{
		this.showText(" <%2s>  ", mask ? "??" : (hand.isValid() ? Integer.toString(hand.getScore()) : "XX"));
		
		boolean isFirst = true;
		for(Card card : hand.getCards())
		{
			if(!isFirst && mask)
			{
				this.showText("[***] ", card.getSuit().getSymbol(), card.getNumber());
				continue;
			}
			
			this.showText("[%s%2d] ", card.getSuit().getSymbol(), card.getNumber());
			isFirst = false;
		}
		
		this.showText("  %s%n", (endString != null) ? endString : "");
	}
	
	private void showText(String format, Object... objects)
	{
		System.out.printf(format, objects);
	}
}
