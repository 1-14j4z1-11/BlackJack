package view;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

/**
 * Base class of frame.
 */
public abstract class BaseFrame extends JFrame
{
	private static final long serialVersionUID = 431383218269935279L;
	private boolean initialized;
	
	/**
	 * Constructor.
	 * @param title Title of this frame
	 */
	public BaseFrame(String title)
	{
		this.setTitle((title != null) ? title : "");
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void setVisible(boolean flag)
	{
		this.initializeIfNeed();
		super.setVisible(flag);
	}
	
	/**
	 * Initializes this frame if this function is called for the first time.<br>
	 * Otherwise, does nothing.
	 */
	private void initializeIfNeed()
	{
		synchronized(this)
		{
			if(this.initialized)
				return;
			
			this.initialized = true;
		}
		
		this.initializeComponent();
		this.resizeComponent();

		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				resizeComponent();
			}
		});
	}
	
	/**
	 * Resizes component included in this frame.
	 */
	private void resizeComponent()
	{
		Insets insets = this.getInsets();
		int width = this.getWidth() - insets.left - insets.right;
		int height = this.getHeight() - insets.top - insets.bottom;
			
		this.resizeComponent(width, height);
	}
	
	/**
	 * Initializes components.
	 */
	protected abstract void initializeComponent();
	
	/**
	 * Resizes components.</br>
	 * This function is called whenever the frame size is chagned.
	 * @param width Frame width
	 * @param height Frame height
	 */
	protected abstract void resizeComponent(int width, int height);
}
