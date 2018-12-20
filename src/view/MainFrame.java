package view;

import java.util.ArrayList;
import java.util.List;

import model.blackjack.BJHand;
import model.state.BJFlow;
import model.state.IStateChangedListener;

public class MainFrame extends BaseFrame implements IStateChangedListener
{
	private static final int MAX_HANDS = 4;
	private final CardsPanel parentPanel = new CardsPanel();
	private final List<CardsPanel> playerPanels = new ArrayList<>(MAX_HANDS);
	
	private final BJFlow flow;
	
	public MainFrame(String title)
	{
		super(title);
		this.flow = new BJFlow(this);
	}
	
	@Override
	protected void initializeComponent()
	{
		this.add(this.parentPanel);
		
		for(int i = 0; i < MAX_HANDS; i++)
		{
			CardsPanel panel = new CardsPanel();
			this.playerPanels.add(panel);
			this.add(panel);
		}
		
		this.onStateChanged();
	}
	
	@Override
	protected void resizeComponent(int width, int height)
	{
		this.parentPanel.setBounds(width * 2 / 5, 0, width / 5, height * 2 / 5);
		
		for(int i = 0; i < MAX_HANDS; i++)
		{
			this.playerPanels.get(i).setBounds(width * (2 * i + 1) / 10, height * 3 / 5, width / 5, height * 2 / 5);
		}
	}
	
	@Override
	public void onStateChanged()
	{
		this.parentPanel.setCard(this.flow.getField().getParent().getHands().get(0).getCards());
		
		List<BJHand> hands = this.flow.getField().getPlayers().get(0).getHands();
		
		for(int i = 0; (i < MAX_HANDS) && (i < hands.size()); i++)
		{
			this.playerPanels.get(i).setCard(hands.get(i).getCards());
		}
	}
}
