package view;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.cards.Card;

public class CardsPanel extends JPanel
{
	private static final double CARD_HEIGHT_ASPECT = 1.5;
	private List<Card> cards;
	
	public List<Card> getCards()
	{
		return Collections.unmodifiableList(this.cards);
	}
	
	public void setCard(List<Card> cards)
	{
		this.cards = new ArrayList<>(cards);
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g)
	{
		if((this.cards == null) || (this.cards.size() == 0))
		{
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			return;
		}
		
		double cardNonOverlap = getCardOverlapRatio(this.cards.size());
		double cardsWidthScale = 1.0 + cardNonOverlap * (this.cards.size() - 1);
		double unitW = this.getWidth() / cardsWidthScale;
		double unitH = this.getHeight() / CARD_HEIGHT_ASPECT;
		double unit = Math.min(unitW, unitH);
		int width = (int)(cardsWidthScale * unit);
		int height = (int)(CARD_HEIGHT_ASPECT * unit);
		int startX = (this.getWidth() - width) / 2;
		int startY = (this.getHeight() - height) / 2;
		int cardMarginWidth = (int)(cardNonOverlap * unit);
		
		for(int i = 0; i < this.cards.size(); i++)
		{
			Image cardImage = getImageOf(this.cards.get(i));
			
			if(cardImage == null)
				continue;
			
			g.drawImage(cardImage, startX + cardMarginWidth * i, startY, (int)unit, height, null);
		}
	}
	
	private static double getCardOverlapRatio(int cardNum)
	{
		return Math.max(Math.min(2.0 / cardNum, 1.0), 0.2);
	}
	
	private static Image getImageOf(Card card)
	{
		try
		{
			String path = (card == null) ? "/img/Back.png" : String.format("/img/%s_%02d.png", card.getSuit().getSymbol(), card.getNumber());
			URL url = CardsPanel.class.getResource(path);
			return ImageIO.read(url);
		}
		catch(IOException e)
		{
			return null;
		}
	}
}
