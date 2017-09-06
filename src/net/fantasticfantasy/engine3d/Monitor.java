package net.fantasticfantasy.engine3d;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import net.fantasticfantasy.engine3d.util.CollectionUtil;

/**The <code>Monitor</code> class represents a
 * user monitor.
 */
public final class Monitor {
	
	/**The null {@link Monitor} value; do <i>NOT</i>
	 * use <code>null</code> as an alternative!
	 */
	public static final Monitor NULL = new Monitor(0);
	
	private static List<Monitor> monitors;
	private static Monitor primary;
	
	protected long name;
	protected int width;
	protected int height;
	protected int redBits;
	protected int greenBits;
	protected int blueBits;
	
	private String physicalName;
	
	private Monitor(long name) {
		this.name = name;
		if (name == 0) {
			return;
		}
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(name);
		this.width = vidMode.width();
		this.height = vidMode.height();
		this.redBits = vidMode.redBits();
		this.greenBits = vidMode.greenBits();
		this.blueBits = vidMode.blueBits();
		this.physicalName = GLFW.glfwGetMonitorName(this.name);
	}
	
	/**Returns the name of the {@link Monitor}
	 * 
	 * @return The name
	 */
	public long getName() {
		return this.name;
	}
	
	/**Returns the {@link Monitor}'s width
	 * 
	 * @return The width
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**Returns the {@link Monitor}'s height
	 * 
	 * @return The height
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**Returns the {@link Monitor}'s red bits
	 * 
	 * @return The red bits
	 */
	public int getRedBits() {
		return this.redBits;
	}
	
	/**Returns the {@link Monitor}'s green bits
	 * 
	 * @return The green bits
	 */
	public int getGreenBits() {
		return this.greenBits;
	}
	
	/**Returns the {@link Monitor}'s blue bits
	 * 
	 * @return The blue bits
	 */
	public int getBlueBits() {
		return this.blueBits;
	}
	
	/**Returns the monitor's physical name.<br>
	 * <b>See</b> {@link GLFW#glfwGetMonitorName(long) glfwGetMonitorName()}
	 * 
	 * @return The monitor name
	 */
	public String getMonitorName() {
		return this.physicalName;
	}
	
	/**Returns whether or not <code>obj</code> equals <code>this</code>.
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (!(obj instanceof Monitor)) {
			return false;
		}
		Monitor other = (Monitor) obj;
		if (this.blueBits != other.blueBits) {
			return false;
		} else if (this.greenBits != other.greenBits) {
			return false;
		} else if (this.height != other.height) {
			return false;
		} else if (this.name != other.name) {
			return false;
		} else if (this.redBits != other.redBits) {
			return false;
		} else if (this.physicalName == null) {
			if (other.physicalName != null) {
				return false;
			}
		} else if (!this.physicalName.equals(other.physicalName)) {
			return false;
		} else if (this.width != other.width) {
			return false;
		}
		return true;
	}
	
	/**Returns a {@link String} representation of this {@link Monitor}.
	 */
	public String toString() {
		return "net.fantasticfantasy.engine3D.Monitor{name=" + this.name +
				",width=" + this.width + ",height=" + this.height + ",redBits=" +
				this.redBits + ",greenBits=" + this.greenBits + ",blueBits=" +
				this.blueBits + ",physicalName=" + this.physicalName + "}";
	}

	/**Returns the primary {@link Monitor}.
	 * 
	 * @return The primary monitor
	 */
	public static Monitor getPrimaryMonitor() {
		return primary;
	}
	
	/**Returns all user monitors.
	 * 
	 * @return All monitors
	 */
	public static List<Monitor> getMonitors() {
		return CollectionUtil.<Monitor>duplicate(monitors);
	}
	
	protected static void init() {
		refreshMonitors();
	}
	
	/**Queries all the user monitors.
	 */
	public static void refreshMonitors() {
		monitors = new ArrayList<>();
		PointerBuffer mnts = GLFW.glfwGetMonitors();
		while (mnts.hasRemaining()) {
			long name = mnts.get();
			monitors.add(new Monitor(name));
		}
		long name = GLFW.glfwGetPrimaryMonitor();
		for (Monitor monitor : monitors) {
			if (monitor.name == name) {
				primary = monitor;
			}
		}
	}
}
