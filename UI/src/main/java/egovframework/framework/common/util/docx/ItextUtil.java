package egovframework.framework.common.util.docx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovPropertiesUtil;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : ItextUtil.java
 * 3. Package  : egovframework.framework.common.util.docx
 * 4. Comment  : 
 * 5. 작성자   : JJH
 * 6. 작성일   : 2016. 7. 27. 오후 10:06:01
 * 7. 변경이력 : 
 *	이름	 : 일자		  : 근거자료   : 변경내용
 *	------------------------------------------------------
 *	JJH : 2016. 7. 27. :			: 신규 개발.
 * </PRE>
 */ 
public class ItextUtil {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	private VelocityEngine velocityEngine;
	
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	/**
	 * <PRE>
	 * 1. MethodName : htmlToPdf
	 * 2. ClassName  : ItextUtil
	 * 3. Comment   : html string 파일을 pdf로 변환
	 * 4. 작성자	: JJH
	 * 5. 작성일	: 2016. 7. 27. 오후 10:06:22
	 * </PRE>
	 *   @return void
	 *   @param outFileNm
	 *   @param velocityNm
	 *   @param param
	 *   @throws Exception
	 */
	public void htmlToPdf(String outFileNm, String velocityNm, DataMap param) throws Exception{
		// 초기화 하면서 여백 설정
		Document document = new Document(PageSize.A4, 70, 70, 80, 80);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(outFileNm)));
		// 백그라운드 설정
//		writer.setPageEvent(new PDFBackground());
		document.open();
		
//		XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
		
		// CSS
		CSSResolver cssResolver = new StyleAttrCSSResolver();
//		CssFile cssFile = helper.getCSS(new FileInputStream("C:/eGovFrame/workspace/projectName/src/main/webapp/css/pdf.css"));
//		cssResolver.addCss(cssFile);
		
		// HTML, 폰트 설정
		XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
		fontProvider.register(EgovPropertiesUtil.getProperty("docx.font.path"), "MalgunGothic"); // MalgunGothic은 alias,
		CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		// img data 사용
		htmlContext.setImageProvider(new Base64ImageProvider());
		
		// Pipelines
		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
		 
		XMLWorker worker = new XMLWorker(css, true);
		XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));
		
		// html 양식 폼 가져오기
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, velocityNm, "UTF-8", param);
		StringReader strReader = new StringReader(text);
		xmlParser.parse(strReader);
		
		document.close();
		writer.close();
	}
	
	/**
	 * <PRE>
	 * 1. MethodName : htmlToPdf
	 * 2. ClassName  : ItextUtil
	 * 3. Comment   : 백그라운드 이미지 들어가는 pdf 변환
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2017. 4. 11. 오전 9:42:25
	 * </PRE>
	 *   @return void
	 *   @param outFileNm
	 *   @param velocityNm
	 *   @param param
	 *   @param backgroundImagePath
	 *   @throws Exception
	 */
	public void htmlToPdf(String outFileNm, String velocityNm, DataMap param, String backgroundImagePath) throws Exception{
		// 초기화 하면서 여백 설정
		Document document = new Document(PageSize.A4, 70, 70, 80, 80);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(outFileNm)));
		// 백그라운드 설정
		writer.setPageEvent(new PDFBackground(backgroundImagePath));
		document.open();
		
//		XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
		
		// CSS
		CSSResolver cssResolver = new StyleAttrCSSResolver();
//		CssFile cssFile = helper.getCSS(new FileInputStream("C:/eGovFrame/workspace/projectName/src/main/webapp/css/pdf.css"));
//		cssResolver.addCss(cssFile);
		
		// HTML, 폰트 설정
		XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
		fontProvider.register(EgovPropertiesUtil.getProperty("docx.font.path"), "MalgunGothic"); // MalgunGothic은 alias,
		CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		// img data 사용
		htmlContext.setImageProvider(new Base64ImageProvider());
		
		// Pipelines
		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
		 
		XMLWorker worker = new XMLWorker(css, true);
		XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));
		
		// html 양식 폼 가져오기
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, velocityNm, "UTF-8", param);
		StringReader strReader = new StringReader(text);
		xmlParser.parse(strReader);
		
		document.close();
		writer.close();
	}
	
	// base64 data 타입의 이미지를 사용할수 있게끔 설정
	class Base64ImageProvider extends AbstractImageProvider {
		 
		@Override
		public Image retrieve(String src) {
			int pos = src.indexOf("base64,");
			try {
				if (src.startsWith("data") && pos > 0) {
					byte[] img = Base64.decode(src.substring(pos + 7));
					return Image.getInstance(img);
				}
				else {
					return Image.getInstance(src);
				}
			} catch (BadElementException ex) {
				return null;
			} catch (IOException ex) {
				return null;
			}
		}

		@Override
		public String getImageRootPath() {
			return null;
		}
	}
	
	// 이미지 백그라운드 설정
	class PDFBackground extends PdfPageEventHelper {
		
		private String imagePath;
		
		public PDFBackground(String imagePath){
			this.imagePath = imagePath;
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			try {
				Image background = Image.getInstance(this.imagePath);
				// This scales the image to the page,
				// use the image's width & height if you don't want to scale.
				float width = document.getPageSize().getWidth();
				float height = document.getPageSize().getHeight();
				
				PdfContentByte canvas = writer.getDirectContentUnder();
				PdfGState state = new PdfGState();
				// 투명도 설정
				state.setFillOpacity(0.6f);
				canvas.setGState(state);
				canvas.addImage(background, width, 0, 0, height, 0, 0);
				
			} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

