import static org.junit.Assert.*;

import java.util.Date;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTests {
	Email email;
	
	@Before
	public void setUp() throws Exception {
		email = new SimpleEmail();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddBcc() throws Exception {
		final String[] null_emails = null;
		final String[] empty_emails = {};
		final String[] emails = {
				"cats@kittens.com",
				"cats@home.com",
				"cool@cats.com",
		};
		// case 1: null emails passed
		try {
			email.addBcc(null_emails);
			fail("AddBcc did not thow EmailException when no emails were provided");
		} catch(EmailException e) {
			assertTrue(email.getBccAddresses().size() == 0);
		}
		
		// case 2: non null empty array of emails passed
		try {
			email.addBcc(empty_emails);
			fail("AddBcc did not thow EmailException when no emails were provided");
		} catch(EmailException e) {
			assertTrue(email.getBccAddresses().size() == 0);
		}
		
		// case 3: non empty list passed
		email.addBcc(emails);
		assertEquals(emails.length, email.getBccAddresses().size());
		email.addBcc(emails);
		assertEquals(emails.length * 2, email.getBccAddresses().size());
	}
	
	@Test
	public void testAddCc() throws Exception {
		// case 1: add two elements to cc list and check length after each addition
		final String cc = "cats@home.com";
		email.addCc(cc);
		assertTrue(email.getCcAddresses().size() == 1);
		email.addCc(cc);
		assertTrue(email.getCcAddresses().size() == 2);
	}
	
    // a cool comment for testing my CI
	@Test
	public void testAddHeader() throws Exception {
		final String str = "placeholder";
	
		// case 1: name is null, value is null
		try {
			email.addHeader(null, null);
			fail("exception not thrown");
		} catch(IllegalArgumentException e) {}
		
		// case 2: name is null, value is empty
		try {
			email.addHeader(null, "");
			fail("exception not thrown");
		} catch(IllegalArgumentException e) {}
		
		// case 3: name is empty, value is null
		try {
			email.addHeader("", null);
			fail("exception not thrown");
		} catch(IllegalArgumentException e) {}
		
		// case 4: name is empty, value is empty
		try {
			email.addHeader("", "");
			fail("exception not thrown");
		} catch(IllegalArgumentException e) {}
		
		// case 5: name is not empty but value is null
		try {
			email.addHeader(str, null);
			fail("exception not thrown");
		} catch(IllegalArgumentException e) {}
		
		// case 6: name is not empty but value is
		try {
			email.addHeader(str, "");
			fail("exception not thrown");
		} catch(IllegalArgumentException e) {}
		
		// case 7: name and value not empty
		email.addHeader(str, str);
	}
	
	@Test
	public void testAddReplyTo() throws Exception {
		final String invalid_email_str = "catskittenscom";
		final String email_str = "cats@kittens.com";
		
		// case 1: invalid email
		try {
			email.addReplyTo(invalid_email_str, "");
			fail("exception not thrown");
		} catch(EmailException e) {}
		
		// case 2: valid email
		email.addReplyTo(email_str, "");
	}
	
	@Test
	public void testBuildMimeMessage() throws Exception {
		// case 1: no setup
		email = new SimpleEmail();
		try {
			email.buildMimeMessage();
			fail("did not throw exception");
		} catch(EmailException e) {}

		// case 2: minimal setup, no subject, message, etc.
		email = new SimpleEmail();
		email.setHostName("host_name");
		email.setFrom("test@email.com");
		email.addTo("cats@kittens.com", "cat");
		email.buildMimeMessage();
		
		// case 3: test with only subject
		email = new SimpleEmail();
		email.setHostName("host_name");
		email.setFrom("test@email.com");
		email.addTo("cats@kittens.com", "cat");
		email.setSubject("placeholder");
		email.buildMimeMessage();
		
		// case 4: test with only subject and charset
		email = new SimpleEmail();
		email.setHostName("host_name");
		email.setFrom("test@email.com");
		email.addTo("cats@kittens.com", "cat");
		email.setSubject("placeholder");
		email.setCharset("utf-8");
		email.buildMimeMessage();
		
		// case 4: test with only content
		email = new SimpleEmail();
		email.setHostName("host_name");
		email.setFrom("test@email.com");
		email.addTo("cats@kittens.com", "cat");
		email.setContent(new MimeMultipart());
		email.buildMimeMessage();
		
		// case 5: test rebuild
		email = new SimpleEmail();
		email.setHostName("host_name");
		email.setFrom("test@email.com");
		email.addTo("cats@kittens.com", "cat");
		email.buildMimeMessage();
		try {
			email.buildMimeMessage();
			fail("did not throw exception");
		} catch(Exception e) {}
		
		// case 6: test with cc
		email = new SimpleEmail();
		email.setHostName("host_name");
		email.setFrom("test@email.com");
		email.addCc("test2@email.com");
		email.buildMimeMessage();
		
		// case 6: test with bcc
		email = new SimpleEmail();
		email.setHostName("host_name");
		email.setFrom("test@email.com");
		email.addBcc("test2@email.com");
		email.buildMimeMessage();
		
		// case 7: test with reply to
		email = new SimpleEmail();
		email.setHostName("host_name");
		email.setFrom("test@email.com");
		email.addBcc("test2@email.com");
		email.addReplyTo("test2@email.com");
		email.buildMimeMessage();
		
		// case 8: test with header
		email = new SimpleEmail();
		email.setHostName("host_name");
		email.setFrom("test@email.com");
		email.addBcc("test2@email.com");
		email.addHeader("name", "value");
		email.buildMimeMessage();
	}
	
	@Test
	public void testGetHostName() throws Exception {
		// case 1: test with null session
		String r = email.getHostName();
		assertEquals(r, null);
		
		// case 2: test with host name set
		email.setHostName("cat host");
		r = email.getHostName();
		assertEquals(r, email.getHostName());
	}
	
	@Test
	public void testGetMailSession() throws Exception {
		// case 1: session is null
		email.setHostName("cat host");
		Session s = email.getMailSession();
		assertEquals(s, email.getMailSession());
	}
	
	@Test
	public void testGetSentDate() throws Exception {
		// case 1: null sent date
		email.setSentDate(null);
		Date d = email.getSentDate();
		assertEquals(d, new Date());
		
		// case 2: non null sent date
		d = new Date();
		email.setSentDate(d);
		Date r = email.getSentDate();
		assertEquals(d, r);
	}
	
	@Test
	public void testGetSocketConnectionTimeout() throws Exception {
		// case 1: set then test value
		email.setSocketConnectionTimeout(1);
		assertEquals(1, email.getSocketConnectionTimeout());
	}
	
	@Test
	public void testSetFrom() throws Exception {
		// case 1: invalid email
		try {
			email.setFrom("cat.com");
			fail("exception not thrown");
		} catch(EmailException e) {}
		
		// case 2: valid email
		email.setFrom("cats@kittens.com");
		assertEquals("cats@kittens.com", email.getFromAddress().toString());
	}
}
