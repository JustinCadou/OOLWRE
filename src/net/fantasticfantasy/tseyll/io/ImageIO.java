package net.fantasticfantasy.tseyll.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import net.fantasticfantasy.tseyll.image.Image;

/**The <code>ImageIO</code> class is used to read and decode data
 * from an {@link InputStream} using the {@link STBImage} library.
 */
public class ImageIO {
	
	/**@STATIC_MODULE_CLASS*/
	private ImageIO() {}
	
	/**Reads the bytes from <code>in</code> and returns the
	 * decoded {@link Image} using {@link STBImage} library.<br>
	 * <b>See</b> {@link STBImage#stbi_load_from_memory(ByteBuffer,
	 * int[], int[], int[], int) stbi_load_from_memory()}
	 * 
	 * @param in - The {@link InputStream} from where to read data
	 * @param initialCapacity - The initial capacity of the
	 * {@link ByteBuffer} where to store data;<br>if lower than the
	 * number of bytes from <code>in</code>, the buffer is
	 * resized to twice its capacity and may cause performance
	 * issues in some cases
	 * 
	 * @return The decoded {@link Image}
	 * 
	 * @throws IOException If any I/O error occurs or if the bytes
	 * from <code>in</code> does not match any valid image format
	 */
	public static Image readImage(InputStream in, int initialCapacity) throws IOException {
		ByteBuffer buffer = BufferUtils.createByteBuffer(initialCapacity);
		try (ReadableByteChannel rbc = Channels.newChannel(in)) {
			while (rbc.read(buffer) != -1) {
				if (buffer.remaining() == 0) {
					buffer = scaleBuffer(buffer);
				}
			}
		}
		buffer.flip();
		int[] w = new int[1];
		int[] h = new int[1];
		final ByteBuffer finalByteBuffer = STBImage.stbi_load_from_memory(buffer, w, h, new int[1], 0);
		if (finalByteBuffer == null) {
			throw new IOException("'in' data does not match any available image format");
		}
		Image image = new Image(w[0], h[0]) {
			public ByteBuffer getBuffer() {
				return finalByteBuffer;
			}
		};
		return image;
	}
	
	/**Resizes <code>buffer</code> to twice its last capacity*/
	private static ByteBuffer scaleBuffer(ByteBuffer buffer) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(buffer.capacity() * 2);
		buffer.flip();
		newBuffer.put(buffer); 
		return newBuffer;
	}
}
