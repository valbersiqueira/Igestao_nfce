package com.br.impressora;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;

import javax.imageio.ImageIO;

import com.br.valber.Log;

public class Logomarca {

	private final byte[] INITIALIZE_PRINTER = new byte[] { 0x1B, 38 };

	private final byte[] PRINT_AND_FEED_PAPER = new byte[] { 0x0A };

	private final byte[] SELECT_BIT_IMAGE_MODE = new byte[] { 27, 97, 49, 27, 42 };
	private final byte[] SET_LINE_SPACING = new byte[] { 0x1B, 0x33 };

	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	ByteArrayOutputStream stream3 = new ByteArrayOutputStream();

	private byte[] buildPOSCommand(byte[] command, byte... args) {
		byte[] posCommand = new byte[command.length + args.length];

		System.arraycopy(command, 0, posCommand, 0, command.length);
		System.arraycopy(args, 0, posCommand, command.length, args.length);

		return posCommand;
	}

	private BitSet getBitsImageData(BufferedImage image) {
		int threshold = 150;
		int index = 0;
		int dimenssions = (image.getWidth() * image.getHeight());
		BitSet imageBitsData = new BitSet(dimenssions);

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int color = image.getRGB(x, y);
				int red = (color & 0x00ff0000) >> 16;
				int green = (color & 0x0000ff00) >> 8;
				int blue = (color & 0x000000ff);
				int luminance = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
				imageBitsData.set(index, (luminance < threshold));
				index++;
			}
		}

		return imageBitsData;
	}

	public ByteArrayOutputStream printImage(File path) {

		try {
			BufferedImage img = ImageIO.read(path);
			// Image imagemMenor = img.getScaledInstance(200, 78, 0);
			Image imagemMenor = img.getScaledInstance(200, 100, 0);
			BufferedImage image = toBufferedImage(imagemMenor);

			BitSet imageBits = getBitsImageData(image);

			byte widthLSB = (byte) (image.getWidth() & 0xFF);
			byte widthMSB = (byte) ((image.getWidth() >> 8) & 0xFF);

			// COMMANDS
			byte[] selectBitImageModeCommand = buildPOSCommand(SELECT_BIT_IMAGE_MODE, (byte) 33, widthLSB, widthMSB);
			byte[] setLineSpacing24Dots = buildPOSCommand(SET_LINE_SPACING, (byte) 24);
			byte[] setLineSpacing30Dots = buildPOSCommand(SET_LINE_SPACING, (byte) 30);

			stream.write(INITIALIZE_PRINTER);

			stream.write(setLineSpacing24Dots);
			int offset = 0;
			while (offset < image.getHeight()) {
				stream.write(selectBitImageModeCommand);

				int imageDataLineIndex = 0;
				byte[] imageDataLine = new byte[3 * image.getWidth()];

				for (int x = 0; x < image.getWidth(); ++x) {

					for (int k = 0; k < 3; ++k) {
						byte slice = 0;

						for (int b = 0; b < 8; ++b) {
							int y = (((offset / 8) + k) * 8) + b;
							int i = (y * image.getWidth()) + x;
							boolean v = false;
							if (i < imageBits.length()) {
								v = imageBits.get(i);
							}
							slice |= (byte) ((v ? 1 : 0) << (7 - b));
						}

						imageDataLine[imageDataLineIndex + k] = slice;

					}

					imageDataLineIndex += 3;
				}
				stream.write(imageDataLine);
				offset += 24;
				stream.write(PRINT_AND_FEED_PAPER);
				stream.write(24);

			}

			stream.write(setLineSpacing30Dots);

		} catch (IOException ex) {
			new Log().montarFileErro(getClass(), "Erro", ex.getMessage());
		}
		return stream;
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
}