package model.state;

import java.util.List;

public interface IState
{
	public int getPlayerIndex();
	
	public int getHandIndex();
	
	public List<BJAction> getActions();
	
	public IState doAction(BJAction action);
}
