package com.dimple.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 图片压缩工具类
 *
 * @author lnj createTime 2018-10-19 15:31
 **/
public class ImageUtil {

	// Thumbnails.of("原图文件的路径")
	// .scale(1f)
	// .outputQuality(0.5f)
	// .toFile("压缩后文件的路径");
	// 其中的scale是可以指定图片的大小，值在0到1之间，1f就是原图大小，0.5就是原图的一半大小，这里的大小是指图片的长宽。
	// 而outputQuality是图片的质量，值也是在0到1，越接近于1质量越好，越接近于0质量越差。

	// 图片默认缩放比率
	private static final double DEFAULT_SCALE = 0.8d;

	// 缩略图后缀
	public static final String SUFFIX = "-thumbnail";

	/**
	 * 生成缩略图到指定的目录
	 *
	 * @param path
	 *            目录
	 * @param files
	 *            要生成缩略图的文件列表
	 * @throws IOException
	 */
	public static List<String> generateThumbnail2Directory(String path, String... files) throws IOException {
		return generateThumbnail2Directory(DEFAULT_SCALE, path, files);
	}

	/**
	 * 生成缩略图到指定的目录
	 *
	 * @param scale
	 *            图片缩放率
	 * @param pathname
	 *            缩略图保存目录
	 * @param files
	 *            要生成缩略图的文件列表
	 * @throws IOException
	 */
	public static List<String> generateThumbnail2Directory(double scale, String pathname, String... files)
			throws IOException {
		Thumbnails.of(files)
				// 图片缩放率，不能和size()一起使用
				.scale(scale)
				// 缩略图保存目录,该目录需存在，否则报错
				.toFiles(new File(pathname), Rename.SUFFIX_HYPHEN_THUMBNAIL);
		List<String> list = new ArrayList<>(files.length);
		for (String file : files) {
			list.add(appendSuffix(file, SUFFIX));
		}
		return list;
	}

	/**
	 * 将指定目录下所有图片生成缩略图
	 *
	 * @param pathname
	 *            文件目录
	 */
	public static void generateDirectoryThumbnail(String pathname) throws IOException {
		generateDirectoryThumbnail(pathname, DEFAULT_SCALE);
	}

	/**
	 * 将指定目录下所有图片生成缩略图
	 *
	 * @param pathname
	 *            文件目录
	 */
	public static void generateDirectoryThumbnail(String pathname, double scale) throws IOException {
		File[] files = new File(pathname).listFiles();
		compressRecurse(files, pathname);
	}

	/**
	 * 文件追加后缀
	 *
	 * @param fileName
	 *            原文件名
	 * @param suffix
	 *            文件后缀
	 * @return
	 */
	public static String appendSuffix(String fileName, String suffix) {
		String newFileName = "";

		int indexOfDot = fileName.lastIndexOf('.');

		if (indexOfDot != -1) {
			newFileName = fileName.substring(0, indexOfDot);
			newFileName += suffix;
			newFileName += fileName.substring(indexOfDot);
		} else {
			newFileName = fileName + suffix;
		}

		return newFileName;
	}

