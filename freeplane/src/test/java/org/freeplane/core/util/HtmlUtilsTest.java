package org.freeplane.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HtmlUtilsTest {

	@Test
	public void testHtmlToPlain_shouldRemoveTrailingWhitespaces() {
		String input = "<html>\n" +
				"  <head>\n" +
				"    \n" +
				"  </head>\n" +
				"  <body>\n" +
				"    <p>\n" +
				"      A paragraph followed by an empty one\n" +
				"    </p>\n" +
				"    <p>\n" +
				"      \n" +
				"    </p>\n" +
				"  </body>\n" +
				"</html>";
		String expected = "A paragraph followed by an empty one";
		String actual = HtmlUtils.htmlToPlain(input, true, true);
		assertEquals(expected, actual);
	}

	@Test
	public void testHtmlToPlain_shouldRetainTrailingNonBreakingSpaces() {
		String input = "<html>\n" +
				"  <head>\n" +
				"    \n" +
				"  </head>\n" +
				"  <body>\n" +
				"    <p>\n" +
				"      Zero\n" +
				"    </p>\n" +
				"    <p>\n" +
				"      One&nbsp; \n" +
				"    </p>\n" +
				"    <p>\n" +
				"      Two&nbsp;&nbsp; \n" +
				"    </p>\n" +
				"    <p>\n" +
				"      Three&nbsp;&nbsp;&nbsp; \n" +
				"    </p>\n" +
				"    <p>\n" +
				"      EOF\n" +
				"    </p>\n" +
				"  </body>\n" +
				"</html>\n";
		String expected = "Zero\n" +
				"One \n" +
				"Two  \n" +
				"Three   \n" +
				"EOF";
		String actual = HtmlUtils.htmlToPlain(input, true, true);
		assertEquals(expected, actual);
	}


	// @Test
    // public void testHtmlToPlain_plainTextOnly() {

    //     assertEquals("Simple text", 
    //         HtmlUtils.htmlToPlain("Simple text", true, true));
            
    //     assertEquals("Multiple\nlines",
    //         HtmlUtils.htmlToPlain("Multiple\nlines", true, true));

    //     assertEquals("Text with spaces",
    //         HtmlUtils.htmlToPlain("Text   with   spaces", true, true));
    // }
    
    // @Test
    // public void testHtmlToPlain_Tags() {
    //     assertEquals("Text",
    //         HtmlUtils.htmlToPlain("<p>Text</p>", true, true));

    //     assertEquals("Text", 
    //         HtmlUtils.htmlToPlain("<p>Text", true, true));
            
    //     assertEquals("Nested text",
    //         HtmlUtils.htmlToPlain("<div><span>Nested text", true, true));

    //     assertEquals("Multiple tags",
    //         HtmlUtils.htmlToPlain("<p><b><i>Multiple tags", true, true));
    //         assertEquals("Link text", 
    //         HtmlUtils.htmlToPlain("<a href='http://example.com'>Link text</a>", true, true));
            
    //     assertEquals("Styled text",
    //         HtmlUtils.htmlToPlain("<p style='color:red'>Styled text</p>", true, true));

    //     assertEquals("Custom attribute",
    //         HtmlUtils.htmlToPlain("<div data-custom='value'>Custom attribute</div>", true, true));

    //     assertEquals("Multiple attributes",
    //         HtmlUtils.htmlToPlain("<p id='test' class='important'>Multiple attributes</p>", true, true));
    // }
    
    
    // @Test
    // public void testHtmlToPlain_completeHtmlDocument() {

    //     String simpleDoc = "<html><head><title>Title</title></head><body>Content</body></html>";
    //     assertEquals("TitleContent", HtmlUtils.htmlToPlain(simpleDoc, true, true));
        
    //     String docWithMeta = "<html><head><meta charset='utf-8'><title>Page</title></head>" +
    //                        "<body>Page content</body></html>";
    //     assertEquals("PagePage content", HtmlUtils.htmlToPlain(docWithMeta, true, true));

    //     String docWithStructure = "<html>\n" +
    //         "<head>\n" +
    //         "  <title>Document</title>\n" +
    //         "</head>\n" +
    //         "<body>\n" +
    //         "  <h1>Header</h1>\n" +
    //         "  <p>Paragraph</p>\n" +
    //         "</body>\n" +
    //         "</html>";
    //     assertEquals("Document\nHeader\nParagraph", HtmlUtils.htmlToPlain(docWithStructure, true, true));

    //     String complexDoc = "<html>\n" +
    //         "<head>\n" +
    //         "  <meta charset='utf-8'>\n" +
    //         "  <title>Complex</title>\n" +
    //         "  <style>body { color: red; }</style>\n" +
    //         "</head>\n" +
    //         "<body>\n" +
    //         "  <div class='container'>\n" +
    //         "    <h1>Main Title</h1>\n" +
    //         "    <p>First paragraph</p>\n" +
    //         "    <p>Second paragraph</p>\n" +
    //         "  </div>\n" +
    //         "</body>\n" +
    //         "</html>";
    //     assertEquals("Main Title\nFirst paragraph\nSecond paragraph",
    //         HtmlUtils.htmlToPlain(complexDoc, true, true));
    // }
}