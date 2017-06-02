package br.com.liberdaderesidencial;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Email extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String redirectURL = "/mail_send.html";

	final String senha = System.getenv("SENHA_EMAIL");
	final String remetenteInterno = System.getenv("CONTA_EMAIL");
	final String assunto = "Contato Residencial Liberdade";

	private Session ConfiguraEmail() {

		Properties props = new Properties();
		/** Parametros de conexao com servidor Hotmail */
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", "smtp.live.com");
		props.put("mail.smtp.socketFactory.port", "587");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(remetenteInterno, senha);
			}
		});

		/** Ativa Debug para sessão */
		session.setDebug(true);
		return session;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String destino;

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String message = request.getParameter("message");

		// Se for mensagem de teste, mandar só pra Stenio
		if ("teste".equals(message)) {
			destino = System.getenv("EMAIL_DESTINO");
		} else {
			destino = System.getenv("EMAIL_DESTINO");
		}

		message = message + "\n\nEnviado por: " + name + "\nResponder para: " + email;

		// Envia o email
		try {
			Message mensagem = new MimeMessage(ConfiguraEmail());
			mensagem.setFrom(new InternetAddress(remetenteInterno)); // Remetente
			mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino)); // Destinatario
			mensagem.setSubject(assunto);// Assunto
			mensagem.setText(message); // Texto
			Transport.send(mensagem); // Envia o email

			System.out.println("E-mail enviado com sucesso!");

			response.sendRedirect(redirectURL);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
}