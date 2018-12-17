package model.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.cards.Deck;

/**
 * BlackJackの全構成を保持するクラス
 */
public class BlackJack
{
	private final int playerNum;
	private Deck deck;
	private BJPlayer parent;
	private List<BJPlayer> players;
	
	/**
	 * コンストラクタ
	 * @param playerNum プレイヤー数
	 */
	public BlackJack(int playerNum)
	{
		if(playerNum <= 0)
			throw new IllegalArgumentException();
		
		this.playerNum = playerNum;
	}
	
	/**
	 * 状態を初期化します。
	 */
	public void initialize()
	{
		this.deck = Deck.create(0);
		this.deck.shuffle();
		this.parent = new BJPlayer(this.deck);
		this.parent.initialize();
		this.players = new ArrayList<>(playerNum);
		
		for(int i = 0; i < playerNum; i++)
		{
			BJPlayer player = new BJPlayer(this.deck);
			player.initialize();
			this.players.add(player);
		}
	}
	
	/**
	 * 親を取得します。
	 */
	public BJPlayer getParent()
	{
		return this.parent;
	}
	
	/**
	 * 子の一覧を取得します。<br>
	 * 取得したListは構造を変更できません。
	 */
	public List<BJPlayer> getPlayers()
	{
		return Collections.unmodifiableList(this.players);
	}
}