	private static void compressRecurse(File[] files, String pathname) throws IOException {
		for (File file : files) {
			// 目录
			if (file.isDirectory()) {
				File[] subFiles = file.listFiles();
				compressRecurse(subFiles, pathname + File.separator + file.getName());
			} else {
				// 文件包含压缩文件后缀或非图片格式，则不再压缩
				String extension = getFileExtention(file.getName());
				if (!file.getName().contains(SUFFIX) && isImage(extension)) {
					generateThumbnail2Directory(pathname, file.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * 根据文件扩展名判断文件是否图片格式
	 *
	 * @param extension
	 *            文件扩展名
	 * @return
	 */
	public static boolean isImage(String extension) {
		String[] imageExtension = new String[] { "jpeg", "jpg", "gif", "bmp", "png" };

		for (String e : imageExtension)
			if (extension.toLowerCase().equals(e))
				return true;

		return false;
	}

	public static String getFileExtention(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		return extension;
	}

	/**
	 * 指定大小进行缩放
	 * 
	 * @throws IOException
	 */
	public static String generateSize(int width, int height, String fileName) throws IOException {
		/*
		 * size(width,height) 若图片横比200小，高比300小，不变
		 * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
		 * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
		 */
		String newFile = appendSuffix(fileName, SUFFIX);
		// 按指定大小把图片进行缩和放（会遵循原图高宽比例）
		// Thumbnails.of(fileName).size(width,height).toFile(newFile);
		// 不按比例，就按指定的大小进行缩放
		Thumbnails.of(fileName).size(width, height).keepAspectRatio(false).toFile(newFile);
		return newFile;
	}

	public static String generateSmall(String fileName) throws IOException {
		String newFile = appendSuffix(fileName, SUFFIX);
		File srcFile = new File(fileName);
		long srcFileSize = srcFile.length();
		System.out.println("源图片：" + fileName + "，大小：" + srcFileSize / 1024 + "kb========"+srcFileSize);
//		if (srcFileSize <= 1048576) {//1M
//			return newFile;
//		}
		//计算宽高
		BufferedImage  bim = ImageIO.read(srcFile);
		int imgWidth = bim.getWidth();
		int imgHeight = bim.getHeight();
		DecimalFormat df = new DecimalFormat("#0.00");
		String scale = df.format(1/(Double.valueOf(srcFileSize) / 1048576));
		System.out.println(">>>>>"+Double.valueOf(scale));
		double quality = Double.valueOf(scale);
		if(quality>1){
			Thumbnails.of(fileName).size(imgWidth, imgHeight).outputQuality(DEFAULT_SCALE).toFile(newFile);
		}else{
			Thumbnails.of(fileName).size(imgWidth, imgHeight).outputQuality(quality).toFile(newFile);
		}
		return newFile;
	}

	/**
	 * 根据指定大小和指定精度压缩图片
	 * 
	 * @param srcPath
	 *            源图片地址
	 * @param desPath
	 *            目标图片地址
	 * @param desFileSize
	 *            指定图片大小，单位kb
	 * @param accuracy
	 *            精度，递归压缩的比率，建议小于0.9
	 * @param desMaxWidth
	 *            目标最大宽度
	 * @param desMaxHeight
	 *            目标最大高度
	 * @return 目标文件路径
	 */
	public static String commpressPicForScale(String srcPath, String desPath, long desFileSize, double accuracy,
			int desMaxWidth, int desMaxHeight) {
		if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(srcPath)) {
			return null;
		}
		if (!new File(srcPath).exists()) {
			return null;
		}
		try {
			File srcFile = new File(srcPath);
			long srcFileSize = srcFile.length();
			System.out.println("源图片：" + srcPath + "，大小：" + srcFileSize / 1024 + "kb");
			// 获取图片信息
			BufferedImage bim = ImageIO.read(srcFile);
			int srcWidth = bim.getWidth();
			int srcHeight = bim.getHeight();

			// 先转换成jpg
			Thumbnails.Builder builder = Thumbnails.of(srcFile).outputFormat("jpg");

			// 指定大小（宽或高超出会才会被缩放）
			if (srcWidth > desMaxWidth || srcHeight > desMaxHeight) {
				builder.size(desMaxWidth, desMaxHeight);
			} else {
				// 宽高均小，指定原大小
				builder.size(srcWidth, srcHeight);
			}

			// 写入到内存
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 字节输出流（写入到内存）
			builder.toOutputStream(baos);

			// 递归压缩，直到目标文件大小小于desFileSize
			byte[] bytes = commpressPicCycle(baos.toByteArray(), desFileSize, accuracy);

			// 输出到文件
			File desFile = new File(desPath);
			FileOutputStream fos = new FileOutputStream(desFile);
			fos.write(bytes);
			fos.close();

			System.out.println("目标图片：" + desPath + "，大小" + desFile.length() / 1024 + "kb");
			System.out.println("图片压缩完成！");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return desPath;
	}

	private static byte[] commpressPicCycle(byte[] bytes, long desFileSize, double accuracy) throws IOException {
		// File srcFileJPG = new File(desPath);
		long srcFileSizeJPG = bytes.length;
		// 2、判断大小，如果小于500kb，不压缩；如果大于等于500kb，压缩
		if (srcFileSizeJPG <= desFileSize * 1024) {
			return bytes;
		}
		// 计算宽高
		BufferedImage bim = ImageIO.read(new ByteArrayInputStream(bytes));
		int srcWdith = bim.getWidth();
		int srcHeigth = bim.getHeight();
		int desWidth = new BigDecimal(srcWdith).multiply(new BigDecimal(accuracy)).intValue();
		int desHeight = new BigDecimal(srcHeigth).multiply(new BigDecimal(accuracy)).intValue();

		ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 字节输出流（写入到内存）
		Thumbnails.of(new ByteArrayInputStream(bytes)).size(desWidth, desHeight).outputQuality(accuracy)
				.toOutputStream(baos);
		return commpressPicCycle(baos.toByteArray(), desFileSize, accuracy);
	}

	
	/**
	 * 
	 * @param srcPath  原图片地址
	 * @param desPath  目标图片地址
	 * @param desFileSize  指定图片大小,单位kb
	 * @param accuracy 精度,递归压缩的比率,建议小于0.9
	 * @return
	 */
	public static String commpressPicForScale2(String srcPath,String desPath,
			long desFileSize , double accuracy){
		try {
			File srcFile  = new File(srcPath);
			long srcFilesize = srcFile.length();
			System.out.println("原图片:"+srcPath + ",大小:" + srcFilesize/1024 + "kb");
			//递归压缩,直到目标文件大小小于desFileSize
			commpressPicCycle2(srcPath,desPath, desFileSize, accuracy);
			
			File desFile = new File(desPath);
			System.out.println("目标图片:" + desPath + ",大小" + desFile.length()/1024 + "kb");
			System.out.println("图片压缩完成!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return desPath;
	}

	public static void commpressPicCycle2(String srcPath,String desPath , long desFileSize,
			double accuracy) throws IOException{
		File imgFile = new File(srcPath);
		long fileSize  = imgFile.length();
		//判断大小,如果小于500k,不压缩,如果大于等于500k,压缩
		if(fileSize <= desFileSize * 500){
			return;
		}
		//计算宽高
		BufferedImage  bim = ImageIO.read(imgFile);
		int imgWidth = bim.getWidth();
		int imgHeight = bim.getHeight();
		int desWidth = new BigDecimal(imgWidth).multiply(
                new BigDecimal(accuracy)).intValue();
        int desHeight = new BigDecimal(imgHeight).multiply(
                new BigDecimal(accuracy)).intValue();
        Thumbnails.of(srcPath).size(desWidth, desHeight).outputQuality(accuracy).toFile(desPath);
        //如果不满足要求,递归直至满足小于1M的要求
        commpressPicCycle2(desPath,desPath, desFileSize, accuracy);
	}
	
	/**
	 * 按照比例进行缩放
	 * 
	 * @throws IOException
	 */
	private void test2() throws IOException {
		/**
		 * scale(比例)
		 */
		Thumbnails.of("images/test.jpg").scale(0.25f).toFile("C:/image_25%.jpg");
		Thumbnails.of("images/test.jpg").scale(1.10f).toFile("C:/image_110%.jpg");
	}

	/**
	 * 不按照比例，指定大小进行缩放
	 * 
	 * @throws IOException
	 */
	private void test3() throws IOException {
		/**
		 * keepAspectRatio(false) 默认是按照比例缩放的
		 */
		Thumbnails.of("images/test.jpg").size(120, 120).keepAspectRatio(false).toFile("C:/image_120x120.jpg");
	}

	/**
	 * 旋转
	 * 
	 * @throws IOException
	 */
	private void test4() throws IOException {
		/**
		 * rotate(角度),正数：顺时针 负数：逆时针
		 */
		Thumbnails.of("images/test.jpg").size(1280, 1024).rotate(90).toFile("C:/image+90.jpg");
		Thumbnails.of("images/test.jpg").size(1280, 1024).rotate(-90).toFile("C:/iamge-90.jpg");
	}

	/**
	 * 水印
	 * 
	 * @throws IOException
	 */
	private void test5() throws IOException {
		/**
		 * watermark(位置，水印图，透明度)
		 */
		Thumbnails.of("images/test.jpg").size(1280, 1024)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("images/watermark.png")), 0.5f)
				.outputQuality(0.8f).toFile("C:/image_watermark_bottom_right.jpg");
		Thumbnails.of("images/test.jpg").size(1280, 1024)
				.watermark(Positions.CENTER, ImageIO.read(new File("images/watermark.png")), 0.5f).outputQuality(0.8f)
				.toFile("C:/image_watermark_center.jpg");
	}

	/**
	 * 裁剪
	 * 
	 * @throws IOException
	 */
	private void test6() throws IOException {
		/**
		 * 图片中心400*400的区域
		 */
		Thumbnails.of("images/test.jpg").sourceRegion(Positions.CENTER, 400, 400).size(200, 200).keepAspectRatio(false)
				.toFile("C:/image_region_center.jpg");
		/**
		 * 图片右下400*400的区域
		 */
		Thumbnails.of("images/test.jpg").sourceRegion(Positions.BOTTOM_RIGHT, 400, 400).size(200, 200)
				.keepAspectRatio(false).toFile("C:/image_region_bootom_right.jpg");
		/**
		 * 指定坐标
		 */
		Thumbnails.of("images/test.jpg").sourceRegion(600, 500, 400, 400).size(200, 200).keepAspectRatio(false)
				.toFile("C:/image_region_coord.jpg");
	}

	/**
	 * 转化图像格式
	 * 
	 * @throws IOException
	 */
	private void test7() throws IOException {
		/**
		 * outputFormat(图像格式)
		 */
		Thumbnails.of("images/test.jpg").size(1280, 1024).outputFormat("png").toFile("C:/image_1280x1024.png");
		Thumbnails.of("images/test.jpg").size(1280, 1024).outputFormat("gif").toFile("C:/image_1280x1024.gif");
	}

	/**
	 * 输出到OutputStream
	 * 
	 * @throws IOException
	 */
	private void test8() throws IOException {
		/**
		 * toOutputStream(流对象)
		 */
		OutputStream os = new FileOutputStream("C:/image_1280x1024_OutputStream.png");
		Thumbnails.of("images/test.jpg").size(1280, 1024).toOutputStream(os);
	}

	/**
	 * 输出到BufferedImage
	 * 
	 * @throws IOException
	 */
	private void test9() throws IOException {
		/**
		 * asBufferedImage() 返回BufferedImage
		 */
		BufferedImage thumbnail = Thumbnails.of("images/test.jpg").size(1280, 1024).asBufferedImage();
		ImageIO.write(thumbnail, "jpg", new File("C:/image_1280x1024_BufferedImage.jpg"));
	}

	public static void main(String[] args) throws IOException {
		// 测试在指定目录下生成缩略图
		// String path = "D:\\image";
		// String[] files = new
		// String[]{"C:\\Users\\liusheng\\Desktop\\all_table20190319.jpg"};

		// List<String> list = ImageUtil.generateThumbnail2Directory(path,
		// files);
		// System.out.println(list);

		// 将指定目录下的图片生成缩略图
		// String path2 =
		// "D:\\workspace\\study\\DimpleBlog-master\\DimpleBlog\\src\\main\\resources\\static\\public\\jquery-transition-slider\\img\\ls";
		// ImageUtil.generateDirectoryThumbnail(path2);

		// File[] files = new File(path2).listFiles();
		// for(File file:files){
		// System.out.println(file.getAbsolutePath());
		// String smallThumbnailPath = ImageUtil.generateSize(72,
		// 72,file.getAbsolutePath());
		// }

		// ImageUtil thumbnailatorTest = new ImageUtil();
		// thumbnailatorTest.test2();
		// thumbnailatorTest.test3();
		// thumbnailatorTest.test4();
		// thumbnailatorTest.test5();
		// thumbnailatorTest.test6();
		// thumbnailatorTest.test7();
		// thumbnailatorTest.test8();
		// thumbnailatorTest.test9();

//		Thumbnails.of("images/BED59E3B0.bmp").scale(0.8f).toFile(
//				"D:/workspace/self/DimpleBlogNew/DimpleBlog-master/images/" + System.currentTimeMillis() + ".bmp");

//		 commpressPicForScale("images/BED59E3B0.bmp","D:/workspace/self/DimpleBlogNew/DimpleBlog-master/images/"
//		 + System.currentTimeMillis() + ".bmp", 5000, 0.8,750,1334); //
		// 图片小于1000kb
		 
//		 commpressPicForScale2("images/BED59E3B0.bmp", "D:/workspace/self/DimpleBlogNew/DimpleBlog-master/images/"
//				 + System.currentTimeMillis() + ".bmp", 1000, 0.8);

//		generateSmall("D:/workspace/self/DimpleBlogNew/DimpleBlog-master/images/Desert.jpg");
		
	}
}