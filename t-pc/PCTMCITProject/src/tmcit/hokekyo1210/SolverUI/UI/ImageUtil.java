package tmcit.hokekyo1210.SolverUI.UI;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

/**
 * there problem with unsigned char. So the conversion would be
 * 0:0,.....127:127, 128:-128, .......255:-1 When writing images:
 * if(data[i][j]>127){ data[i][j] = data[i][j] -256 } Now convert into MacRoman
 * encoding.
 * 
 * @author ravi
 * 
 */
public class ImageUtil {
	private int numRows, numCols, maxGrey = 0, magicNum = 0, dataRed[][], dataGreen[][], dataBlue[][];
	private boolean color = false;
	private BufferedImage buffImg;
	private List<String> options;
	public ImageUtil() {

	}
	
	List<String> getOptions(){
		return options;
	}

	public ImageUtil(String inpImgPath) {
		read(inpImgPath);
	}

	public void init(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		this.dataRed = new int[numRows][numCols];
		this.dataGreen = new int[numRows][numCols];
		this.dataBlue = new int[numRows][numCols];

		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				dataRed[i][j] = -1;
				dataGreen[i][j] = 0;
				dataBlue[i][j] = 0;
			}
		}
	}

	public boolean read(String inpImgPath) {
		inpImgPath = inpImgPath.replaceAll("\\\\", "/");
		File inpImgFile = new File(inpImgPath);
		boolean returnVal = false;
		try {
			String fileExt = inpImgFile.getName().substring(inpImgFile.getName().lastIndexOf('.') + 1);

			// Check availability using a filename extension
			if (canReadExtension(fileExt)) {
				extractMetaData(inpImgPath);
				this.buffImg = ImageIO.read(inpImgFile);
				this.numRows = buffImg.getHeight();
				this.numCols = buffImg.getWidth();
				dataRed = new int[numRows][numCols];
				Raster r = buffImg.getData();
				color = (buffImg.getData().getNumBands() == 3) ? true : false;
				if (color) {
					this.dataGreen = new int[numRows][numCols];
					this.dataBlue = new int[numRows][numCols];
				}
				for (int i = 0; i < numRows; i++) {
					for (int j = 0; j < numCols; j++) {
						dataRed[i][j] = r.getSample(j, i, 0);
						if (color) {
							dataGreen[i][j] = r.getSample(j, i, 1);
							dataBlue[i][j] = r.getSample(j, i, 2);
						}
					}
				}
				returnVal = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			returnVal = false;
		}
		return returnVal;
	}

	/**
	 * This method extracts the metadata of PGM and PPM Image files only. Reads
	 * and stores Maxgray nd magic number values.
	 * 
	 * @param srcFilePath
	 */
	public void extractMetaData(String srcFilePath) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(srcFilePath)));
			try {
				int metaDataCount = 0;
				char ch;
				
				options = new ArrayList<String>();
				StringBuilder pixelValueConstructor;
				while ((ch = (char) input.read()) != -1 && Character.getType(ch) != 0) {

					if (!Character.isWhitespace(ch)) {
						// ignore comments
						if (metaDataCount < 3 && ch == '#') {
							String str = "";
							input.read();
							while ((ch = (char) input.read()) != '\n'){
								str += ch;
							}
							options.add(str);
						} else if (metaDataCount == 0) {
							// check if file is beginning with 'P[magicNum2]'
							if (ch == 'P') {
								magicNum = Character.getNumericValue((char) input.read());
								if (magicNum == 2 || magicNum == 3 || magicNum == 5 || magicNum == 6) {
									metaDataCount++;
								} else {
									System.out.println("Invalid magic Num for PGM or PPM Image");
									System.exit(0);
								}

							}
						} else if (metaDataCount == 1) {
							if (Character.isLetter(ch)) {
								System.out.println("Invalid PGM or PPM Image. Expecting NumCols");
								System.exit(0);
							} else {
								pixelValueConstructor = new StringBuilder();
								pixelValueConstructor.append(ch);
								while ((ch = (char) input.read()) != -1 && !Character.isWhitespace(ch)) {
									pixelValueConstructor.append(ch);
								}
								metaDataCount++;
								numCols = Integer.parseInt(pixelValueConstructor.toString());
							}
						} else if (metaDataCount == 2) {
							if (Character.isLetter(ch)) {
								System.out.println("Invalid PGM or PPM Image. Expecting NumRows");
								System.exit(0);
							} else {
								pixelValueConstructor = new StringBuilder();
								pixelValueConstructor.append(ch);
								while ((ch = (char) input.read()) != -1 && !Character.isWhitespace(ch)) {
									pixelValueConstructor.append(ch);
								}
								metaDataCount++;
								numRows = Integer.parseInt(pixelValueConstructor.toString());
							}
						} else if (metaDataCount == 3) {
							if (Character.isLetter(ch)) {
								System.out.println("Invalid PGM or PPM Image. Expecting MaxGrey");
								System.exit(0);
							} else {
								pixelValueConstructor = new StringBuilder();
								pixelValueConstructor.append(ch);
								while ((ch = (char) input.read()) != -1 && !Character.isWhitespace(ch)) {
									pixelValueConstructor.append(ch);
								}
								metaDataCount++;
								maxGrey = Integer.parseInt(pixelValueConstructor.toString());
							}
						}
						if (metaDataCount == 4) {
							// Start of image Data. We are readin using the
							// library
							break;
						}
					}
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// Returns true if the specified file extension can be read
	public static boolean canReadExtension(String fileExt) {
		Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(fileExt);
		return iter.hasNext();
	}

	/**
	 * P5 is PGM file with ASCII data. P6 is PPM file with ASCII data, Each
	 * pixel stored as RGB values
	 * 
	 * @param filePath
	 */
	public void save(String filePath) {
		String fileExt = filePath.substring(filePath.lastIndexOf('.') + 1);
		PrintWriter pw = null;
		try {
			if (fileExt.equalsIgnoreCase("pgm")) {

				pw = new PrintWriter(new FileWriter(filePath));
				pw.println("P5 " + this.numCols + " " + this.numRows + " 255");
				for (int i = 0; i < this.numRows; i++) {
					for (int j = 0; j < this.numCols; j++) {

						pw.print((dataRed[i][j] > 127) ? getMacRomanEncoding(dataRed[i][j] - 256)
								: getMacRomanEncoding(dataRed[i][j]));
					}
				}
				System.out.println("Successfully created P5 " + filePath);

			} else if (fileExt.equalsIgnoreCase("ppm")) {

				pw = new PrintWriter(new FileWriter(filePath));
				pw.println("P6 " + this.numCols + " " + this.numRows + " 255");
				for (int i = 0; i < this.numRows; i++) {
					for (int j = 0; j < this.numCols; j++) {
						pw.print((dataRed[i][j] > 127) ? getMacRomanEncoding(dataRed[i][j] - 256)
								: getMacRomanEncoding(dataRed[i][j]));
						pw.print((dataGreen[i][j] > 127) ? getMacRomanEncoding(dataGreen[i][j] - 256)
								: getMacRomanEncoding(dataGreen[i][j]));
						pw.print((dataBlue[i][j] > 127) ? getMacRomanEncoding(dataBlue[i][j] - 256)
								: getMacRomanEncoding(dataBlue[i][j]));
					}
				}

				System.out.println("Successfully created P6 " + filePath);
			} else {
				System.out.println("Invalid Target File extension. Expecting .pgm or .ppm");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pw.close();
			pw.flush();
		}

	}

	/**
	 * MacRoman encoding values range from -128 to +127
	 * 
	 * @param value
	 * @return
	 */

	public String getMacRomanEncoding(int value) {
		byte b[] = new byte[1];
		b[0] = Byte.parseByte(Integer.toString(value));
		try {
			return new String(b, "MacRoman");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public String getMacRomanEncoding(String value) {
		byte b[] = new byte[1];
		b[0] = Byte.parseByte(value);
		try {
			return new String(b, "MacRoman");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public int getMaxGrey() {
		return maxGrey;
	}

	public int getMagicNum() {
		return magicNum;
	}

	public int[][] getDataRed() {
		return dataRed;
	}

	public int[][] getDataGreen() {
		return dataGreen;
	}

	public int[][] getDataBlue() {
		return dataBlue;
	}

	public boolean isColor() {
		return color;
	}

	public BufferedImage getBuffImg() {
		return buffImg;
	}

	public void setDataRed(int[][] dataRed) {
		this.dataRed = dataRed;
	}

	public void setDataGreen(int[][] dataGreen) {
		this.dataGreen = dataGreen;
	}

	public void setDataBlue(int[][] dataBlue) {
		this.dataBlue = dataBlue;
	}

	public void setBuffImg(BufferedImage buffImg) {
		this.buffImg = buffImg;
	}

}
